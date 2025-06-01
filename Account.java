// Account class for account info.

import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private boolean active;
    private ArrayList<Calendar> calendars;

    // Constructor.
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
        this.calendars = new ArrayList<>();
    }

    // Getters.
    public String getUsername() { 
        return username; 
    }
    public String getPassword() { 
        return password; 
    }
    public boolean isActive() { 
        return active; 
    }

    // Deactivate Account Method.
    public void deactivateAccount() {
        this.active = false;
        calendars.removeIf(Calendar :: isPrivate);
    }

    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    public void removeCalendar(String calendarName) {
        calendars.removeIf(cal -> cal.getName().equals(calendarName));
    }
}
