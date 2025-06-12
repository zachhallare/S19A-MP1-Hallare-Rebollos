import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;


public class CalendarViewer {
    private Scanner scanner;

    public CalendarViewer(Scanner scanner) {
        this.scanner = scanner;
    }

    public void viewCalendar(Account loggedInAccount) {
        ArrayList<Calendar> calendars = loggedInAccount.getCalendars();

        if (!calendars.isEmpty()) {
            // choosing a calendar
            System.out.println("\nSelect a calendar to view: ");
            for (int i = 0; i < calendars.size(); i++) {
                System.out.println((i + 1) + ". " + calendars.get(i).getName());
            }   

            System.out.print("Enter option number: ");
            int calendarIndex = getUserOption(1, calendars.size()) - 1;
            Calendar selectedCalendar = calendars.get(calendarIndex);

            // get the month and year sa calendar
            System.out.print("\nEnter month (1-12): ");
            int month = getUserOption(1, 12);

            System.out.print("Enter year (e.g., 2025): ");
            int year = promptYear();

            // display calendar FINALLY
            printCalendar(month, year, selectedCalendar.getEntries());
        }
    }

    private void printCalendar(int month, int year, ArrayList<CalendarEntry> entries) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int startDay = (firstDay.getDayOfWeek().getValue() % 7) + 1;    // makes sunday = 1.'
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        System.out.println("\n+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;

        // Empty cells before the start day.
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print each day.
        for (int day = 1; day <= daysInMonth; day++) {
            boolean hasEntry = false;
            for (int i = 0; i < entries.size(); i++) {
                LocalDate entryDate = entries.get(i).getDate();
                if (entryDate.getYear() == year && entryDate.getMonthValue() == month && entryDate.getDayOfMonth() == day) {
                    hasEntry = true;
                }
            }

            if (hasEntry) {
                System.out.printf("| %2d   *    ", day);
            } 
            else {
                System.out.printf("| %2d        ", day);
            }

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

        // Print entries.
        if (!entries.isEmpty()) {
            System.out.println("\nEntries:");
            for (int i = 0; i < entries.size(); i++) {
                CalendarEntry entry = entries.get(i);
                LocalDate date = entry.getDate();
                if (date.getYear() == year && date.getMonthValue() == month) {
                    System.out.println("- " + date + " " + entry.getTimeStart() + " to " + entry.getTimeEnd() + ": " + entry.getTitle());
                }
            }
        }
    }

    // get year.
    private int promptYear() {
        int year = -1;
        while (year < 1) {
            if (scanner.hasNextInt()) {
                year = scanner.nextInt();
            }
            else {
                scanner.nextInt();
            }
            if (year < 1) {
                System.out.println("Invalid year. Enter again: ");
            }
        }
        scanner.nextLine();
        return year;
    }

    // get user option (month and calendar index)
    private int getUserOption(int min, int max) {
        int option = -1;
        while (option < min || option > max) {
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            else {
                scanner.next();
            }

            if (option < min || option > max) {
                System.out.print("Invalid option. Choose again: ");
            }
        }
        scanner.nextLine();
        return option;
    }
}
