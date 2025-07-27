package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;

public class AccountPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 20);
    private JButton mainButton;
    private JButton backButton;

    public AccountPage(Router router, LogicController logic, boolean isLoginPage) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createTitlePanel(isLoginPage), BorderLayout.NORTH);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel formPanel = createFormPanel(usernameField, passwordField, isLoginPage, router, logic);
        add(formPanel, BorderLayout.CENTER);

        addButtonLogic(usernameField, passwordField, isLoginPage, router, logic);
    }


    private JPanel createTitlePanel(boolean isLoginPage) {
        JLabel titleLabel = new JLabel(isLoginPage ? "Login" : "Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titleLabel.setBorder(new EmptyBorder(80, 0, 20, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        return titlePanel;
    }


    private JPanel createFormPanel(JTextField usernameField, JPasswordField passwordField, boolean isLoginPage, Router router, LogicController logic) {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 50));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 220, 30, 220));

        // Username.
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        usernameField.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Password.
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passwordField.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Password Panel with toggle.
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(createToggleButton(passwordField), BorderLayout.EAST);

        // Buttons.
        mainButton = createMainButton(isLoginPage);
        backButton = createBackButton(usernameField, passwordField, router);

        // Add components.
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordPanel);
        formPanel.add(mainButton);
        formPanel.add(backButton);

        return formPanel;
    }


    private JButton createToggleButton(JPasswordField passwordField) {
        JButton toggleButton = new JButton("Show");
        toggleButton.setFocusPainted(false);
        toggleButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        toggleButton.setBackground(Color.WHITE);
        toggleButton.setForeground(FOREGROUND_COLOR.darker());
        toggleButton.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        toggleButton.addActionListener(ev -> {
            if (passwordField.getEchoChar() != (char) 0) {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("Hide");
            } else {
                passwordField.setEchoChar('\u2022');
                toggleButton.setText("Show");
            }
        });

        return toggleButton;
    }


    private JButton createMainButton(boolean isLoginPage) {
        JButton button = new JButton(isLoginPage ? "Login" : "Sign Up");
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);

        return button;
    }


    private JButton createBackButton(JTextField usernameField, JPasswordField passwordField, Router router) {
        JButton button = new JButton("Back");
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            router.showLandingPage();
        });

        return button;
    }


    private void addButtonLogic(JTextField usernameField, JPasswordField passwordField, boolean isLoginPage, Router router, LogicController logic) {
        mainButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
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
            } else {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.",
                    isLoginPage ? "Login Failed" : "Signup Failed", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
