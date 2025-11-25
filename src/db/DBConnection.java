package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:shiftmate.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load SQLite JDBC driver. Make sure sqlite-jdbc JAR is on the classpath.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL);
    }
}
