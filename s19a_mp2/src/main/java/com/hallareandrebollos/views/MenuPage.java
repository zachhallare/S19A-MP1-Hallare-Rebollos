package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;


/**
 * The MenuPage class represents the main menu UI panel of the application,
 * allowing users to navigate to different features such as selecting or adding a calendar,
 * deleting their account, or logging out.
 */
public class MenuPage extends JPanel {
    
    /** Background color for the page. */
    private static final Color BACKGROUND_COLOR = new Color(0xE0E0E0);

    /** Primary text color for titles and labels. */
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    
    /** Font used for main titles. */
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    
    /** Font used for subtitles or secondary text. */
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);


    /**
     * Constructs the MenuPage UI and initializes its components.
     * @param router the Router instance for navigating between pages
     * @param logic  the LogicController instance for managing application logic
     */
    public MenuPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createButtonPanel(router, logic), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    
    /**
     * Creates and returns the panel containing the title and subtitle labels.
     * @return a JPanel with title and subtitle
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(70, 0, 30, 0));

        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Manage your calendars with ease", SwingConstants.CENTER);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(Color.DARK_GRAY);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        return titlePanel;
    }


    /**
     * Creates and returns the panel containing all menu buttons.
     * @param router the Router for page navigation
     * @param logic  the LogicController for executing user actions
     * @return a JPanel with menu buttons
     */
    private JPanel createButtonPanel(Router router, LogicController logic) {
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 275, 0, 275));

        // Select Calendar
        JButton selectButton = createStandardButton("Select Calendar");
        selectButton.addActionListener(e -> router.showCalendarListPage());
        centerPanel.add(selectButton);

        // Add Calendar
        JButton addButton = createStandardButton("Add Calendar");
        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter calendar name:");
            if (name != null && !name.isBlank()) {
                String[] options = {"Default", "Personal", "Family"};
                int type = JOptionPane.showOptionDialog(this,
                        "Choose calendar type:", "Calendar Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                boolean exists = logic.checkCalendarDuplicate(name, true, logic.getCurrentAccount().getUsername());
                if (exists) {
                    type = 5;
                }
                exists = logic.checkCalendarDuplicate(name, false, logic.getCurrentAccount().getUsername());
                if (exists) {
                    type = 5;
                }
                switch (type) {
                    case 0 -> logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, true);
                    case 1 -> logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, false);
                    case 2 -> {
                        int passcode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter family calendar passcode:"));
                        logic.addFamilyCalendar(logic.getCurrentAccount().getUsername(), name, passcode);
                    }
                    default -> JOptionPane.showMessageDialog(this, "Calendar with this name already exists.");
                }
            }
        });
        centerPanel.add(addButton);

        // Account Settings
        JButton settingsButton = createStandardButton("Account Settings");
        settingsButton.addActionListener(e -> showAccountSettingsDialog(router, logic));
        centerPanel.add(settingsButton);

        // Logout
        JButton logoutButton = createStandardButton("Logout");
        logoutButton.addActionListener(e -> {
            logic.logoutAccount();
            router.showLandingPage();
        });
        centerPanel.add(logoutButton);

        return centerPanel;
    }


    /**
     * Creates a standardized button with consistent style.
     * @param text the label for the button
     * @return a styled JButton
     */
    private JButton createStandardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }


    /**
     * Shows the account settings dialog with options for changing password and deleting account.
     * @param router the Router for page navigation
     * @param logic  the LogicController for executing user actions
     */
    private void showAccountSettingsDialog(Router router, LogicController logic) {
        String[] options = {"Change Password", "Delete Account", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose an account setting option:", "Account Settings",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[2]);
        
        switch (choice) {
            case 0 -> handleChangePassword(router, logic);
            case 1 -> handleDeleteAccount(router, logic);
        }
    }


    /**
     * Handles the change password functionality.
     */
    private void handleChangePassword(Router router, LogicController logic) {
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
        if (newPassword != null && !newPassword.isBlank()) {
            logic.changePassword(logic.getCurrentAccount().getUsername(), newPassword);
            JOptionPane.showMessageDialog(this, "Password changed successfully.");
            router.showLandingPage();
        }
    }


    /**
     * Handles the delete account functionality.
     * @param router the Router for page navigation
     * @param logic  the LogicController for executing user actions
     */
    private void handleDeleteAccount(Router router, LogicController logic) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?\nThis action cannot be undone.", 
                "Confirm Account Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            logic.deactivateAccount();
            router.showLandingPage();
        }
    }


    /**
     * Creates and returns the panel containing the footer quote.
     * @return a JPanel with a motivational quote
     */
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 80, 0));

        JLabel quoteLabel = new JLabel("\"A better plan starts with a better menu.\"");
        quoteLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        quoteLabel.setForeground(Color.GRAY);
        footer.add(quoteLabel);

        return footer;
    }
}

