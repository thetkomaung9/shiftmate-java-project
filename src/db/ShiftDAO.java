package db;

import model.Shift;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO {

    public boolean insertShift(Shift shift) {
        String sql = "INSERT INTO shifts(user_id, date, planned_start, planned_end, shift_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, shift.getUserId());
            pstmt.setString(2, shift.getDate());
            pstmt.setString(3, shift.getPlannedStart());
            pstmt.setString(4, shift.getPlannedEnd());
            pstmt.setString(5, shift.getShiftType());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Shift> getShiftsByUser(int userId) {
        List<Shift> list = new ArrayList<>();
        String sql = "SELECT * FROM shifts WHERE user_id = ? ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Shift s = new Shift(
                        rs.getInt("shift_id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("planned_start"),
                        rs.getString("planned_end"),
                        rs.getString("shift_type")
                );
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
