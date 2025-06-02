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
    private int yearNumber;
    private String dayString;
    private ArrayList<CalendarEntry> entries;
    private boolean isToday;
    private boolean isSelected;

    // Default constructor
    public CalendarDate(int weekNumber, int monthNumber, int dayNumber, int yearNumber, String dayString, 
                        ArrayList<CalendarEntry> entries, boolean isToday, boolean isSelected) {
        this.weekNumber = weekNumber;
        this.monthNumber = monthNumber;
        this.dayNumber = dayNumber;
        this.yearNumber = yearNumber;
        this.dayString = dayString;
        this.isToday = isToday;
        this.isSelected = isSelected;

        if (this.entries == null) {
            this.entries = new ArrayList<>();
        } else {
            this.entries = new ArrayList<>(entries);
        }
    }
    // displays the top portion of the date, such as the week, month, and day.
    // expected output to be as follows:
    /*
     *##########################
     *#\t September 22, 2025\t #
     *#\t Week 38\t\t\t        #
     *##########################
     */
    public void DisplayDayDetailsTop() {
        System.out.println("##########################");
        System.out.printf("#\t %s %d, %d\t #\n", this.getMonthName(this.monthNumber), this.dayNumber, this.yearNumber);
        System.out.printf("#\t Week %d\t\t\t #\n", this.weekNumber);
        System.out.println("##########################");
    }

    // to get monthName from monthNumber
    private String getMonthName(int monthNumber) {
        return switch (monthNumber) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "Invalid Month";
        };
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
    public void setYear(int yearNumber) {
        this.yearNumber = yearNumber;
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
