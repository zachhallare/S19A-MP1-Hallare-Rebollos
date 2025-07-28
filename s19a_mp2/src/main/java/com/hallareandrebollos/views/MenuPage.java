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

public class MenuPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xE0E0E0);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Color ACCENT_COLOR = new Color(0xFF9999);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);

    public MenuPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createButtonPanel(router, logic), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    
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
                String[] options = {"Private", "Public"};
                int type = JOptionPane.showOptionDialog(this,
                        "Choose calendar type:", "Calendar Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                boolean isPublic = (type == 1);
                boolean exists = logic.checkCalendarDuplicate(name, isPublic, logic.getCurrentAccount().getUsername());

                if (!exists) {
                    logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, isPublic);
                    JOptionPane.showMessageDialog(this, "Calendar added!");
                    router.showCalendarListPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Calendar already exists.");
                }
            }
        });
        centerPanel.add(addButton);

        // Delete Account
        JButton deleteAccButton = createStandardButton("Delete Account");
        deleteAccButton.setBackground(ACCENT_COLOR);
        deleteAccButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete your account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                logic.deactivateAccount();
                router.showLandingPage();
            }
        });
        centerPanel.add(deleteAccButton);

        // Logout
        JButton logoutButton = createStandardButton("Logout");
        logoutButton.addActionListener(e -> {
            logic.logoutAccount();
            router.showLandingPage();
        });
        centerPanel.add(logoutButton);

        return centerPanel;
    }


    private JButton createStandardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }


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

