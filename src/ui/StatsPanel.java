package ui;

import model.User;
import model.WorkLog;
import service.WorkLogService;

import javax.swing.*;
import java.util.List;

public class StatsPanel extends JPanel {

    private User currentUser;
    private WorkLogService workLogService = new WorkLogService();
    private JTextField txtMonth;
    private JTextArea txtResult;

    public StatsPanel(User user) {
        this.currentUser = user;
        setLayout(null);

        JLabel lblMonth = new JLabel("Month (YYYY-MM):");
        lblMonth.setBounds(20, 20, 150, 25);
        add(lblMonth);

        txtMonth = new JTextField();
        txtMonth.setBounds(180, 20, 100, 25);
        add(txtMonth);

        JButton btnCalc = new JButton("Calculate");
        btnCalc.setBounds(300, 20, 120, 25);
        btnCalc.addActionListener(e -> calculateStats());
        add(btnCalc);

        txtResult = new JTextArea();
        txtResult.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResult);
        scroll.setBounds(20, 60, 400, 300);
        add(scroll);
    }

    private void calculateStats() {
        String month = txtMonth.getText().trim();
        if (month.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter month like 2025-11");
            return;
        }

        List<WorkLog> logs = workLogService.getLogsByMonth(currentUser.getUserId(), month);
        if (logs.isEmpty()) {
            txtResult.setText("No logs for this month.");
            return;
        }

        int totalLogs = logs.size();
        int totalTired = 0;
        for (WorkLog w : logs) {
            totalTired += w.getTiredLevel();
        }
        double avgTired = totalLogs > 0 ? (double) totalTired / totalLogs : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("Total logs: ").append(totalLogs).append("\n");
        sb.append("Average tired level: ").append(String.format("%.2f", avgTired)).append("\n");
        sb.append("\nDetailed logs:\n");
        for (WorkLog w : logs) {
            sb.append(w.getDate())
                    .append("  In: ").append(w.getClockIn())
                    .append("  Out: ").append(w.getClockOut())
                    .append("  Tired: ").append(w.getTiredLevel())
                    .append("\n");
        }

        txtResult.setText(sb.toString());
    }
}
