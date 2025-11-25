package db;

import cloud.FirebaseClient;
import model.User;
import java.io.IOException;

/**
 * Firestore-based User Data Access Object
 * Syncs user data between local SQLite and Firebase Firestore
 */
public class FirestoreUserDAO {
    
    private final FirebaseClient firebaseClient;
    private final UserDAO localDAO = new UserDAO();
    private static final String COLLECTION = "users";
    
    public FirestoreUserDAO() throws IOException {
        this.firebaseClient = new FirebaseClient();
    }
    
    /**
     * Find user by name - first checks local DB, then syncs with Firestore
     */
    public User findByName(String name) {
        // Try local first
        User user = localDAO.findByName(name);
        if (user != null) {
            return user;
        }
        
        // Sync with Firestore
        try {
            String result = firebaseClient.get(COLLECTION + "?where=name," + name);
            System.out.println("Firestore query result: " + result);
        } catch (Exception e) {
            System.err.println("Error querying Firestore: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Insert user and sync to Firestore
     */
    public boolean insertUser(User user) {
        // Insert to local DB first
        boolean inserted = localDAO.insertUser(user);
        
        if (inserted && user.getUserId() > 0) {
            // Sync to Firestore
            syncUserToFirestore(user);
        }
        
        return inserted;
    }
    
    /**
     * Sync a user to Firestore
     */
    private void syncUserToFirestore(User user) {
        try {
            String docId = "user_" + user.getUserId();
            String jsonBody = "{\n" +
                "  \"fields\": {\n" +
                "    \"name\": {\"stringValue\": \"" + user.getName() + "\"},\n" +
                "    \"password\": {\"stringValue\": \"" + user.getPassword() + "\"}\n" +
                "  }\n" +
                "}";
            
            String result = firebaseClient.post(COLLECTION + "/" + docId, jsonBody);
            System.out.println("User synced to Firestore: " + user.getName());
        } catch (Exception e) {
            System.err.println("Error syncing user to Firestore: " + e.getMessage());
        }
    }
    
    /**
     * Get all users from Firestore
     */
    public String getAllUsers() {
        try {
            return firebaseClient.get(COLLECTION);
        } catch (Exception e) {
            System.err.println("Error retrieving users from Firestore: " + e.getMessage());
            return "{}";
        }
    }
}
