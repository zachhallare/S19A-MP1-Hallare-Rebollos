package com.hallareandrebollos.controls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.hallareandrebollos.models.Account;
import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.FamilyCalendar;
import com.hallareandrebollos.models.Journal;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;
import com.hallareandrebollos.models.Theme;

/**
 * Controls the logic of the calendar application, including account management,
 * calendar management, and entry operations.
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

    /** Currently selected month (1–12) for calendar view. */
    private int selectedMonth;

    /** Currently selected year for calendar view. */
    private int selectedYear;

    /** Currently selected day (1–31) for calendar view. */
    private int selectedDay;

    /** Current application theme. */
    private Theme currentTheme;


    /**
     * Constructs a LogicController and initializes all lists and indices.
     */
    public LogicController() {
        this.accounts = new ArrayList<>();
        this.CalendarObjects = new ArrayList<>();
        this.accountIndex = -1; // No account is logged in initially
        this.CalendarObjectIndex = -1; // No CalendarObject is selected initially
        this.currentTheme = new Theme(); // Initialize with default light theme
    }


    /**
     * Authenticates the provided username and password. If matched, sets the
     * accountIndex to the corresponding user.
     * @param username The input username.
     * @param password The input password.
     * @return true if authentication is successful and the account is active, and false otherwise.
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
     * Adds a new calendar to the system if it does not already exist.
     * The calendar can be public or private and will be associated with the given user.
     * If a calendar with the same name and visibility already exists, it will not be added.
     * @param username the username of the account creating the calendar.
     * @param CalendarObjectName the name/title of the calendar to be created.
     * @param isPublic true if the calendar should be public; false if it should be private.
     */
    public void addCalendarObject(String username, String CalendarObjectName, boolean isPublic) {
        if (!checkCalendarDuplicate(CalendarObjectName, isPublic, username)) {
            CalendarObject CalendarObject = new CalendarObject(CalendarObjectName, isPublic);
            CalendarObjects.add(CalendarObject);
            int accountIndex = getAccountFromIndex(username);
            if (accountIndex >= 0 && accountIndex < accounts.size()) {
                Account account = accounts.get(accountIndex);
                account.addOwnedCalendar(CalendarObjectName);
            } else {
                System.out.println("Account not found: " + username);
            }
        } else {
            System.out.println("Duplicate Found. Ignoring creation of calendar.");
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
            if (this.accountIndex >= 0 && this.accountIndex < accounts.size()) {
                Account currentAccount = accounts.get(this.accountIndex);
                String calendarName = CalendarObjects.get(this.CalendarObjectIndex).getCalendarName();
                currentAccount.removeOwnedCalendar(calendarName);
            CalendarObjects.remove(this.CalendarObjectIndex);
            }
            this.CalendarObjectIndex = -1;
        }
    }


    /**
     * Adds a new entry to the currently selected calendar.
     * @param title Title of the entry.
     * @param description Description of the entry.
     * @param startTime Start time in milliseconds since epoch.
     * @param endTime End time in milliseconds since epoch.
     */
    public void addEntryToCurrentCalendarObject(String type, String title, String description, LocalDate date, 
                                                String priority, String status, String createdBy, String finishedBy,
                                                String venue, String organizer, String modality, String link, 
                                                LocalTime startTime, LocalTime endTime) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            Entry entry = null;
            boolean canAdd = false;

            if (type != null) {
                type = type.toLowerCase();
                
                if (type.equals("task")) {
                    if (priority != null && status != null && createdBy != null) {
                        if (finishedBy != null && !finishedBy.isBlank()) {
                            entry = new Task(title, date, description, priority, status, createdBy, finishedBy);
                        } else {
                            entry = new Task(title, date, description, priority, status, createdBy);
                        }
                        canAdd = true;
                    }
                    
                } else if (type.equals("event")) {
                    if (venue != null && organizer != null && startTime != null && endTime != null) {
                        entry = new Event(title, date, description, venue, organizer, startTime, endTime);
                        canAdd = true;
                    }

                } else if (type.equals("meeting")) {
                    if (modality != null) {
                        if ((venue == null || venue.isBlank()) && (link == null || link.isBlank())) {
                            entry = new Meeting(title, date, description, modality, startTime, endTime);
                        } else {
                            entry = new Meeting(title, date, startTime, endTime, description, modality, venue, link);
                        }
                        canAdd = true;
                    }

                } else if (type.equals("journal")) {
                    boolean journalExists = false;
                    for (Entry e : currentCalendarObject.getEntries()) {
                        if (e instanceof Journal && e.getDate().equals(date)) {
                            journalExists = true;
                        }
                    }
                    if (!journalExists && description != null && !description.isBlank()) {
                        entry = new Journal(title, date, description);
                        canAdd = true;
                    }
                }
            }
            
            if (canAdd && entry != null) {
                currentCalendarObject.addEntry(entry);
            }
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
     * Retrieves all entries from the currently selected calendar that match the given date.
     * @param date The date to filter entries by.
     * @return A list of entries scheduled for the specified date. Returns an empty list if no calendar
     *         is selected or no entries match the date.
     */
    public ArrayList<Entry> getEntriesForDate(LocalDate date) {
        ArrayList<Entry> result = new ArrayList<>();

        if (CalendarObjectIndex >= 0 && CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject current = CalendarObjects.get(CalendarObjectIndex);
            ArrayList<Entry> entries = current.getEntries();

            for (Entry e : entries) {
                if (e.getDate().equals(date)) {
                    result.add(e);
                }
            }
        }

        return result;
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
     * Sets the current calendar object.
     * @param calendar The calendar object to set as current.
     */
    public void setCurrentCalendar(CalendarObject calendar) {
        if (calendar != null) {
            this.CalendarObjectIndex = CalendarObjects.indexOf(calendar);
        } else {
            this.CalendarObjectIndex = -1; // No calendar selected
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
        Account currentAccount = getCurrentAccount();
        if (currentAccount == null) return privateCalendarObjects;
        for (CalendarObject calendarObject : this.CalendarObjects) {
            if (!calendarObject.isPublic() && currentAccount.getOwnedCalendars().contains(calendarObject.getCalendarName())) {
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


    /**
     * Returns the index of the account with the specified username.
     * @param username the username to search for.
     * @return the index of the matching account, or -1 if not found.
     */
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
    

    /**
     * Sets the selected month for calendar operations.
     * @param month the month to set (1–12). Ignored if out of range.
     */
    public void setSelectedMonth(int month) {
        if (month >= 1 && month <= 12) {
            this.selectedMonth = month;
        }
    }


    /**
     * Returns the currently selected month.
     * @return the selected month (1–12).
     */
    public int getSelectedMonth() {
        return this.selectedMonth;
    }


    /**
     * Sets the selected year for calendar operations.
     *
     * @param year the year to set (must be between 1970 and 2998). Ignored if out of range.
     */
    public void setSelectedYear(int year) {
        if (year >= 1970 && year < 2999) {
            this.selectedYear = year;
        }
    }


    /**
     * Returns the currently selected year.
     *
     * @return the selected year.
     */
    public int getSelectedYear() {
        return this.selectedYear;
    }


    /**
     * Sets the selected day for calendar operations.
     * @param day the day to set (1–31). Ignored if out of range.
     */
    public void setSelectedDay(int day) {
        if (day >= 1 && day <= 31) {
            this.selectedDay = day;
        }
    }


    /**
     * Returns the currently selected day.
     * @return the selected day (1–31).
     */
    public int getSelectedDay() {
        return this.selectedDay;
    }


    /**
     * Checks whether a calendar with the specified name and visibility already exists.
     * Public calendar names must be globally unique.
     * Private calendar names must be unique across all private calendars.
     * @param calendarName the name of the calendar to check.
     * @param isPublic true if checking for a public calendar; false if private.
     * @param username the username of the account attempting to create the calendar.
     * @return true if a duplicate exists; false otherwise.
     */
    public boolean checkCalendarDuplicate(String calendarName, boolean isPublic, String username) {
        boolean isDuplicate = false;
        for (CalendarObject obj : this.CalendarObjects) {
            if (obj.getCalendarName().equals(calendarName)) {
            if (isPublic && obj.isPublic()) {
                isDuplicate = true;
            }
            if (!isPublic && !obj.isPublic()) {
                isDuplicate = true;
            }
            }
        }
        return isDuplicate;
    }

    /**
     * Creates a new {@code FamilyCalendar} with the specified name and passcode,
     * and adds it to the system.
     *
     * @param username     The username of the account creating the family calendar.
     * @param calendarName The name of the family calendar.
     * @param passcode     The numeric passcode required to join or access the calendar.
     */
    public void addFamilyCalendar(String username, String calendarName, int passcode) {
        FamilyCalendar familyCalendar = new FamilyCalendar(passcode, calendarName, true);
        addCalendarInstance(familyCalendar);
    }


    /**
     * Checks whether the currently selected calendar is a {@code FamilyCalendar}.
     *
     * @return {@code true} if the current calendar is a FamilyCalendar; {@code false} otherwise.
     */
    public boolean checkCurrentCalendarIsFamily() {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendar = CalendarObjects.get(this.CalendarObjectIndex);
            return currentCalendar instanceof FamilyCalendar;
        }
        return false;
    }


    /**
     * Retrieves the currently selected calendar as a {@code FamilyCalendar}, if applicable.
     *
     * @return The current {@code FamilyCalendar}, or {@code null} if none is selected
     *         or if the selected calendar is not a FamilyCalendar.
     */
    public FamilyCalendar getCurrentFamilyCalendar() {
        if (checkCurrentCalendarIsFamily()) {
            CalendarObject currentCalendar = CalendarObjects.get(this.CalendarObjectIndex);
            if (currentCalendar instanceof FamilyCalendar) {
                return (FamilyCalendar) currentCalendar;
            }
        }
        return null; // No FamilyCalendar is selected
    }

    public void changePassword(String username, String newPassword) {
        int accountIndex = getAccountFromIndex(username);
        if (accountIndex >= 0 && accountIndex < accounts.size()) {
            Account account = accounts.get(accountIndex);
            account.setPassword(newPassword);
        } else {
            System.out.println("Account not found: " + username);
        }
    }

    /**
     * Returns the current application theme.
     * @return The current Theme instance
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Sets the current application theme.
     * @param theme The new Theme to apply
     */
    public void setCurrentTheme(Theme theme) {
        this.currentTheme = theme;
    }

    /**
     * Changes the current theme to the specified theme type.
     * @param themeType The theme type to switch to
     */
    public void changeTheme(Theme.ThemeType themeType) {
        this.currentTheme.applyTheme(themeType);
    }
}
