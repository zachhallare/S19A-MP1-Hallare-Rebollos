import java.util.ArrayList;

/**
 * Controls the logic of the calendar application, including account
 * management, calendar management, and entry operations.
 */
public class LogicController {
    /** List of all user accounts. */
    private ArrayList<Account> accounts; 

    /** List of all calendar objects (both public and private). */
    private ArrayList<CalendarObject> CalendarObjects;

    /** Index of the currently logged-in account. */
    private int accountIndex;

    /** Index of the currently selected calendar object. */
    private int CalendarObjectIndex; 

    private int selectedMonth;
    private int selectedYear;


    /**
     * Constructs a LogicController and initializes all lists and indices.
     */
    public LogicController() {
        this.accounts = new ArrayList<>();
        this.CalendarObjects = new ArrayList<>();
        this.accountIndex = -1; // No account is logged in initially
        this.CalendarObjectIndex = -1; // No CalendarObject is selected initially
    }
    

    /**
     * Returns the current month (0–11) in the system's time zone.
     * @return The current month index.
     */
    public int getCurrentMonth() {
        long currentTimeMillis = System.currentTimeMillis();
        java.util.Calendar CalendarLogic = java.util.Calendar.getInstance();
        CalendarLogic.setTimeInMillis(currentTimeMillis);
        return CalendarLogic.get(java.util.Calendar.MONTH);
    }

