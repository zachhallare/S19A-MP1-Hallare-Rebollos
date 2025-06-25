public class Entry {
    private long timeStart;              // Start time of the entry in milliseconds since epoch.
    private long timeEnd;                // End time of the entry in milliseconds since epoch.
    private String title;
    private String description;

    public Entry(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public long getDuration() {
        return this.timeEnd - this.timeStart; // Returns duration in milliseconds
    }

    public void setStartTime(long startTime) {
        this.timeStart = startTime;
    }

    public void setEndTime(long endTime) {
        this.timeEnd = endTime;
    }

    public long getStartTime() {
        return this.timeStart;
    }

    public long getEndTime() {
        return this.timeEnd;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}
