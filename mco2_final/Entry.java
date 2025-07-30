package mco2_final;

import java.time.LocalDate;

/**
 * Represents a single entry in a calendar with a title, description,
 * and a specific date. This is an abstract base class intended to be
 * extended by specific entry types like Task, Event, Meeting, or Journal.
 */
public abstract class Entry {

    /** Title of the calendar entry. */
    protected String title;

    /** Description of the calendar entry. */
    protected String description;       // Optional except for Journal.

    /** Date of the calendar entry. */
    protected LocalDate date;


    /**
     * Constructs a new Entry with the specified title and date.
     * Description is set to null by default.
     * @param title The title of the entry.
     * @param date  The date the entry occurs.
     */
    public Entry(String title, LocalDate date) {
        this.title = title;
        this.date = date;
        this.description = null;
    }


    /**
     * Constructs a new Entry with the specified title, date, and description.
     * @param title       The title of the entry.
     * @param date        The date the entry occurs.
     * @param description The description of the entry.
     */
    public Entry(String title, LocalDate date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }


    /**
     * Gets the title of the entry.
     * @return The entry title.
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Gets the description of the entry.
     * @return The entry description.
     */
    public String getDescription() {
        return this.description;
    }


    /**
     * Gets the date associated with this entry.
     *
     * @return The entry date.
     */
    public LocalDate getDate() {
        return this.date;
    }


    /**
     * Returns the type of this entry (e.g., "Task", "Event", "Journal", "Meeting").
     * @return The type of the entry.
     */
    public abstract String getType();       


    /**
     * Returns a formatted string to be displayed in the UI,
     * typically showing relevant details of the entry.
     * @return A human-readable string representing this entry.
     */
    public abstract String toDisplayString();       // for ui.


    /**
     * Creates and returns a deep copy of this entry.
     * @return A new Entry object that is a deep copy of the current entry.
     */
    public abstract Entry copy();
}
