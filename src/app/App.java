package app;

import cloud.FirebaseConfig;
import db.DBInitializer;
import javax.swing.SwingUtilities;
import ui.LoginFrame;

public class App {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FirebaseConfig.initializeFirebase();
            FirebaseConfig.printStatus();
            
            // Initialize database (create tables if not exist)
            DBInitializer.init();

            // Launch login window on Event Dispatch Thread
            SwingUtilities.invokeLater(() -> new LoginFrame());
        } catch (Exception e) {
            System.err.println("Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
