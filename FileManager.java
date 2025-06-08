import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
    private static final String FILE_NAME = "UserCredentials.txt";

    // Load accounts from file (in any).
    public static ArrayList<Account> loadAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String lineInFile;
            while ((lineInFile = reader.readLine()) != null) {
                String[] userData = lineInFile.split(",");
                String username = userData[0].trim();
                String password = userData[1].trim();
                accounts.add(new Account(username, password));
            }
        }
        catch (IOException error) {
            System.out.println("File reading error: " + error.getMessage());
        }

        return accounts;
    }

    // Add a new account to the file.
    public static void addAccount(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {     // true = it will appead in the file.
            writer.write(account.getUsername() + ", " + account.getPassword());
            writer.newLine();
        }  
        catch (IOException error) {
            System.out.println("File writing error: " + error.getMessage());
        }     
    }
}
