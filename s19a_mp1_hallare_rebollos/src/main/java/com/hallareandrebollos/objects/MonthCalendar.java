package com.hallareandrebollos.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;

public class MonthCalendar {
    private ArrayList<Entry> entries;   // List of entries for the month.
    private int monthNumber;            // Month number (1-12).
    private int yearNumber;             // Year number.
    private int daysInMonth;            // Number of days in the month.
    private int startDay;               // Day of the week the month starts on (1-7, where 1 is Sunday).
    private int currentDay;             // Current day of the month (1-31). -1 if not today.
    private int selectedDay;            // Selected day of the month (1-31). -1 if not selected.

    private String ownerUsername;       // Username of the owner of the calendar.
    private String calendarName;        // Name of the calendar.

    // Default Constructor: Initializes the month calendar with current month and year. For null safety.
    public MonthCalendar(ArrayList<Entry> entries) {
        this.entries = (entries != null) ? entries : new ArrayList<>();
        LocalDate today = LocalDate.now();      // For simplicity.
        this.monthNumber = today.getMonthValue();
        this.yearNumber = today.getYear();
        this.daysInMonth = YearMonth.of(yearNumber, monthNumber).lengthOfMonth();
        this.startDay = convertStartDay(LocalDate.of(yearNumber, monthNumber, 1).getDayOfWeek().getValue());
        this.currentDay = (today.getMonthValue() == this.monthNumber && today.getYear() == this.yearNumber) 
                            ? today.getDayOfMonth() : -1;     // Check if today is in the current month.
        this.selectedDay = -1;
    }

    // Constructor: Initializes the month calendar with specified month and year. Assumes that selected month and year are not today.
    public MonthCalendar(int monthNumber, int yearNumber, ArrayList<Entry> entries) {
        this.entries = (entries != null) ? entries : new ArrayList<>();
        this.monthNumber = monthNumber;
        this.yearNumber = yearNumber;
        this.daysInMonth = YearMonth.of(yearNumber, monthNumber).lengthOfMonth();
        this.startDay = convertStartDay(LocalDate.of(yearNumber, monthNumber, 1).getDayOfWeek().getValue());
        this.currentDay = -1;       // No current day set for specified month/year.
        this.selectedDay = -1;
    }


