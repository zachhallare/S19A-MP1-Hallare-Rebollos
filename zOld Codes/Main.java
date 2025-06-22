import java.util.*;

// Serves as the main file containing the startup 
// menu, main menu, and login/logout flow.

public class Main {
    private AccountManager accountManager;
    private Scanner scanner;

    // Constructor: initializes the AccountManager and Scanner for user input.
    public Main() {
        accountManager = new AccountManager();
        scanner = new Scanner(System.in);
    }

    // Starts the main loop for the calendar app.
    public void start() {
        boolean running = true;
        Account loggedInAccount = null;     // Keeps track of the current logged-in user.

        while (running) {
            if (loggedInAccount == null) {
                // Will execute while the user is not yet logged in.
                displayStartupMenu();
                int option = getUserOption(1, 3);
                switch (option) {
                    case 1 -> loggedInAccount = accountManager.login();     // Sends user to the login menu.
                    case 2 -> loggedInAccount = accountManager.createAccount();     // Asks the user to create new account.
                    case 3 -> {
                        System.out.println("Exiting application. Thank you for using!");
                        running = false;
                    }
                }
            }
            else {
                // Will execute when the user has logged in.
                displayMainMenu(loggedInAccount);
                int option = getUserOption(1, 6);
                switch (option) {
                    case 1 -> {
                        // Views monthly calendar.
                        CalendarViewer viewer = new CalendarViewer(scanner); 
                        viewer.viewCalendar(loggedInAccount);
                    }
                    case 2 -> {
                        // Adds a calendar to the list.
                        System.out.println("Add Calendar");       // Placeholder for adding calendar 
                    }
                    case 3 -> {
                        // Remove a calendar made.
                        System.out.println("Remove Calendar");        // Placeholder for deleting calendar 
                    }
                    case 4 -> {
                        // Can add entries per calender.
                        // P.S. not sure what to do here yet, feel free to experiment with it.
                        System.out.println("Manage Calender Entries");        // Placeholder for entry management.
                    }
                    case 5 -> {
                        // Logout (takes user back to starting menu).
                        accountManager.logout();
                        loggedInAccount = null;
                    }
                    case 6 -> {
                        // Account "deletion" (deletes only private calendars).
                        accountManager.deleteAccount(loggedInAccount);
                        loggedInAccount = null;
                    }
                }
            }
        }
    }


    // Displays the starting menu (before login).
    private void displayStartupMenu() {
        System.out.println("\n=== DIGITAL CALENDAR APPLICATION ===");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");
    }

    // Displays the main menu (after login).
    private void displayMainMenu(Account account) {
        System.out.println("\n=== Welcome, " + account.getUsername() + " ===");
        
        // Shows all the calendars made by the user.
        System.out.println("Made Calendars:");
        for (Calendar cal : account.getCalendars()) {
            System.out.println(" - " + cal.getName() + (cal.isPublic() ? " (Public)" : " (Private)"));
        }
        
        // Options for the user to choose from.
        System.out.println("\n1. View Monthly Calendar");
        System.out.println("2. Add Calendar");
        System.out.println("3. Delete Calendar");
        System.out.println("4. Manage Calendar Entries");
        System.out.println("5. Logout");
        System.out.println("6. Delete Account ");
        System.out.print("Choose option: ");
    }


    // A helper method for safely getting user inputs.
    private int getUserOption(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            else {
                scanner.next();
            }

            if (choice < min || choice > max) {
                System.out.print("Invalid option. Choose again: ");
            }
        }
        scanner.nextLine();
        return choice;
    }

    
    // Launches the program.
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
}
