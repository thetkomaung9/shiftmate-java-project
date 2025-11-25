package model;

public class WorkLog {
    private int logId;
    private int userId;
    private String date;
    private String clockIn;
    private String clockOut;
    private int tiredLevel;
    private String note;

    public WorkLog(int logId, int userId, String date, String clockIn, String clockOut, int tiredLevel, String note) {
        this.logId = logId;
        this.userId = userId;
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.tiredLevel = tiredLevel;
        this.note = note;
    }

    public WorkLog(int userId, String date, String clockIn, String clockOut, int tiredLevel, String note) {
        this(0, userId, date, clockIn, clockOut, tiredLevel, note);
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    public int getTiredLevel() {
        return tiredLevel;
    }

    public void setTiredLevel(int tiredLevel) {
        this.tiredLevel = tiredLevel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
