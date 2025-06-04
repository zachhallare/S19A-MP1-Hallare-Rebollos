import java.util.*;

// Views the calender. based on my lab activity 1 before.
// idk how to integrate it yet i just wanted to see how it would look first.
// uh... i tried :(

public class CalendarView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);   
        
        System.out.print("Enter the month (1=January, 12=December): ");
        int month = scanner.nextInt();     
        
        System.out.print("Enter the start day (1=Sunday, 7=Saturday): ");
        int startDay = scanner.nextInt();  

        System.out.print("Enter the year: ");
        int year = scanner.nextInt();    

        printCalendar(month, startDay, year);
        scanner.close();
    }

    // prints the calendar for given month, year, and starting day of week.
    public static void printCalendar(int month, int startDay, int year) {
        int daysInMonth = getDaysInMonth(month, year);

        System.out.println("\n+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;

        // print empty slots before the 1st day
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // print oh my days
        for (int day = 1; day <= daysInMonth; day++) {
            System.out.printf("| %2d        ", day);

            if (currentDayPosition % 7 == 0) {
                System.out.println("|");
                System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
            }
            currentDayPosition++;
        }

        // print empty slots if last week is incomplete
        if ((currentDayPosition - 1) % 7 != 0) {
            int remainingSlots = 7 - ((currentDayPosition - 1) % 7);
            for (int i = 0; i < remainingSlots; i++) {
                System.out.print("|           ");
            }
            System.out.println("|");
            System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        }
    }

    // returns the number of days in the month or year
    public static int getDaysInMonth(int month, int year) {
        switch(month) {
            case 2:  // February, check leap year
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4: case 6: case 9: case 11:
                return 30;
            default:
                return 31;
        }
    }
}
