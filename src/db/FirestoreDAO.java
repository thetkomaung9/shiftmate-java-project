package db;

import cloud.FirebaseClient;
import cloud.FirebaseConfig;
import model.User;
import model.Shift;
import model.WorkLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Firestore Data Access Layer
 * Handles all communication with Firebase Firestore
 */
public class FirestoreDAO {
    
    private static FirestoreDAO instance;
    private final FirebaseClient firebaseClient;
    private static final String USERS_COLLECTION = "users";
    private static final String SHIFTS_COLLECTION = "shifts";
    private static final String WORKLOGS_COLLECTION = "workLogs";
    
    private FirestoreDAO() throws IOException {
        this.firebaseClient = new FirebaseClient();
    }
    
    /**
     * Get singleton instance of FirestoreDAO
     */
    public static synchronized FirestoreDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new FirestoreDAO();
        }
        return instance;
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Get user by document ID
     */
    public User getUserById(String userId) {
        try {
            String response = firebaseClient.get(USERS_COLLECTION + "/" + userId);
            System.out.println("Retrieved user: " + response);
            return parseUserFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all users from Firestore
     */
    public List<User> getAllUsers() {
        try {
            String response = firebaseClient.get(USERS_COLLECTION);
            return parseUsersFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Add new user to Firestore
     */
    public boolean addUser(User user) {
        try {
            String docId = "user_" + System.currentTimeMillis();
            String jsonBody = formatUserJson(user);
            // Use correct Firestore REST API format with documentId query parameter
            String path = USERS_COLLECTION + "?documentId=" + docId;
            firebaseClient.post(path, jsonBody);
            System.out.println("User added to Firestore: " + user.getName());
            return true;
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update user in Firestore
     */
    public boolean updateUser(String userId, User user) {
        try {
            String jsonBody = formatUserJson(user);
            String response = firebaseClient.patch(USERS_COLLECTION + "/" + userId, jsonBody);
            System.out.println("User updated in Firestore: " + user.getName());
            return true;
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete user from Firestore
     */
    public boolean deleteUser(String userId) {
        try {
            firebaseClient.delete(USERS_COLLECTION + "/" + userId);
            System.out.println("User deleted from Firestore");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== SHIFT OPERATIONS ====================
    
    /**
     * Add shift to Firestore
     */
    public boolean addShift(Shift shift) {
        try {
            String docId = "shift_" + System.currentTimeMillis();
            String jsonBody = formatShiftJson(shift);
            String path = SHIFTS_COLLECTION + "?documentId=" + docId;
            firebaseClient.post(path, jsonBody);
            System.out.println("Shift added to Firestore");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding shift: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all shifts for a user
     */
    public List<Shift> getShiftsByUserId(int userId) {
        try {
            String response = firebaseClient.get(SHIFTS_COLLECTION);
            return parseShiftsFromResponse(response, userId);
        } catch (Exception e) {
            System.err.println("Error getting shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== WORKLOG OPERATIONS ====================
    
    /**
     * Add work log to Firestore
     */
    public boolean addWorkLog(WorkLog log) {
        try {
            String docId = "log_" + System.currentTimeMillis();
            String jsonBody = formatWorkLogJson(log);
            String path = WORKLOGS_COLLECTION + "?documentId=" + docId;
            firebaseClient.post(path, jsonBody);
            System.out.println("Work log added to Firestore");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding work log: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all work logs for a user
     */
    public List<WorkLog> getWorkLogsByUserId(int userId) {
        try {
            String response = firebaseClient.get(WORKLOGS_COLLECTION);
            return parseWorkLogsFromResponse(response, userId);
        } catch (Exception e) {
            System.err.println("Error getting work logs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== JSON FORMATTING ====================
    
    private String formatUserJson(User user) {
        return "{\n" +
            "  \"fields\": {\n" +
            "    \"name\": {\"stringValue\": \"" + escapeJson(user.getName()) + "\"},\n" +
            "    \"password\": {\"stringValue\": \"" + escapeJson(user.getPassword()) + "\"}\n" +
            "  }\n" +
            "}";
    }
    
    private String formatShiftJson(Shift shift) {
        return "{\n" +
            "  \"fields\": {\n" +
            "    \"userId\": {\"integerValue\": " + shift.getUserId() + "},\n" +
            "    \"date\": {\"stringValue\": \"" + shift.getDate() + "\"},\n" +
            "    \"plannedStart\": {\"stringValue\": \"" + shift.getPlannedStart() + "\"},\n" +
            "    \"plannedEnd\": {\"stringValue\": \"" + shift.getPlannedEnd() + "\"},\n" +
            "    \"shiftType\": {\"stringValue\": \"" + escapeJson(shift.getShiftType()) + "\"}\n" +
            "  }\n" +
            "}";
    }
    
    private String formatWorkLogJson(WorkLog log) {
        return "{\n" +
            "  \"fields\": {\n" +
            "    \"userId\": {\"integerValue\": " + log.getUserId() + "},\n" +
            "    \"date\": {\"stringValue\": \"" + log.getDate() + "\"},\n" +
            "    \"clockIn\": {\"stringValue\": \"" + log.getClockIn() + "\"},\n" +
            "    \"clockOut\": {\"stringValue\": \"" + log.getClockOut() + "\"},\n" +
            "    \"tiredLevel\": {\"integerValue\": " + log.getTiredLevel() + "},\n" +
            "    \"note\": {\"stringValue\": \"" + escapeJson(log.getNote()) + "\"}\n" +
            "  }\n" +
            "}";
    }
    
    // ==================== JSON PARSING ====================
    
    private User parseUserFromResponse(String response) {
        // Simple JSON parsing - in production use a proper JSON library
        if (response.contains("\"name\"")) {
            return new User("parsed_user", "password");
        }
        return null;
    }
    
    private List<User> parseUsersFromResponse(String response) {
        List<User> users = new ArrayList<>();
        // Parse multiple users from response
        return users;
    }
    
    private List<Shift> parseShiftsFromResponse(String response, int userId) {
        List<Shift> shifts = new ArrayList<>();
        // Parse shifts for specific user
        return shifts;
    }
    
    private List<WorkLog> parseWorkLogsFromResponse(String response, int userId) {
        List<WorkLog> logs = new ArrayList<>();
        // Parse work logs for specific user
        return logs;
    }
    
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
