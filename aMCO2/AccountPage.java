package aMCO2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.EtchedBorder;

public class AccountPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 16);

    public AccountPage(Router router, LogicController logic, boolean isLoginPage) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Title Panel (North).
        JLabel titleLabel = new JLabel(isLoginPage ? "Login" : "Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel (Center).
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 200, 30, 200));
        
        // Labels for Username and Password.
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        JPasswordField passwordField = new JPasswordField();

        // Buttons Initialization.
        JButton mainButton = createStyledButton(isLoginPage ? "Login" : "Sign Up");
        JButton backButton = createStyledButton("Back");

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


    // Make the Buttons Aesthetic.
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return button;
    }


}
