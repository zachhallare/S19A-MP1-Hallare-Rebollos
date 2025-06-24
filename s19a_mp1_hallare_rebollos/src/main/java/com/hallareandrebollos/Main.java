package com.hallareandrebollos;

import com.hallareandrebollos.lib.TextInterface;

public class Main {
    public static void main(String[] args) {
        TextInterface ui = new TextInterface();

        while (ui.getPageIndex() != -1) {
            switch (ui.getPageIndex()) {
                case 1 -> {
                    // Main Page.
                    ui.MenuPage();
                    ui.loadCalendarList();
                    ui.MenuPageLogic();
                }
                case 2 -> {
                    // Display Calendar.
                    ui.loadCalendarList();
                    ui.CalendarDisplayController();
                }
                default -> {
                    // Login Page.
                    ui.LoginPage();
                    ui.LoginPageLogic();
                }
            }
        }

    }
}