package com.hallareandrebollos.views;

import java.awt.BorderLayout;
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
import com.hallareandrebollos.models.Theme;


/**
 * LandingPage is the initial screen of the Digital Calendar app.
 * It presents the title, subtitle, and two main options for the user: Login or Sign Up.
 */
public class LandingPage extends JPanel {

    /** Reference to the LogicController for theme access. */
    private final LogicController logic;


    /**
     * Constructs the LandingPage and lays out all UI components.
     * @param router Router used to navigate to other views
     * @param logic LogicController for accessing application logic and themes
     */
    public LandingPage(Router router, LogicController logic) {
        this.logic = logic;
        setLayout(new BorderLayout());
        applyTheme();

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createCenterPanel(router), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Applies the current theme to this panel.
     */
    private void applyTheme() {
        Theme theme = logic.getCurrentTheme();
        setBackground(theme.getBackgroundColor());
    }


    /**
     * Creates the top panel containing the main title and subtitle.
     * @return a JPanel with title and subtitle labels
     */
    private JPanel createTitlePanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(80, 0, 30, 0));

        JLabel titleLabel = new JLabel("Digital Calendar", SwingConstants.CENTER);
        titleLabel.setFont(theme.getTitleFont());
        titleLabel.setForeground(theme.getForegroundColor());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Plan smart. Stay on track.", SwingConstants.CENTER);
        subtitleLabel.setFont(theme.getSubtitleFont());
        subtitleLabel.setForeground(theme.getSubtitleColor());
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
        Theme theme = logic.getCurrentTheme();
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(40, 0, 90, 0));

        JLabel quoteLabel = new JLabel("\"Your time is valuable. Use it wisely.\"");
        quoteLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        quoteLabel.setForeground(theme.getSubtitleColor());
        footer.add(quoteLabel);

        return footer;
    }


    /**
     * Styles the given JButton with consistent font, background, and other visual properties.
     * @param button the JButton to style
     */
    private void styleButton(JButton button) {
        Theme theme = logic.getCurrentTheme();
        
        button.setFont(theme.getButtonFont());
        button.setFocusPainted(false);
        button.setBackground(theme.getPrimaryButtonColor());
        button.setForeground(theme.getButtonTextColor());
    }
}
