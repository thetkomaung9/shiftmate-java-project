package ui;

import model.User;
import model.WorkLog;
import service.WorkLogService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.List;

public class WorkLogPanel extends JPanel {

    private User currentUser;
    private WorkLogService workLogService = new WorkLogService();
    private JTextField txtDate, txtIn, txtOut, txtTired, txtNote, txtMonth;
    private DefaultTableModel tableModel;

    public WorkLogPanel(User user) {
        this.currentUser = user;
        setLayout(null);

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        lblDate.setBounds(20, 20, 150, 25);
        add(lblDate);
        txtDate = new JTextField();
        txtDate.setBounds(180, 20, 120, 25);
        add(txtDate);

        JLabel lblIn = new JLabel("Clock In (HH:MM):");
        lblIn.setBounds(20, 50, 150, 25);
        add(lblIn);
        txtIn = new JTextField();
        txtIn.setBounds(180, 50, 120, 25);
        add(txtIn);

        JLabel lblOut = new JLabel("Clock Out (HH:MM):");
        lblOut.setBounds(20, 80, 150, 25);
        add(lblOut);
        txtOut = new JTextField();
        txtOut.setBounds(180, 80, 120, 25);
        add(txtOut);

        JLabel lblTired = new JLabel("Tired (1-5):");
        lblTired.setBounds(20, 110, 150, 25);
        add(lblTired);
        txtTired = new JTextField();
        txtTired.setBounds(180, 110, 120, 25);
        add(txtTired);

        JLabel lblNote = new JLabel("Note:");
        lblNote.setBounds(20, 140, 150, 25);
        add(lblNote);
        txtNote = new JTextField();
        txtNote.setBounds(180, 140, 200, 25);
        add(txtNote);

        JButton btnAdd = new JButton("Add Log");
        btnAdd.setBounds(400, 20, 120, 25);
        btnAdd.addActionListener(this::onAddLog);
        add(btnAdd);

        JLabel lblMonth = new JLabel("View Month (YYYY-MM):");
        lblMonth.setBounds(20, 180, 170, 25);
        add(lblMonth);
        txtMonth = new JTextField();
        txtMonth.setBounds(200, 180, 100, 25);
        add(txtMonth);

        JButton btnLoad = new JButton("Load Logs");
        btnLoad.setBounds(320, 180, 120, 25);
        btnLoad.addActionListener(e -> loadTableData());
        add(btnLoad);

        tableModel = new DefaultTableModel(new Object[]{"Date", "In", "Out", "Tired", "Note"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 220, 600, 230);
        add(scroll);
    }

    private void onAddLog(ActionEvent e) {
        try {
            String date = txtDate.getText().trim();
            String in = txtIn.getText().trim();
            String out = txtOut.getText().trim();
            int tired = Integer.parseInt(txtTired.getText().trim());
            String note = txtNote.getText().trim();

            WorkLog log = new WorkLog(currentUser.getUserId(), date, in, out, tired, note);
            boolean ok = workLogService.addLog(log);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Work log added.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add work log.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        String month = txtMonth.getText().trim();
        if (month.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter month like 2025-11");
            return;
        }
        List<WorkLog> list = workLogService.getLogsByMonth(currentUser.getUserId(), month);
        for (WorkLog w : list) {
            tableModel.addRow(new Object[]{w.getDate(), w.getClockIn(), w.getClockOut(), w.getTiredLevel(), w.getNote()});
        }
    }
}
