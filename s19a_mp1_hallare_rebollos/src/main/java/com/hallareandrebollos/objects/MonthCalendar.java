package com.hallareandrebollos.objects;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

public class MonthCalendar {
    private ArrayList<Entry> entries;   // List of entries for the month.
    private int monthNumber;            // Month number (1-12).
    private int yearNumber;             // Year number.
    private int daysInMonth;            // Number of days in the month.
    private int startDay;               // Day of the week the month starts on (1-7, where 1 is Sunday).
    private int firstDay;               // First day of the month if sunday isn't the first day of the week.
    private int currentDay;             // Current day of the month (1-31). -1 if not today.
    private int calendarID;             // ID of the calendar.
    private int connectedOwner;         // ID of the owner of the calendar. -1 if public calendar.

    // Default Constructor: Initializes the month calendar with current month and year. For null safety.
    public MonthCalendar(ArrayList<Entry> entries) {
        this.entries = (entries != null) ? entries : new ArrayList<>();
        LocalDate today = LocalDate.now();      // For simplicity.
        this.monthNumber = today.getMonthValue();
        this.yearNumber = today.getYear();
        this.daysInMonth = YearMonth.of(yearNumber, monthNumber).lengthOfMonth();
        this.startDay = LocalDate.of(yearNumber, monthNumber, 1).getDayOfWeek().getValue();
        this.firstDay = (startDay == 7) ? 1 : startDay + 1;     // Adjust if the week starts on Sunday.
        this.currentDay = (today.getMonthValue() == this.monthNumber && today.getYear() == this.yearNumber) 
                            ? today.getDayOfMonth() : -1;     // Check if today is in the current month.
    }

    // Constructor: Initializes the month calendar with specified month and year. Assumes that selected month and year are not today.
    public MonthCalendar(int monthNumber, int yearNumber, ArrayList<Entry> entries) {
        this.entries = (entries != null) ? entries : new ArrayList<>();
        this.monthNumber = monthNumber;
        this.yearNumber = yearNumber;
        this.daysInMonth = YearMonth.of(yearNumber, monthNumber).lengthOfMonth();
        this.startDay = LocalDate.of(yearNumber, monthNumber, 1).getDayOfWeek().getValue();
        this.firstDay = (this.startDay == 7) ? 1 : this.startDay + 1; // Adjust if the week starts on Sunday.
        this.currentDay = -1;       // No current day set for specified month/year.
    }


