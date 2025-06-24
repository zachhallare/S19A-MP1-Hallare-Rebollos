package com.hallareandrebollos.lib;

import java.io.File;
import java.io.IOException;
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

        // Ensure the needed folders exist.
        createFolder("data");
        createFolder("data/calendars");
        createFolder("data/calendars/public");
    }


    // Creates the folders if they don't exist yet.
    private void createFolder(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Call this after login/signup to ensure the folder of each user exists.
    private void ensureUserFolderExist() {
        if (this.loggedInAccount != null) {
            createFolder("data/calendars/" + this.loggedInAccount.getUsername());
        }
    }


    // LOGICS
    // Main logic for the user login page.
    public void LoginPageLogic() {
        System.out.print("Enter number: ");
        int selectedOption = scanner.nextInt();     // Read user input for the selected option.
        scanner.nextLine();     // For safety measures (if may extra newline).
        System.out.println("\n");

        switch (selectedOption) {
            case 1 -> {
                // 1. Login.
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
            case 2 -> {
                // 2. Sign Up.
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
                            this.loggedInAccount = tempAccount;
                            ensureUserFolderExist();
                            validSignup = true;
                        } else {
                            System.out.println("Failed to create account. Please try again.\n");
                        }
                    }
                }

                System.out.println("Account created successfully!");
                System.out.println("\n");
                this.pageIndex = 1;         // Set the page index to the main menu.
            }
            case 3 -> {
                // 3. Exit.
                System.out.println("Exiting the application...");
                this.pageIndex = -1;
            }
            default ->
                // If invalid option.
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
                // 1. View Today.
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[ Loading Today's Date ]-------|");
                System.out.println("+---------------------------------------+");
                loadTodayCalendar();
            }
            case 2 -> {
                // 2. Select Calendar.
                loadCalendarList();     // Load the calendar list for the logged-in user.
                CalendarListPage();     // Display the calendar list page.
                calendarSelector();     // Allow the user to select a calendar.
                CalendarDisplayController();
            }
            case 3 -> {
                // 3. Add Calendar.
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[     Add Calendar     ]-------|");
                System.out.println("+---------------------------------------+");
                System.out.println("1. Add from Public Calendars");
                System.out.println("2. Create a New Calendar");
                int calendarOption = scanner.nextInt();
                scanner.nextLine();     // in case may new line.

                // User can choose from the list of publicly available Calendars.
                if (calendarOption == 1) {
                    loadCalendarList();
                    for (int i = 0; i < publicCalendarIDs.size(); i++) {
                        System.out.println((i + 1) + ". " + publicCalendarIDs.get(i));
                    }

                    System.out.print("Enter the name of public calendar to add: ");
                    String selectedName = scanner.nextLine();

                    if (publicCalendarIDs.contains(selectedName) && !calendarIDs.contains(selectedName)) {
                        // Copy public calendar file into user's folder.
                        File source = new File("data/calendars/public/" + selectedName + ".txt");
                        File dest = new File("data/calendars/" + loggedInAccount.getUsername() + "/" + selectedName + ".txt");

                        try {
                            java.nio.file.Files.copy(source.toPath(), dest.toPath());
                            System.out.println("Calendar added successfully!");
                        } catch (IOException e) {
                            System.out.println("Failed to add calendar: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid selection or calendar already added.");
                    }
                }

                // User can create an new calendar and decide if it's private or public.
                else if (calendarOption == 2) {
                    System.out.print("Enter new calendar name: ");
                    String newCalendarName = scanner.nextLine();
                    System.out.print("Make it public? (yes/no): ");
                    String visibility = scanner.nextLine().trim().toLowerCase();

                    MonthCalendar newCalendar = new MonthCalendar(new ArrayList<>());
                    newCalendar.setSelectedDay(LocalDate.now().getDayOfMonth());

                    // Set calendar name and owner before saving.
                    newCalendar.setCalendarName(newCalendarName);
                    newCalendar.setOwnerUsername(loggedInAccount.getUsername());

                    boolean saved = newCalendar.saveCalendar(visibility.equals("yes") ? "" : loggedInAccount.getUsername());
                    if (saved) {
                        System.out.println("Calendar created and saved as " + newCalendarName + ".txt");
                    } else {
                        System.out.println("Failed to save calendar.");
                    }
                }
            }
            case 4 -> {
                // 4. Remove Calendar.
                System.out.print("Enter calendar name to delete: ");
                String toDelete = scanner.nextLine();

                if (toDelete.equalsIgnoreCase(loggedInAccount.getUsername())) {
                    System.out.println("Cannot delete your calendar.");
                } else {
                    MonthCalendar temp = new MonthCalendar(new ArrayList<>());
                    if (temp.deleteCalendar(loggedInAccount.getUsername(), toDelete)) {
                        System.out.println("Calendar deleted.");
                        calendarIDs.remove(toDelete);
                    } else {
                        System.out.println("Calendar could not be deleted.");
                    }
                }
            }
            case 5 -> {
                // 5. Delete Account.
                // Confirmation first, in case na misclick ni user.
                System.out.print("Are you sure you want to deactivate your account? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.equals("yes")) {
                    this.loggedInAccount.deactivateAccount();
                    System.out.println("Account deactivated. Logging out...\n\n");
                    this.loggedInAccount = null;
                    this.pageIndex = 0;     // Sends the user back to the login page.
                } else {
                    System.out.println("Deactivation cancelled.\n\n");
                }
            }
            case 6 -> {
                // 6. Logout.
                System.out.println("Logging out...\n\n");
                this.loggedInAccount = null;        // Clear the logged-in account.
                this.pageIndex = 0;                 // Reset to the login page.
            }
            default -> {
                System.out.println("Invalid option. Please try again.\n\n");
            }
        }
    }


    public void CalendarPageLogic(int selectedOption) {
        System.out.print("Enter number: ");
        selectedOption = scanner.nextInt();     // Read user input for the selected option.
        scanner.nextLine();         // For safety measures (if may extra newline).
        
        switch (selectedOption) {
            case 1 -> {
                // 1. View Entries.
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
                // 2. Add Entry.
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

                    Entry entry = new Entry(title, description);
                    if (entry.setStartTime(startTime) && entry.setEndTime(endTime) && entry.setDate(date)) {
                        currentCalendar.addEntry(entry);
                        System.out.println("Entry added successfully!");
                    } else {
                        System.out.println("Invalid time or date format. Please try again.");
                    }
                } else {
                    System.out.println("No calendar is currently selected.");
                }
            }
            case 3 -> {
                // 3. Edit Entry.
                System.out.print("Enter title of entry to edit: ");
                String oldTitle = scanner.nextLine();
                Entry oldEntry = null;
                int index = 0;

                // Find the index of the entry.
                while (index < currentCalendar.getEntries().size()) {
                    Entry entry = currentCalendar.getEntries().get(index);
                    if (entry.getTitle().equalsIgnoreCase(oldTitle)) {
                        oldEntry = entry;
                        index = currentCalendar.getEntries().size();    // Ends the loop.
                    } else {
                        index++;
                    }
                }

                if (oldEntry == null) {
                        System.out.println("Entry not found.");
                } else {
                    System.out.print("New Title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("New Description: ");
                    String newDesc = scanner.nextLine();
                    System.out.print("New Start Time (HH:mm): ");
                    String newStart = scanner.nextLine();
                    System.out.print("New End Time (HH:mm): ");
                    String newEnd = scanner.nextLine();

                    Entry newEntry = new Entry(newTitle, newDesc);
                    newEntry.setDate(oldEntry.getDate().toString());
                    newEntry.setStartTime(newStart);
                    newEntry.setEndTime(newEnd);

                    currentCalendar.editEntry(oldEntry, newEntry);
                    System.out.println("Entry updated.");
                }
            }
            case 4 -> {
                // 4. Delete Entry.
                System.out.println("+---------------------------------------+");
                System.out.println("|---------[   Deleting Entry   ]--------|");
                System.out.println("+---------------------------------------+");
                System.out.println("Enter title of entry to delete:");
                String title = scanner.nextLine();

                if (title.isEmpty()) {
                    System.out.println("Title cannot be empty. Please try again.");
                } else if (!this.currentCalendar.entryExists(title)) {
                    System.out.println("No entry found with the title: " + title);
                } else {
                    this.currentCalendar.deleteEntry(title);
                    System.out.println("Entry"+ title + "deleted successfully!");
                }
            }
            case 5 -> {
                // 5. Back to Menu.
                System.out.println("Returning to the current calendar menu...");
                this.pageIndex = 2;
            }
            default -> System.out.println("Invalid option. Please try again.");
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
        LocalDate today = LocalDate.now();
        boolean found = false;

        for (String calendarID : calendarIDs) {
            MonthCalendar temp = new MonthCalendar(new ArrayList<>());

            if (temp.loadCalendar(loggedInAccount.getUsername(), calendarID)) {
                if (temp.getMonthNumber() == today.getMonthValue() &&
                    temp.getYearNumber() == today.getYear()) {

                    if (!found) {  // Only assign the first valid one
                        this.currentCalendar = temp;
                        this.currentCalendar.setSelectedDay(today.getDayOfMonth());
                        this.currentCalendar.displayCalendar();
                        this.currentCalendar.displayEntries();
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No calendar matches today's date.\n\n");
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


    public void DaySelector() {
        boolean loopLock = false;       // Loop lock to ensure valid input.
        while (!loopLock) {
            System.out.println("Select a Date to view (Enter 0 to return):");
            int input = scanner.nextInt();
            scanner.nextLine();

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
        System.out.println("|--------[      2. Sign Up      ]--------|");
        System.out.println("|--------[       3. Exit        ]--------|");
        System.out.println("+----------------------------------------+");
    }

    public void MenuPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[  1. View Today      ]--------|");
        System.out.println("|--------[  2. Select Calendar ]--------|");
        System.out.println("|--------[  3. Add Calendar    ]--------|");
        System.out.println("|--------[  4. Remove Calendar ]--------|");
        System.out.println("|--------[  5. Delete Account  ]--------|");
        System.out.println("|--------[  6. Logout          ]--------|");
        System.out.println("+---------------------------------------+");
    }

    public void CalendarMenuPage() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[   1. View Entries   ]--------|");
        System.out.println("|--------[   2. Add Entry      ]--------|");
        System.out.println("|--------[   3. Edit Entry     ]--------|");
        System.out.println("|--------[   4. Delete Entry   ]--------|");
        System.out.println("|--------[   5. Back to Menu   ]--------|");
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