    // Adjusts the start day based on the month.
    private int convertStartDay(int javaDayOfWeek) {
        return (javaDayOfWeek % 7) + 1;
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
            boolean hasEntry = hasEntryOnDay(day);

            if (day == currentDay) {
                // Displays the current day with brackets.
                System.out.printf("|[%2d]       ", day);
            } else if (hasEntry) {
                // Displays if the day has an entry.
                System.out.printf("| %2d   *    ", day);
            } else {
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


    // Checks if it has entry on a day.
    private boolean hasEntryOnDay(int day) {
        for (Entry entry : entries) {
            LocalDate entryDate = entry.getDate();
            if (entryDate.getYear() == this.yearNumber && 
                entryDate.getMonthValue() == this.monthNumber && 
                entryDate.getDayOfMonth() == day) {
                    return true;
            }
        }
        return false;
    } 


    public void displayEntries() {
        System.out.println();

        if (this.selectedDay == -1) {
            System.out.println("No day has been selected.");
        } else {
            System.out.println("Entries for " + this.monthNumber + "/" + this.selectedDay + "/" + this.yearNumber + ":");
            boolean hasEntries = false;

            // Sorts the entries first using Java's built it sorter (TimSort: O(n) Best case).
            entries.sort(Comparator.comparing(Entry::getDate).thenComparing(entry -> entry.getTimeStart()));

            // Checks based on year, month, and day.
            for (Entry entry : entries) {
                boolean sameYear = entry.getDate().getYear() == this.yearNumber;
                boolean sameMonth = entry.getDate().getMonthValue() == this.monthNumber;
                boolean sameDay = entry.getDate().getDayOfMonth() == this.selectedDay;

                if (sameYear && sameMonth && sameDay) {
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


    public void deleteEntry(String title) {
        // Deletes an entry from the calendar.
        int indexToDelete = -1;
        for (int i = 0; i < this.entries.size(); i++) {
            if (this.entries.get(i).getTitle().equalsIgnoreCase(title)) {
                indexToDelete = i;
            }
        }

        // Will only delete if the condition matches.
        if (indexToDelete != -1) {
            this.entries.remove(indexToDelete);
        }
    }

    
    public void editEntry(Entry oldEntry, Entry newEntry) {
        // Modifies an entry from the calendar.
        int index = this.entries.indexOf(oldEntry);
        if (index != -1) {
            this.entries.set(index, newEntry);
        } else {
            System.out.println("Entry could not be edited.");
        }
    }


    public boolean entryExists(LocalDate date, String title) {
        // Checks if an entry with the specified title exists in the calendar.
        for (Entry entry : entries) {
            if (entry.getDate().equals(date) && entry.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }


    public boolean saveCalendar(String ownerUsername) {
        // Saves calendar to ownerID folder inside data/calendars.
        // The calendar is saved in a text file format.
        // Saves them as "calendarID.txt" format.
        // First line is the ownername.
        // Second line is the month number.
        // Third line is the year number.
        // Fourth line and beyond are the entries in the format:
        // "Date(uuuu-MM-dd), Title, Start Time(HH:mm:ss), End Time(HH:mm:ss), Description(Anything beyond this point)".
        String filePath = (ownerUsername.isEmpty()) ? 
            "data/calendars/public/" + this.calendarName + ".txt" : 
            "data/calendars/" + this.ownerUsername + "/" + this.calendarName + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(ownerUsername);        // it's already a string.
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
            return true;    // Successfully saved.
        } catch (IOException e) {
            System.out.println("Error saving calendar: " + e.getMessage());
            return false;   // Failed to save.
        }
    }


    public boolean deleteCalendar(String username, String calendarID) {
        // Deletes the calendar file.
        String filePath = (username.equals("-1")) ? 
            "data/calendars/public/" + calendarID + ".txt" : 
            "data/calendars/" + username + "/" + calendarID + ".txt";

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();       // Returns true if deletion was successful.
        } else {
            System.out.println("Calendar file does not exist.");
            return false;       // File not found.
        }
    }


    public boolean loadCalendar(String username, String calendarID) {
    // Loads the calendar from the specified owner ID and calendar ID.
        String filePath = username.equals("public") ? 
            "data/calendars/public/" + calendarID + ".txt" : 
            "data/calendars/" + username + "/" + calendarID + ".txt";
        
        boolean returnValue = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();      // Skips over ownerUsername.
            this.monthNumber = Integer.parseInt(reader.readLine().trim());
            this.yearNumber = Integer.parseInt(reader.readLine().trim());
            this.daysInMonth = YearMonth.of(this.yearNumber, this.monthNumber).lengthOfMonth();
            this.startDay = convertStartDay(LocalDate.of(this.yearNumber, this.monthNumber, 1).getDayOfWeek().getValue());
            this.currentDay = -1;   // No current day set for specified month/year.
            this.entries.clear();   // Clear existing entries before loading.

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                LocalDate date = LocalDate.parse(parts[0].trim());
                String title = parts[1].trim();
                LocalTime timeStart = LocalTime.parse(parts[2].trim());
                LocalTime timeEnd = LocalTime.parse(parts[3].trim());
                String description = parts.length > 4 ? parts[4].trim() : "";

                Entry entry = new Entry(title, description);
                entry.setDate(date.toString());
                entry.setStartTime(timeStart.toString());
                entry.setEndTime(timeEnd.toString());
                this.entries.add(entry);
            }
            returnValue = true;
        } catch (IOException e) {
            System.out.println("Error loading calendar: " + e.getMessage());
            returnValue = false;
        }

        return returnValue;
    }


    // Getters.
    public int getMonthNumber() {
        return this.monthNumber;
    }

    public int getYearNumber() {
        return this.yearNumber;
    }

    public int getDaysInMonth() {
        return this.daysInMonth;
    }

    public int getSelectedDay() {
        return this.selectedDay;
    }

    // Setters.
    public void setSelectedDay(int selectedDay) {
        this.selectedDay = selectedDay;
    }
}
