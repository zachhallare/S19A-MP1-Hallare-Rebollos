package com.hallareandrebollos.lib;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.hallareandrebollos.objects.MonthCalendar;

public class CalendarManager {
    private MonthCalendar currentCalendar;
    private ArrayList<String> calendarIDs;
    private ArrayList<String> publicCalendarIDs;
    private ArrayList<MonthCalendar> calendarList;
    private ArrayList<MonthCalendar> publicCalendarList;
    private Scanner scanner;

    // Constructor.
    public CalendarManager(Scanner scanner) {
        this.scanner = scanner;
        this.calendarIDs = new ArrayList<>();
        this.publicCalendarIDs = new ArrayList<>();
        this.calendarList = new ArrayList<>();
        this.publicCalendarList = new ArrayList<>();
    }


    // Load calendar IDs from user and public directories
    public void loadCalendarList(String username) {
        calendarIDs.clear();
        publicCalendarIDs.clear();

        File userDir = new File("data/calendars/" + username + "/");
        if (userDir.exists() && userDir.isDirectory()) {
            File[] userFiles = userDir.listFiles((d, name) -> name.endsWith(".txt"));
            if (userFiles != null) {
                for (File file : userFiles) {
                    String id = file.getName().replace(".txt", "");
                    calendarIDs.add(id);
                }
            }
        }

        File publicDir = new File("data/calendars/public/");
        if (publicDir.exists() && publicDir.isDirectory()) {
            File[] publicFiles = publicDir.listFiles((d, name) -> name.endsWith(".txt"));
            if (publicFiles != null) {
                for (File file : publicFiles) {
                    String id = file.getName().replace(".txt", "");
                    publicCalendarIDs.add(id);
                }
            }
        }
    }


    // Load today's calendar if it matches a user's calendar
    public void loadTodayCalendar(String username) {
        LocalDate today = LocalDate.now();
        boolean found = false;

        for (String calendarID : calendarIDs) {
            MonthCalendar temp = new MonthCalendar(new ArrayList<>());

            if (temp.loadCalendar(username, calendarID)) {
                if (temp.getMonthNumber() == today.getMonthValue() &&
                    temp.getYearNumber() == today.getYear()) {

                    if (!found) {
                        currentCalendar = temp;
                        currentCalendar.setSelectedDay(today.getDayOfMonth());
                        currentCalendar.displayCalendar();
                        currentCalendar.displayEntries();
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No calendar matches today's date.\n\n");
        }
    }

    
    // Select and load a calendar from either private or public lists
    public void calendarSelector(String username) {
        boolean loopLock = false;

        while (!loopLock) {
            System.out.println("Enter Calendar ID: ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Calendar ID cannot be empty. Please try again.");
            } else if (calendarIDs.contains(input)) {
                currentCalendar = new MonthCalendar(new ArrayList<>());
                if (currentCalendar.loadCalendar(username, input)) {
                    loopLock = true;
                } else {
                    System.out.println("Failed to load calendar with ID: " + input);
                }
            } else if (publicCalendarIDs.contains(input)) {
                currentCalendar = new MonthCalendar(new ArrayList<>());
                if (currentCalendar.loadCalendar("public", input)) {
                    loopLock = true;
                } else {
                    System.out.println("Failed to load public calendar with ID: " + input);
                }
            } else {
                System.out.println("Invalid Calendar ID. Please try again.");
            }
        }
    }


    // Prompt user to choose a day from the current calendar
    public void daySelector() {
        boolean loopLock = false;

        while (!loopLock) {
            System.out.println("Select a Date to view (Enter 0 to return):");
            int input = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (input == 0) {
                loopLock = true;
            } else if (currentCalendar != null &&
                       input >= 1 && input <= currentCalendar.getDaysInMonth()) {
                currentCalendar.setSelectedDay(input);
                loopLock = true;
            } else {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    
    // Control calendar display and flow to the calendar menu
    public void calendarDisplayController(Runnable calendarMenuPage) {
        if (currentCalendar != null) {
            currentCalendar.displayCalendar();
            daySelector();
            calendarMenuPage.run();
        } else {
            System.out.println("No calendar is currently selected.");
        }
    }


    // Getters and Setters.
    public MonthCalendar getCurrentCalendar() {
        return currentCalendar;
    }

    public void setCurrentCalendar(MonthCalendar calendar) {
        this.currentCalendar = calendar;
    }

    public ArrayList<String> getCalendarIDs() {
        return calendarIDs;
    }

    public ArrayList<String> getPublicCalendarIDs() {
        return publicCalendarIDs;
    }
}
