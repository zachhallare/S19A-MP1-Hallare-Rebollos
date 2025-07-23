
package com.hallareandrebollos.services;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.ui.AccountPage;
import com.hallareandrebollos.ui.CalendarListPage;
import com.hallareandrebollos.ui.CalendarPage;
import com.hallareandrebollos.ui.LandingPage;
import com.hallareandrebollos.ui.MenuPage;

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
        this.logicController = new LogicController();
        this.landingPage = new LandingPage(this, this.logicController);
        this.loginPage = new AccountPage(this, this.logicController, true);      // true = login mode.
        this.signupPage = new AccountPage(this, this.logicController, false);   // false = signup mode.
        this.menuPage = new MenuPage(this, this.logicController);
        this.calendarPage = new CalendarPage(this, this.logicController);
        this.calendarListPage = new CalendarListPage(this, this.logicController);

        // Main content panel
        this.contentPanel = new JPanel(new BorderLayout());
        this.setContentPane(this.contentPanel);

        // Initial View.
        showPage(this.landingPage);
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
        showPage(this.landingPage);
    }

    public void showLoginPage() {
        showPage(this.loginPage);
    }

    public void showSignupPage() {
        showPage(this.signupPage);
    }

    public void showMenuPage() {
        showPage(this.menuPage);
    }

    public void showCalendarPage(CalendarObject calendar) {
        this.logicController.setCurrentCalendar(calendar);
        showPage(this.calendarPage);
    }

    public void showCalendarListPage() {
        this.calendarListPage.redrawContents();
        showPage(this.calendarListPage);
    }
}
