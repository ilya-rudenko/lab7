package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    String login = "s335157";
    String password = "wjn868";

    String host = "jdbc:postgresql://pg:5432/studs";

    public Connection connect(){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(host,login,password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return conn;
    }
}
