package mco2_final;

import java.awt.BorderLayout;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Router class acts as the main JFrame and controller for navigating 
 * between different GUI pages in the Digital Calendar application.
 * It handles the initialization and switching of all major views 
 * such as landing, login, signup, calendar, and entry form pages.
 */
public final class Router extends JFrame {

    /** Logic controller that handles application logic and state. */
    private final LogicController logicController;
    
    /** Landing page shown at application start. */
    private final LandingPage landingPage;

    /** Signup page for creating new accounts. */
    private final AccountPage signupPage;
    
    /** Login page for existing users. */
    private final AccountPage loginPage;
    
    /** Main menu page after successful login. */
    private final MenuPage menuPage;
    
    /** List view of all accessible calendars. */
    private CalendarListPage calendarListPage;
    
    /** Monthly calendar view. */
    private CalendarPage calendarPage;
    
    /** Weekly calendar view. */
    private final WeeklyView weeklyView;

    /** Dynamic page for adding or editing an entry. */
    private EntryForm entryForm;
    
    /** Panel container that holds the current visible view. */
    private final JPanel contentPanel;


    /**
     * Constructs the Router JFrame, initializes all views and sets 
     * the landing page as the default visible screen.
     */
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

    
    /**
     * Returns the LogicController instance for accessing and modifying application logic.
     * @return the logic controller
     */
    public LogicController getLogicController() {
        return this.logicController;
    }

    
    /**
     * Replaces the current content panel with the given JPanel.
     * @param panel the panel to be displayed
     */
    public void showPage(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    /** Displays the landing page. */
    public void showLandingPage() {
        showPage(this.landingPage);
    }


    /** Displays the login page. */
    public void showLoginPage() {
        showPage(this.loginPage);
    }


    /** Displays the signup page. */
    public void showSignupPage() {
        showPage(this.signupPage);
    }


    /** Displays the main menu page. */
    public void showMenuPage() {
        showPage(this.menuPage);
    }


    /**
     * Displays the calendar page for a specific calendar.
     * @param calendar the calendar object to load
     */
    public void showCalendarPage(CalendarObject calendar) {
        this.logicController.setCurrentCalendar(calendar);
        this.calendarPage = new CalendarPage(this, this.logicController);
        showPage(this.calendarPage);
    }


    /** 
     * Displays the calendar list page and refreshes its contents. 
     */
    public void showCalendarListPage() {
        this.calendarListPage.redrawContents();
        showPage(this.calendarListPage);
    }


    /**
     * Displays the entry form for creating a new entry.
     * @param entryToEdit the entry to edit, or null for a new entry
     */
    public void showEntryForm(Entry entryToEdit) {
        showEntryForm(entryToEdit, null);
    }


    /**
     * Displays the entry form with an optional preselected date.
     * @param entryToEdit     the entry to edit, or null for new
     * @param preselectedDate the date to prefill in the form, or null
     */
    public void showEntryForm(Entry entryToEdit, LocalDate preselectedDate) {
        this.entryForm = new EntryForm(this, this.logicController, entryToEdit, preselectedDate);
        showPage(this.entryForm);
    }


    /**
     * Displays the weekly calendar view starting from a given date.
     * If no date is specified, shows the current week.
     * @param startDate the date to anchor the weekly view, or null for current date
     */
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
