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

    public boolean createAccount(String username, String password) {
        // Creates a new account with the specified username and password.
        // The account is initially inactive and has no owned calendars.
        // Automatically saves the details to data/accounts.txt.
        this.username = username;
        this.password = password;
        this.isActive = true;

        String filePath = "data/accounts.txt";
        File file = new File(filePath);

        // Ensure the data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Create the file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating accounts file: " + e.getMessage());
                return false;
            }
        }

        boolean exists = false;
        int maxID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    if (id > maxID) {
                        maxID = id;
                    }
                    if (parts[2].equals(username)) {
                        exists = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in reading accounts file: " + e.getMessage());
        }

        if (!exists) {
            this.accountID = maxID + 1;
            String newAccountLine = this.accountID + ", " + this.isActive + ", " + this.username + ", " + this.password + "\n";
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(newAccountLine);
                return true;
            } catch (IOException e) {
                System.out.println("Error in writing new account: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean authenticate(String username, String password) {
        // Authenticates the account by checking the provided username and password in data/accounts.txt.
        // Per line format is "accountID, isActive, username, password".
        // Every integer after password is a Calendar ID owned by the account.
        // Example: "1, true, user123, pass123".
        String filePath = "data/accounts.txt";
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while (((line = reader.readLine()) != null) && !found) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) { // Only process lines that have enough parts.
                    int id = Integer.parseInt(parts[0]);
                    boolean active = Boolean.parseBoolean(parts[1]);
                    String fileUsername = parts[2];
                    String filePassword = parts[3];

                    if (fileUsername.equals(username) && filePassword.equals(password)) {
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
}
