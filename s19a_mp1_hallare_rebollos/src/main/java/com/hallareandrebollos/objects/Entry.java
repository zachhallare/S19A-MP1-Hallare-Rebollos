package com.hallareandrebollos.objects;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entry {
    private static int entryCounter = 0;    // Static counter to assign unique IDs. 
    private int entryID;                    // Unique ID of the entry (easier Identification).
    private String title;                   // Title of the entry.
    private String description;             // Description of the entry.
    private LocalDate date;                 // Date of the entry.
    private LocalTime timeStart;            // Start time of the entry.
    private LocalTime timeEnd;              // End time of the entry.

    // Default Constructor.
    public Entry() {
        this.entryID = ++entryCounter;
        this.title = "";
        this.description = "";
        this.date = LocalDate.now();
        this.timeStart = LocalTime.of(0, 0);
        this.timeEnd = LocalTime.of(0, 0);
    }

    // Semi-Full Constructor.
    public Entry(String title, String description) {
        this.entryID = ++entryCounter;
        this.title = title;
        this.description = description;
        this.date = LocalDate.now();
        this.timeStart = LocalTime.of(0, 0);
        this.timeEnd = LocalTime.of(0, 0);
    }

    // Full Constructor.
    public Entry(String title, String description, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.entryID = ++entryCounter;
        this.title = title;
        this.description = description;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    
    public boolean setStartTime(String startTime) {
        // Parses "HH:mm" to set StartTime
        try {
            String[] parts = startTime.split(":");
            if (parts.length == 2) {
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                this.timeStart = LocalTime.of(hour, minute);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Invalid start time format. Please use HH:mm.");
        }
        return false;
    }


    public boolean setEndTime(String endTime) {
        // Parses "HH:mm" to set endTime
        try {
            String[] parts = endTime.split(":");
            if (parts.length == 2) {
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                this.timeEnd = LocalTime.of(hour, minute);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Invalid start time format. Please use HH:mm.");
        }
        return false;
    }


    public boolean setDate(String date) {
        // Parses "yyyy-MM-dd" to set date
        try {
            this.date = LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
        return false;
    }

    // Checks if time range range is valid. 
    public boolean isValidTimeRange() {
        return timeEnd.isAfter(timeStart);
    }

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

    // Save Format
    // "Date(uuuu-MM-dd), Title, Start Time(HH:mm:ss), End Time(HH:mm:ss), Description(Anything beyond this point)".
    public String toSaveFormat() {
        return entryID + ", " + getDateString() + ", " + this.title + ", " + getTimeStartString() + ", " + getTimeEndString() + ", " + this.description;
    }


    // Getters
    public int getEntryID() {
        return this.entryID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getTimeStart() {
        return this.timeStart;
    }

    public LocalTime getTimeEnd() {
        return this.timeEnd;
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
