package aMCO2;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event extends Entry {
    private String venue;
    private String organizer;
    private LocalTime startTime;
    private LocalTime endTime;

    public Event(String title, LocalDate date, String description, String venue, 
                String organizer, LocalTime startTime, LocalTime endTime) {
        super(title, date, description);
        this.venue = venue;
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getVenue() { 
        return this.venue; 
    }
    
    public String getOrganizer() { 
        return this.organizer; 
    }

    public LocalTime getStartTime() { 
        return this.startTime; 
    }
    
    public LocalTime getEndTime() { 
        return this.endTime; 
    }

    @Override
    public String getType() { 
        return "Event"; 
    }

    @Override
    public String toDisplayString() {
        return "[Event] " + title + " at " + venue + " (" + startTime + "–" + endTime + ")";
    }

    @Override
    public Event copy() {
        return new Event(title, date, description, venue, organizer, startTime, endTime);
    }
}
