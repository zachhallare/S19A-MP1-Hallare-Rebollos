package mco2_final;

import java.util.ArrayList;

/**
 * Represents a calendar that contains multiple entries.
 * The calendar can be either public or private and is associated with a specific year.
 */
public class CalendarObject {

    /** The name/title of the calendar. */
    private String calendarName;

    /** Indicates if the calendar is public or private. */
    private boolean isPublic;

    /** List of entries in the calendar. */
    final private ArrayList<Entry> entries;

    
    /**
     * Constructs a new CalendarObject with the specified name, visibility, and year.
     * @param calendarName the name of the calendar.
     * @param isPublic true if the calendar is public; false if private.
     */
    public CalendarObject(String calendarName, boolean isPublic) {
        this.calendarName = calendarName;
        this.isPublic = isPublic;
        this.entries = new ArrayList<>();
    }


    /**
     * Returns the name of the calendar
     * @return The calendar name.
     */
    public String getName() {
        return this.calendarName;
    }


    /**
     * Adds a new entry to the calendar.
     * @param entry The entry to add.
     */
    public void addEntry(Entry entry) {
        entries.add(entry);
    }


    /**
     * Replaces an existing entry with a new one.
     * @param oldEntry The existing entry to replace.
     * @param newEntry The new entry to insert.
     */
    public void editEntry(Entry oldEntry, Entry newEntry) {
        int index = entries.indexOf(oldEntry);
        if (index != -1) {
            entries.set(index, newEntry);
        }
    }


    /**
     * Removes an entry from the calendar.
     * @param entry The entry to remove.
     */
    public void removeEntry(Entry entry) {
        entries.remove(entry);
    }


    /**
     * Sets the public/private visibility of the calendar.
     * @param isPublic true to make the calendar public, false to make it private.
     */
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }


    /**
     * Sets the name/title of the calendar.
     * @param title The new title for the calendar.
     */
    public void setTitle(String title) {
        this.calendarName = title;
    }


    /**
     * Returns the list of entries in the calendar.
     * @return A list of Entry objects.
     */
    public ArrayList<Entry> getEntries() {
        return this.entries;
    }


    /**
     * Returns the name of the calendar.
     * @return The calendar name.
     */
    public String getCalendarName() {
        return this.calendarName;
    }


    /**
     * Checks whether the calendar is public.
     * @return true if the calendar is public, false otherwise.
     */
    public boolean isPublic() {
        return this.isPublic;
    }

    
    /**
     * Creates a copy of this CalendarObject, including all of its entries.
     * @return a new CalendarObject with the same name, visibility, and copied entries.
     */
    public CalendarObject copy() {
        CalendarObject copiedCalendar = new CalendarObject(this.calendarName, this.isPublic);
        for (Entry entry : this.entries) {
            copiedCalendar.addEntry(entry.copy());
        }
        return copiedCalendar;
    }
}
