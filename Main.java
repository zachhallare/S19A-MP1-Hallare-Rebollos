import java.util.*;

// i'm treating this like the main file.
// i'm basing this off my old java projects.
// has the startup menu, main menu, and login/logout flow

public class Main {
    private AccountManager accountManager;
    private Scanner scanner;

    // Constructor
    public Main() {
        accountManager = new AccountManager();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        Account loggedInAccount = null;

        while (running) {
            if (loggedInAccount == null) {
                displayStartupMenu();
                int option = getUserOption(1, 3);
                switch (option) {
                    case 1 -> loggedInAccount = accountManager.login();
                    case 2 -> loggedInAccount = accountManager.createAccount();
                    case 3 -> {
                        System.out.println("Exiting application. Thank you for using!");
                        running = false;
                    }
                }
            }
            else {
                displayMainMenu(loggedInAccount);
                int option = getUserOption(1, 6);
                switch (option) {
                    // Temporary placeholders muna.
                    case 1 -> {
                        // views monthly calendar.
                        CalendarViewer viewer = new CalendarViewer(scanner); 
                        viewer.viewCalendar(loggedInAccount);
                    }
                    case 2 -> System.out.println("Add Calendar");
                    case 3 -> System.out.println("Delete Calender");
                    case 4 -> System.out.println("Manage Calender Entries");
                    case 5 -> {
                        accountManager.logout();
                        loggedInAccount = null;
                    }
                    case 6 -> {
                        accountManager.deleteAccount(loggedInAccount);
                        loggedInAccount = null;
                    }
                }
            }
        }
    }


    // Displays starting menu.
    private void displayStartupMenu() {
        System.out.println("\n=== DIGITAL CALENDAR APPLICATION ===");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");
    }

    // Displays main menu.
    private void displayMainMenu(Account account) {
        System.out.println("\n=== Welcome, " + account.getUsername() + " ===");
        
        System.out.println("Made Calendars:");
        for (Calendar cal : account.getCalendars()) {
            System.out.println("- " + cal.getName() + (cal.isPublic() ? " (Public)" : " (Private)"));
        }
        
        System.out.println("\n1. View Monthly Calendar");
        System.out.println("2. Add Calendar");
        System.out.println("3. Delete Calendar");
        System.out.println("4. Manage Calendar Entries");
        System.out.println("5. Logout");
        System.out.println("6. Delete Account");
        System.out.print("Choose option: ");
    }


    // So the start method looks cleaner while functional.
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

    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
}
