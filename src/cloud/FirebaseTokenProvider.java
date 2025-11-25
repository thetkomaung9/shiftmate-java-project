package cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FirebaseTokenProvider {
    
    private String cachedToken;
    private long tokenExpiryTime;
    private static final long EXPIRY_BUFFER_MS = 60000; // Refresh 1 min before expiry

    public FirebaseTokenProvider() {
        // lazy checks in getAccessToken()
    }

    public String getAccessToken() throws IOException {
        // Return cached token if still valid
        if (cachedToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            return cachedToken;
        }

        String keyPath = FirebaseConfig.getServiceAccountKeyPath();
        if (keyPath == null) {
            throw new IOException("Service account key not found. Cannot obtain Firebase token.");
        }

        if (!isGcloudAvailable()) {
            throw new IOException("gcloud CLI is not installed. Please install Google Cloud SDK.");
        }

        try {
            // Use gcloud to get access token with proper environment
            ProcessBuilder pb = new ProcessBuilder("bash", "-c",
                "export GOOGLE_APPLICATION_CREDENTIALS=" + keyPath + " && " +
                "gcloud auth application-default print-access-token");
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String token = reader.readLine();
            
            int exitCode = process.waitFor();
            if (exitCode != 0 || token == null || token.isEmpty()) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorMsg = errorReader.readLine();
                throw new IOException("Failed to get access token from gcloud: " + errorMsg);
            }

            cachedToken = token.trim();
            // Assume token is valid for 1 hour
            tokenExpiryTime = System.currentTimeMillis() + (3600 * 1000 - EXPIRY_BUFFER_MS);
            return cachedToken;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while getting access token", e);
        }
    }

    private boolean isGcloudAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder("which", "gcloud");
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
