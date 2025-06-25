
import java.util.ArrayList;

public class LogicController {
    private ArrayList<Account> accounts; // List of user accounts
    private ArrayList<CalendarObject> CalendarObjects; // List of CalendarObjects
    private int accountIndex; // Index of the currently logged-in account
    private int CalendarObjectIndex; // Index of the currently selected CalendarObject

    public LogicController() {
        this.accounts = new ArrayList<>();
        this.CalendarObjects = new ArrayList<>();
        this.accountIndex = -1; // No account is logged in initially
        this.CalendarObjectIndex = -1; // No CalendarObject is selected initially
    }
    // gets date in local timezone
    // converts to UTC
    // returns month in UTC
    // ensures matches with milliseconds since epoch
    // returns month as int 0-11
    public int getCurrentMonth() {
        long currentTimeMillis = System.currentTimeMillis();
        java.util.Calendar CalendarLogic = java.util.Calendar.getInstance();
        CalendarLogic.setTimeInMillis(currentTimeMillis);
        return CalendarLogic.get(java.util.Calendar.MONTH);
    }

    // gets date in local timezone
    // converts to UTC
    // returns year in UTC
    // ensures matches with milliseconds since epoch
    // returns year as int e.g. 2025
    public int getCurrentYear() {
        long currentTimeMillis = System.currentTimeMillis();
        java.util.Calendar CalendarLogic = java.util.Calendar.getInstance();
        CalendarLogic.setTimeInMillis(currentTimeMillis);
        return CalendarLogic.get(java.util.Calendar.YEAR);
    }

    public void authenticateAccount(String username, String password) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.authenticate(username, password)) {
                this.accountIndex = i;
                i += accounts.size();
            }
        }
    }

    public void logoutAccount() {
        this.accountIndex = -1;
        this.CalendarObjectIndex = -1;
    }

    public void addAccount(String username, String password) {
        Account account = new Account(username, password);
        addCalendarObject(username, false, getCurrentYear());
        accounts.add(account);
    }

    public void deactivateAccount() {
        if (this.accountIndex >= 0 && this.accountIndex < accounts.size()) {
            Account account = accounts.get(this.accountIndex);
            account.setIsActive(false);
        }
    }

    // ensures no duplicate usernames
    // returns true if username already exists
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

    public void addCalendarObject(String CalendarObjectName, boolean isPublic, int yearIdentifier) {
        CalendarObject CalendarObject = new CalendarObject(CalendarObjectName, isPublic, yearIdentifier);
        CalendarObjects.add(CalendarObject);
    }

    public boolean addCalendarInstance(CalendarObject calendarObject) {
        if (calendarObject != null) {
            CalendarObjects.add(calendarObject);
            return true;
        }
        return false;
    }

    public void removeCurrentCalendarObject() {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObjects.remove(this.CalendarObjectIndex);
            this.CalendarObjectIndex = -1;
        }
    }

    public void addEntryToCurrentCalendarObject(String title, String description, long startTime, long endTime) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            Entry entry = new Entry(title, description);
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            currentCalendarObject.addEntry(entry);
        }
    }

    public void editEntryInCurrentCalendarObject(Entry oldEntry, Entry newEntry) {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            CalendarObject currentCalendarObject = CalendarObjects.get(this.CalendarObjectIndex);
            currentCalendarObject.editEntry(oldEntry, newEntry);
        }
    }

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

    public void convertToPublic(String CalendarObjectName) {
        for (int i = 0; i < this.CalendarObjects.size(); i++) {
            if (this.CalendarObjects.get(i).getCalendarName().equals(CalendarObjectName)) {
                CalendarObjects.get(i).setIsPublic(true);
                i += this.CalendarObjects.size();
            }
        }
        System.out.println("CalendarObject not found.");
    }

    public void setAccountIndex(int index) {
        if (index >= 0 && index < accounts.size()) {
            this.accountIndex = index;
        } else {
            System.out.println("Invalid account index.");
        }
    }

    public void setCalendarObjectIndex(int index) {
        if (index >= 0 && index < CalendarObjects.size()) {
            this.CalendarObjectIndex = index;
        } else {
            System.out.println("Invalid CalendarObject index.");
        }
    }

    public Account getCurrentAccount() {
        if (this.accountIndex >= 0 && this.accountIndex < accounts.size()) {
            return accounts.get(this.accountIndex);
        }
        return null; // No account is logged in
    }

    public ArrayList<CalendarObject> getPublicCalendarObjects() {
        ArrayList<CalendarObject> publicCalendarObjects = new ArrayList<>();
        for (CalendarObject calendarObject : CalendarObjects) {
            if (calendarObject.isPublic()) {
                publicCalendarObjects.add(calendarObject);
            }
        }
        return publicCalendarObjects;
    }

    public ArrayList<CalendarObject> getPrivateCalendarObjects() {
        ArrayList<CalendarObject> privateCalendarObjects = new ArrayList<>();
        for (CalendarObject calendarObject : this.CalendarObjects) {
            if (!calendarObject.isPublic() && this.getCurrentAccount().getOwnedCalendars().contains(calendarObject.getCalendarName())) {
                privateCalendarObjects.add(calendarObject);
            }
        }
        return privateCalendarObjects;
    }

    public CalendarObject getCurrentCalendarObject() {
        if (this.CalendarObjectIndex >= 0 && this.CalendarObjectIndex < CalendarObjects.size()) {
            return CalendarObjects.get(this.CalendarObjectIndex);
        }
        return null; // No CalendarObject is selected
    }

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
}
