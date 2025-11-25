package ui;

import service.UserService;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField txtName;
    private JPasswordField txtPassword;
    private UserService userService = new UserService();

    public LoginFrame() {
        setTitle("ShiftMate - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Username:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        btnLogin.addActionListener(this::onLogin);
        btnRegister.addActionListener(this::onRegister);

        panel.add(btnLogin);
        panel.add(btnRegister);

        add(panel);
        setVisible(true);
    }

    private void onLogin(ActionEvent e) {
        String name = txtName.getText().trim();
        String password = new String(txtPassword.getPassword());

        User user = userService.login(name, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new MainFrame(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Check name/password.");
        }
    }

    private void onRegister(ActionEvent e) {
        String name = txtName.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter name and password.");
            return;
        }

        boolean ok = userService.register(name, password);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registered successfully. You can login now.");
        } else {
            JOptionPane.showMessageDialog(this, "User already exists.");
        }
    }
}
