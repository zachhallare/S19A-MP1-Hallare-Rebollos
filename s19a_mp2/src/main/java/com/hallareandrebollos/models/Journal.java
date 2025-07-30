package com.hallareandrebollos.models;

import java.time.LocalDate;


/**
 * Represents a journal entry in the calendar.
 * Journals are typically private and limited to one per day per user.
 */
public class Journal extends Entry {

    /**
     * Constructs a new Journal entry with the specified title, date, and description.
     * @param title       The title of the journal entry.
     * @param date        The date associated with the journal.
     * @param description A detailed description or content of the journal.
     */
    public Journal(String title, LocalDate date, String description) {
        super(title, date, description);
    }


    /**
     * Returns the type of this entry.
     * @return A string indicating this is a "Journal".
     */
    @Override
    public String getType() {
        return "Journal"; 
    }


    /**
     * Returns a brief string representation of the journal entry for display purposes.
     * @return A formatted string containing the entry type and title.
     */
    @Override
    public String toDisplayString() {
        return "[Journal] " + title;
    }


    /**
     * Creates a deep copy of this journal entry.
     * @return A new Journal instance with the same title, date, and description.
     */
    @Override
    public Journal copy() {
        return new Journal(title, date, description);
    }
}
