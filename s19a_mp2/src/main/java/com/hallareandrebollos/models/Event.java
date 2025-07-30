package com.hallareandrebollos.models;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Represents an event-type calendar entry with a venue, organizer, 
 * and specific start and end times.
 */
public class Event extends Entry {

    /** Location where the event will take place. */
    final private String venue;

    /** Username of the person organizing the event. */
    final private String organizer;

    /** Starting time of the event. */
    final private LocalTime startTime;

    /** Ending time of the event. */
    final private LocalTime endTime;


    /**
     * Constructs a new Event entry.
     * @param title       The title of the event.
     * @param date        The date the event is scheduled.
     * @param description A description of the event.
     * @param venue       The location of the event.
     * @param organizer   The username of the person organizing the event.
     * @param startTime   The starting time of the event.
     * @param endTime     The ending time of the event.
     */
    public Event(String title, LocalDate date, String description, String venue, 
                String organizer, LocalTime startTime, LocalTime endTime) {
        super(title, date, description);
        this.venue = venue;
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    /**
     * Returns the venue of the event.
     * @return The event venue.
     */
    public String getVenue() { 
        return this.venue; 
    }
    

    /**
     * Returns the organizer of the event.
     * @return The username of the event organizer.
     */
    public String getOrganizer() { 
        return this.organizer; 
    }


    /**
     * Returns the start time of the event.
     *
     * @return The event's start time.
     */
    public LocalTime getStartTime() { 
        return this.startTime; 
    }
    

    /**
     * Returns the end time of the event.
     *
     * @return The event's end time.
     */
    public LocalTime getEndTime() { 
        return this.endTime; 
    }


    /**
     * Returns the type of this entry.
     *
     * @return The string "Event".
     */
    @Override
    public String getType() { 
        return "Event"; 
    }


    /**
     * Returns a formatted string for UI display of this event.
     * @return A string showing type, title, venue, and time.
     */
    @Override
    public String toDisplayString() {
        return "[Event] " + title + " at " + venue + " (" + startTime + "–" + endTime + ")";
    }


    /**
     * Creates and returns a deep copy of this Event.
     * @return A new Event object with the same data.
     */
    @Override
    public Event copy() {
        return new Event(title, date, description, venue, organizer, startTime, endTime);
    }
}
