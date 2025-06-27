import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all console-based UI display and input logic for the digital calendar
 * system. Works in tandem with the {@link LogicController} to interact with
 * accounts, calendars, and entries.
 */
public class DisplayController {

    /**
     * The controller responsible for managing core application logic.
     */
    private LogicController logicController;

    /**
     * Scanner used for reading user input.
     */
    private Scanner scanner;

    /**
     * Constructs a DisplayController with a reference to the logic controller.
     *
     * @param logicController The logic controller used to interact with
     * application state.
     */
    public DisplayController(LogicController logicController) {
        this.logicController = logicController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the monthly calendar view with indicators for days that contain
     * entries.
     *
     * @param monthidx The numeric month (1-12) to display.
     */
    public void displayCalendar() {
        int monthidx = this.logicController.getSelectedMonth();
        int yearidx = this.logicController.getSelectedYear();
        CalendarObject calendarObject = this.logicController.getCurrentCalendarObject();
        System.out.println("+---------------------------------------+");
        System.out.println("|       Calendar: " + monthidx + "/" + yearidx + " (" + calendarObject.getCalendarName() + ")" + "\t|");
        System.out.println("+---------------------------------------+");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("|   Sunday  |   Monday  |  Tuesday  | Wednesday |  Thursday |   Friday  | Saturday  |");
        System.out.println("+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        int currentDayPosition = 1;
        int startDay = ((LocalDate.of(yearidx, monthidx, currentDayPosition).getDayOfWeek().getValue()) % 7) + 1;
        int daysInMonth = YearMonth.of(yearidx, monthidx).lengthOfMonth();

        // Empty cells before the start day.
        for (int i = 1; i < startDay; i++) {
            System.out.print("|           ");
            currentDayPosition++;
        }

        // Print each day.
        for (int day = 1; day <= daysInMonth; day++) {
            long dayStartCheckRange = LocalDateTime.of(yearidx, monthidx, day, 0, 0)
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            long dayEndCheckRange = LocalDateTime.of(yearidx, monthidx, day, 23, 59, 59)
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

    /**
     * Displays all entries for a specific day in a calendar.
     *
     * @param monthNumber The month of the entries.
     * @param dayNumber The day to display entries for.
     */
    public void displayEntriesOfDay(int dayNumber) {
        CalendarObject calendarObject = this.logicController.getCurrentCalendarObject();
        int monthNumber = this.logicController.getSelectedMonth();
        int yearidx = this.logicController.getSelectedYear();
        System.out.println("+------------------+------------------+------------------+------------------+");
        System.out.println("|       Calendar: " + calendarObject.getCalendarName() + "\t\t|");
        System.out.println("+------------------+------------------+------------------+------------------+");
        System.out.println("|       Title      |     Description  |      Start       |       End        |");
        System.out.println("+------------------+------------------+------------------+------------------+");

        LocalDate date = LocalDate.of(yearidx, monthNumber, dayNumber);
        long dayStartCheckRange = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long dayEndCheckRange = date.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli();
        ArrayList<Entry> entries = calendarObject.getEntries();
        boolean hasEntries = false;
        for (Entry entry : entries) {
            if (entry.getStartTime() >= dayStartCheckRange && entry.getEndTime() <= dayEndCheckRange) {
                hasEntries = true;
                System.out.printf("| %-16s | %-16s | %-16s | %-16s |\n",
                        entry.getTitle(),
                        entry.getDescription(),
                        LocalDateTime.ofEpochSecond(entry.getStartTime() / 1000, 0, ZoneOffset.UTC),
                        LocalDateTime.ofEpochSecond(entry.getEndTime() / 1000, 0, ZoneOffset.UTC));
            }
        }
        if (!hasEntries) {
            System.out.println("| No entries found for this day.                                      |");
        }
        System.out.println("+------------------+------------------+------------------+------------------+");
    }

    /**
     * Displays the landing page where users can log in, sign up, or exit.
     *
     * @return A string indicating the next page to navigate to.
     */
    public String displayLandingPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar   ]-------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[       1. Login       ]-------|");
        System.out.println("|--------[      2. Sign Up      ]-------|");
        System.out.println("|--------[       3. Exit        ]-------|");
        System.out.println("+---------------------------------------+");
        System.out.print("Please select an option: ");
        int choice = this.scanner.nextInt();
        this.scanner.nextLine(); // Consume newline character
        switch (choice) {
            case 1:
                return "login";
            case 2:
                return "signup";
            case 3:
                return "exit";
            default:
                System.out.println("Invalid option. Please try again.");
                return "landing";
        }
    }

    /**
     * Displays the login page for user authentication.
     *
     * @return A string indicating the next page to navigate to.
     */
    public String displayLoginPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|-----------[   Login Page   ]----------|");
        System.out.println("+---------------------------------------+");
        System.out.print("Enter Username(Leave blank to cancel): ");
        String username = scanner.nextLine();
        System.out.print("Enter Password(Leave blank to cancel): ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Login cancelled.");
            return "landing";
        }

        if (!this.logicController.authenticateAccount(username, password)) {
            System.out.println("Invalid username or password. Please try again.");
            return "login";
        }

        if (this.logicController.getCurrentAccount() != null) {
            return "menu";
        } else {
            return "login";
        }
    }

