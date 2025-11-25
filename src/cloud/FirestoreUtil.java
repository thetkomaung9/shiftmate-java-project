package cloud;

import model.User;
import model.Shift;
import model.WorkLog;

public class FirestoreUtil {
    
    /**
     * Converts a Java object to Firestore JSON format
     */
    public static String toFirestoreJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        StringBuilder json = new StringBuilder("{");
        
        if (obj instanceof User) {
            User user = (User) obj;
            json.append("\"name\":\"").append(escapeJson(user.getName())).append("\",");
            json.append("\"password\":\"").append(escapeJson(user.getPassword())).append("\"");
        } else if (obj instanceof Shift) {
            Shift shift = (Shift) obj;
            json.append("\"userId\":").append(shift.getUserId()).append(",");
            json.append("\"date\":\"").append(shift.getDate()).append("\",");
            json.append("\"plannedStart\":\"").append(shift.getPlannedStart()).append("\",");
            json.append("\"plannedEnd\":\"").append(shift.getPlannedEnd()).append("\",");
            json.append("\"shiftType\":\"").append(escapeJson(shift.getShiftType())).append("\"");
        } else if (obj instanceof WorkLog) {
            WorkLog log = (WorkLog) obj;
            json.append("\"userId\":").append(log.getUserId()).append(",");
            json.append("\"date\":\"").append(log.getDate()).append("\",");
            json.append("\"clockIn\":\"").append(log.getClockIn()).append("\",");
            json.append("\"clockOut\":\"").append(log.getClockOut()).append("\",");
            json.append("\"tiredLevel\":").append(log.getTiredLevel()).append(",");
            json.append("\"note\":\"").append(escapeJson(log.getNote())).append("\"");
        }
        
        json.append("}");
        return json.toString();
    }
    
    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    /**
     * Wraps object in Firestore document format
     */
    public static String toFirestoreDocument(String documentId, Object obj) {
        return "{\n  \"fields\": " + toFirestoreJson(obj) + "\n}";
    }
}
