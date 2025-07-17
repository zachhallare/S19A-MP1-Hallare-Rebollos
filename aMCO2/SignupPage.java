package aMCO2;

import java.awt.BorderLayout;
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

public class SignupPage extends JPanel {
    public SignupPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

        JLabel usernameLabel = new JLabel("Choose Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Choose Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton signupButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signupButton);
        formPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);

        signupButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            boolean inputsValid = !username.isEmpty() && !password.isEmpty();

            if (!inputsValid) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Signup Failed", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean alreadyExists = logic.existingUsername(username); // check before adding
                if (alreadyExists) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Signup Failed", JOptionPane.WARNING_MESSAGE);
                } else {
                    logic.addAccount(username, password);
                    JOptionPane.showMessageDialog(this, "Account created successfully!");
                    router.showLoginPage();
                }
            }
        });

        backButton.addActionListener(e -> router.showLandingPage());
    }
}
