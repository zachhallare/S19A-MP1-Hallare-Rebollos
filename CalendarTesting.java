import java.time.*;
import java.util.*;

// this is sample outputs for testing.

public class CalendarTesting {
    public static void main(String[] args) {
        ArrayList<CalendarEntry> entries = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        entries.add(new CalendarEntry("Meeting", LocalDate.of(2025, 6, 12), LocalTime.of(10, 0), LocalTime.of(11, 0), "Project Discussion"));
        entries.add(new CalendarEntry("Doctor", LocalDate.of(2025, 6, 18), LocalTime.of(15, 0), LocalTime.of(16, 0), "Check-up"));

        // Try Month 6 (June)
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        
        // Try Year 2025
        System.out.print("Enter year: ");
        int year = scanner.nextInt();

        CalendarView.printCalendar(month, year, entries);
        scanner.close();
    }
}
