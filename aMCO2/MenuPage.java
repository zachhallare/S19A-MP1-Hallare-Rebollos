package aMCO2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPage extends JPanel {
    public MenuPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton calendarButton = new JButton("View Calendar");
        JButton entryButton = new JButton("Manage Entries");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(calendarButton);
        buttonPanel.add(entryButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);


        // PS: work in progress.
        // calendarButton.addActionListener(e -> router.showCalendarPage());
        // entryButton.addActionListener(e -> router.showEntryPage());
        // logoutButton.addActionListener(e -> {
        //     logic.logout(); 
        //     router.showLandingPage();
        // });
    }
}
