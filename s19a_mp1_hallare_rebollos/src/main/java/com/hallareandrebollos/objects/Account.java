package com.hallareandrebollos.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Account {

    private String username;        // Username of the account.
    private String password;        // Password of the account.
    private int accountID;         // Unique ID of the account.
    private boolean isActive;

    // Default Constructor: Initializes the account with default values.
    public Account() {
        this.username = "";
        this.password = "";
        this.accountID = -1; // Default ID for null safety.
        this.isActive = false; // Account is inactive by default.
    }

    // Full Constructor: Initializes the account with specified values.
    public Account(String username, String password, int accountID, boolean isActive, ArrayList<Integer> ownedCalendars) {
        this.username = username;
        this.password = password;
        this.accountID = accountID;
        this.isActive = isActive;
    }

    public boolean createAccount(String username, String password) {
        // Creates a new account with the specified username and password.
        // The account is initially inactive and has no owned calendars.
        // Automatically saves the details to resource/accounts.txt.
        this.username = username;
        this.password = password;
        this.accountID = (int) (Math.random() * 10000);
        this.isActive = true;
        String filePath = "resource/accounts.txt";

        boolean success = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean exists = false;
            boolean checkFlag = false;
            while (((line = reader.readLine()) != null) && !checkFlag) {
                String[] parts = line.split(", ");
                if (parts.length >= 4 && parts[2].equals(username)) {
                    exists = true; // Username already exists.
                    checkFlag = true; // Stop checking further lines.
                }
            }
            if (!exists) {
                // If the username does not exist, append the new account to the file.
                StringBuilder sb = new StringBuilder();
                sb.append(accountID).append(", ").append(isActive).append(", ").append(username)
                  .append(", ").append(password);
                sb.append("\n");
                java.nio.file.Files.write(java.nio.file.Paths.get(filePath), sb.toString().getBytes(), java.nio.file.StandardOpenOption.APPEND);
                success = true; // Account created successfully.
            }
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
        return success;
    }

    public boolean authenticate(String username, String password) {
        // Authenticates the account by checking the provided username and password in resource/accounts.txt.
        // Per line format is "accountID, isActive, username, password".
        // Every integer after password is a Calendar ID owned by the account.
        // Example: "1, true, user123, pass123".
        String filePath = "resource/accounts.txt";
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
                        this.isActive = active;
                        this.username = fileUsername;
                        this.password = filePassword;
                        found = true; // Account found and authenticated.
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading accounts file: " + e.getMessage());
        }
        return found; // Returns true if the account was found and authenticated, false otherwise.
    }
}
