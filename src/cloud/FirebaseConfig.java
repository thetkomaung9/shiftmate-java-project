package cloud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Firebase configuration and initialization
 */
public class FirebaseConfig {
    
    private static final String PROJECT_ID = "shiftmate-app-2025";
    private static final String DATABASE = "(default)";
    private static final String PRIMARY_KEY_PATH = "resources/key.json";
    private static final String SECONDARY_KEY_PATH = "resources/firebase-key.json";
    private static String resolvedKeyPath;
    
    public static String getProjectId() {
        return PROJECT_ID;
    }
    
    public static String getDatabase() {
        return DATABASE;
    }
    
    public static void initializeFirebase() throws IOException {
        Path keyPath = findServiceAccountKey();
        resolvedKeyPath = keyPath != null ? keyPath.toString() : null;

        if (resolvedKeyPath == null) {
            System.out.println("⚠️  Service account key not found. Running in local-only mode (SQLite).");
            return;
        }

        // Authenticate with gcloud if available
        authenticateWithGCloud();

        System.out.println("✅ Firebase initialized successfully");
        System.out.println("   Project ID: " + PROJECT_ID);
        System.out.println("   Database: " + DATABASE);
    }
    
    private static void authenticateWithGCloud() throws IOException {
        String keyPath = getServiceAccountKeyPath();
        if (keyPath == null) {
            return;
        }

        try {
            // Verify gcloud is installed
            ProcessBuilder which = new ProcessBuilder("which", "gcloud");
            Process whichProc = which.start();
            if (whichProc.waitFor() != 0) {
                System.out.println("⚠️  gcloud CLI not available; skipping Firebase auth. Firestore calls may fail.");
                return;
            }
            
            // Set service account credentials
            ProcessBuilder pb = new ProcessBuilder("gcloud", "auth", "activate-service-account", "--key-file=" + keyPath);
            Process p = pb.start();
            p.waitFor();
            
            // Set default project
            pb = new ProcessBuilder("gcloud", "config", "set", "project", PROJECT_ID);
            p = pb.start();
            p.waitFor();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Authentication interrupted", e);
        }
    }
    
    /**
     * Print Firebase connection status
     */
    public static void printStatus() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Firebase Configuration Status");
        System.out.println("=".repeat(50));
        System.out.println("Project ID: " + PROJECT_ID);
        System.out.println("Database: " + DATABASE);
        System.out.println("Service Account: shiftmate-service@shiftmate-app-2025.iam.gserviceaccount.com");
        System.out.println("Firestore URL: https://console.firebase.google.com/project/" + PROJECT_ID + "/firestore");
        System.out.println("Key path: " + (resolvedKeyPath == null ? "not found (local-only mode)" : resolvedKeyPath));
        System.out.println("=".repeat(50) + "\n");
    }

    /**
     * Resolve the service account key path that exists on disk.
     */
    public static String getServiceAccountKeyPath() {
        if (resolvedKeyPath != null) {
            return resolvedKeyPath;
        }
        Path key = findServiceAccountKey();
        resolvedKeyPath = key != null ? key.toString() : null;
        return resolvedKeyPath;
    }

    private static Path findServiceAccountKey() {
        Path primary = Paths.get(PRIMARY_KEY_PATH);
        if (Files.exists(primary)) {
            return primary;
        }
        Path secondary = Paths.get(SECONDARY_KEY_PATH);
        if (Files.exists(secondary)) {
            return secondary;
        }
        return null;
    }
}
