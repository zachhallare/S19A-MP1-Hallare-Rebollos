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

    // Deactivates the account and removes all private calendars associated with it.
    public void deactivateAccount() {
        this.active = false;
        ArrayList<Calendar> filteredCalendars = new ArrayList<>();

        // Loop through existing calendars and keep only the public ones.
        for (Calendar cal : calendars) {
            if (cal.isPublic()) {     // If calendar is public.
                filteredCalendars.add(cal);     // Then keep it.
            }
        }

        calendars = filteredCalendars;
    }


    // // Adds a new calendar to the account.
    // public boolean addCalendar(Calendar calendar) {
    //     boolean successfulCreation = false;
    //     // check if calendar with the same name already exists
    //     // cannot use break or return here, :(
    //     for (Calendar cal : calendars) {
    //         if (cal.getName().equals(calendar.getName())) {
    //             successfulCreation = false; // Calendar with the same name exists.
    //         } else {
    //             calendars.add(calendar); // Add the new calendar.
    //             successfulCreation = true; // Calendar with the same name does not exist.
    //         }
    //     }
    //     return successfulCreation;
    // }


    // yo brent i changed up the logic a bit, i put the calendar.add outside the for loop.
    // cus i think it adds the calendar during the loop and could incorrectly allow duplicates?
    // not sure tho :>

    // Adds a new calendar to the account.
    public boolean addCalendar(Calendar calendar) {
        boolean successfulCreation = true;
        // check if calendar with the same name already exists
        // cannot use break or return here, :(
        for (Calendar cal : calendars) {
            if (cal.getName().equals(calendar.getName())) {
                successfulCreation = false; // Calendar with the same name exists.
            }
        }

        if (successfulCreation) {
            calendars.add(calendar); // Add the new calendar.
        }

        return successfulCreation;
    }


    // // Removes a calendar with the specified name from the account.
    // public void removeCalendar(String calendarName) {
    //     // New list to store calendars that should remain.
    //     ArrayList<Calendar> updatedCalendars = new ArrayList<>();
    //     // Loop through all calendars and check if they should be kept.
    //     for (Calendar cal : calendars) {
    //         boolean shouldKeep = !cal.getName().equals(calendarName);
    //         if (shouldKeep) {
    //             updatedCalendars.add(cal);
    //         }
    //     }
    //     calendars = updatedCalendars;
    // }


    // Removes calendar from account's list. 
    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);     // i just realized we can do this lol.
    }
}
