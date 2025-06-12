// Allows users to view a monthly calender for a selected calendar object.
// It displays the days of the month in a grid format and marks dates with entries.

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;

public class CalendarViewer {
    private Scanner scanner;

    // Constructor: With Scanner to Reduce Redundancy.
    public CalendarViewer(Scanner scanner) {
        this.scanner = scanner;
    }

    // Main method to view a selected calender for a given month and year.
    public void viewCalendar(Account loggedInAccount) {
        ArrayList<Calendar> calendars = loggedInAccount.getCalendars();

        if (!calendars.isEmpty()) {
            // Show list of calendars to choose from.
            System.out.println("\nSelect a calendar to view: ");
            for (int i = 0; i < calendars.size(); i++) {
                System.out.println((i + 1) + ". " + calendars.get(i).getName());
            }   

            System.out.print("Enter option number: ");
            int calendarIndex = getUserOption(1, calendars.size()) - 1;
            Calendar selectedCalendar = calendars.get(calendarIndex);

            // Ask user for month and year to find for display.
            System.out.print("\nEnter month (1-12): ");
            int month = getUserOption(1, 12);
            System.out.print("Enter year (e.g., 2025): ");
            int year = promptYear();

            // Displays the calendar.
            printCalendar(month, year, selectedCalendar.getEntries());
        }
    }

    // Displays the actual calendar grid and marks days with entries.
    private void printCalendar(int month, int year, ArrayList<CalendarEntry> entries) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int startDay = (firstDay.getDayOfWeek().getValue() % 7) + 1;    // makes sunday = 1.'
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        // Show header.
        System.out.println("\n+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;

        // Print leading empty cells before the start day.
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print day numbers, mark with "*" if there's an entry.
        for (int day = 1; day <= daysInMonth; day++) {
            int entryIndex = 0;
            boolean hasEntry = false;

            // Check if any entry matches the current day.
            while (entryIndex < entries.size() && !hasEntry) {
                LocalDate entryDate = entries.get(entryIndex).getDate();
                if (entryDate.getYear() == year && entryDate.getMonthValue() == month && entryDate.getDayOfMonth() == day) {
                    hasEntry = true;
                }
                entryIndex++;
            }

            // Print the day.
            if (hasEntry) {
                System.out.printf("| %2d   *    ", day);
            } 
            else {
                System.out.printf("| %2d        ", day);
            }

            // New line every end of the week.
            if (currentDayPosition % 7 == 0) {
                System.out.println("|");
                System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
            }

            currentDayPosition++;
        }

        // Print remaining empty cells after the last day.
        int trailing = (currentDayPosition - 1) % 7;
        if (trailing != 0) {
            int remaining = 7 - trailing;
            for (int i = 0; i < remaining; i++) {
                System.out.print("|           ");
            }
            System.out.println("|");
            System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        }

        // Print details of all entries within the selected month.
        if (!entries.isEmpty()) {
            System.out.println("\nEntries:");
            for (int i = 0; i < entries.size(); i++) {
                CalendarEntry entry = entries.get(i);
                LocalDate date = entry.getDate();
                if (date.getYear() == year && date.getMonthValue() == month) {
                    System.out.println(" - " + date + " " + entry.getTimeStart() + " to " + entry.getTimeEnd() + ": " + entry.getTitle());
                }
            }
        }
    }


    // Prompts the user for valid year.
    private int promptYear() {
        int year = -1;
        while (year < 1) {
            if (scanner.hasNextInt()) {
                year = scanner.nextInt();
            }
            else {
                scanner.nextInt();      // Discard invalid input.
            }
            if (year < 1) {
                System.out.println("Invalid year. Enter again: ");
            }
        }
        scanner.nextLine();     // Clears buffer.
        return year;
    }


    // Prompts for a valid number in a range.
    private int getUserOption(int min, int max) {
        int option = -1;
        while (option < min || option > max) {
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            else {
                scanner.next();     // Discard invalid input.
            }

            if (option < min || option > max) {
                System.out.print("Invalid option. Choose again: ");
            }
        }
        scanner.nextLine();     // Clears buffer.
        return option;
    }
}
