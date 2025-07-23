package com.hallareandrebollos.models;

import java.time.LocalDate;

/**
 * Represents a single entry in a calendar with a title, description,
 * start time, and end time (measured in milliseconds since epoch).
 */
public abstract class Entry {
    /** Title of the calendar entry. */
    protected String title;

    /** Description of the calendar entry. */
    protected String description;       // Optional except for Journal.

    /** Date of the calendar entry. */
    protected LocalDate date;


    public Entry(String title, LocalDate date) {
        this.title = title;
        this.date = date;
        this.description = null;
    }

    /**
     * Constructs a new Entry with the specified title and description.
     * @param title       The title of the entry.
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

    public LocalDate getDate() {
        return this.date;
    }


    public abstract String getType();       

    public abstract String toDisplayString();       // for ui.

    /**
     * Creates and returns a deep copy of this entry.
     * @return a new Entry object.
     */
    public abstract Entry copy();
}
