package database;

public enum SQLRequests {

    addMovie("INSERT INTO " +"s335157Movies" +
            "(id, name, xCoordinate, yCoordinate, oscarsCount, goldenPalmCount, genre, mpaaRating, " +
            "personName, personHeight, personHairColor, personEyeColor, personNationality, personLocationX, personLocationY, personLocationZ, username) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

    generateId("SELECT nextval('ids')"),

    addUserWithPassword("INSERT INTO s335157Users (login, password, salt) VALUES(?, ?, ?)"),

    checkUser("SELECT * FROM s335157Users WHERE login=? AND password=?"),

    getSalt("SELECT * FROM s335157Users WHERE login=?"),

    updateMovie("UPDATE s335157Movies SET " +
            "name=?, xCoordinate=?, yCoordinate=?, oscarsCount=?, goldenPalmCount=?, genre=?, mpaaRating=?, " +
            "personName=?, personHeight=?, personHairColor=?, personEyeColor=?, personNationality=?, personLocationX=?, personLocationY=?, personLocationZ=?, username=? "+
            "WHERE id = ?"),

    getById("SELECT * FROM s335157Movies WHERE id = ?"),

    deleteById("DELETE FROM s335157Movies WHERE id = ?"),

    clearAllByUser("DELETE FROM s335157Movies WHERE username = ?"),

    takeAll("SELECT * FROM s335157Movies"),

    takeAllByUser("SELECT * FROM s335157Movies WHERE username = ?");

    private final String statement;

    SQLRequests(String aStatement) {
        statement = aStatement;
    }

    public String getStatement() {
        return statement;
    }
}