    /**
     * Authenticates the provided username and password.
     * If matched, sets the accountIndex to the corresponding user.
     * @param username The input username.
     * @param password The input password.
     */
    public boolean authenticateAccount(String username, String password) {
        boolean isAuthenticated = false;
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.authenticate(username, password) && account.getIsActive()) {
                this.accountIndex = i;
                i += accounts.size();
                isAuthenticated = true;
            }
        }
        return isAuthenticated;
    }

    /**
     * Logs out the current account and clears selected calendar.
     */
    public void logoutAccount() {
        this.accountIndex = -1;
        this.CalendarObjectIndex = -1;
    }

    /**
     * Adds a new account with a default calendar for the current year.
     * @param username The username for the new account.
     * @param password The password for the new account.
     */
    public void addAccount(String username, String password) {
        Account account = new Account(username, password);
        accounts.add(account);
    }

    /**
     * Deactivates the currently logged-in account.
     */
    public void deactivateAccount() {
        if (this.accountIndex >= 0 && this.accountIndex < accounts.size()) {
            Account account = accounts.get(this.accountIndex);
            account.setIsActive(false);
        }
    }

    /**
     * Checks if a username already exists.
     * @param username The username to check.
     * @return true if username exists, false otherwise.
     */
    public boolean existingUsername(String username) {
        boolean isUsernameExisting = false;
        for (int i = 0; i < this.accounts.size(); i++) {
            if (this.accounts.get(i).getUsername().equals(username)) {
                isUsernameExisting = true;
                i += this.accounts.size();
            }
        }
        return isUsernameExisting;
    }

    /**
     * Adds a new calendar object to the system.
     * @param CalendarObjectName The name of the calendar.
     * @param isPublic           Whether the calendar is public.
     */
    public void addCalendarObject(String username, String CalendarObjectName, boolean isPublic) {
        CalendarObject CalendarObject = new CalendarObject(CalendarObjectName, isPublic);
        CalendarObjects.add(CalendarObject);
        int accountIndex = getAccountFromIndex(username);
        if (accountIndex >= 0 && accountIndex < accounts.size()) {
            Account account = accounts.get(accountIndex);
            account.addOwnedCalendar(CalendarObjectName);
        } else {
            System.out.println("Account not found: " + username);
        }
    }

    /**
     * Adds an existing CalendarObject to the system.
     * @param calendarObject The CalendarObject to add.
     * @return true if successfully added, false if null.
     */
    public boolean addCalendarInstance(CalendarObject calendarObject) {
        if (calendarObject != null) {
            CalendarObjects.add(calendarObject);
            return true;
        }
        return false;
    }

    /**
     * Removes the currently selected calendar from the system.
     */
    public void removeCurrentCalendarObject() {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObjects.remove(this.CalendarObjectIndex);
            this.CalendarObjectIndex = -1;
        }
    }

    /**
     * Adds a new entry to the currently selected calendar.
     * @param title       Title of the entry.
     * @param description Description of the entry.
     * @param startTime   Start time in milliseconds since epoch.
     * @param endTime     End time in milliseconds since epoch.
     */
    public void addEntryToCurrentCalendarObject(String title, String description, long startTime, long endTime) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            Entry entry = new Entry(title, description);
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            currentCalendarObject.addEntry(entry);
        }
    }

    /**
     * Edits an existing entry in the currently selected calendar.
     * @param oldEntry The entry to replace.
     * @param newEntry The new entry data.
     */
    public void editEntryInCurrentCalendarObject(Entry oldEntry, Entry newEntry) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            currentCalendarObject.editEntry(oldEntry, newEntry);
        }
    }

    /**
     * Removes an entry from the currently selected calendar based on title.
     * @param title Title of the entry to remove.
     */
    public void removeEntryFromCurrentCalendarObject(String title) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            ArrayList<Entry> entries = currentCalendarObject.getEntries();
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getTitle().equals(title)) {
                    currentCalendarObject.removeEntry(entries.get(i));
                    i += entries.size();
                }
            }
        }
    }

    /**
     * Converts a calendar to public visibility by name.
     * @param CalendarObjectName Name of the calendar to convert.
     */
    public void convertToPublic(String CalendarObjectName) {
        for (int i = 0; i < this.CalendarObjects.size(); i++) {
            if (this.CalendarObjects.get(i).getCalendarName().equals(CalendarObjectName)) {
                CalendarObjects.get(i).setIsPublic(true);
                i += this.CalendarObjects.size();
            }
        }
        System.out.println("CalendarObject not found.");
    }

    /**
     * Sets the current account index.
     * @param index Index to set as current account.
     */
    public void setAccountIndex(int index) {
        if (index >= 0 && index < accounts.size()) {
            this.accountIndex = index;
        } else {
            System.out.println("Invalid account index.");
        }
    }

    /**
     * Sets the current calendar object index.
     * @param index Index to set as current calendar.
     */
    public void setCalendarObjectIndex(int index) {
        if (index >= 0 && index < CalendarObjects.size()) {
            this.CalendarObjectIndex = index;
        } else {
            System.out.println("Invalid CalendarObject index.");
        }
    }

    /**
     * Returns the currently logged-in account.
     * @return The current Account or null if none.
     */
    public Account getCurrentAccount() {
        if (this.accountIndex >= 0 && this.accountIndex < accounts.size()) {
            return accounts.get(this.accountIndex);
        }
        return null; // No account is logged in
    }

    /**
     * Returns all public calendars in the system.
     * @return List of public CalendarObjects.
     */
    public ArrayList<CalendarObject> getPublicCalendarObjects() {
        ArrayList<CalendarObject> publicCalendarObjects = new ArrayList<>();
        for (CalendarObject calendarObject : this.CalendarObjects) {
            if (calendarObject.isPublic()) {
                publicCalendarObjects.add(calendarObject);
            }
        }
        return publicCalendarObjects;
    }

    /**
     * Returns all private calendars owned by the current account.
     * @return List of private CalendarObjects owned by the user.
     */
    public ArrayList<CalendarObject> getPrivateCalendarObjects() {
        ArrayList<CalendarObject> privateCalendarObjects = new ArrayList<>();
        for (CalendarObject calendarObject : this.CalendarObjects) {
            if (!calendarObject.isPublic() && this.getCurrentAccount().getOwnedCalendars().contains(calendarObject.getCalendarName())) {
                privateCalendarObjects.add(calendarObject);
            }
        }
        return privateCalendarObjects;
    }

    /**
     * Returns the currently selected calendar object.
     * @return The selected CalendarObject or null if none.
     */
    public CalendarObject getCurrentCalendarObject() {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            return CalendarObjects.get(this.CalendarObjectIndex);
        }
        return null; // No CalendarObject is selected
    }

    /**
     * Returns the index of a calendar by name and visibility.
     * @param CalendarObjectName Name of the calendar.
     * @param isPublic Whether the calendar is public.
     * @return Index of the matching calendar or -1 if not found.
     */
    public int getCalendarFromName(String CalendarObjectName, boolean isPublic) {
        int foundIdx = -1;
        for (int i = 0; i < this.CalendarObjects.size(); i++) {
            if (this.CalendarObjects.get(i).getCalendarName().equals(CalendarObjectName) && this.CalendarObjects.get(i).isPublic() == isPublic) {
                foundIdx = i;
                i += this.CalendarObjects.size();
            }
        }
        return foundIdx;
    }

    public int getAccountFromIndex(String username) {
        int foundIdx = -1;
        for (int i = 0; i < this.accounts.size(); i++) {
            if (this.accounts.get(i).getUsername().equals(username)) {
                foundIdx = i;
                i += this.accounts.size();
            }
        }
        return foundIdx;
    }

    public void setSelectedMonth(int month) {
        if (month >= 0 && month < 12) {
            this.selectedMonth = month;
        }
    }

    public int getSelectedMonth() {
        return this.selectedMonth;
    }

    public void setSelectedYear(int year) {
        if (year >= 1970 && year < 2999) {
            this.selectedYear = year;
        }
    }

    public int getSelectedYear() {
        return this.selectedYear;
    }
}
