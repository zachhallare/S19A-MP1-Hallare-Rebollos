package com.hallareandrebollos;

import com.hallareandrebollos.lib.TextInterface;

public class Main {
    public static void main(String[] args) {
        TextInterface ui = new TextInterface();

        while (ui.getPageIndex() != -1) {
            switch (ui.getPageIndex()) {
                case 1 -> {
                    // Main Page.
                    ui.menuPage();
                    ui.loadCalendarList();
                    ui.menuPageLogic();
                }
                case 2 -> {
                    // Display Calendar.
                    ui.loadCalendarList();
                    ui.calendarDisplayController();
                }
                default -> {
                    // Login Page.
                    ui.loginPage();
                    ui.loginPageLogic();
                }
            }
        }

    }
}