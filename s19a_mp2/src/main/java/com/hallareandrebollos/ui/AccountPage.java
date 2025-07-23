package com.hallareandrebollos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.services.LogicController;
import com.hallareandrebollos.services.Router;

public class AccountPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 20);

    final private JButton mainButton;
    final private JButton backButton;

    public AccountPage(Router router, LogicController logic, boolean isLoginPage) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Title Panel (North).
        JLabel titleLabel = new JLabel(isLoginPage ? "Login" : "Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titleLabel.setBorder(new EmptyBorder(80, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel (Center).
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 50));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 220, 30, 220));
        
        // Labels for Username and Password.
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        usernameField.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passwordField.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Buttons Initialization.
        mainButton = new JButton(isLoginPage ? "Login" : "Sign Up");
        mainButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        mainButton.setFocusPainted(false);
        mainButton.setBackground(Color.LIGHT_GRAY);

        backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.LIGHT_GRAY);

        // Form Panels Initialization.
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(mainButton);
        formPanel.add(backButton);
        add(formPanel, BorderLayout.CENTER);

        // Main Button Logic.
        mainButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.",
                    isLoginPage ? "Login Failed" : "Signup Failed", JOptionPane.WARNING_MESSAGE);
            } else {
                if (isLoginPage) {
                    if (logic.authenticateAccount(username, password)) {
                        JOptionPane.showMessageDialog(this, "Login successful!");
                        usernameField.setText("");
                        passwordField.setText("");
                        router.showMenuPage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.", 
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (logic.existingUsername(username)) {
                        JOptionPane.showMessageDialog(this, "Username already exists.",
                            "Signup Failed", JOptionPane.WARNING_MESSAGE);
                    } else {
                        logic.addAccount(username, password);
                        JOptionPane.showMessageDialog(this, "Account created successfully!");
                        usernameField.setText("");
                        passwordField.setText("");
                        router.showLandingPage();
                    }
                }
            }
        });

        // Back Button Logic.
        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            router.showLandingPage();
        });
    }
}
