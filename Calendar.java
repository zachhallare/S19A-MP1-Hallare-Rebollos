import java.util.ArrayList;

public class Calendar {
    private String name;
    private boolean isPublic;      // true = public, false = private.
    private Account owner;
    private ArrayList<CalendarEntry> entries;
    
    public Calendar(String name, boolean isPublic, Account owner) {
        this.name = name;
        this.isPublic = isPublic;
        this.owner = owner;
        this.entries = new ArrayList<>();
    }

    // display top calendar information
    /*
     * ##########################
     * #\t Calendar Name: abacadabra\t #
     * # Sunday # Monday # Tuesday # Wednesday # Thursday # Friday # Saturday #
     * ##########################
     */
    public void displayCalendarDetails() {
        System.out.println("##########################");
        System.out.printf("#\t Calendar Name: %s\t #\n", this.name);
        System.out.println("# Sunday # Monday # Tuesday # Wednesday # Thursday # Friday # Saturday #");
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
    public void delete() {
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
