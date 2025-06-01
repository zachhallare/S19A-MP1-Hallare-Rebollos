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
