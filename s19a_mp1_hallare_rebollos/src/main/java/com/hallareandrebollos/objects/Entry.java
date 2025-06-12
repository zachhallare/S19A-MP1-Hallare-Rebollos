package com.hallareandrebollos.objects;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entry {
    private String title;          // Title of the entry.
    private String description;    // Description of the entry.
    private LocalDate date;          // Date of the entry.
    private LocalTime timeStart;      // Start time of the entry.
    private LocalTime timeEnd;        // End time of the entry.

    // Default Constructor.
    public Entry() {
        this.title = "";
        this.description = "";
        this.date = LocalDate.now();
        this.timeStart = LocalTime.of(0, 0);
        this.timeEnd = LocalTime.of(0, 0);
    }

    // Full Constructor.
    public Entry(String title, String description, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    // Save Format
    // "Date(uuuu-MM-dd), Title, Start Time(HH:mm:ss), End Time(HH:mm:ss), Description(Anything beyond this point)".

    // Convert date into a string format.
    public String getDateString() {
        return date.toString();
    }

    // Convert start time into a string format.
    public String getTimeStartString() {
        return timeStart.toString();
    }

    // Convert end time into a string format.
    public String getTimeEndString() {
        return timeEnd.toString();
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTimeStart() {
        return timeStart;
    }
    public LocalTime getTimeEnd() {
        return timeEnd;
    }
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }
    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}
