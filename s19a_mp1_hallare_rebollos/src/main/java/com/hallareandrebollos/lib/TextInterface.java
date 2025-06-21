package com.hallareandrebollos.lib;
import java.io.*;
import java.util.*;
import com.hallareandrebollos.objects.Account;
import com.hallareandrebollos.objects.MonthCalendar;

public class TextInterface {
    private Scanner scanner;                    // Scanner for user input.
    private Account loggedInAccount;            // The currently logged-in account.
    private int currentCalendarID;              // The ID of the currently selected calendar.
    private int pageIndex;                      // Page index to track the current page in the interface.
    private ArrayList<Integer> calendarIDs;     // List of calendar IDs associated with the logged-in account.
    private MonthCalendar currentCalendar;      // The currently loaded calendar object.

    public TextInterface() {
        this.loggedInAccount = null;                // Initially, no account is logged in.
        this.currentCalendarID = -1;                // No calendar is selected initially.
        this.scanner = new Scanner(System.in);      // Initialize the scanner for user input.
        this.pageIndex = 0;                         // Start at the first page.
    }

    public void MenuPageLogic() {
        int selectedOption = scanner.nextInt();     // Read user input for the selected option.
        switch (selectedOption) {
            case 1 -> {
                // Display today's date and any entries for today.
                System.out.println("+---------------------------------------+");
                System.out.println("|--------[ Loading Today's Date ]-------|");
                System.out.println("+---------------------------------------+");
                LoadTodayCalendar();

            }
            case 2 -> {
                LoadCalendarList(this.loggedInAccount.getUsername()); // Load the calendar list for the logged-in user.
                CalendarListPage(this.calendarIDs); // Display the list of calendars.
            }
            case 3 -> {
                System.out.println("Logging out...");
                this.loggedInAccount = null; // Clear the logged-in account.
                this.pageIndex = 0; // Reset to the login page.
            }
            default ->
                System.out.println("Invalid option. Please try again.");
        }
    }

    public void LoginPageLogic() {
        int selectedOption = scanner.nextInt(); // Read user input for the selected option.
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
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Username and Password cannot be empty. Please try again.");
                } else {
                    Account tempAccount = new Account(); 
                    if (tempAccount.authenticate(username, password)) {
                        this.loggedInAccount = tempAccount;
                        System.out.println("Login successful!");
                        this.pageIndex = 1;         // Set the page index to the main menu.
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                }
            }
            // Sign Up Page.
            case 2 -> {
                System.out.println("+----------------------------------+");
                System.out.println("|-------[   Sign Up Page   ]-------|");
                System.out.println("+----------------------------------+");
                
                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                
                // Attempt to create a new account.
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Username and Password cannot be empty. Please try again.");
                } else {
                    Account tempAccount = new Account();        // This should be replaced with actual account creation logic.
                    if (tempAccount.createAccount(username, password)) {
                        this.loggedInAccount = tempAccount;
                        System.out.println("Account created successfully!");
                        this.pageIndex = 1;         // Set the page index to the main menu.
                    } else {
                        System.out.println("Failed to create account. Please try again.");
                    }
                }
            }
            // Exit the program.
            case 3 -> {
                System.out.println("Exiting the application...");
                System.exit(0);     // I don't think this is allowed :((
            }
            // If invalid option.
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    public void LoadCalendarList(String username) {
        // This method should load the calendar IDs associated with the given username.
        // looks into the directory resources/calendars/username/ and retrieves each file name as a calendar ID.
        this.calendarIDs = new ArrayList<>();
        File dir = new File("resources/calendars/" + username + "/");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        String idStr = fileName.substring(0, fileName.length() - 4);
                        try {
                            int id = Integer.parseInt(idStr);
                            this.calendarIDs.add(id);
                        } 
                        catch (NumberFormatException e) {
                            System.err.println("Invalid calendar ID format in file: " + fileName);
                        }
                    }
                }
            }
        }
    }

    public void LoadTodayCalendar() {
        // This method should load the calendar for today.
        // checks each calendar ID in the calendarIDs list and loads the one that matches today's date.
        if (calendarIDs == null || loggedInAccount == null) return;  
    }

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

    public void CalendarListPage(ArrayList<Integer> calendarIDs) {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[   Select a Calendar ]--------|");
        System.out.println("|---------------------------------------|");

        for (int i = 0; i < calendarIDs.size(); i++) {
            System.out.println("| " + (i + 1) + calendarIDs.get(i) + "|");
        }

        System.out.println("+---------------------------------------+");
    }

    // checks the public calendar list
    public void LoadCalendarList() {

    }
}
