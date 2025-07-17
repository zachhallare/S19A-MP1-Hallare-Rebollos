package aMCO2;

/**
 * Represents a single entry in a calendar with a title, description,
 * start time, and end time (measured in milliseconds since epoch).
 */
public class Entry {
    /** Start time of the entry in milliseconds since epoch. */
    private long timeStart;       
    
    /** End time of the entry in milliseconds since epoch. */
    private long timeEnd;     
    
    /** Title of the calendar entry. */
    private String title;

    /** Description of the calendar entry. */
    private String description;


    /**
     * Constructs a new Entry with the specified title and description.
     * @param title       The title of the entry.
     * @param description The description of the entry.
     */
    public Entry(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Returns the duration of the entry in milliseconds.
     * @return The difference between end time and start time.
     */
    public long getDuration() {
        return this.timeEnd - this.timeStart; // Returns duration in milliseconds
    }

    /**
     * Sets the start time of the entry.
     * @param startTime The start time in milliseconds since epoch.
     */
    public void setStartTime(long startTime) {
        this.timeStart = startTime;
    }

    /**
     * Sets the end time of the entry.
     * @param endTime The end time in milliseconds since epoch.
     */
    public void setEndTime(long endTime) {
        this.timeEnd = endTime;
    }

    /**
     * Gets the start time of the entry.
     * @return Start time in milliseconds since epoch.
     */
    public long getStartTime() {
        return this.timeStart;
    }

    /**
     * Gets the end time of the entry.
     * @return End time in milliseconds since epoch.
     */
    public long getEndTime() {
        return this.timeEnd;
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
     * Creates and returns a deep copy of this entry.
     * @return a new Entry object with the same title, description, start, and end times
     */
    public Entry copy() {
        Entry copiedEntry = new Entry(this.title, this.description);
        copiedEntry.setStartTime(this.timeStart);
        copiedEntry.setEndTime(this.timeEnd);
        return copiedEntry;
    }
}
