
import java.util.ArrayList;

public class CalendarObject {
    private String calendarName;
    private boolean isPublic; // Indicates if the calendar is public or private
    private ArrayList<Entry> entries; // List of entries in the calendar
    private int monthIdentifier; // Current month of the calendar
    private int yearIdentifier; // Current year of the calendar

    public CalendarObject(String calendarName, boolean isPublic, int monthIdentifier, int yearIdentifier) {
        this.calendarName = calendarName;
        this.isPublic = isPublic;
        this.monthIdentifier = monthIdentifier;
        this.yearIdentifier = yearIdentifier;
        this.entries = new ArrayList<>();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void editEntry(Entry oldEntry, Entry newEntry) {
        int index = entries.indexOf(oldEntry);
        if (index != -1) {
            entries.set(index, newEntry);
        }
    }

    public void removeEntry(Entry entry) {
        entries.remove(entry);
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public ArrayList<Entry> getEntries() {
        return this.entries;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public int getMonthIdentifier() {
        return this.monthIdentifier;
    }

    public int getYearIdentifier() {
        return this.yearIdentifier;
    }
}
