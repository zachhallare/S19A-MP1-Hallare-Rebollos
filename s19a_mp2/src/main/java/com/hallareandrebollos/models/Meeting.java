package com.hallareandrebollos.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting extends Entry {
    final private String modality;  // required
    final private String venue;     // optional
    final private String link;      // optional
    final private LocalTime startTime;
    final private LocalTime endTime;

    public Meeting(String title, LocalDate date, String description, String modality, LocalTime startTime, LocalTime endTime) {
        super(title, date, description);
        this.modality = modality;
        this.venue = null;
        this.link = null;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public Meeting(String title, LocalDate date, LocalTime startTime, LocalTime endTime, String description,
                    String modality, String venue, String link) {
        super(title, date, description);
        this.modality = modality;
        this.venue = venue;
        this.link = link;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getModality() { 
        return modality; 
    }
    
    public String getVenue() { 
        return venue; 
    }
    
    public String getLink() { 
        return link; 
    }

    public LocalTime getStartTime() { 
        return startTime; 
    }

    public LocalTime getEndTime() { 
        return endTime; 
    }

    @Override
    public String getType() { 
        return "Meeting"; 
    }

    @Override
    public String toDisplayString() {
        return "[Meeting] " + title + " (" + modality + ")";
    }

    @Override
    public Meeting copy() {
        return new Meeting(title, date, startTime, endTime, description, modality, venue, link);
    }
}
