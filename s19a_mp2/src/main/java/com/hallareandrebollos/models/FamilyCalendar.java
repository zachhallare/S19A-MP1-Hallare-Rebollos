package com.hallareandrebollos.models;

/**
 * Represents a calendar that contains multiple entries.
 * The calendar can be either public or private and is associated with a specific year.
 */
public class FamilyCalendar extends CalendarObject {
    private final int passcode;

    public FamilyCalendar(int passcode, String calendarName, boolean isPublic) {
        super(calendarName, isPublic);
        this.passcode = passcode;
    }

    public boolean isPasscodeCorrect(int inputPasscode) {
        return this.passcode == inputPasscode;
    }
}
