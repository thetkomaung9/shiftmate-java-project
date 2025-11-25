package db;

import model.WorkLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkLogDAO {

    public boolean insertWorkLog(WorkLog log) {
        String sql = "INSERT INTO work_logs(user_id, date, clock_in, clock_out, tired_level, note) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, log.getUserId());
            pstmt.setString(2, log.getDate());
            pstmt.setString(3, log.getClockIn());
            pstmt.setString(4, log.getClockOut());
            pstmt.setInt(5, log.getTiredLevel());
            pstmt.setString(6, log.getNote());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<WorkLog> getLogsByUserAndMonth(int userId, String monthPrefix) {
        // monthPrefix example: "2025-11" to get all 2025-11-xx
        List<WorkLog> list = new ArrayList<>();
        String sql = "SELECT * FROM work_logs WHERE user_id = ? AND date LIKE ? ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, monthPrefix + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WorkLog log = new WorkLog(
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("clock_in"),
                        rs.getString("clock_out"),
                        rs.getInt("tired_level"),
                        rs.getString("note")
                );
                list.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
