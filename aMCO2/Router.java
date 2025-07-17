package aMCO2;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Router {
    private JFrame frame;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private MenuPage menuPage;
    private CalendarPage calendarPage;
    private EntryPage entryPage;
    private LogicController logic;

    public Router() {
        logic = new LogicController();

        // Main Frame.
        frame = new JFrame("Calendar App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // center on screen
        frame.setLayout(new BorderLayout());

        // Pass the Router so pages can switch views.
        landingPage = new LandingPage(this, logic);
        loginPage = new LoginPage(this, logic);
        signupPage = new SignupPage(this, logic);
        menuPage = new MenuPage(this, logic);
        calendarPage = new CalendarPage(this, logic);
        entryPage = new EntryPage(this, logic);

        setPage(landingPage);
        frame.setVisible(true);
    }

    // Page switching method
    public void setPage(JPanel page) {
        frame.setContentPane(page);
        frame.revalidate();
        frame.repaint();
    }

    // Page getters (used by child pages to navigate)
    public void showLandingPage() {
        setPage(landingPage);
    }

    public void showLoginPage() {
        setPage(loginPage);
    }

    public void showSignupPage() {
        setPage(signupPage);
    }

    public void showMenuPage() {
        setPage(menuPage);
    }

    public void showCalendarPage() {
        setPage(calendarPage);
    }

    public void showEntryPage() {
        setPage(entryPage);
    }
}
