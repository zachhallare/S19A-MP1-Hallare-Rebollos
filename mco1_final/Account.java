import java.util.ArrayList;

/**
 * Represents a user account in the digital calendar.
 * Each account has a username, password, active status, and a list of owned calendars.
 */
public class Account {
    /** Username of the account. */
    private String username;       
    
    /** Password of the account. */
    private String password; 
    
    /** Indicates if the account is active. */
    private boolean isActive;

    /** List of calendars owned by the account. */
    private ArrayList<String> ownedCalendars; 


    /**
     * Constructs a new Account with the given username and password.
     * The account is set to active by default and starts with no owned calendars.
     * @param username The username for the account.
     * @param password The password for the account.
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.isActive = true;
        this.ownedCalendars = new ArrayList<>();
    }

    /**
     * Adds a calendar to the list of calendars owned by this account,
     * if it is not already present.
     * @param calendarName The name of the calendar to add.
     */
    public void addOwnedCalendar(String calendarName) {
        if (!ownedCalendars.contains(calendarName)) {
            ownedCalendars.add(calendarName);
        }
    }

    /**
     * Returns the list of calendar names owned by this account.
     * @return A list of owned calendar names.
     */
    public ArrayList<String> getOwnedCalendars() {
        return this.ownedCalendars;
    }

    /**
     * Returns whether the account is currently active.
     * @return {@code true} if the account is active, {@code false} otherwise.
     */
    public boolean getIsActive() {
        return this.isActive;
    }

    /**
     * Returns the username of the account.
     * @return The account's username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Authenticates the account using the given username and password.
     * @param username The username to check.
     * @param password The password to check.
     * @return true if the credentials match this account, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        return this.password.equals(password) && this.username.equals(username);
    }

    /**
     * Sets the active status of the account.
     * @param isActive {@code true} to activate the account, {@code false} to deactivate it.
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void removeOwnedCalendar(String calendarName) {
        ownedCalendars.remove(calendarName);
    }
}
