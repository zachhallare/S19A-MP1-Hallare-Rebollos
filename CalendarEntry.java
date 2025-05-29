import java.time.LocalDate;
import java.time.LocalTime;

// P.S. Change anything i dont know what to do with this code
// P.S.S. I know this is a simple class, but it is used in the Calendar class, so it is important to have it here.
// P.S.S.S. Basically we treat each calendar as an object containing multiple CalendarEntry objects.
// P.S.S.S.S. Where each account has multiple calendars, and each calendar has multiple entries.
public class CalendarEntry {
    private String title;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String description;

    // Default constructor
    public CalendarEntry() {
        this.title = "";
        this.date = LocalDate.now();
        this.timeStart = LocalTime.of(0, 0);
        this.timeEnd = LocalTime.of(0, 0);
        this.description = "";
    }

    // Actual constructor
    public CalendarEntry(String title, LocalDate date, LocalTime timeStart, LocalTime timeEnd, String description) {
        this.title = title;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.description = description;
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTimeStart() {
        return timeStart;
    }
    public LocalTime getTimeEnd() {
        return timeEnd;
    }
    public String getDescription() {
        return description;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }
    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
