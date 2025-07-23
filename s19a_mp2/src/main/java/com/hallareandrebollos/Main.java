package com.hallareandrebollos;

import com.hallareandrebollos.services.Router;

/**
 * Entry point of the Digital Calendar application.
 * Initializes controllers and handles the main navigation loop.
 */
public class Main {
    /**
     * Main method that starts the program and manages user interface flow.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Router();
    }
}