    /**
     * Displays the sign-up page to create a new account.
     *
     * @return A string indicating the next page to navigate to.
     */
    public String displaySignUpPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|----------[   Sign Up Page   ]---------|");
        System.out.println("+---------------------------------------+");
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
            this.logicController.addCalendarObject(username, username, false);
            System.out.println("Account created successfully. Please log in.");
            return "landing";
        } else {
            System.out.println("Username already exists. Please try again.");
            return "signup";
        }
    }

    /**
     * Displays the main menu after logging in. Provides access to calendar
     * operations.
     *
     * @return A string indicating the next page to navigate to.
     */
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

                    for (CalendarObject calendar : sharedCalendars) {
                        System.out.printf("|   Public    %-22s|\n", calendar.getCalendarName());
                    }
                    System.out.println("+---------------------------------------+");
                    for (CalendarObject calendar : calendarList) {
                        System.out.printf("|   Private    %-22s|\n", calendar.getCalendarName());
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
                        return "calendar";      // Views the calendar 
                    } else {
                        System.out.printf("%s calendar not found.\n", isPublic ? "Public" : "Private");
                    }
                }
                return "menu";
            case 2:
                System.out.println("+---------------------------------------+");
                System.out.println("|----------[   Add Calendar   ]---------|");
                System.out.println("|---------------------------------------|");
                System.out.println("|---[ 1. Add from Public Calendars  ]---|");
                System.out.println("|---[ 2. Create a New Calendar      ]---|");
                System.out.println("+---------------------------------------+");
                System.out.print("Enter number: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 1) {
                    ArrayList<CalendarObject> sharedCalendarList = logicController.getPublicCalendarObjects();
                    for (CalendarObject cal : sharedCalendarList) {
                        System.out.println("- " + cal.getCalendarName());
                    }

                    System.out.println("Enter the name of public calendar to add: ");
                    String selectedName = scanner.nextLine();

                    CalendarObject selectedCalendar = sharedCalendars.stream()
                            .filter(calendar -> calendar.isPublic() && calendar.getCalendarName().equalsIgnoreCase(selectedName))
                            .findFirst()
                            .orElse(null);

                    if (selectedCalendar != null) {
                        CalendarObject copiedCalendar = new CalendarObject(selectedCalendar.getCalendarName(), false);
                        this.logicController.addCalendarInstance(copiedCalendar);
                        this.logicController.getCurrentAccount().addOwnedCalendar(copiedCalendar.getCalendarName());
                        System.out.printf("Calendar %s copied successfully.\n", copiedCalendar.getCalendarName());
                    } else {
                        System.out.printf("Calendar %s not found.\n", selectedName);
                    }
                } else if (option == 2) {
                    System.out.print("Enter new calendar name: ");
                    String newCalendarName = scanner.nextLine();
                    scanner.nextLine();
                    System.out.print("Make it public? (yes/no): ");
                    String visibility = scanner.nextLine().trim().toLowerCase();

                    CalendarObject newCalendarObject = new CalendarObject(newCalendarName, false);
                    switch (visibility) {
                        case "yes" ->
                            newCalendarObject.setIsPublic(true);
                        case "no" ->
                            newCalendarObject.setIsPublic(false);
                        default ->
                            System.out.println("Invalid input. Calendar will be created as private.");
                    }

                    this.logicController.addCalendarInstance(newCalendarObject);
                    if (!newCalendarObject.isPublic()) {
                        this.logicController.getCurrentAccount().addOwnedCalendar(newCalendarObject.getCalendarName());
                    }
                    System.out.println("New calendar created successfully.");
                }
                return "menu";
            case 3:
                LocalDate today = LocalDate.now();
                CalendarObject currentCalendar = this.logicController.getCurrentCalendarObject();
                if (currentCalendar != null) {
                    System.out.println("Today's Date: " + today.toString());
                    displayEntriesOfDay(today.getDayOfMonth());
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
                System.out.println("|----------[   Logging Out   ]----------|");
                System.out.println("+---------------------------------------+");
                this.logicController.logoutAccount();
                return "landing";
            default:
                return "menu";
        }
    }

    /**
     * Displays and handles options to view, add, edit, or remove entries in the
     * selected calendar.
     *
     * @param monthidx The selected month (1-12) for which entry operations
     * apply.
     */
    public String displayEntryOptions() {
        boolean stayInEntryMenu = true;

        while (stayInEntryMenu) {
            int monthidx = this.logicController.getSelectedMonth();
            int yearidx = this.logicController.getSelectedYear();
            displayCalendar();
            System.out.println("+---------------------------------------+");
            System.out.println("|--------[    Entry Options    ]--------|");
            System.out.println("+---------------------------------------+");
            System.out.println("|--------[   1. Next Month     ]--------|");
            System.out.println("|--------[   2. Previous Month ]--------|");
            System.out.println("|--------[   3. Switch MM/YYYY ]--------|");
            System.out.println("|--------[   4. View Entries   ]--------|");
            System.out.println("|--------[   5. Add Entry      ]--------|");
            System.out.println("|--------[   6. Edit Entry     ]--------|");
            System.out.println("|--------[   7. Remove Entry   ]--------|");
            System.out.println("|--------[   8. Remove Calendar]--------|");
            System.out.println("|--------[   9. Back to Menu   ]--------|");
            System.out.println("+---------------------------------------+");
            System.out.print("Please select an option: ");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    if (monthidx == 12) {
                        monthidx = 1;
                        yearidx++;
                    } else {
                        monthidx++;
                    }
                    this.logicController.setSelectedMonth(monthidx);
                    this.logicController.setSelectedYear(yearidx);
                    break;
                case 2:
                    if (monthidx == 1) {
                        monthidx = 12;
                        yearidx--;
                    } else {
                        monthidx--;
                    }
                    this.logicController.setSelectedMonth(monthidx);
                    this.logicController.setSelectedYear(yearidx);
                    break;
                case 3:
                    System.out.print("Enter new month (1-12): ");
                    monthidx = this.scanner.nextInt();
                    this.scanner.nextLine();
                    System.out.print("Enter new year: ");
                    yearidx = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if ((monthidx < 1 || monthidx > 12) || (yearidx < 1970 || yearidx > 2999)) {
                        System.out.println("Invalid selection. Please try again.");
                    } else {
                        this.logicController.setSelectedMonth(monthidx);
                        this.logicController.setSelectedYear(yearidx);
                    }
                    break;
                case 4:
                    System.out.println("Select a day to view entries:");
                    System.out.print("Enter day (1-31): ");
                    int day = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (day < 1 || day > 31) {
                        System.out.println("Invalid day selection. Please try again.");
                    } else {
                        displayEntriesOfDay(day);
                    }
                    break;
                case 5:
                    System.out.println("Enter Day of the Month for the entry: ");
                    int entryDay = this.scanner.nextInt();
                    this.scanner.nextLine();
                    System.out.println("Enter Title of the entry: ");
                    String title = this.scanner.nextLine();
                    System.out.println("Enter Description of the entry: ");
                    String description = this.scanner.nextLine();
                    int maximumDaysInMonth = YearMonth.of(yearidx, monthidx).lengthOfMonth();
                    if (entryDay < 1 || entryDay > maximumDaysInMonth) {
                        System.out.println("Invalid day selection. Please try again.");
                    } else {
                        System.out.println("Enter Start Time (HH:MM): ");
                        String startTimeInput = this.scanner.nextLine();
                        System.out.println("Enter End Time (HH:MM): ");
                        String endTimeInput = this.scanner.nextLine();
                        try {
                            LocalDateTime startTime = LocalDateTime.of(
                                    yearidx,
                                    monthidx, entryDay,
                                    Integer.parseInt(startTimeInput.split(":")[0]),
                                    Integer.parseInt(startTimeInput.split(":")[1])
                            );
                            LocalDateTime endTime = LocalDateTime.of(
                                    yearidx,
                                    monthidx, entryDay,
                                    Integer.parseInt(endTimeInput.split(":")[0]),
                                    Integer.parseInt(endTimeInput.split(":")[1])
                            );

                            if (endTime.isBefore(startTime)) {
                                System.out.println("End time cannot be before start time. Please try again.");
                            } else {
                                long startMillis = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                long endMillis = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                this.logicController.addEntryToCurrentCalendarObject(title, description, startMillis, endMillis);
                                System.out.println("Entry added successfully.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid time format. Please use HH:MM format.");
                        }
                    }
                    break;
                case 6:
                    System.out.println("Select a day to view entries:");
                    System.out.print("Enter day (1-31): ");
                    int dayInMonth = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (dayInMonth < 1 || dayInMonth > 31) {
                        System.out.println("Invalid day selection. Please try again.");
                    } else {
                        displayEntriesOfDay(dayInMonth);
                        System.out.println("Enter the title of the entry you want to edit: ");
                        String entryTitle = this.scanner.nextLine();
                        Entry entryToEdit = this.logicController.getCurrentCalendarObject().getEntries().stream()
                                .filter(entry -> entry.getTitle().equalsIgnoreCase(entryTitle))
                                .findFirst()
                                .orElse(null);
                        if (entryToEdit != null) {
                            System.out.println("+---------------------------------------+");
                            System.out.println("|----[      Edit Entry Options     ]----|");
                            System.out.println("+---------------------------------------+");
                            System.out.println("|----[  1. Edit Title              ]----|");
                            System.out.println("|----[  2. Edit Description        ]----|");
                            System.out.println("|----[  3. Edit Start Time (HH:MM) ]----|");
                            System.out.println("|----[  4. Edit End Time (HH:MM)   ]----|");
                            System.out.println("|----[  5. Edit All 4 Fields       ]----|");
                            System.out.println("+---------------------------------------+");
                            System.out.println("Choose what you want to edit: ");
                            int editOption = this.scanner.nextInt();
                            this.scanner.nextLine();

                            String newTitle = entryToEdit.getTitle();
                            String newDescription = entryToEdit.getDescription();
                            long newStartTime = entryToEdit.getStartTime();
                            long newEndTime = entryToEdit.getEndTime();

                            if (editOption == 1) {
                                System.out.print("Enter new title: ");
                                String input = this.scanner.nextLine();
                                if (!input.isBlank()) {
                                    newTitle = input;
                                }
                            } else if (editOption == 2) {
                                System.out.print("Enter new description: ");
                                String input = this.scanner.nextLine();
                                if (!input.isBlank()) {
                                    newDescription = input;
                                }
                            } else if (editOption == 3) {
                                System.out.print("Enter new start time (HH:MM): ");
                                String input = this.scanner.nextLine();
                                try {
                                    if (!input.isBlank()) {
                                        LocalDateTime start = LocalDateTime.of(
                                                yearidx,
                                                monthidx, dayInMonth, Integer.parseInt(input.split(":")[0]),
                                                Integer.parseInt(input.split(":")[1])
                                        );
                                        newStartTime = start.toInstant(ZoneOffset.UTC).toEpochMilli();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid start time format.");
                                }
                            } else if (editOption == 4) {
                                System.out.println("Enter new End Time (HH:MM): ");
                                String input = this.scanner.nextLine();
                                try {
                                    if (!input.isBlank()) {
                                        LocalDateTime end = LocalDateTime.of(yearidx,
                                                monthidx, dayInMonth, Integer.parseInt(input.split(":")[0]),
                                                Integer.parseInt(input.split(":")[1])
                                        );
                                        newEndTime = end.toInstant(ZoneOffset.UTC).toEpochMilli();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid start time format.");
                                }
                            } else if (editOption == 5) {
                                System.out.println("Enter new Title (blank to keep current): ");
                                String inputTitle = this.scanner.nextLine();
                                if (!inputTitle.isBlank()) {
                                    newTitle = inputTitle;
                                }

                                System.out.println("Enter new Description (blank to keep current): ");
                                String inputDesc = this.scanner.nextLine();
                                if (!inputDesc.isBlank()) {
                                    newDescription = inputDesc;
                                }

                                System.out.println("Enter new Start Time (HH:MM, blank to keep current): ");
                                String inputStart = this.scanner.nextLine();
                                if (!inputStart.isBlank()) {
                                    try {
                                        LocalDateTime start = LocalDateTime.of(yearidx,
                                                monthidx, dayInMonth, Integer.parseInt(inputStart.split(":")[0]),
                                                Integer.parseInt(inputStart.split(":")[1])
                                        );
                                        newStartTime = start.toInstant(ZoneOffset.UTC).toEpochMilli();
                                    } catch (Exception e) {
                                        System.out.println("Invalid start time format.");
                                    }
                                }

                                System.out.println("Enter new End Time (HH:MM, blank to keep current): ");
                                String inputEnd = this.scanner.nextLine();
                                if (!inputEnd.isBlank()) {
                                    try {
                                        LocalDateTime end = LocalDateTime.of(
                                                yearidx,
                                                monthidx, dayInMonth, Integer.parseInt(inputEnd.split(":")[0]),
                                                Integer.parseInt(inputEnd.split(":")[1])
                                        );
                                        newEndTime = end.toInstant(ZoneOffset.UTC).toEpochMilli();
                                    } catch (Exception e) {
                                        System.out.println("Invalid end time format.");
                                    }
                                }
                            } else {
                                System.out.println("End time cannot be before start time. Edit cancelled.");
                            }

                            if (newEndTime < newStartTime) {
                                System.out.println("End time cannot be before start time.");
                            } else {
                                Entry updatedEntry = new Entry(newTitle, newDescription);
                                updatedEntry.setStartTime(newStartTime);
                                updatedEntry.setEndTime(newEndTime);
                                this.logicController.editEntryInCurrentCalendarObject(entryToEdit, updatedEntry);
                                System.out.println("Entry updated successfully.");
                            }
                        }
                    }
                    break;
                case 7:
                    System.out.println("Enter day of entry to remove (1-31): ");
                    int dayToRemove = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (dayToRemove < 1 || dayToRemove > 31) {
                        System.out.println("Invalid day. Try again.");
                    } else {
                        displayEntriesOfDay(dayToRemove);
                        System.out.print("Enter the title of the entry to remove: ");
                        String removeTitle = this.scanner.nextLine();
                        this.logicController.removeEntryFromCurrentCalendarObject(removeTitle);
                        System.out.println("Entry was removed successfully!");
                    }
                    break;
                case 8:
                    this.logicController.removeCurrentCalendarObject();
                    stayInEntryMenu = false;        // Exit loop and return to menu.
                    System.out.println("Calendar Deleted.");
                case 9:
                    stayInEntryMenu = false;        // Exit loop and return to menu.
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        return "menu";
    }

    /**
     * Displays the month selection interface and renders the selected month's
     * calendar.
     */
    public String displayMonthSelection() {
        String nextPage = "menu";
        System.out.println("+---------------------------------------+");
        System.out.println("|----------[   Select Month   ]---------|");
        System.out.println("+---------------------------------------+");
        System.out.println("|--[ 1. Jan   |  2. Feb   |  3. Mar  ]--|");
        System.out.println("|--[ 4. Apr   |  5. May   |  6. Jun  ]--|");
        System.out.println("|--[ 7. Jul   |  8. Aug   |  9. Sep  ]--|");
        System.out.println("|--[ 10. Oct  |  11. Nov  |  12. Dec ]--|");
        System.out.println("+---------------------------------------+");
        System.out.println("|-----------[ 0. Back to Menu ]---------|");
        System.out.println("+---------------------------------------+");
        System.out.print("Please select a month (1-12): ");
        int monthChoice = this.scanner.nextInt();
        this.scanner.nextLine();
        if (monthChoice == 0) {
            System.out.println("Returning to menu...");
            nextPage = "menu";
        } else if (monthChoice < 1 || monthChoice > 12) {
            System.out.println("Invalid month selection. Returning to menu.");
        } else {
            int yearidx = LocalDate.now().getYear();
            this.logicController.setSelectedMonth(monthChoice);
            this.logicController.setSelectedYear(yearidx);
            displayEntryOptions();
            nextPage = "entry";
        }
        return nextPage;
    }
}
