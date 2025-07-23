package com.hallareandrebollos.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.hallareandrebollos.services.LogicController;
import com.hallareandrebollos.services.Router;

public class LandingPage extends JPanel {
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 40;
    private static final int GAP = 60;

    public LandingPage(Router router, LogicController logic) {
        setLayout(null);
        setBackground(new Color(0xD3D3D3)); // Light grey

        // Title Label.
        JLabel titleLabel = new JLabel("Digital Calendar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x36454F));
        titleLabel.setBounds(50, 60, 400, 40);
        add(titleLabel);

        // Login Button.
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(170, 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        styleButton(loginButton);
        loginButton.addActionListener(e -> router.showLoginPage());
        add(loginButton);
        
        // Signup Button.
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(170, 150 + GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
        styleButton(signupButton);
        signupButton.addActionListener(e -> router.showSignupPage());
        add(signupButton);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        button.setFocusable(false);
    }
}
