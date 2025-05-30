import java.util.ArrayList;

// P.S. This class represents a date in the calendar, containing information about the week, month, day, and entries for that date.
// P.S.S. It includes methods to get and set various properties of the date, such as whether it is today or selected.
// P.S.S.S. It also contains a list of CalendarEntry objects, which represent individual events or entries for that date.
// P.S.S.S.S. The class is designed to be used in a calendar application, where each date can have multiple entries associated with it.
// P.S.S.S.S.S. Incomplete implementation, but it provides a good starting point for managing calendar dates and their entries.
public class CalendarDate {
    private int weekNumber;
    private int monthNumber;
    private int dayNumber;
    private String dayString;
    private ArrayList<CalendarEntry> entries;
    private boolean isToday;
    private boolean isSelected;

    // Default constructor
    public CalendarDate(int weekNumber, int monthNumber, int dayNumber, String dayString, 
                        ArrayList<CalendarEntry> entries, boolean isToday, boolean isSelected) {
        this.weekNumber = weekNumber;
        this.monthNumber = monthNumber;
        this.dayNumber = dayNumber;
        this.dayString = dayString;
        this.isToday = isToday;
        this.isSelected = isSelected;

        if (this.entries == null) {
            this.entries = new ArrayList<>();
        } else {
            this.entries = new ArrayList<>(entries);
        }
    }

    // Getters
    public int getWeekNumber() {
        return weekNumber;
    }
    public int getMonthNumber() {
        return monthNumber;
    }
    public int getDayNumber() {
        return dayNumber;
    }
    public String getDayString() {
        return dayString;
    }
    public ArrayList<CalendarEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    public boolean isToday() {
        return isToday;
    }
    public boolean isSelected() {
        return isSelected;
    }

    // Setters
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
    public void setDayString(String dayString) {
        this.dayString = dayString;
    }
    public void setEntries(ArrayList<CalendarEntry> entries) {
        if (entries == null) {
            this.entries = new ArrayList<>();
        } else {
            this.entries = new ArrayList<>(entries);
        }
    }
    public void setToday(boolean isToday) {
        this.isToday = isToday;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