    // Displays the calendar.
    public void displayCalendar() {
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;

        // Empty cells before the start day.
        for (int i = 1; i < this.startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print each day.
        for (int day = 1; day <= this.daysInMonth; day++) {
            boolean hasEntry = false;
            for (int i = 0; i < entries.size(); i++) {
                LocalDate entryDate = entries.get(i).getDate();
                if (entryDate.getYear() == this.yearNumber && entryDate.getMonthValue() == this.monthNumber && entryDate.getDayOfMonth() == day) {
                    hasEntry = true;
                }
            }

            // Displays the current day with brackets.
            if (day == currentDay) {
                System.out.printf("|[%2d]       ", day);
            }
            // Displays if the day has an entry.
            else if (hasEntry) {
                System.out.printf("| %2d   *    ", day);
            } 
            else {
                System.out.printf("| %2d        ", day);
            }

            // If it reached the end of the week.
            if (currentDayPosition % 7 == 0) {
                System.out.println("|");
                System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
            }
            
            currentDayPosition++;
        }


        // Fill remaining cells.
        int trailing = (currentDayPosition - 1) % 7;
        if (trailing != 0) {
            int remaining = 7 - trailing;
            for (int i = 0; i < remaining; i++) {
                System.out.print("|           ");
            }
            System.out.println("|");
            System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        }
    }

    public void displayEntries(int dayNumber) {
        System.out.println("\nEntries for " + monthNumber + "/" + dayNumber + "/" + yearNumber + ":");
        boolean hasEntries = false;
        for (Entry entry : entries) {
            if (entry.getDate().getYear() == yearNumber && 
                entry.getDate().getMonthValue() == monthNumber && 
                entry.getDate().getDayOfMonth() == dayNumber) {
                System.out.println("["+ entry.getTimeStartString() + " - " + entry.getTimeEndString() + "] " + entry.getTitle());
                System.out.println("Description: " + entry.getDescription());
                System.out.println("-----------------------------");
                hasEntries = true;
            }
        }
        if (!hasEntries) {
            System.out.println("No entries for this day.");
        }
    }

    public void addEntry(Entry entry) {
        // Adds an entry to the calendar.
        if (entry != null && entry.getDate().getYear() == this.yearNumber && 
            entry.getDate().getMonthValue() == this.monthNumber) {
            this.entries.add(entry);
        } 
        else {
            System.out.println("Entry date does not match the calendar month/year.");
        }
    }

    public void deleteEntry(Entry entry) {
        // Deletes an entry from the calendar.
        if (entry != null) {
            this.entries.remove(entry);
        } 
        else {
            System.out.println("Entry could not be deleted.");
        }
    }

    // i think we need this.
    public void editEntry(Entry oldEntry, Entry newEntry) {
        // Modifies an entry from the calendar.
        int index = this.entries.indexOf(oldEntry);
        if (index != -1) {
            this.entries.set(index, newEntry);
        } 
        else {
            System.out.println("Entry could not be edited.");
        }
    }
    /*
     * 
     * REQUIRES UPDATE...
     * 
     */

    public boolean saveCalendar() {
        // Saves calendar to ownerID folder inside resources/calendars.
        // The calendar is saved in a text file format.
        // Saves them as "calendarID.txt" format.
        // First line is the owner ID.
        // Second line is the month number.
        // Third line is the year number.
        // Fourth line and beyond are the entries in the format:
        // "Date(uuuu-MM-dd), Title, Start Time(HH:mm:ss), End Time(HH:mm:ss), Description(Anything beyond this point)".
        String filePath = (this.connectedOwner == -1) ? 
            "resources/calendars/public/" + this.calendarID + ".txt" : 
            "resources/calendars/" + this.connectedOwner + "/" + this.calendarID + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(this.connectedOwner));
            writer.newLine();
            writer.write(String.valueOf(this.monthNumber));
            writer.newLine();
            writer.write(String.valueOf(this.yearNumber));
            writer.newLine();

            for (Entry entry : this.entries) {
                writer.write(entry.getDateString() + ", " + entry.getTitle() + ", " + 
                             entry.getTimeStartString() + ", " + entry.getTimeEndString() + ", " + 
                             entry.getDescription());
                writer.newLine();
            }
            return true; // Successfully saved.
        } 
        catch (IOException e) {
            System.out.println("Error saving calendar: " + e.getMessage());
            return false; // Failed to save.
        }
    }

    public boolean deleteCalendar(String username, String calendarID) {
        // Deletes the calendar file.
        String filePath = (username.equals("-1")) ? 
            "resources/calendars/public/" + calendarID + ".txt" : 
            "resources/calendars/" + username + "/" + calendarID + ".txt";

        java.io.File file = new java.io.File(filePath);
        if (file.exists()) {
            return file.delete(); // Returns true if deletion was successful.
        } 
        else {
            System.out.println("Calendar file does not exist.");
            return false; // File not found.
        }
    }

    public boolean loadCalendar(String username, String calendarID) {
    // Loads the calendar from the specified owner ID and calendar ID.
        String filePath = (username.equals("-1")) ? 
            "resources/calendars/public/" + calendarID + ".txt" : 
            "resources/calendars/" + username + "/" + calendarID + ".txt";
        boolean returnValue = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            this.monthNumber = Integer.parseInt(reader.readLine().trim());
            this.yearNumber = Integer.parseInt(reader.readLine().trim());
            this.daysInMonth = java.time.YearMonth.of(this.yearNumber, this.monthNumber).lengthOfMonth();
            this.startDay = java.time.LocalDate.of(this.yearNumber, this.monthNumber, 1).getDayOfWeek().getValue();
            this.firstDay = (this.startDay == 7) ? 1 : this.startDay + 1; // Adjust if the week starts on Sunday.
            this.currentDay = -1; // No current day set for specified month/year.
            this.entries.clear(); // Clear existing entries before loading.

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                LocalDate date = LocalDate.parse(parts[0].trim());
                String title = parts[1].trim();
                LocalTime timeStart = LocalTime.parse(parts[2].trim());
                LocalTime timeEnd = LocalTime.parse(parts[3].trim());
                String description = parts.length > 4 ? parts[4].trim() : "";

                Entry entry = new Entry(title, description, date, timeStart, timeEnd);
                this.entries.add(entry);
            }
            returnValue = true;
        } 
        catch (IOException e) {
            System.out.println("Error loading calendar: " + e.getMessage());
            returnValue = false;
        }
        return returnValue;
    }
}
