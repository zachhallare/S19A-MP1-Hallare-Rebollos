package com.hallareandrebollos.views;

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

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;

public class LandingPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);

    public LandingPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createCenterPanel(router), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }


    private JPanel createTitlePanel() {
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
        
        return titlePanel;
    }


    private JPanel createCenterPanel(Router router) {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 35));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 275, 20, 275));
        add(centerPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> router.showLoginPage());

        JButton signupButton = new JButton("Sign Up");
        styleButton(signupButton);
        signupButton.addActionListener(e -> router.showSignupPage());

        centerPanel.add(loginButton);
        centerPanel.add(signupButton);

        return centerPanel;
    }


    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(40, 0, 90, 0));

        JLabel quoteLabel = new JLabel("\"Your time is valuable. Use it wisely.\"");
        quoteLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        quoteLabel.setForeground(Color.GRAY);
        footer.add(quoteLabel);

        return footer;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 22));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
    }
}
