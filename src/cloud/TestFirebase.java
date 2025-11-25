package cloud;

import db.FirestoreDAO;
import model.User;
import java.io.IOException;

public class TestFirebase {

    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FirebaseConfig.initializeFirebase();
            FirebaseConfig.printStatus();
            
            // Test Firestore connection
            System.out.println("\n🔗 Testing Firestore connection...\n");
            
            FirebaseClient firebase = new FirebaseClient();
            String result = firebase.get("users");
            System.out.println("✅ Firestore Response: " + result);
            
            // Test adding a user to Firestore
            System.out.println("\n📝 Testing user creation in Firestore...\n");
            FirestoreDAO dao = FirestoreDAO.getInstance();
            User testUser = new User("testuser", "password123");
            boolean added = dao.addUser(testUser);
            if (added) {
                System.out.println("✅ User successfully added to Firestore");
            } else {
                System.out.println("❌ Failed to add user");
            }
            
            // Retrieve users
            System.out.println("\n📖 Retrieving users from Firestore...\n");
            String allUsers = firebase.get("users");
            System.out.println("✅ All Users: " + allUsers);
            
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error accessing Firebase: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
