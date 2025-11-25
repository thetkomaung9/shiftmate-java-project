package ui;

import model.Shift;
import model.User;
import service.ShiftService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.List;

public class SchedulePanel extends JPanel {

    private User currentUser;
    private ShiftService shiftService = new ShiftService();
    private JTextField txtDate, txtStart, txtEnd, txtType;
    private DefaultTableModel tableModel;

    public SchedulePanel(User user) {
        this.currentUser = user;
        setLayout(null);

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        lblDate.setBounds(20, 20, 150, 25);
        add(lblDate);
        txtDate = new JTextField();
        txtDate.setBounds(180, 20, 120, 25);
        add(txtDate);

        JLabel lblStart = new JLabel("Start (HH:MM):");
        lblStart.setBounds(20, 50, 150, 25);
        add(lblStart);
        txtStart = new JTextField();
        txtStart.setBounds(180, 50, 120, 25);
        add(txtStart);

        JLabel lblEnd = new JLabel("End (HH:MM):");
        lblEnd.setBounds(20, 80, 150, 25);
        add(lblEnd);
        txtEnd = new JTextField();
        txtEnd.setBounds(180, 80, 120, 25);
        add(txtEnd);

        JLabel lblType = new JLabel("Type (day/night):");
        lblType.setBounds(20, 110, 150, 25);
        add(lblType);
        txtType = new JTextField();
        txtType.setBounds(180, 110, 120, 25);
        add(txtType);

        JButton btnAdd = new JButton("Add Shift");
        btnAdd.setBounds(320, 20, 120, 25);
        btnAdd.addActionListener(this::onAddShift);
        add(btnAdd);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(320, 50, 120, 25);
        btnRefresh.addActionListener(e -> loadTableData());
        add(btnRefresh);

        tableModel = new DefaultTableModel(new Object[]{"Date", "Start", "End", "Type"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 150, 500, 300);
        add(scroll);

        loadTableData();
    }

    private void onAddShift(ActionEvent e) {
        String date = txtDate.getText().trim();
        String start = txtStart.getText().trim();
        String end = txtEnd.getText().trim();
        String type = txtType.getText().trim();

        if (date.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill date, start, end.");
            return;
        }

        Shift shift = new Shift(currentUser.getUserId(), date, start, end, type);
        boolean ok = shiftService.addShift(shift);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Shift added.");
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add shift.");
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Shift> list = shiftService.getShiftsForUser(currentUser.getUserId());
        for (Shift s : list) {
            tableModel.addRow(new Object[]{s.getDate(), s.getPlannedStart(), s.getPlannedEnd(), s.getShiftType()});
        }
    }
}
