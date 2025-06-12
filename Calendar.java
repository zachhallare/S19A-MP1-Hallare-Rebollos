// Represents a calendar that belongs to a specific user.
// The calendar can be either public or private.
// It holds a list of entries the user can add later on.

import java.util.ArrayList;

public class Calendar {
    private String name;            // Name of the calendar.
    private boolean isPublic;       // Calendar Visibility: true = public, false = private.
    private Account owner;          // Account that owns this calendar.
    private ArrayList<CalendarEntry> entries;       // Lists of entries.
    
    // Constructor: Calender with name, visibility, and owner.
    public Calendar(String name, boolean isPublic, Account owner) {
        this.name = name;
        this.isPublic = isPublic;
        this.owner = owner;
        this.entries = new ArrayList<>();
    }

    // Displays the calendar's name (not used yet, still can be used).
    public void displayCalendarName() {
        System.out.println("##########################");
        System.out.printf("#\t Calendar Name: %s\t #\n", this.name);
        System.out.println("##########################");
    }

    // Getters.
    public String getName() {
        return name;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public Account getOwner() {
        return owner;
    }
    public ArrayList<CalendarEntry> getEntries() {
        return entries;
    }

    // Add a calendar entry.
    public void addEntry(CalendarEntry entry) {
        entries.add(entry);
    }

    // Remove a calendar entry.
    public void removeEntry(CalendarEntry entry) {
        entries.remove(entry);
    }

    // Delete calender contents.
    public void deleteCalendarEntries() {
        entries.clear();
    } 

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrivate(boolean isPrivate) {
        this.isPublic = !isPrivate;
    }





}
