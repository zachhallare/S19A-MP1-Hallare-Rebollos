package com.hallareandrebollos.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Account {
    private int accountID;          // Unique ID for the account.
    private String username;        // Username of the account.
    private String password;        // Password of the account.
    private boolean isActive;

    private static final String ACCOUNT_FILE_PATH = "data/accounts.txt";

    // Default Constructor: Initializes the account with default values.
    public Account() {
        this.accountID = -1;
        this.username = "";
        this.password = "";
        this.isActive = false; // Account is inactive by default.
    }

    // Full Constructor: Initializes the account with specified values.
    public Account(int accountID, String username, String password, boolean isActive) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public boolean createAccount(String inputUsername, String inputPassword) {
        // Creates a new account with the specified username and password.
        // The account is initially inactive and has no owned calendars.
        // Automatically saves the details to data/accounts.txt.
        boolean isCreated = false;

        boolean validPassword = !inputPassword.contains(" ");       // Is valid as long as it doesn't contain space.
        if (validPassword) {
             // Ensure the data directory exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // Create the file if it doesn't exist
            File file = new File(ACCOUNT_FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error creating accounts file: " + e.getMessage());
                }
            }

            boolean exists = false;
            int maxID = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        if (id > maxID) {
                            maxID = id;
                        }
                        if (parts[2].equalsIgnoreCase(inputUsername)) {
                            exists = true;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading accounts file: " + e.getMessage());
            }
        
            if (!exists) {
                this.accountID = maxID + 1;
                this.username = inputUsername;
                this.password = inputPassword;
                this.isActive = true;

                String newAccountLine = this.accountID + ", " + this.isActive + ", " + this.username + ", " + this.password + "\n";
            
                try (FileWriter writer = new FileWriter(ACCOUNT_FILE_PATH, true)) {
                    writer.write(newAccountLine);
                    isCreated = true;
                } catch (IOException e) {
                    System.out.println("Error in writing new account: " + e.getMessage());
                }
            } else {
                System.out.println("Username already exists.\n");

            }
        } else {
            System.out.println("Password cannot contain spaces.");
        }

        return isCreated;
    }


    public boolean authenticate(String inputUsername, String inputPassword) {
        // Authenticates the account by checking the provided username and password in data/accounts.txt.
        // Per line format is "accountID, isActive, username, password".
        // Every integer after password is a Calendar ID owned by the account.
        // Example: "1, true, user123, pass123".
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null && !found) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) { // Only process lines that have enough parts.
                    int id = Integer.parseInt(parts[0]);
                    boolean active = Boolean.parseBoolean(parts[1]);
                    String fileUsername = parts[2];
                    String filePassword = parts[3];

                    if (fileUsername.equals(inputUsername) && filePassword.equals(inputPassword) && active) {
                        this.accountID = id;
                        this.username = fileUsername;
                        this.password = filePassword;
                        this.isActive = active;
                        found = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in reading accounts file: " + e.getMessage());
        }

        return found; // Returns true if the account was found and authenticated, false otherwise.
    }


    public void deactivateAccount() {
        File file = new File(ACCOUNT_FILE_PATH);
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    if (id == this.accountID) {
                        parts[1] = "false";
                        line = parts[0] + ", false, " + parts[2] + ", " + parts[3];
                    }
                }
                updatedContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating account status: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(updatedContent.toString());
            this.isActive = false;
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }

        // Delete all of the user's private calendars.
        File userCalendarFolder = new File("data/calendars/" + this.username + "/");
        if (userCalendarFolder.exists() && userCalendarFolder.isDirectory()) {
            File[] files = userCalendarFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        boolean deleted = f.delete();
                        if (!deleted) {
                            System.out.println("Failed to delete calendar: " + f.getName());
                        }
                    }
                }
            }

            // Attempt to delete the folder itself.
            if (!userCalendarFolder.delete()) {
                System.out.println("Could not delete user calendar folder." + userCalendarFolder.getName());
            }
        }

    }


    // Getters.
    public int getAccountID() {
        return this.accountID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
