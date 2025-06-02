import java.util.ArrayList;

// each account can have multiple calendars, this can prob 
// serve as a container for the calendarEntry class you made brent.

public class Calendar {
    private String name;
    private boolean isPrivate;
    private ArrayList<CalendarEntry> entries;
    
    public Calendar(String name, boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
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

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public ArrayList<CalendarEntry> getEntries() {
        return entries;
    }

    public void addEntry(CalendarEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(CalendarEntry entry) {
        entries.remove(entry);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }





}
