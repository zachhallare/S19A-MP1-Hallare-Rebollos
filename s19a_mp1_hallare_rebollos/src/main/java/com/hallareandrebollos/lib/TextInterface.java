package com.hallareandrebollos.lib;

import java.util.ArrayList;
import java.util.Scanner;

import com.hallareandrebollos.objects.Account;

public class TextInterface {
    private Scanner scanner; // Scanner for user input.

    private Account loggedInAccount; // The currently logged-in account.
    private int currentCalendarID; // The ID of the currently selected calendar.
    private int pageIdx; // Page index to track the current page in the interface.

    private ArrayList<Integer> calendarIDs; // List of calendar IDs associated with the logged-in account.

    public TextInterface() {
        this.loggedInAccount = null; // Initially, no account is logged in.
        this.currentCalendarID = -1; // No calendar is selected initially.
        this.scanner = new Scanner(System.in); // Initialize the scanner for user input.
        this.pageIdx = 0; // Start at the first page.
    }

    public void LoginPageLogic() {
        int selectedOption = scanner.nextInt(); // Read user input for the selected option.
        switch (selectedOption) {
            case 1 -> {
                System.out.println("+----------------------------------+");
                System.out.println("|--------[   Login Page   ]--------|");
                System.out.println("+----------------------------------+");
                System.out.print("Enter Username: ");
                String username = scanner.next();
                System.out.print("Enter Password: ");
                String password = scanner.next();
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Username and Password cannot be empty. Please try again.");
                    break;
                }

                // Attempt to authenticate the user.
                this.loggedInAccount = new Account(); // This should be replaced with actual authentication logic.
                if (this.loggedInAccount.authenticate(username, password)) {
                    System.out.println("Login successful!");
                    this.pageIdx = 1; // Set the page index to the main menu.
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            }
            case 2 -> {
                System.out.println("+----------------------------------+");
                System.out.println("|--------[   Sign Up Page   ]-------|");
                System.out.println("+----------------------------------+");
                System.out.print("Enter Username: ");
                String username = scanner.next();
                System.out.print("Enter Password: ");
                String password = scanner.next();
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Username and Password cannot be empty. Please try again.");
                    break;
                }

                // Attempt to create a new account.
                this.loggedInAccount = new Account(); // This should be replaced with actual account creation logic.
                if (this.loggedInAccount.createAccount(username, password)) {
                    System.out.println("Account created successfully!");
                    this.pageIdx = 1; // Set the page index to the main menu.
                } else {
                    System.out.println("Failed to create account. Please try again.");
                }
            }
            case 3 -> {
                System.out.println("Exiting the application...");
                System.exit(0);
            }
            default ->
                System.out.println("Invalid option. Please try again.");
        }
    }

    public void LoginPage() {
        System.out.println("+----------------------------------+");
        System.out.println("|--------[Digital Calendar]--------|");
        System.out.println("|----------------------------------|");
        System.out.println("|--------[    1. Login    ]--------|");
        System.out.println("|--------[   2. Sign up   ]--------|");
        System.out.println("|--------[    3. Exit     ]--------|");
        System.out.println("+----------------------------------+");
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
