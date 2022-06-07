package database;

import data.Movie;
import data.MovieGenre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Синглтон для работы с базой данных
 * Исполнение запросов и т.п.
 */
public class DBManager {
    //For Database
    private final static Logger logger = LogManager.getLogger();
    private static final String DB_URL = "jdbc:postgresql://pg:5432/studs";
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/studs";
    private MessageDigest digest;
    private final Random random = new SecureRandom();

    private static String USER;
    private static String PASS;
    private static final String FILE_WITH_ACCOUNT = "account";
    private static final String TABLE_NAME = "s335157Movies";
    private static final String USERS_TABLE = "s335157Users";
    private static final String pepper = "1@#$&^%$)3";

    //читаем данные аккаунта для входа подключения к бд, ищем драйвер
    static {
        try{
            USER = System.getenv("DB_LOGIN");
            PASS = System.getenv("DB_PASSWORD");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
        }
    }
    public DBManager(Connection connection) throws NoSuchAlgorithmException {

        try{
            digest = MessageDigest.getInstance("SHA-224");

            try {
                this.connection = connection;
                System.out.println("Connection to database was successful");
                createTable();
            } catch (SQLException e) {
                System.out.println("Connection to database failed");
                e.printStackTrace();
            }}
        catch (Exception e){
            logger.error("Something went wrong");
        }
    }

    private Connection connection;

    public DBManager(String dbUrl, String user, String pass) {
        try{
            digest = MessageDigest.getInstance("SHA-224");

        try {
            connection = DriverManager.getConnection(dbUrl, user, pass);
            System.out.println("Connection to database was successful");
            createTable();
        } catch (SQLException e) {
            System.out.println("Connection to database failed");
            e.printStackTrace();
        }}
        catch (Exception e){
            logger.error("Something went wrong");
        }
    }

    public DBManager(String address, int port, String dbName, String user, String pass) {
        this("jdbc:postgresql://" + address + ":" + port + "/" + dbName, user, pass);
    }

    public DBManager() {
        this(DB_URL, USER, PASS);
    }

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();
//        statement.executeUpdate("DROP TABLE "+TABLE_NAME);
//        statement.executeUpdate("DROP TABLE "+USERS_TABLE);

        statement.executeUpdate("CREATE SEQUENCE IF NOT EXISTS ids START 1");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" (" +
                "id int PRIMARY KEY," +
                "name varchar(255) NOT NULL CHECK (name<>'')," +
                "xCoordinate int CHECK (xCoordinate < 293)," +
                "yCoordinate int NOT NULL CHECK (yCoordinate < 686)," +
                "creationDate date NOT NULL DEFAULT (current_date)," +
                "creationTime time NOT NULL DEFAULT (current_time)," +
                "oscarsCount bigint NOT NULL CHECK (oscarsCount > 0)," +
                "goldenPalmCount bigint NOT NULL CHECK (goldenPalmCount > 0)," +
                "genre varchar(100) CHECK (genre='ACTION' OR genre='MUSICAL' OR genre='THRILLER' OR genre = 'HORROR' or genre = 'FANTASY')," +
                "mpaaRating varchar(100) CHECK (mpaaRating='G' OR mpaaRating='PG' OR mpaaRating='PG_13')," +

