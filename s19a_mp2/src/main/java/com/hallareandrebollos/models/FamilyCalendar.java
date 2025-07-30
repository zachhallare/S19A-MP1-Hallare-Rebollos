package com.hallareandrebollos.models;

/**
 * Represents a calendar that contains multiple entries.
 * The calendar can be either public or private and is associated with a specific year.
 */
public class FamilyCalendar extends CalendarObject {

    /** The passcode required to access this family calendar. */
    private final int passcode;


    /**
     * Constructs a new FamilyCalendar with the specified passcode, name, and visibility.
     * @param passcode     The passcode to access the calendar.
     * @param calendarName The name of the calendar.
     * @param isPublic     Whether the calendar is public.
     */
    public FamilyCalendar(int passcode, String calendarName, boolean isPublic) {
        super(calendarName, isPublic);
        this.passcode = passcode;
    }


    /**
     * Checks if the provided passcode matches the calendar's passcode.
     * @param inputPasscode The passcode entered by the user.
     * @return true if the passcode matches, false otherwise.
     */
    public boolean isPasscodeCorrect(int inputPasscode) {
        return this.passcode == inputPasscode;
    }
}
