
import java.util.ArrayList;

public class Account {
    private String username;        // Username of the account.
    private String password;        // Password of the account.
    private boolean isActive;       // Indicates if the account is active.
    private ArrayList<String> ownedCalendars; // List of calendars owned by the account.

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.isActive = true;
        this.ownedCalendars = new ArrayList<>();
    }

    public ArrayList<String> getOwnedCalendars() {
        return this.ownedCalendars;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean authenticate(String username, String password) {
        return this.password.equals(password) && this.username.equals(username);
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
