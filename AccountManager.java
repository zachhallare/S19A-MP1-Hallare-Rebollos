import java.util.*;

public class AccountManager {
    private ArrayList<Account> accounts;
    private Scanner scanner;

    // Constructor.
    public AccountManager() {
        accounts = new ArrayList<>();
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
    public boolean createAccount {

    }
}
