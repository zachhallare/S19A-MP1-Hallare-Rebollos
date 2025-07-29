
package com.hallareandrebollos.controls;

import java.awt.BorderLayout;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.views.AccountPage;
import com.hallareandrebollos.views.CalendarListPage;
import com.hallareandrebollos.views.CalendarPage;
import com.hallareandrebollos.views.EntryForm;
import com.hallareandrebollos.views.LandingPage;
import com.hallareandrebollos.views.MenuPage;
import com.hallareandrebollos.views.WeeklyView;

public final class Router extends JFrame {
    // Main Controller.
    private final LogicController logicController;
    
    // Individual Pages.
    private final LandingPage landingPage;
    private final AccountPage signupPage;
    private final AccountPage loginPage;
    private final MenuPage menuPage;
    private CalendarListPage calendarListPage;
    private CalendarPage calendarPage;
    private final WeeklyView weeklyView;

    // Dynamic Pages.
    private EntryForm entryForm;
    
    // Container for Switching Views.
    private final JPanel contentPanel;

    public Router() {
        setTitle("Digital Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Logic Controller.
        this.logicController = new LogicController();

        // Individual Pages.
        this.landingPage = new LandingPage(this, this.logicController);
        this.loginPage = new AccountPage(this, this.logicController, true);      // true = login mode.
        this.signupPage = new AccountPage(this, this.logicController, false);   // false = signup mode.
        this.menuPage = new MenuPage(this, this.logicController);
        this.calendarPage = new CalendarPage(this, this.logicController);
        this.calendarListPage = new CalendarListPage(this, this.logicController);
        this.weeklyView = new WeeklyView(this, this.logicController);

        // Main Content Panel.
        this.contentPanel = new JPanel(new BorderLayout());
        this.setContentPane(this.contentPanel);

        // Show Initial Page.
        showPage(this.landingPage);
        setVisible(true);
    }

    // Expose the logic controller.
    public LogicController getLogicController() {
        return this.logicController;
    }

    
    // Main Method for Showing Pages.
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
        this.calendarPage = new CalendarPage(this, this.logicController);
        showPage(this.calendarPage);
    }

    public void showCalendarListPage() {
        this.calendarListPage.redrawContents();
        showPage(this.calendarListPage);
    }

    public void showEntryForm(Entry entryToEdit) {
        showEntryForm(entryToEdit, null);
    }

    public void showEntryForm(Entry entryToEdit, LocalDate preselectedDate) {
        this.entryForm = new EntryForm(this, this.logicController, entryToEdit, preselectedDate);
        showPage(this.entryForm);
    }

    public void showWeeklyView(LocalDate startDate) {
        this.weeklyView.updateWeekView();
        if (startDate != null) {
            this.weeklyView.moveToSpecificWeek(startDate);
        } else {
            this.weeklyView.moveToSpecificWeek(LocalDate.now());
            
        }
        showPage(this.weeklyView);
    }
}
