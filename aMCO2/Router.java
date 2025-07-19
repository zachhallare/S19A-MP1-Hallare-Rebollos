package aMCO2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class Router extends JFrame {
    private final LandingPage landingPage;
    private final AccountPage loginPage;
    private final AccountPage signupPage;
    private final MenuPage menuPage;
    // private final CalendarPage calendarPage;
    private final LogicController controller;


    public Router() {
        setTitle("Digital Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Individual pages.
        controller = new LogicController();
        landingPage = new LandingPage(this, controller);
        loginPage = new AccountPage(this, controller, true);      // true = login mode.
        signupPage = new AccountPage(this, controller, false);   // false = signup mode.
        menuPage = new MenuPage(this, controller);
        // calendarPage = new CalendarPage(this, logic);

        // Initial View.
        showPage(landingPage, 500, 400);
        setVisible(true);
    }


    // Page Switching Method.
    public void showPage(JPanel panel, int width, int height) {
        setSize(width, height);
        setResizable(false);
        setContentPane(panel);
        revalidate();     // recalculate component sizes and positions.
        repaint();        // redraws the component on the screen.
        setLocationRelativeTo(null);         // Puts in to the center screen.
    }


    // Navigation Methods.
    public void showLandingPage() {
        showPage(landingPage, 500, 400);
    }

    public void showLoginPage() {
        showPage(loginPage, 500, 400);
    }

    public void showSignupPage() {
        showPage(signupPage, 500, 400);
    }

    public void showMenuPage() {
        showPage(menuPage, 800, 600);
    }

    // public void showCalendarPage() {
    //     showPage(calendarPage, 800, 600);
    // }

    // public void showEntryPage() {
    //     showPage(entryPage, 800, 600);
    // }
}
