package com.hallareandrebollos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.services.LogicController;
import com.hallareandrebollos.services.Router;

public class LandingPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);

    public LandingPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Title Section (North).
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(80, 0, 30, 0));

        JLabel titleLabel = new JLabel("Digital Calendar", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Plan smart. Stay on track.", SwingConstants.CENTER);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(Color.DARK_GRAY);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        // Button Section (Center).
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 35));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 275, 20, 275));
        add(centerPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 22));
        loginButton.setFocusPainted(false);
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.addActionListener(e -> router.showLoginPage());

        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("SansSerif", Font.PLAIN, 22));
        signupButton.setFocusPainted(false);
        signupButton.setBackground(Color.LIGHT_GRAY);
        signupButton.addActionListener(e -> router.showSignupPage());

        centerPanel.add(loginButton);
        centerPanel.add(signupButton);

        // Footer Panel.
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(40, 0, 90, 0));

        JLabel quoteLabel = new JLabel("\"Your time is valuable. Use it wisely.\"");
        quoteLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        quoteLabel.setForeground(Color.GRAY);
        footer.add(quoteLabel);
        add(footer, BorderLayout.SOUTH);
    }
}
