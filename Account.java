// Account.java represents a user account.
// It manages the user credentials and status.

import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private boolean active;
    private ArrayList<Calendar> calendars;

    // Constructor: Initializes user account with needed information.
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
        this.calendars = new ArrayList<>();

        // Create a blank default private calendar after creating an account. 
        // "this" refers to the owner of the calender as it's named after them.
        Calendar defaultCalendar = new Calendar(username, false, this);
        calendars.add(defaultCalendar);
    }

    // Getters.
    public String getUsername() { 
        return username; 
    }
    public String getPassword() { 
        return password; 
    }

    // Returns whether the account is active.
    public boolean isActive() { 
        return active; 
    }
    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }



    // "Deletes" the account and removes all private calendars.
    public void deactivateAccount() {
        active = false;     
        // this can be changed up in the AccountManager so the user can't access their account once it's deactivated.
        ArrayList<Calendar> publicCalendars = new ArrayList<>();
        for (Calendar cal : calendars) {
            if (cal.isPublic()) {     
                publicCalendars.add(cal);     
            }
        }

        calendars.clear();
        calendars.addAll(publicCalendars);
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


    // P.S. i dont think this method was used lol.
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
