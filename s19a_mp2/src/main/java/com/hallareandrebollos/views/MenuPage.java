package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Theme;


/**
 * The MenuPage class represents the main menu UI panel of the application,
 * allowing users to navigate to different features such as selecting or adding a calendar,
 * deleting their account, or logging out.
 */
public class MenuPage extends JPanel {
    
    /** Reference to the LogicController for theme access. */
    private final LogicController logic;
    
    /** Reference to the Router for navigation. */
    private final Router router;


    /**
     * Constructs the MenuPage UI and initializes its components.
     * @param router the Router instance for navigating between pages
     * @param logic  the LogicController instance for managing application logic
     */
    public MenuPage(Router router, LogicController logic) {
        this.router = router;
        this.logic = logic;
        
        setLayout(new BorderLayout());
        applyTheme();

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createButtonPanel(router, logic), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Applies the current theme to this panel.
     */
    private void applyTheme() {
        Theme theme = logic.getCurrentTheme();
        setBackground(theme.getBackgroundColor());
    }

    
    /**
     * Creates and returns the panel containing the title and subtitle labels.
     * @return a JPanel with title and subtitle
     */
    private JPanel createTitlePanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(70, 0, 30, 0));

        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(theme.getTitleFont());
        titleLabel.setForeground(theme.getForegroundColor());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Manage your calendars with ease", SwingConstants.CENTER);
        subtitleLabel.setFont(theme.getSubtitleFont());
        subtitleLabel.setForeground(theme.getSubtitleColor());
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

        // Account Settings
        JButton settingsButton = createStandardButton("Account Settings");
        settingsButton.addActionListener(e -> showAccountSettingsDialog(router, logic));
        centerPanel.add(settingsButton);

        // Change Theme
        JButton themeButton = createStandardButton("Change Theme");
        themeButton.addActionListener(e -> showThemeSelectionDialog());
        centerPanel.add(themeButton);

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
        Theme theme = logic.getCurrentTheme();
        
        JButton button = new JButton(text);
        button.setFont(theme.getButtonFont());
        button.setFocusPainted(false);
        button.setBackground(theme.getPrimaryButtonColor());
        button.setForeground(theme.getButtonTextColor());
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }


    /**
     * Shows the theme selection dialog with all available themes.
     */
    private void showThemeSelectionDialog() {
        Theme.ThemeType[] themes = Theme.getAllThemes();
        
        JDialog dialog = new JDialog();
        dialog.setTitle("Choose a Theme");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        
        // Apply current theme to dialog
        Theme currentTheme = logic.getCurrentTheme();
        dialog.getContentPane().setBackground(currentTheme.getBackgroundColor());
        
        JLabel titleLabel = new JLabel("Select Theme", SwingConstants.CENTER);
        titleLabel.setFont(currentTheme.getTitleFont());
        titleLabel.setForeground(currentTheme.getForegroundColor());
        titleLabel.setBorder(new EmptyBorder(20, 20, 10, 20));
        dialog.add(titleLabel, BorderLayout.NORTH);
        
        JPanel themePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        themePanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        themePanel.setOpaque(false);
        
        for (Theme.ThemeType themeType : themes) {
            Theme previewTheme = new Theme(themeType);
            JButton themeButton = createThemePreviewButton(themeType, previewTheme);
            themeButton.addActionListener(e -> {
                logic.changeTheme(themeType);
                refreshUI();
                dialog.dispose();
            });
            themePanel.add(themeButton);
        }
        
        dialog.add(themePanel, BorderLayout.CENTER);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(currentTheme.getButtonFont());
        cancelButton.setBackground(currentTheme.getSecondaryButtonColor());
        cancelButton.setForeground(currentTheme.getButtonTextColor());
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Creates a theme preview button showing the theme's actual colors.
     */
    private JButton createThemePreviewButton(Theme.ThemeType themeType, Theme previewTheme) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(250, 100));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        
        // Set the button's background to the theme's background
        button.setBackground(previewTheme.getBackgroundColor());
        
        // Create preview content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel nameLabel = new JLabel(Theme.getThemeName(themeType), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(previewTheme.getForegroundColor());
        
        JPanel colorPreview = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        colorPreview.setOpaque(false);
        
        // Show small color swatches
        JPanel primarySwatch = new JPanel();
        primarySwatch.setBackground(previewTheme.getPrimaryButtonColor());
        primarySwatch.setPreferredSize(new Dimension(20, 15));
        
        JPanel panelSwatch = new JPanel();
        panelSwatch.setBackground(previewTheme.getPanelColor());
        panelSwatch.setPreferredSize(new Dimension(20, 15));
        
        JPanel accentSwatch = new JPanel();
        accentSwatch.setBackground(previewTheme.getAccentColor());
        accentSwatch.setPreferredSize(new Dimension(20, 15));
        
        colorPreview.add(primarySwatch);
        colorPreview.add(panelSwatch);
        colorPreview.add(accentSwatch);
        
        contentPanel.add(nameLabel, BorderLayout.CENTER);
        contentPanel.add(colorPreview, BorderLayout.SOUTH);
        
        button.add(contentPanel);
        
        return button;
    }

    /**
     * Refreshes the UI to apply the new theme.
     */
    private void refreshUI() {
        // Refresh this panel
        applyTheme();
        
        // Remove and recreate all components to apply theme
        removeAll();
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createButtonPanel(router, logic), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
        
        revalidate();
        repaint();
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
        Theme theme = logic.getCurrentTheme();
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 80, 0));

        JLabel quoteLabel = new JLabel("\"A better plan starts with a better menu.\"");
        quoteLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        quoteLabel.setForeground(theme.getSubtitleColor());
        footer.add(quoteLabel);

        return footer;
    }
}

