package com.hallareandrebollos;

import com.hallareandrebollos.lib.*;

public class Main {
    public static void main(String[] args) {
        // im just testing it, but we can make this as the main thing too.

        TextInterface ui = new TextInterface();
        boolean running = true;

        while (running) {
            if (ui.getPageIndex() == 0) {
                ui.LoginPage();
                ui.LoginPageLogic();
            } else if (ui.getPageIndex() == 1) {
                ui.MenuPage();
                ui.MenuPageLogic();
            } else if (ui.getPageIndex() == 2) {
                ui.CalendarMenuPage();
                System.out.print("Select an option: ");
                int option = ui.getScanner().nextInt();
                ui.getScanner().nextLine(); // Consume newline

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter day number to view entries: ");
                        
                        int day = ui.getScanner().nextInt();
                        ui.getScanner().nextLine();

                        if (ui.getCurrentCalendar() != null) {
                            ui.getCurrentCalendar().displayEntries(day);
                        } else {
                            System.out.println("No calendar selected.");
                        }
                    }
                    case 2 -> {
                        ui.setPageIndex(1); // Back to Menu Page
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } else if (ui.getPageIndex() == -1) {
                running = false;
                System.out.println("Exiting... Thank you for using the calendar.");
            } else {
                System.out.println("Invalid state. Resetting to login.");
                ui.setPageIndex(0);
        }
    }

    }
}