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

        // Default private calendar name after the username.
        Calendar defaultCalendar = new Calendar(username, false, this);     // "this" passes itself as the owner.
        calendars.add(defaultCalendar);
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
    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

    // Deactivates the account and removes all private calendars.
    public void deactivateAccount() {
        active = false;
        ArrayList<Calendar> publicCalendars = new ArrayList<>();

        for (Calendar cal : calendars) {
            if (cal.isPublic()) {     
                publicCalendars.add(cal);     
            }
        }

        calendars = publicCalendars;
    }


    // Add calendar to account if name doesn't conflict.
    public boolean addCalendar(Calendar calendar) {
        boolean exists = false;

        for (Calendar cal : calendars) {
            if (cal.getName().equals(calendar.getName())) {
                exists = true; 
            }
        }

        if (!exists) {
            calendars.add(calendar); 
        }

        return !exists;
    }


    // Removes calendar from account's list. 
    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);    
    }


    // Check if account owns a calendar
    public boolean ownsCalendar(Calendar calendar) {
        boolean owns = false;

        for (Calendar cal : calendars) {
            if (cal == calendar && cal.getOwner() == this) {
                owns = true;
            }
        }

        return owns;
    }
}
