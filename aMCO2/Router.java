package aMCO2;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Router extends JFrame {
    private final LandingPage landingPage;
    private final AccountPage loginPage;
    private final AccountPage signupPage;
    private final MenuPage menuPage;
    private final CalendarPage calendarPage;
    private final LogicController logicController;
    private final CalendarListPage calendarListPage;
    private final JPanel contentPanel;

    public Router() {
        setTitle("Digital Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        // Individual pages.
        logicController = new LogicController();
        landingPage = new LandingPage(this, logicController);
        loginPage = new AccountPage(this, logicController, true);      // true = login mode.
        signupPage = new AccountPage(this, logicController, false);   // false = signup mode.
        menuPage = new MenuPage(this, logicController);
        calendarPage = new CalendarPage(this, logicController);
        calendarListPage = new CalendarListPage(this, logicController);

        // Main content panel
        contentPanel = new JPanel(new BorderLayout());
        setContentPane(contentPanel);

        // Initial View.
        showPage(landingPage);
        setVisible(true);
    }

    // Page Switching Method.
    public void showPage(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Navigation Methods.
    public void showLandingPage() {
        showPage(landingPage);
    }

    public void showLoginPage() {
        showPage(loginPage);
    }

    public void showSignupPage() {
        showPage(signupPage);
    }

    public void showMenuPage() {
        showPage(menuPage);
    }

    public void showCalendarPage() {
        showPage(calendarPage);
    }
}
