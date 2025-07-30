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


/**
 * LandingPage is the initial screen of the Digital Calendar app.
 * It presents the title, subtitle, and two main options for the user: Login or Sign Up.
 */
public class LandingPage extends JPanel {

    /** Background color for the landing page */
    private static final Color BACKGROUND_COLOR = new Color(0xD3D3D3);

    /** Foreground color for titles and labels */
    private static final Color FOREGROUND_COLOR = new Color(0x36454F);

    /** Font used for the main title */
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);

    /** Font used for the subtitle below the title */
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);


    /**
     * Constructs the LandingPage and lays out all UI components.
     * @param router Router used to navigate to other views
     * @param logic LogicController for accessing application logic (not used here, but may be in future)
     */
    public LandingPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createCenterPanel(router), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }


    /**
     * Creates the top panel containing the main title and subtitle.
     * @return a JPanel with title and subtitle labels
     */
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


    /**
     * Creates the center panel containing the "Login" and "Sign Up" buttons.
     * @param router Router used to switch views when buttons are clicked
     * @return a JPanel with two vertically stacked buttons
     */
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


    /**
     * Creates the footer panel that displays a motivational quote.
     * @return a JPanel containing a quote label
     */
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


    /**
     * Styles the given JButton with consistent font, background, and other visual properties.
     * @param button the JButton to style
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 22));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
    }
}
