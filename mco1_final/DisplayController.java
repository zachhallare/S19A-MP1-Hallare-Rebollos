
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Scanner;

import s19a_mp1_hallare_rebollos.src.main.java.com.hallareandrebollos.objects.MonthCalendar;

public class DisplayController {

    private LogicController logicController;
    private Scanner scanner;

    public DisplayController(LogicController logicController) {
        this.logicController = logicController;
        this.scanner = new Scanner(System.in);
    }

    public void displayCalendar() {
        CalendarObject calendarObject = logicController.getCurrentCalendarObject();
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;
        int startDay = ((LocalDate.of(calendarObject.getYearIdentifier(), calendarObject.getMonthIdentifier(), currentDayPosition).getDayOfWeek().getValue()) % 7) + 1;
        int daysInMonth = YearMonth.of(calendarObject.getYearIdentifier(), calendarObject.getMonthIdentifier()).lengthOfMonth();

        // Empty cells before the start day.
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print each day.
        for (int day = 1; day <= daysInMonth; day++) {
            long dayStartCheckRange = LocalDateTime.of(calendarObject.getYearIdentifier(), calendarObject.getMonthIdentifier(), day, 0, 0)
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            long dayEndCheckRange = LocalDateTime.of(calendarObject.getYearIdentifier(), calendarObject.getMonthIdentifier(), day, 23, 59, 59)
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            boolean hasEntry = calendarObject.getEntries().stream()
                    .anyMatch(entry -> entry.getStartTime() >= dayStartCheckRange && entry.getEndTime() <= dayEndCheckRange);

            if (day == LocalDate.now().getDayOfMonth()
                    && calendarObject.getMonthIdentifier() == LocalDate.now().getMonthValue()
                    && calendarObject.getYearIdentifier() == LocalDate.now().getYear()) {
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

    public void displayEntries() {
        CalendarObject calendarObject = logicController.getCurrentCalendarObject();
        System.out.println("+------------------+------------------+------------------+------------------+");
        System.out.println("|       Title      |     Description  |      Start       |       End        |");
        System.out.println("+------------------+------------------+------------------+------------------+");

        for (Entry entry : calendarObject.getEntries()) {
            String title = entry.getTitle();
            String description = entry.getDescription();
            String startTime = LocalDateTime.ofEpochSecond(entry.getStartTime() / 1000, 0, ZoneOffset.UTC).toString();
            String endTime = LocalDateTime.ofEpochSecond(entry.getEndTime() / 1000, 0, ZoneOffset.UTC).toString();

            System.out.printf("| %-16s | %-16s | %-16s | %-16s |\n", title, description, startTime, endTime);
            System.out.println("+------------------+------------------+------------------+------------------+");
        }

        System.out.println("+------------------+------------------+------------------+------------------+");
    }

    public String displayLandingPage() {
        System.out.println("+----------------------------------------+");
        System.out.println("|--------[   Digital Calendar   ]--------|");
        System.out.println("|----------------------------------------|");
        System.out.println("|--------[       1. Login       ]--------|");
        System.out.println("|--------[      2. Sign Up      ]--------|");
        System.out.println("|--------[       3. Exit        ]--------|");
        System.out.println("+----------------------------------------+");
        System.out.print("Please select an option: ");
        int choice = this.scanner.nextInt();
        this.scanner.nextLine(); // Consume newline character
        switch (choice) {
            case 1 -> {
                return "login";
            }
            case 2 -> {
                return "signup";
            }
            case 3 -> {
                return "exit";
            }
            default -> {
                System.out.println("Invalid option. Please try again.");
                return "landing";
            }
        }
    }

    public String displayLoginPage() {
        System.out.println("+----------------------------------+");
        System.out.println("|--------[   Login Page   ]--------|");
        System.out.println("+----------------------------------+");
        System.out.print("Enter Username(Leave blank to cancel): ");
        String username = scanner.nextLine();
        System.out.print("Enter Password(Leave blank to cancel): ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Login cancelled.");
            return "landing";
        }

        logicController.authenticateAccount(username, password);

        if (logicController.getCurrentAccount() != null) {
            return "calendar";
        } else {
            return "login";
        }
    }

    public String displaySignUpPage() {
        System.out.println("+----------------------------------+");
        System.out.println("|-------[   Sign Up Page   ]-------|");
        System.out.println("+----------------------------------+");
        System.out.print("Enter Username(Leave blank to cancel): ");
        String username = scanner.nextLine();
        System.out.print("Enter Password(Leave blank to cancel): ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Sign up cancelled.");
            return "landing";
        }

        if (!logicController.existingUsername(username)) {
            logicController.addAccount(username, password);
            System.out.println("Account created successfully. Please log in.");
            return "login";
        } else {
            System.out.println("Username already exists. Please try again.");
            return "signup";
        }
    }

    public String displayMenuPage() {
        ArrayList<CalendarObject> calendarList = logicController.getPrivateCalendarObjects();
        ArrayList<CalendarObject> sharedCalendars = logicController.getPublicCalendarObjects();

        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[  1. Select Calendar ]--------|");
        System.out.println("|--------[  2. Add Calendar    ]--------|");
        System.out.println("|--------[  3. Remove Calendar ]--------|");
        System.out.println("|--------[  4. Delete Account  ]--------|");
        System.out.println("|--------[  5. Logout          ]--------|");
        System.out.println("+---------------------------------------+");
        System.out.print("Please select an option: ");
        int choice = this.scanner.nextInt();
        this.scanner.nextLine();

        switch (choice) {
            case 1:
                if (calendarList.isEmpty() && sharedCalendars.isEmpty()) {
                    System.out.println("No calendars available. Please add a calendar first.");
                } else {
                    System.out.println("+---------------------------------------+");
                    System.out.println("|--------[   Digital Calendar  ]--------|");
                    System.out.println("|---------------------------------------|");
                    System.out.println("|--------[    Calendar List    ]--------|");
                    System.out.println("|---------------------------------------|");

                    for (int i = 0; i < sharedCalendars.size(); i++) {
                        System.out.printf("|   Public    %s \t\t|\n", sharedCalendars.get(i));
                    }
                    System.out.println("+---------------------------------------+");
                    for (int i = 0; i < calendarList.size(); i++) {
                        System.out.printf("|   Private    %s \t\t|\n", calendarList.get(i));
                    }
                    System.out.println("+---------------------------------------+");
                }
                return "menu";
            case 2:
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[     Add Calendar     ]-------|");
                System.out.println("|---------------------------------------|");
                System.out.println("|----[ 1. Add from Public Calendars]----|");
                System.out.println("|----[ 2. Create a New Calendar    ]----|");
                System.out.println("+---------------------------------------+");
                System.out.print("Enter number: ");
                int option = scanner.nextInt();
                scanner.nextLine();     // in case may new line.
                // The following variables and logic are placeholders; you should replace them with your actual implementation.
                ArrayList<String> publicIDs = new ArrayList<>(); // Placeholder: populate with actual public calendar IDs
                // Example: publicIDs = logicController.getPublicCalendarIDs();
                // Example: calendarManager and loggedInAccount should be replaced with your actual objects
                if (option == 1) {
                    for (int i = 0; i < publicIDs.size(); i++) {
                        System.out.println((i + 1) + ". " + publicIDs.get(i));
                    }

                    System.out.print("Enter the name of public calendar to add: ");
                    String selectedName = scanner.nextLine();

                    // Placeholder: replace calendarManager and loggedInAccount with your actual objects
                    // Example: if (publicIDs.contains(selectedName) && !calendarManager.getCalendarIDs().contains(selectedName)) {
                    if (publicIDs.contains(selectedName)) {
                        File src = new File("data/calendars/public/" + selectedName + ".txt");
                        File dst = new File("data/calendars/USERNAME/" + selectedName + ".txt"); // Replace USERNAME appropriately

                        try {
                            java.nio.file.Files.copy(src.toPath(), dst.toPath());
                            System.out.println("Calendar added successfully!");
                        } catch (IOException e) {
                            System.out.println("Failed to add calendar: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid selection or calendar already added.");
                    }
                } // User can create a new calendar and decide if it's private or public.
                else if (option == 2) {
                    System.out.print("Enter new calendar name: ");
                    String newCalendarName = scanner.nextLine();
                    System.out.print("Make it public? (yes/no): ");
                    String visibility = scanner.nextLine().trim().toLowerCase();

                    CalendarObject newCalendarObject = new CalendarObject(newCalendarName, false, choice, option);
                    newCalendar.setCalendarName(newCalendarName);
                    // Placeholder: replace with actual username
                    newCalendar.setOwnerUsername("USERNAME");
                    newCalendar.setSelectedDay(LocalDate.now().getDayOfMonth());

                    boolean saved = newCalendar.saveCalendar(visibility.equals("yes") ? "" : "USERNAME");
                    if (saved) {
                        System.out.println("Calendar created and saved as " + newCalendarName + ".txt");
                    } else {
                        System.out.println("Failed to save calendar.");
                    }
                }
                return "menu";
            case 3:
                // Implement remove calendar logic here
                System.out.println("Remove calendar functionality not implemented yet.");
                return "menu";
            case 4:
                // Implement delete account logic here
                System.out.println("Delete account functionality not implemented yet.");
                return "menu";
            case 5:
                logicController.logoutAccount();
                return "landing";
            default:
                return "menu";
        }
    }
}
