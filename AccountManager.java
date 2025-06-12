// AccountManager handles user account creation, login,
// deletion, and tracks all accounts in the system.

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {
    private ArrayList<Account> accounts;        // Arraylist of all accounts loaded from text file.
    private Scanner scanner;                    

    // Constructor: Loads accounts and initializes scanner.
    public AccountManager() {
        accounts = FileManager.loadAccounts();      // Lists the accounts already made.
        scanner = new Scanner(System.in);
    }

    // Logs in an existing account if credentials match and account is currently active.
    public Account login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Account acc : accounts) {
            // Check for matching credentials and ensure the account is active.
            if (acc.isActive() && acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + acc.getUsername() + ".");
                return acc;
            }
        }

        // If no matching account was found.
        System.out.println("Invalid username or password.");
        return null;
    }

    // Create new account with unique username and adds it to the account list.
    public Account createAccount() {
        System.out.println("\n--- Create Account ---");
        String username = "";
        boolean isUnique = false;
        
        // Loops until a valid username is provided.
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

        // P.S. maybe we can do a random password generator as a bonus? wdyt.
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Create the new account.
        Account newAccount = new Account(username, password);
        // Calendar defaultCal = new Calendar(username, false, newAccount);
        // newAccount.addCalendar(defaultCal);

        // Add to list and file.
        accounts.add(newAccount);
        FileManager.addAccount(newAccount);
        System.out.println("Account created sucessfully!");
        
        return newAccount;
    }


    // Checks if username is already used by another account.
    private boolean usernameExists(String username) {
        for (Account acc : accounts) {
            if (acc.getUsername().equals(username) && acc.isActive()) {
                return true;
            }
        }
        return false;
    }


    // Logs out the current account.
    public void logout() {
        System.out.println("Logged out successfully.");
    }


    // Returns the list of all accounts.
    public ArrayList<Account> getAccounts() {
        return accounts;
    }


    // Deactivates/"deletes" an account.
    public void deleteAccount(Account account) {
        account.deactivateAccount();
        System.out.println("Account " + account.getUsername() + " deactivated.");
    }
}
