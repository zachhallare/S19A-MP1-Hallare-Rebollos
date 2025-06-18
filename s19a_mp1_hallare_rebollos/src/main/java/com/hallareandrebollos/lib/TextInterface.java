package com.hallareandrebollos.lib;

import java.util.ArrayList;
import java.util.Scanner;

import com.hallareandrebollos.objects.Account;

public class TextInterface {
    private Account loggedInAccount; // The currently logged-in account.
    private int currentCalendarID; // The ID of the currently selected calendar.
    private Scanner scanner; // Scanner for user input.

    public TextInterface() {
        this.loggedInAccount = null; // Initially, no account is logged in.
        this.currentCalendarID = -1; // No calendar is selected initially.
        this.scanner = new Scanner(System.in); // Initialize the scanner for user input.
    }

    public void SignIn() {
        System.out.println("+----------------------------------+");
        System.out.println("|--------[Digital Calendar]--------|");
        System.out.println("|----------------------------------|");
        System.out.println("|--------[    1. Login    ]--------|");
        System.out.println("|--------[   2. Sign up   ]--------|");
        System.out.println("|--------[    3. Exit     ]--------|");
        System.out.println("+----------------------------------+");
    }

    public void Menu() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[    1. View Today    ]--------|");
        System.out.println("|--------[  2. Select Calendar ]--------|");
        System.out.println("|--------[       3. Logout     ]--------|");
        System.out.println("+---------------------------------------+");
    }

    public void CalendarMenu() {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[    1. View Entries  ]--------|");
        System.out.println("|--------[    2. Add Entry     ]--------|");
        System.out.println("|--------[    3. Delete Entry  ]--------|");
        System.out.println("|--------[       4. Back       ]--------|");
        System.out.println("+---------------------------------------+");
    }

    public void CalendarList(ArrayList<Integer> calendarIDs) {
        System.out.println("+---------------------------------------+");
        System.out.println("|--------[   Digital Calendar  ]--------|");
        System.out.println("|---------------------------------------|");
        System.out.println("|--------[   Select a Calendar ]--------|");
        System.out.println("|---------------------------------------|");
        
        for (int i = 0; i < calendarIDs.size(); i++) {
            System.out.println("| " + (i + 1) + calendarIDs.get(i) + "|");
        }
        
        System.out.println("+---------------------------------------+");
    }
}
