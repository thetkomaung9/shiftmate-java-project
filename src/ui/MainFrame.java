package ui;

import javax.swing.*;
import model.User;

public class MainFrame extends JFrame {

    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("ShiftMate - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Schedule", new SchedulePanel(currentUser));
        tabPane.addTab("Work Log", new WorkLogPanel(currentUser));
        tabPane.addTab("Statistics", new StatsPanel(currentUser));

        // ✅ Correct line
        tabPane.addTab("Profile", new ProfilePanel(currentUser));

        add(tabPane);
        setVisible(true);
    }
}
