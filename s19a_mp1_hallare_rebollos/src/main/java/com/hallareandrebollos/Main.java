package com.hallareandrebollos;

import com.hallareandrebollos.lib.TextInterface;

public class Main {
    public static void main(String[] args) {
        TextInterface ui = new TextInterface();

        while (ui.getPageIndex() != -1) {
            switch (ui.getPageIndex()) {
                // Main Page.
                case 1 -> {
                    ui.MenuPage();
                    ui.MenuPageLogic();
                }
                // Display Calendar.
                case 2 -> ui.CalendarDisplayController();
                // Login Page.
                default -> {
                    ui.LoginPage();
                    ui.LoginPageLogic();
                }
            }
        }

    }
}