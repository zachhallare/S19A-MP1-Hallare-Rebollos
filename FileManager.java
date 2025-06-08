import java.io.BufferedReader;
import java.io.FileReader;
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
                String username = userData[0];
                String password = userData[1];
                accounts.add(new Account(username, password));
            }
        }
        catch (IOException error) {
            System.out.println("File reading error: " + error.getMessage());
        }

        return accounts;
    }
}
