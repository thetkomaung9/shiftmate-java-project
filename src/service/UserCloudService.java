package service;

import cloud.FirebaseClient;
import model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Firestore 의 "users/tkm" 문서를 읽고/쓰는 서비스
 */
public class UserCloudService {

    private final FirebaseClient client = new FirebaseClient();

    // users/tkm document path
    private String userDocPath() {
        return "users/" + FirebaseClient.USER_ID;
    }

    // Firestore → User (best-effort parsing without external JSON lib)
    public User loadUser() {
        try {
            String json = client.get(userDocPath());
            User u = new User();
            u.setUserId(1);
            u.setUsername(FirebaseClient.USER_ID);

            u.setName(extractStringValue(json, "name"));
            u.setPassword(extractStringValue(json, "password"));
            u.setBirthday(extractStringValue(json, "birthday"));
            u.setPhone(extractStringValue(json, "phone"));
            u.setEmergencyContact(extractStringValue(json, "emergencyContact"));
            u.setPayrollNote(extractStringValue(json, "payrollNote"));

            u.setHourlyWage(extractDoubleValue(json, "hourlyWage", 10000));
            u.setWeekendExtra(extractDoubleValue(json, "weekendExtra", 0.5));
            u.setNightExtra(extractDoubleValue(json, "nightExtra", 0.3));
            u.setTaxDeduction(extractDoubleValue(json, "taxDeduction", 0.03));

            if (u.getName() == null || u.getName().isEmpty()) {
                u.setName(FirebaseClient.USER_ID);
            }
            return u;
        } catch (Exception e) {
            // error 발생 시 기본 사용자 리턴
            User u = new User();
            u.setUserId(1);
            u.setUsername("tkm");
            u.setName("tkm");
            u.setHourlyWage(10000);
            u.setWeekendExtra(0.5);
            u.setNightExtra(0.3);
            u.setTaxDeduction(0.03);
            return u;
        }
    }

    // User → Firestore
    public boolean saveUser(User u) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n  \"fields\": {\n");
            appendStringField(sb, "name", u.getName(), true);
            appendStringField(sb, "password", u.getPassword(), true);
            appendStringField(sb, "birthday", u.getBirthday(), true);
            appendStringField(sb, "phone", u.getPhone(), true);
            appendStringField(sb, "emergencyContact", u.getEmergencyContact(), true);
            appendDoubleField(sb, "hourlyWage", u.getHourlyWage(), true);
            appendDoubleField(sb, "weekendExtra", u.getWeekendExtra(), true);
            appendDoubleField(sb, "nightExtra", u.getNightExtra(), true);
            appendDoubleField(sb, "taxDeduction", u.getTaxDeduction(), true);
            appendStringField(sb, "payrollNote", u.getPayrollNote(), false);
            sb.append("\n  }\n}");

            client.patch(userDocPath(), sb.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // helper methods
    private String extractStringValue(String json, String key) {
        // naive regex: "key":{"stringValue":"..."}
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\{\\s*\"stringValue\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : "";
    }

    private double extractDoubleValue(String json, String key, double def) {
        Pattern doublePattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\{\\s*\"doubleValue\"\\s*:\\s*([0-9.+-Ee]+)");
        Matcher m = doublePattern.matcher(json);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException ignored) {}
        }
        Pattern intPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\{\\s*\"integerValue\"\\s*:\\s*\"?([0-9.+-Ee]+)\"?");
        m = intPattern.matcher(json);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException ignored) {}
        }
        return def;
    }

    private void appendStringField(StringBuilder sb, String key, String value, boolean trailingComma) {
        sb.append("    \"").append(key).append("\": {\"stringValue\": \"").append(escape(value)).append("\"}");
        if (trailingComma) sb.append(",");
        sb.append("\n");
    }

    private void appendDoubleField(StringBuilder sb, String key, double value, boolean trailingComma) {
        sb.append("    \"").append(key).append("\": {\"doubleValue\": ").append(value).append("}");
        if (trailingComma) sb.append(",");
        sb.append("\n");
    }

    private String escape(String v) {
        if (v == null) return "";
        return v.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
