package com.hallareandrebollos.lib;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.hallareandrebollos.objects.Account;
import com.hallareandrebollos.objects.Entry;
import com.hallareandrebollos.objects.MonthCalendar;

public class TextInterface {

    private Scanner scanner;                        // Scanner for user input.
    private Account loggedInAccount;                // The currently logged-in account.
    private int currentCalendarID;                  // The ID of the currently selected calendar.
    private int pageIndex;                          // Page index to track the current page in the interface.
    private ArrayList<String> calendarIDs;          // List of calendar IDs associated with the logged-in account.
    private ArrayList<String> publicCalendarIDs;    // List of public calendar IDs.
    private MonthCalendar currentCalendar;          // The currently loaded calendar object.

    // Constructor.
    public TextInterface() {
        this.scanner = new Scanner(System.in);      // Initialize the scanner for user input.
        this.loggedInAccount = null;                // Initially, no account is logged in.
        this.currentCalendarID = -1;                // No calendar is selected initially.
        this.pageIndex = 0;                         // Start at the first page.
        this.calendarIDs = new ArrayList<>();
        this.publicCalendarIDs = new ArrayList<>();
    }


    // Main logic for the user login page.
    public void LoginPageLogic() {
        System.out.print("Enter number: ");
        int selectedOption = scanner.nextInt();     // Read user input for the selected option.
        scanner.nextLine();     // For safety measures (if may extra newline).
        System.out.println("\n");

        switch (selectedOption) {
            // Login Page.
            case 1 -> {
                System.out.println("+----------------------------------+");
                System.out.println("|--------[   Login Page   ]--------|");
                System.out.println("+----------------------------------+");

                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                // Attempt to authenticate the user.
                boolean validLogin = true;
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Username and Password cannot be empty. Please try again.");
                    validLogin = false;
                }

                Account tempAccount = null;
                if (validLogin) {
                    tempAccount = new Account();
                    if (!tempAccount.authenticate(username, password)) {
                        System.out.println("Invalid username or password. or the account is deactivated. Please try again.");
                        System.out.println("\n");
                        validLogin = false;
                    }
                }

                if (validLogin) {
                    this.loggedInAccount = tempAccount;
                    System.out.println("Login successful!");
                    System.out.println("\n");
                    this.pageIndex = 1;         // Set the page index to the main menu.
                }
            }
            // Sign Up Page.
            case 2 -> {
                System.out.println("+----------------------------------+");
                System.out.println("|-------[   Sign Up Page   ]-------|");
                System.out.println("+----------------------------------+");

                boolean validSignup = false;
                Account tempAccount = new Account();

                while (!validSignup) {
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine().trim();
                    
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine().trim();

                    if (username.isEmpty() || password.isEmpty()) {
                        System.out.println("Username and Password cannot be empty. Please try again.");
                    } else {
                        boolean created = tempAccount.createAccount(username, password);
                        if (created) {
                            validSignup = true;
                        } else {
                            System.out.println("Failed to create account. Please try again.\n");
                        }
                    }
                }

                this.loggedInAccount = tempAccount;
                System.out.println("Account created successfully!");
                System.out.println("\n");
                this.pageIndex = 1;         // Set the page index to the main menu.
            }
            // Exit the program.
            case 3 -> {
                System.out.println("Exiting the application...");
                this.pageIndex = -1;
            }
            // If invalid option.
            default ->
                System.out.println("Invalid option. Please try again.");
        }
    }


    // Main logic for the main menu page.
    public void MenuPageLogic() {
        System.out.print("Enter number: ");
        int selectedOption = scanner.nextInt();     // Read user input for the selected option.
        scanner.nextLine();     // For safety measures (if may extra newline).

        switch (selectedOption) {
            case 1 -> {
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[ Loading Today's Date ]-------|");
                System.out.println("+---------------------------------------+");
                loadTodayCalendar();
            }
            case 2 -> {
                loadCalendarList();     // Load the calendar list for the logged-in user.
                CalendarListPage();     // Display the calendar list page.
                calendarSelector();     // Allow the user to select a calendar.
            }
            case 3 -> {
                System.out.println("Logging out...");
                System.out.println("\n");
                this.loggedInAccount = null;        // Clear the logged-in account.
                this.pageIndex = 0;                 // Reset to the login page.
            }
            case 4 -> {
                // Confirmation first, in case na misclick ni user.
                System.out.print("Are you sure you want to deactivate your account? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.equals("yes")) {
                    this.loggedInAccount.deactivateAccount();
                    System.out.println("Account deactivated. Logging out...");
                    System.out.println("\n");
                    this.loggedInAccount = null;
                    this.pageIndex = 0;     // Sends the user back to the login page.
                } else {
                    System.out.println("Deactivation cancelled.");
                    System.out.println("\n");
                }
            }
            default -> {
                System.out.println("Invalid option. Please try again.");
                System.out.println("\n");
            }
        }
    }


    public void loadCalendarList() {
        // This method should load the calendar IDs associated with the given username.
        // looks into the directory data/calendars/username/ and retrieves each file name as a calendar ID.
        this.calendarIDs.clear();
        this.publicCalendarIDs.clear();
        
        File userDir = new File("data/calendars/" + this.loggedInAccount.getUsername() + "/");
        if (userDir.exists() && userDir.isDirectory()) {
            File[] userFiles = userDir.listFiles((d, name) -> name.endsWith(".txt"));
            if (userFiles != null) {
                for (File file : userFiles) {
                    String id = file.getName().replace(".txt", "");
                    this.calendarIDs.add(id);
                }
            }
        }

        File publicDir = new File("data/calendars/public/");
        if (publicDir.exists() && publicDir.isDirectory()) {
            File[] publicFiles = publicDir.listFiles((d, name) -> name.endsWith(".txt"));
            if (publicFiles != null) {
                for (File file : publicFiles) {
                    String id = file.getName().replace(".txt", "");
                    this.publicCalendarIDs.add(id);
                }
            }
        }
    }


    public void loadTodayCalendar() {
        // This method should load the calendar for today.
        // checks each calendar ID in the calendarIDs list and loads the one that matches today's date.
        boolean found = false;

        if (!this.calendarIDs.isEmpty()) {
            LocalDate today = LocalDate.now();

            for (String calenderID : this.calendarIDs) {
                MonthCalendar calendar = new MonthCalendar(new ArrayList<>());

                if (!found && calendar.loadCalendar(this.loggedInAccount.getUsername(), calenderID)) {
                    if (calendar.getMonthNumber() == today.getMonthValue() &&
                        calendar.getYearNumber() == today.getYear()) {
                            this.currentCalendar = calendar;            // Set the current calendar to the one matching today's date.
                            this.currentCalendar.setSelectedDay(today.getDayOfMonth());
                            this.currentCalendar.displayCalendar();     // Display the calendar.
                            this.currentCalendar.displayEntries();      // Display the entries for today.
                            found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No calendar matches today's date.");
            System.out.println("\n");
        }
    }

    public void calendarSelector() {
        boolean loopLock = false; // Loop lock to ensure valid input.
        while (!loopLock) {
            System.out.println("Enter Calendar ID: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Calendar ID cannot be empty. Please try again.");
            } else if (calendarIDs.contains(input)){
                this.currentCalendar = new MonthCalendar(new ArrayList<>());
                if (this.currentCalendar.loadCalendar(this.loggedInAccount.getUsername(), input)) {
                    this.currentCalendarID = Integer.parseInt(input);
                    loopLock = true; // Valid input, exit the loop.
                } else {
                    System.out.println("Failed to load calendar with ID: " + input);
                }
            } else if (this.publicCalendarIDs.contains(input)) {
                    this.currentCalendar = new MonthCalendar(new ArrayList<>());
                    if (this.currentCalendar.loadCalendar("public", input)) {
                        this.currentCalendarID = Integer.parseInt(input);
                        loopLock = true; // Valid input, exit the loop.
                    } else {
                        System.out.println("Failed to load public calendar with ID: " + input);
                    }
            } else {
                System.out.println("Invalid Calendar ID. Please try again.");
            }
        }
    }

    public void CalendarPageLogic(int selectedOption) {
        selectedOption = scanner.nextInt();     // Read user input for the selected option.
        scanner.nextLine();     // For safety measures (if may extra newline).
        
        switch (selectedOption) {
            case 1 -> {
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[   Viewing Entries   ]--------|");
                System.out.println("+---------------------------------------+");
                
                if (this.currentCalendar != null) {
                    this.currentCalendar.displayEntries();
                } else {
                    System.out.println("No calendar is currently selected.");
                }
            }
            case 2 -> {
                if (this.currentCalendar != null && this.currentCalendar.getSelectedDay() != -1) {
                    System.out.println("+---------------------------------------+");
                    System.out.println("|--------[   Adding New Entry   ]-------|");
                    System.out.println("+---------------------------------------+");
                    System.out.println("Please enter the details for the new entry:");
                    
                    System.out.println("Title: ");
                    String title = scanner.nextLine();
                    System.out.println("Description: ");
                    String description = scanner.nextLine();
                    System.out.println("Start Time (HH:mm): ");
                    String startTime = scanner.nextLine();
                    System.out.println("End Time (HH:mm): ");
                    String endTime = scanner.nextLine();

                    String date = this.currentCalendar.getYearNumber() + "-" + 
                                this.currentCalendar.getMonthNumber() + "-" + 
                                this.currentCalendar.getSelectedDay();

                    boolean validEntry = true;
                    if (title.isEmpty() || description.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                        System.out.println("All fields are required. Please try again.");
                        validEntry = false;
                    }

                    Entry newEntry = null;
                    if (validEntry) {
                        newEntry = new Entry(title, description);
                        if (!newEntry.setStartTime(startTime) || !newEntry.setEndTime(endTime) || !newEntry.setDate(date)) {
                            System.out.println("Invalid time or date format. Please try again.");
                            validEntry = false;
                        }
                    }

                    if (validEntry) {
                        this.currentCalendar.addEntry(newEntry); // Add the new entry to the current calendar.
                        System.out.println("Entry added successfully!");
                    }
                } else {
                    System.out.println("No calendar is currently selected.");
                }
            }
            case 3 -> {
                if (this.currentCalendar != null && this.currentCalendar.getSelectedDay() != -1) {
                    System.out.println("+---------------------------------------+");
                    System.out.println("|---------[   Deleting Entry   ]--------|");
                    System.out.println("+---------------------------------------+");
                    System.out.println("Please enter the title of the entry to delete:");
                    String title = scanner.nextLine();

                    boolean canDelete = true;
                    if (title.isEmpty()) {
                        System.out.println("Title cannot be empty. Please try again.");
                        canDelete = false;
                    }
                    if (canDelete && !this.currentCalendar.entryExists(title)) {
                        System.out.println("No entry found with the title: " + title);
                        canDelete = false;
                    }

                    if (canDelete) {
                        this.currentCalendar.deleteEntry(title);
                        System.out.println("Entry"+ title + "deleted successfully!");
                    }
                } else {
                    System.out.println("No calendar is currently selected.");
                }
            }
            case 4 -> {
                System.out.println("Returning to the current calendar menu...");
                this.pageIndex = 2;
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    public void DaySelector() {
        boolean loopLock = false;       // Loop lock to ensure valid input.
        while (!loopLock) {
            System.out.println("Select a Date to view (Enter 0 to return):");
            int input = scanner.nextInt();
            if (input == 0) {
                loopLock = true;        // Exit the loop if user enters 0.
            } else if (input >= 1 && input <= this.currentCalendar.getDaysInMonth()) {
                this.currentCalendar.setSelectedDay(input);
                loopLock = true;
            } else {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    public void CalendarDisplayController() {
        // This method should display the current calendar and allow the user to select a day.
        if (this.currentCalendar != null) {
            this.currentCalendar.displayCalendar();     // Display the calendar.
            DaySelector();                              // Allow the user to select a day.
            CalendarMenuPage();                         // Show the calendar menu page.
        } else {
            System.out.println("No calendar is currently selected.");
        }
    }

    // ASCII Art for Each Pages.
    public void LoginPage() {
        System.out.println("+----------------------------------------+");
        System.out.println("|--------[   Digital Calendar   ]--------|");
        System.out.println("|----------------------------------------|");
        System.out.println("|--------[       1. Login       ]--------|");
        System.out.println("|--------[      2. Sign up      ]--------|");
        System.out.println("|--------[       3. Exit        ]--------|");
        System.out.println("+----------------------------------------+");
    }

    public void MenuPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[    1. View Today    ]--------|");
        System.out.println("|--------[  2. Select Calendar ]--------|");
        System.out.println("|--------[       3. Logout     ]--------|");
        System.out.println("|--------[  4. Delete Account  ]--------|");
        System.out.println("+---------------------------------------+");
    }

    public void CalendarMenuPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[    1. View Entries  ]--------|");
        System.out.println("|--------[    2. Add Entry     ]--------|");
        System.out.println("|--------[    3. Delete Entry  ]--------|");
        System.out.println("|--------[       4. Back       ]--------|");
        System.out.println("+---------------------------------------+");
    }

    public void CalendarListPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[   Select a Calendar ]--------|");
        System.out.println("|---------------------------------------|");

        for (int i = 0; i < this.publicCalendarIDs.size(); i++) {
            System.out.printf("| %2d. Public ID: %s |\n", (i + 1), this.publicCalendarIDs.get(i));
        }
        System.out.println("+---------------------------------------+");
        for (int i = 0; i < this.calendarIDs.size(); i++) {
            System.out.printf("| %2d. Private ID: %s |\n" + (i + 1) + this.calendarIDs.get(i));
        }

        System.out.println("+---------------------------------------+");
    }


    // Getters and Setters.
    public Scanner getScanner() {
        return this.scanner;
    }

    public MonthCalendar getCurrentCalendar() {
        return this.currentCalendar;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }
}
