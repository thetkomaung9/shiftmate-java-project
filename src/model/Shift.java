package model;

public class Shift {
    private int shiftId;
    private int userId;
    private String date;
    private String plannedStart;
    private String plannedEnd;
    private String shiftType;

    public Shift(int shiftId, int userId, String date, String plannedStart, String plannedEnd, String shiftType) {
        this.shiftId = shiftId;
        this.userId = userId;
        this.date = date;
        this.plannedStart = plannedStart;
        this.plannedEnd = plannedEnd;
        this.shiftType = shiftType;
    }

    public Shift(int userId, String date, String plannedStart, String plannedEnd, String shiftType) {
        this(0, userId, date, plannedStart, plannedEnd, shiftType);
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
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

    public String getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(String plannedStart) {
        this.plannedStart = plannedStart;
    }

    public String getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(String plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }
}
