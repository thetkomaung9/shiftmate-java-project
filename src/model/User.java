package model;

public class User {
    private int userId;
    private String username; // email/unique id in cloud
    private String name;
    private String password;
    private String birthday;
    private String phone;
    private String emergencyContact;
    private double hourlyWage;
    private double weekendExtra;
    private double nightExtra;
    private double taxDeduction;
    private String payrollNote;

    public User(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.username = name;
    }

    public User(String name, String password) {
        this(0, name, password);
    }

    public User() {
        this(0, "", "");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public double getWeekendExtra() {
        return weekendExtra;
    }

    public void setWeekendExtra(double weekendExtra) {
        this.weekendExtra = weekendExtra;
    }

    public double getNightExtra() {
        return nightExtra;
    }

    public void setNightExtra(double nightExtra) {
        this.nightExtra = nightExtra;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public String getPayrollNote() {
        return payrollNote;
    }

    public void setPayrollNote(String payrollNote) {
        this.payrollNote = payrollNote;
    }
}
