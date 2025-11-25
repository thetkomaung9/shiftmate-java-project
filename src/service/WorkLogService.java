package service;

import db.WorkLogDAO;
import model.WorkLog;

import java.util.List;

public class WorkLogService {

    private WorkLogDAO workLogDAO = new WorkLogDAO();

    public boolean addLog(WorkLog log) {
        return workLogDAO.insertWorkLog(log);
    }

    public List<WorkLog> getLogsByMonth(int userId, String monthPrefix) {
        return workLogDAO.getLogsByUserAndMonth(userId, monthPrefix);
    }
}
