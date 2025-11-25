package ui;

import model.User;
import service.UserCloudService;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private final UserCloudService userCloudService = new UserCloudService();
    private User currentUser;

    private JTextField txtEmail, txtName, txtBirthday, txtPhone, txtEmergency;
    private JTextField txtHourly, txtWeekend, txtNight, txtTax, txtPayrollNote;
    private JTextField txtExpectedHours;
    private JLabel lblPreview;

    public ProfilePanel(User user) {
        this.currentUser = user;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        JLabel title = new JLabel("User Profile & Payroll Settings");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridwidth = 1;

        // Email
        addLabel(gbc, "Email:", 0, row);
        txtEmail = new JTextField(user.getUsername());
        txtEmail.setEditable(false);
        addField(gbc, txtEmail, 1, row++);

        // Name
        addLabel(gbc, "Name:", 0, row);
        txtName = new JTextField(user.getName());
        addField(gbc, txtName, 1, row++);

        // Birthday
        addLabel(gbc, "Birthday (YYYY-MM-DD):", 0, row);
        txtBirthday = new JTextField(user.getBirthday());
        addField(gbc, txtBirthday, 1, row++);

        // Phone
        addLabel(gbc, "Phone:", 0, row);
        txtPhone = new JTextField(user.getPhone());
        addField(gbc, txtPhone, 1, row++);

        // Emergency contact
        addLabel(gbc, "Emergency Contact:", 0, row);
        txtEmergency = new JTextField(user.getEmergencyContact());
        addField(gbc, txtEmergency, 1, row++);

        // Separator
        addSeparator(gbc, "Payroll Settings", row++);
        
        // Hourly wage
        addLabel(gbc, "Hourly Wage:", 0, row);
        txtHourly = new JTextField(String.valueOf(user.getHourlyWage()));
        addField(gbc, txtHourly, 1, row++);

        // Weekend extra
        addLabel(gbc, "Weekend Extra (%):", 0, row);
        txtWeekend = new JTextField(String.valueOf(user.getWeekendExtra() * 100));
        addField(gbc, txtWeekend, 1, row++);

        // Night extra
        addLabel(gbc, "Night Extra (%):", 0, row);
        txtNight = new JTextField(String.valueOf(user.getNightExtra() * 100));
        addField(gbc, txtNight, 1, row++);

        // Tax deduction
        addLabel(gbc, "Tax Deduction (%):", 0, row);
        txtTax = new JTextField(String.valueOf(user.getTaxDeduction() * 100));
        addField(gbc, txtTax, 1, row++);

        // Payroll note
        addLabel(gbc, "Payroll Note:", 0, row);
        txtPayrollNote = new JTextField(user.getPayrollNote());
        addField(gbc, txtPayrollNote, 1, row++);

        // Separator
        addSeparator(gbc, "Salary Preview", row++);

        // Expected hours
        addLabel(gbc, "Expected Hours (monthly):", 0, row);
        txtExpectedHours = new JTextField("160");
        addField(gbc, txtExpectedHours, 1, row++);

        addLabel(gbc, "Preview:", 0, row);
        lblPreview = new JLabel("-");
        addField(gbc, lblPreview, 1, row++);

        JButton btnPreview = new JButton("Preview Salary");
        JButton btnSave = new JButton("Save Profile");

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnPreview);
        btnPanel.add(btnSave);
        add(btnPanel, gbc);

        btnPreview.addActionListener(e -> computeSalary());
        btnSave.addActionListener(e -> saveUser());
    }

    private void addLabel(GridBagConstraints gbc, String text, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private void addField(GridBagConstraints gbc, JComponent comp, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        add(comp, gbc);
    }

    private void addSeparator(GridBagConstraints gbc, String title, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel lbl = new JLabel(" " + title);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        add(lbl, gbc);
        gbc.gridwidth = 1;
    }

    private void computeSalary() {
        try {
            double wage = Double.parseDouble(txtHourly.getText());
            double weekend = Double.parseDouble(txtWeekend.getText()) / 100.0;
            double night = Double.parseDouble(txtNight.getText()) / 100.0;
            double tax = Double.parseDouble(txtTax.getText()) / 100.0;
            double hours = Double.parseDouble(txtExpectedHours.getText());

            // simple preview
            double weekendHours = hours * 0.2;
            double nightHours = hours * 0.1;
            double normalHours = hours - weekendHours - nightHours;

            double gross =
                    normalHours * wage +
                    weekendHours * wage * (1 + weekend) +
                    nightHours * wage * (1 + night);

            double net = gross - (gross * tax);

            lblPreview.setText(String.format("Gross: %.0f / Net: %.0f", gross, net));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void saveUser() {
        try {
            currentUser.setName(txtName.getText());
            currentUser.setBirthday(txtBirthday.getText());
            currentUser.setPhone(txtPhone.getText());
            currentUser.setEmergencyContact(txtEmergency.getText());

            currentUser.setHourlyWage(Double.parseDouble(txtHourly.getText()));
            currentUser.setWeekendExtra(Double.parseDouble(txtWeekend.getText()) / 100.0);
            currentUser.setNightExtra(Double.parseDouble(txtNight.getText()) / 100.0);
            currentUser.setTaxDeduction(Double.parseDouble(txtTax.getText()) / 100.0);
            currentUser.setPayrollNote(txtPayrollNote.getText());

            boolean ok = userCloudService.saveUser(currentUser);
            if (ok) JOptionPane.showMessageDialog(this, "Saved!");
            else JOptionPane.showMessageDialog(this, "Save failed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input error: " + ex.getMessage());
        }
    }
}
