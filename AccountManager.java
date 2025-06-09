import java.util.ArrayList;
import java.util.Scanner;

// ts handles user account creation, login, deletion, and tracks all accounts.

public class AccountManager {
    private ArrayList<Account> accounts;
    private Scanner scanner;

    // Constructor.
    public AccountManager() {
        accounts = FileManager.loadAccounts();
        scanner = new Scanner(System.in);
    }

    // Login
    public Account login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Account acc : accounts) {
            if (acc.isActive() && acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + acc.getUsername() + ".");
                return acc;
            }
        }

        System.out.println("Invalid username or password.");
        return null;
    }

    // Create new account.
    public Account createAccount() {
        System.out.println("\n--- Create Account ---");
        String username = "";
        boolean isUnique = false;
        
        while (!isUnique) {
            System.out.print("Enter username: ");
            username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
            }
            else if (usernameExists(username)) {
                System.out.println("Username already exists. Try a different one.");
            }
            else {
                isUnique = true;
            }
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account newAccount = new Account(username, password);

        // Add default private calendar with username as calendar name.
        Calendar defaultCal = new Calendar(username, false, newAccount);
        newAccount.addCalendar(defaultCal);

        accounts.add(newAccount);
        FileManager.addAccount(newAccount);
        System.out.println("Account created sucessfully!");
        
        return newAccount;
    }

    // Checks if username exists.
    private boolean usernameExists(String username) {
        for (Account acc : accounts) {
            if (acc.getUsername().equals(username) && acc.isActive()) {
                return true;
            }
        }
        return false;
    }


    public void logout() {
        System.out.println("Logged out successfully.");
    }


    public ArrayList<Account> getAccounts() {
        return accounts;
    }


    // Delete account by deactivating and removing private calenders.
    public void deleteAccount(Account account) {
        account.deactivateAccount();
        FileManager.deleteAccount(account.getUsername());
        System.out.println("Account " + account.getUsername() + " deleted successfully.");
    }
}
