package service;

import db.ShiftDAO;
import model.Shift;

import java.util.List;

public class ShiftService {

    private ShiftDAO shiftDAO = new ShiftDAO();

    public boolean addShift(Shift shift) {
        return shiftDAO.insertShift(shift);
    }

    public List<Shift> getShiftsForUser(int userId) {
        return shiftDAO.getShiftsByUser(userId);
    }
}
