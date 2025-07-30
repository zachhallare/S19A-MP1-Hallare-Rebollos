package mco2_final;

import java.awt.BorderLayout;
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


/**
 * Represents the login/signup GUI page where users can either log in to an existing account
 * or create a new account.
 */
public class AccountPage extends JPanel {

    /** Reference to the LogicController for theme access. */
    private final LogicController logic;
    
    /** Button for submitting login or signup. */
    private JButton mainButton;

    /** Button for navigating back to the landing page. */
    private JButton backButton;


    /**
     * Constructs the account page.
     * @param router       the router used to switch between views
     * @param logic        the logic controller for authentication and account operations
     * @param isLoginPage  true if displaying login UI; false if displaying sign-up UI
     */
    public AccountPage(Router router, LogicController logic, boolean isLoginPage) {
        this.logic = logic;
        setLayout(new BorderLayout());
        applyTheme();

        add(createTitlePanel(isLoginPage), BorderLayout.NORTH);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel formPanel = createFormPanel(usernameField, passwordField, isLoginPage, router, logic);
        add(formPanel, BorderLayout.CENTER);

        addButtonLogic(usernameField, passwordField, isLoginPage, router, logic);
    }

    /**
     * Applies the current theme to this panel.
     */
    private void applyTheme() {
        Theme theme = logic.getCurrentTheme();
        setBackground(theme.getBackgroundColor());
    }


    /**
     * Creates the title panel.
     * @param isLoginPage whether the page is for login
     * @return a JPanel with the title
     */
    private JPanel createTitlePanel(boolean isLoginPage) {
        Theme theme = logic.getCurrentTheme();
        
        JLabel titleLabel = new JLabel(isLoginPage ? "Login" : "Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(theme.getTitleFont());
        titleLabel.setForeground(theme.getForegroundColor());
        titleLabel.setBorder(new EmptyBorder(80, 0, 20, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        return titlePanel;
    }


    /**
     * Creates the form panel containing input fields and buttons.
     * @param usernameField input field for the username
     * @param passwordField input field for the password
     * @param isLoginPage   true if for login; false if for signup
     * @param router        router to switch pages
     * @param logic         logic controller for authentication and account creation
     * @return the form panel
     */
    private JPanel createFormPanel(JTextField usernameField, JPasswordField passwordField, boolean isLoginPage, Router router, LogicController logic) {
        Theme theme = this.logic.getCurrentTheme();
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 50));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 220, 30, 220));

        // Username.
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(theme.getSubtitleFont());
        usernameLabel.setForeground(theme.getTextColor());
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setFont(theme.getRegularFont());
        usernameField.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Password.
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(theme.getSubtitleFont());
        passwordLabel.setForeground(theme.getTextColor());
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setFont(theme.getRegularFont());
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


    /**
     * Creates a toggle button to show/hide the password.
     * @param passwordField the password field to toggle visibility
     * @return the toggle button
     */
    private JButton createToggleButton(JPasswordField passwordField) {
        Theme theme = logic.getCurrentTheme();
        
        JButton toggleButton = new JButton("Show");
        toggleButton.setFocusPainted(false);
        toggleButton.setFont(theme.getRegularFont());
        toggleButton.setBackground(theme.getPanelColor());
        toggleButton.setForeground(theme.getSubtitleColor());
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


    /**
     * Creates the main login/signup button.
     * @param isLoginPage true if login; false if signup
     * @return the button
     */
    private JButton createMainButton(boolean isLoginPage) {
        Theme theme = logic.getCurrentTheme();
        
        JButton button = new JButton(isLoginPage ? "Login" : "Sign Up");
        button.setFont(theme.getButtonFont());
        button.setFocusPainted(false);
        button.setBackground(theme.getPrimaryButtonColor());
        button.setForeground(theme.getButtonTextColor());

        return button;
    }


    /**
     * Creates the back button to return to the landing page.
     * @param usernameField the username input field
     * @param passwordField the password input field
     * @param router        router used for navigation
     * @return the back button
     */
    private JButton createBackButton(JTextField usernameField, JPasswordField passwordField, Router router) {
        Theme theme = logic.getCurrentTheme();
        
        JButton button = new JButton("Back");
        button.setFont(theme.getButtonFont());
        button.setFocusPainted(false);
        button.setBackground(theme.getSecondaryButtonColor());
        button.setForeground(theme.getButtonTextColor());
        button.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            router.showLandingPage();
        });

        return button;
    }


    /**
     * Adds logic to the main login/signup button to handle authentication and account creation.
     * @param usernameField input field for the username
     * @param passwordField input field for the password
     * @param isLoginPage   true if login; false if signup
     * @param router        router for navigation
     * @param logic         logic controller for account operations
     */
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
                        logic.addCalendarObject(username, username, false);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.",
                    isLoginPage ? "Login Failed" : "Signup Failed", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
