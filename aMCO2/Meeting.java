package aMCO2;

import java.time.LocalDate;

public class Meeting extends Entry {
    private String modality;  // required
    private String venue;     // optional
    private String link;      // optional

    public Meeting(String title, LocalDate date, String description, String modality) {
        super(title, date, description);
        this.modality = modality;
        this.venue = null;
        this.link = null;
    }

    public Meeting(String title, LocalDate date, String description,
                    String modality, String venue, String link) {
        super(title, date, description);
        this.modality = modality;
        this.venue = venue;
        this.link = link;
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
        return new Meeting(title, date, description, modality, venue, link);
    }
}
