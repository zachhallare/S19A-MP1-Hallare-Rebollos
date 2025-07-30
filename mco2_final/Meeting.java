package mco2_final;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Represents a meeting entry in the calendar.
 * A meeting includes a title, date, time range, modality (e.g., online or in-person),
 * and optional venue and link.
 */
public class Meeting extends Entry {

    /** Required modality of the meeting (e.g., "Online", "In-Person"). */
    final private String modality;  
    
    /** Optional venue where the meeting will take place. */
    final private String venue;    

    /** Optional link (e.g., Zoom or Google Meet link) for online meetings. */
    final private String link;      

    /** Start time of the meeting. */
    final private LocalTime startTime;

    /** End time of the meeting. */
    final private LocalTime endTime;


    /**
     * Constructs a Meeting with required fields and no venue or link.
     * @param title       The title of the meeting.
     * @param date        The date of the meeting.
     * @param description A description of the meeting.
     * @param modality    The modality (e.g., "Online" or "In-Person").
     * @param startTime   The start time of the meeting.
     * @param endTime     The end time of the meeting.
     */
    public Meeting(String title, LocalDate date, String description, String modality, LocalTime startTime, LocalTime endTime) {
        super(title, date, description);
        this.modality = modality;
        this.venue = null;
        this.link = null;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    /**
     * Constructs a Meeting with all possible fields specified.
     * @param title       The title of the meeting.
     * @param date        The date of the meeting.
     * @param startTime   The start time of the meeting.
     * @param endTime     The end time of the meeting.
     * @param description A description of the meeting.
     * @param modality    The modality (e.g., "Online" or "In-Person").
     * @param venue       The venue of the meeting (optional).
     * @param link        The meeting link (optional).
     */
    public Meeting(String title, LocalDate date, LocalTime startTime, LocalTime endTime, String description,
                    String modality, String venue, String link) {
        super(title, date, description);
        this.modality = modality;
        this.venue = venue;
        this.link = link;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    /**
     * Returns the modality of the meeting.
     * @return The modality as a string.
     */
    public String getModality() { 
        return modality; 
    }
    

    /**
     * Returns the venue of the meeting.
     * @return The venue string, or null if not specified.
     */
    public String getVenue() { 
        return venue; 
    }
    

    /**
     * Returns the meeting link.
     * @return The meeting link string, or null if not specified.
     */
    public String getLink() { 
        return link; 
    }


    /**
     * Returns the start time of the meeting.
     * @return The meeting's start time.
     */
    public LocalTime getStartTime() { 
        return startTime; 
    }


    /**
     * Returns the end time of the meeting.
     * @return The meeting's end time.
     */
    public LocalTime getEndTime() { 
        return endTime; 
    }


    /**
     * Returns the type of the entry.
     * @return The string "Meeting".
     */
    @Override
    public String getType() { 
        return "Meeting"; 
    }


    /**
     * Returns a concise string for display purposes.
     * @return A formatted string including type, title, and modality.
     */
    @Override
    public String toDisplayString() {
        return "[Meeting] " + title + " (" + modality + ")";
    }


    /**
     * Creates a deep copy of this meeting entry.
     * @return A new {@code Meeting} instance with the same attributes.
     */
    @Override
    public Meeting copy() {
        return new Meeting(title, date, startTime, endTime, description, modality, venue, link);
    }
}