                "personName varchar(255) NOT NULL CHECK (personName<>'')," +
                "personHeight bigint NOT NULL CHECK (personHeight>0)," +
                "personHairColor varchar(6) NOT NULL CHECK (personHairColor='GREEN' OR personHairColor='RED' OR " +
                "personHairColor='ORANGE' OR personHairColor='WHITE')," +
                "personEyeColor varchar(6) NOT NULL CHECK (personEyeColor='GREEN' OR personEyeColor='RED' OR " +
                "personEyeColor='ORANGE' OR personEyeColor='WHITE')," +
                "personNationality varchar(11) CHECK (personNationality='RUSSIA' OR personNationality='USA' OR " +
                "personNationality='CHINA' OR personNationality='ITALY' OR personNationality='THAILAND')," +
                "personLocationX bigint CHECK (personLocationX<>NULL), "+
                "personLocationY float,"+
                "personLocationZ bigint,"+
                "username varchar(255))");

                 statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+USERS_TABLE+" (" +
                "login varchar(255) PRIMARY KEY," +
                "password BYTEA," +
                "salt BYTEA)");
    }

    public ResultSet getCollection() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.takeAll.getStatement());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error("SQL problem with taking all collection!");
            return null;
        }
    }

    public Integer addMovie(Movie movie) {

//        System.out.println(movie);
//        return 3;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.addMovie.getStatement());
            Integer newId = setMovieToStatement(preparedStatement, movie);
            preparedStatement.executeUpdate();
            return (newId == null) ? 0 : newId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("SQL problem with adding new element!");
            return 0;
        }
    }

    public DBAnswer updateById(Movie movie, int id) {
        try {
            DBAnswer status = getByID(id, movie.getUser());
            if (!status.equals(DBAnswer.SUCCESSFUL)) return status;
            PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.updateMovie.getStatement());
            setUpdatedMovieToStatement(preparedStatement, movie, id);
            preparedStatement.executeUpdate();
            return DBAnswer.SUCCESSFUL;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL problem with updating element !");
            return DBAnswer.SQLPROBLEM;
        }
    }

    public DBAnswer removeById(int id, String user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.deleteById.getStatement());) {
            DBAnswer status = getByID(id, user);
            if (!status.equals(DBAnswer.SUCCESSFUL)) return status;
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return DBAnswer.SUCCESSFUL;
        } catch (SQLException e) {
            logger.error("SQL problem with removing element!");
            return DBAnswer.SQLPROBLEM;
        }
    }

    public DBAnswer getByID(int id, String user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.getById.getStatement())) {
            preparedStatement.setInt(1, id);
            ResultSet deletingLabWork = preparedStatement.executeQuery();
            if (!deletingLabWork.next()) return DBAnswer.OBJECTNOTEXIST;
            if (!deletingLabWork.getString("username").equals(user)) return DBAnswer.PERMISSIONDENIED;
            return DBAnswer.SUCCESSFUL;
        } catch (SQLException e) {
            logger.error("SQL problem with getting element!");
            return DBAnswer.SQLPROBLEM;
        }
    }

    public DBAnswer clear(String user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.clearAllByUser.getStatement())) {
            preparedStatement.setString(1, user);
            preparedStatement.executeUpdate();
            return DBAnswer.SUCCESSFUL;
        } catch (SQLException e) {
            logger.error("SQL problem with removing elements!");
            return DBAnswer.SQLPROBLEM;
        }
    }

    public boolean addUser(String user, String password) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.addUserWithPassword.getStatement())) {
            byte[] salt = new byte[6];
            random.nextBytes(salt);
            preparedStatement.setString(1, user);
            preparedStatement.setBytes(2, getHash(password,salt)); //getBytes
            preparedStatement.setBytes(3, salt);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL problem with adding user!");
            return false;
        }
    }

    public boolean loginUser(String user, String password) {
        byte[] salt;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.getSalt.getStatement())) {
            preparedStatement.setString(1, user);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next())
                salt = result.getBytes("salt");
            else return false;
        } catch (SQLException e) {
            logger.error("SQL problem with get user salt!");
            return false;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLRequests.checkUser.getStatement())) {
            preparedStatement.setString(1, user);
            preparedStatement.setBytes(2, getHash(password, salt));
            ResultSet login = preparedStatement.executeQuery();
            return login.next();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL problem with logging user!");
            return false;
        }
    }

    private Integer generateId() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQLRequests.generateId.getStatement());
            if (resultSet.next()) return resultSet.getInt("nextval");
            return null;
        } catch (SQLException e) {
            logger.error("SQL problem with generating id!");
            return null;
        }
    }

    private Integer setMovieToStatement(PreparedStatement statement, Movie movie) throws SQLException {
        Integer newId = generateId();
        if (newId == null) return null;

        movie.setId(newId);
        statement.setInt(1, newId);
        statement.setString(2, movie.getName());
        statement.setInt(3, (int)movie.getCoordinates().getX());
        statement.setInt(4, movie.getCoordinates().getY());
        statement.setInt(5, movie.getOscarsCount());
        statement.setInt(6, movie.getGoldenPalmCount());
        if (movie.getGenre()==null){
            statement.setString(7,null);
        }
        else statement.setString(7, movie.getGenre().getValue());
        if (movie.getMpaaRating()==null){
            statement.setString(8,null);
        }
        else statement.setString(8, movie.getMpaaRating().getValue());
        statement.setString(9, movie.getDirector().getName());
        statement.setDouble(10, movie.getDirector().getHeight());
        statement.setString(11, movie.getDirector().getHairColor().toString());
        statement.setString(12, movie.getDirector().getEyeColor().toString());
        if (movie.getDirector().getNationality()==null) {
            statement.setString(13,null);
        }
        else statement.setString(13, movie.getDirector().getNationality().toString());
        statement.setLong(14,movie.getDirector().getLocation().getX());
        statement.setFloat(15,movie.getDirector().getLocation().getY());
        statement.setLong(16,movie.getDirector().getLocation().getZ());
        statement.setString(17, movie.getUser());

        return newId;
    }

    private void setUpdatedMovieToStatement(PreparedStatement statement, Movie movie, Integer id) throws SQLException {
        movie.setId(id);

        statement.setString(1, movie.getName());
        statement.setInt(2, (int)movie.getCoordinates().getX());
        statement.setInt(3, movie.getCoordinates().getY());
        statement.setInt(4, movie.getOscarsCount());
        statement.setInt(5, movie.getGoldenPalmCount());
        if (movie.getGenre()==null){
            statement.setString(6,null);
        }
        else statement.setString(6, movie.getGenre().getValue());
        if (movie.getMpaaRating()==null){
            statement.setString(7,null);
        }
        else statement.setString(7, movie.getMpaaRating().getValue());
        statement.setString(8, movie.getDirector().getName());
        statement.setDouble(9, movie.getDirector().getHeight());
        statement.setString(10, movie.getDirector().getHairColor().toString());
        statement.setString(11, movie.getDirector().getEyeColor().toString());
        if (movie.getDirector().getNationality()==null) {
            statement.setString(12,null);
        }
        else statement.setString(12, movie.getDirector().getNationality().toString());
        statement.setLong(13,movie.getDirector().getLocation().getX());
        statement.setFloat(14,movie.getDirector().getLocation().getY());
        statement.setLong(15,movie.getDirector().getLocation().getZ());
        statement.setString(16, movie.getUser());
        statement.setInt(17, movie.getId());
    }

    private byte[] getHash(String password, byte[] salt) {
        digest.update("}aZy*}".getBytes(StandardCharsets.UTF_8));
        if ((password == null)) {
            digest.update("null".getBytes(StandardCharsets.UTF_8));
        } else {
            digest.update(password.getBytes(StandardCharsets.UTF_8));
        }
        digest.update(salt);
        return digest.digest();
    }
}