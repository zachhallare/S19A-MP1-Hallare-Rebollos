package com.hallareandrebollos;

import com.hallareandrebollos.lib.TextInterface;

public class Main {
    public static void main(String[] args) {
        TextInterface ui = new TextInterface();

        while (ui.getPageIndex() != -1) {
            switch (ui.getPageIndex()) {
                case 0 -> {
                    // Login Page.
                    ui.loginPage();
                    ui.loginPageLogic();
                }
                case 1 -> {
                    // Main Page.
                    ui.menuPage();
                    ui.menuPageLogic();
                }
                case 2 -> {
                    // Display Calendar.
                    ui.calendarMenuPage();
                    ui.calendarPageLogic(0);
                }
                default -> {
                    System.out.println("Unknown page index. Returning to login page...\n\n");
                    ui.setPageIndex(0);
                }
            }
        }

        System.out.println("Program exited. Goodbye!");
    }
}