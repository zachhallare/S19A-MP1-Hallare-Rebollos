package com.hallareandrebollos.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Account {

    private String username;        // Username of the account.
    private String password;        // Password of the account.
    private int accountID;         // Unique ID of the account.
    private boolean isActive;
    private ArrayList<Integer> ownedCalendars; // List of calendars owned by the account. stores them in Calendar ID format.

    // Default Constructor: Initializes the account with default values.
    public Account() {
        this.username = "";
        this.password = "";
        this.accountID = -1; // Default ID for null safety.
        this.isActive = false; // Account is inactive by default.
        this.ownedCalendars = new ArrayList<>(); // Initializes the list of owned calendars.
    }

    // Full Constructor: Initializes the account with specified values.
    public Account(String username, String password, int accountID, boolean isActive, ArrayList<Integer> ownedCalendars) {
        this.username = username;
        this.password = password;
        this.accountID = accountID;
        this.isActive = isActive;
        this.ownedCalendars = (ownedCalendars != null) ? ownedCalendars : new ArrayList<>(); // Initializes the list of owned calendars.
    }

    public boolean ownsCalendar(int calendarID) {
        // Checks if the account owns a calendar with the specified ID.
        return ownedCalendars.contains(calendarID);
    }

    public boolean deleteCalendar(int calendarID) {
        // Deletes the calendar with the specified ID from the account.
        if (ownsCalendar(calendarID)) {
            ownedCalendars.remove(Integer.valueOf(calendarID));
            return true; // Successfully deleted the calendar.
        }
        return false; // Calendar not found in the account.
    }

    public boolean addCalendar(int calendarID) {
        // Adds a calendar with the specified ID to the account if it doesn't already exist.
        if (!ownsCalendar(calendarID)) {
            ownedCalendars.add(calendarID);
            return true; // Successfully added the calendar.
        }
        return false; // Calendar already exists in the account.
    }

    public boolean authenticate(String username, String password) {
        // Authenticates the account by checking the provided username and password in resource/accounts.txt.
        // Per line format is "accountID, isActive, username, password, <CalendarIDList>".
        // Every integer after password is a Calendar ID owned by the account.
        // Example: "1, true, user123, pass123, 101, 102, 103".
        String filePath = "resource/accounts.txt";
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while (((line = reader.readLine()) != null) && !found) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) { // Only process lines that have enough parts.

                    int id = Integer.parseInt(parts[0]);
                    boolean active = Boolean.parseBoolean(parts[1]);
                    String fileUsername = parts[2];
                    String filePassword = parts[3];

                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        this.accountID = id;
                        this.isActive = active;
                        this.username = fileUsername;
                        this.password = filePassword;

                        // Load owned calendars from the remaining parts of the line.
                        for (int i = 4; i < parts.length; i++) {
                            ownedCalendars.add(Integer.valueOf(parts[i]));
                        }
                        found = true; // Account found and authenticated.
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading accounts file: " + e.getMessage());
        }
        return found; // Returns true if the account was found and authenticated, false otherwise.
    }
}
