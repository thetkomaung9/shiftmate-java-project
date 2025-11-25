package db;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {

    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "password TEXT" +
                    ");";

            String createShifts = "CREATE TABLE IF NOT EXISTS shifts (" +
                    "shift_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "date TEXT," +
                    "planned_start TEXT," +
                    "planned_end TEXT," +
                    "shift_type TEXT," +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                    ");";

            String createWorkLogs = "CREATE TABLE IF NOT EXISTS work_logs (" +
                    "log_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "date TEXT," +
                    "clock_in TEXT," +
                    "clock_out TEXT," +
                    "tired_level INTEGER," +
                    "note TEXT," +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                    ");";

            stmt.execute(createUsers);
            stmt.execute(createShifts);
            stmt.execute(createWorkLogs);

            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
