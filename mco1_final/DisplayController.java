
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Scanner;

public class DisplayController {

    private LogicController logicController;
    private Scanner scanner;

    public DisplayController(LogicController logicController) {
        this.logicController = logicController;
        this.scanner = new Scanner(System.in);
    }

    public void displayCalendar(int monthidx) {
        CalendarObject calendarObject = this.logicController.getCurrentCalendarObject();
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;
        int startDay = ((LocalDate.of(calendarObject.getYearIdentifier(), monthidx, currentDayPosition).getDayOfWeek().getValue()) % 7) + 1;
        int daysInMonth = YearMonth.of(calendarObject.getYearIdentifier(), monthidx).lengthOfMonth();

        // Empty cells before the start day.
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print each day.
        for (int day = 1; day <= daysInMonth; day++) {
            long dayStartCheckRange = LocalDateTime.of(calendarObject.getYearIdentifier(), monthidx, day, 0, 0)
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            long dayEndCheckRange = LocalDateTime.of(calendarObject.getYearIdentifier(), monthidx, day, 23, 59, 59)
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            boolean hasEntry = calendarObject.getEntries().stream()
                    .anyMatch(entry -> entry.getStartTime() >= dayStartCheckRange && entry.getEndTime() <= dayEndCheckRange);

            if (hasEntry) {
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
        CalendarObject calendarObject = this.logicController.getCurrentCalendarObject();
        System.out.println("+------------------+------------------+------------------+------------------+");
        System.out.println("|       Calendar: " + calendarObject.getCalendarName() + "\t\t|");
        System.out.println("+------------------+------------------+------------------+------------------+");
        System.out.println("|       Title      |     Description  |      Start       |       End        |");
        System.out.println("+------------------+------------------+------------------+------------------+");

        if (calendarObject.getEntries().isEmpty()) {
            System.out.println("|      No entries found! You are free at this specified time! Enjoy!        |");
            System.out.println("+------------------+------------------+------------------+------------------+");

        } else {
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

        this.logicController.authenticateAccount(username, password);

        if (this.logicController.getCurrentAccount() != null) {
            return "menu";
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

        if (!this.logicController.existingUsername(username)) {
            this.logicController.addAccount(username, password);
            // create a default calendar for the user
            // using the current year

            System.out.println("Account created successfully. Please log in.");
            return "login";
        } else {
            System.out.println("Username already exists. Please try again.");
            return "signup";
        }
    }

    public String displayMenuPage() {
        ArrayList<CalendarObject> calendarList = this.logicController.getPrivateCalendarObjects();
        ArrayList<CalendarObject> sharedCalendars = this.logicController.getPublicCalendarObjects();

        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[  1. Select Calendar ]--------|");
        System.out.println("|--------[  2. Add Calendar    ]--------|");
        System.out.println("|--------[  3. View Today      ]--------|");
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
                    System.out.println("If there are duplicates, kindly input \"public calendar_name\" to select a public calendar.");
                    System.out.print("Enter the name of the calendar to select: ");
                    String calendarName = scanner.nextLine();

                    CalendarObject selectedCalendar;
                    boolean isPublic = false;

                    if (calendarName.toLowerCase().startsWith("public ")) {
                        String publicCalendarName = calendarName.substring(7).trim();
                        selectedCalendar = sharedCalendars.stream()
                                .filter(calendar -> calendar.getCalendarName().equalsIgnoreCase(publicCalendarName))
                                .findFirst()
                                .orElse(null);
                        isPublic = true;
                    } else {
                        selectedCalendar = calendarList.stream()
                                .filter(calendar -> calendar.getCalendarName().equalsIgnoreCase(calendarName))
                                .findFirst()
                                .orElse(null);
                    }

                    if (selectedCalendar != null) {
                        int calendarIndex = this.logicController.getCalendarFromName(selectedCalendar.getCalendarName(), isPublic);
                        this.logicController.setCalendarObjectIndex(calendarIndex);
                        System.out.printf("Selected %s calendar: %s\n", isPublic ? "public" : "private", selectedCalendar.getCalendarName());
                    } else {
                        System.out.printf("%s calendar not found.\n", isPublic ? "Public" : "Private");
                    }
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
                scanner.nextLine();

                if (option == 1) {
                    for (int i = 0; i < sharedCalendars.size(); i++) {
                        System.out.println((i + 1) + ". " + sharedCalendars.get(i).getCalendarName());
                    }

                    System.out.println("Enter the name of public calendar to add: ");
                    String selectedName = scanner.nextLine();

                    CalendarObject selectedCalendar = sharedCalendars.stream()
                            .filter(calendar -> calendar.isPublic() && calendar.getCalendarName().equalsIgnoreCase(selectedName))
                            .findFirst()
                            .orElse(null);

                    if (selectedCalendar != null) {
                        selectedCalendar.setIsPublic(false);
                        this.logicController.addCalendarInstance(selectedCalendar);
                        this.logicController.getCurrentAccount().addOwnedCalendar(selectedCalendar.getCalendarName());
                        System.out.printf("Calendar %s copied successfully added successfully.\n", selectedCalendar.getCalendarName());
                    } else {
                        System.out.printf("Calendar %s not found.\n", selectedName);
                    }
                } else if (option == 2) {
                    System.out.print("Enter new calendar name: ");
                    String newCalendarName = scanner.nextLine();
                    System.out.println("Select the year for the new calendar: ");
                    System.out.print("Enter year (e.g., 2023): ");
                    int yearidx = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Make it public? (yes/no): ");
                    String visibility = scanner.nextLine().trim().toLowerCase();

                    CalendarObject newCalendarObject = new CalendarObject(newCalendarName, false, yearidx);
                    switch (visibility) {
                        case "yes" ->
                            newCalendarObject.setIsPublic(true);
                        case "no" ->
                            newCalendarObject.setIsPublic(false);
                        default ->
                            System.out.println("Invalid input. Calendar will be created as private.");
                    }
                }
                return "menu";
            case 3:
                LocalDate today = LocalDate.now();
                CalendarObject currentCalendar = this.logicController.getCurrentCalendarObject();
                if (currentCalendar != null
                        && currentCalendar.getYearIdentifier() == today.getYear()) {
                    System.out.println("Today's Date: " + today.toString());
                    displayEntries();
                }
                return "menu";
            case 4:
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[   Delete Account   ]---------|");
                System.out.println("+---------------------------------------+");
                System.out.print("Are you sure you want to delete your account? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("yes")) {
                    this.logicController.deactivateAccount();
                    return "landing";
                } else {
                    System.out.println("Deactivation cancelled.\n\n");
                }
                return "menu";
            case 5:
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[    Logging Out     ]---------|");
                System.out.println("+---------------------------------------+");
                this.logicController.logoutAccount();
                return "landing";
            default:
                return "menu";
        }
    }
}
