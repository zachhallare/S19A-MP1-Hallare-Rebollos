
package com.hallareandrebollos.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.hallareandrebollos.services.LogicController;
import com.hallareandrebollos.services.Router;

public class AccountPage extends JPanel {
    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 40;
    private static final int START_Y = 130;
    private static final int GAP_Y = 50;

    public AccountPage(Router router, LogicController logic, boolean isLoginPage) {
        setLayout(null);
        setBackground(new Color(0xD3D3D3));

        // Title.
        String title = isLoginPage ? "Login" : "Create an Account";
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x36454F));
        titleLabel.setBounds(50, 60, 400, 40);
        add(titleLabel);

        // Username.
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(90, START_Y, 100, 30);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(usernameLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(190, START_Y, FIELD_WIDTH, FIELD_HEIGHT);
        add(usernameField);

        // Password.
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(90, START_Y + GAP_Y, 100, 30);
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(190, START_Y + GAP_Y, FIELD_WIDTH, FIELD_HEIGHT);
        add(passwordField);

        // Main Button Layout (Login or Signup).
        String mainButtonText = isLoginPage ? "Login" : "Sign Up";
        JButton mainButton = new JButton(mainButtonText);
        mainButton.setBounds(70, START_Y + 2 * GAP_Y + 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        styleButton(mainButton);
        add(mainButton);

        // Back to Landing Page Button Layout.
        JButton backButton = new JButton("Back");
        backButton.setBounds(250, START_Y + 2 * GAP_Y + 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        styleButton(backButton);
        add(backButton);


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


    // Make the Buttons Aesthetic.
    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEtchedBorder());
    }


}
