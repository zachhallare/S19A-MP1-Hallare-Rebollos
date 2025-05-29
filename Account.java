// Account class for account info.

public class Account {
    private String username;
    private String password;
    private boolean active;

    // Constructor.
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
    }

    // Getters.
    public String getUsername() {
        return username;
    }

    public String getUsername() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    // Deactivate Account Method.
    public void deactivateAccount() {
        this.active = false;
    }
}
