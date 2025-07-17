package aMCO2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LandingPage extends JPanel {
    public LandingPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to the Digital Calendar!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 10, 30, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        signupButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Login.
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                router.showLoginPage();
            }
        });

        // Sign Up.
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                router.showSignupPage();
            }
        });
    }
}
