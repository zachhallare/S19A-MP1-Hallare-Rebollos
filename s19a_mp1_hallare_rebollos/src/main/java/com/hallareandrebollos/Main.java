package com.hallareandrebollos;

import com.hallareandrebollos.lib.TextInterface;

public class Main {
    public static void main(String[] args) {
        // im just testing it, but we can make this as the main thing too.

        TextInterface ui = new TextInterface();

        while (ui.getPageIndex() != -1) {
            switch (ui.getPageIndex()) {
                case 1 -> {
                    ui.MenuPage();
                    ui.MenuPageLogic();
                }
                case 2 -> ui.CalendarDisplayController();
                default -> {
                    ui.LoginPage();
                    ui.LoginPageLogic();
                }
            }
        }

    }
}