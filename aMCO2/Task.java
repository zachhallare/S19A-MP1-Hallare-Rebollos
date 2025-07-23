package aMCO2;

import java.time.LocalDate;

// Task has an additional priority (required) attribute which allows the task to be sorted
// from high then medium and low in the displays; status (required) that states if a Task
// is done or pending; created by (required); and finished by (optional). It doesn’t have
// a start and end time.

public class Task extends Entry {
    private String priority;    // High, Medium, Low.
    private String status;      // Pending, Done.
    private String createdBy;
    private String finishedBy;  // optional.

    // with no finishedby.
    public Task(String title, LocalDate date, String description,
                String priority, String status, String createdBy) {
        super(title, date, description);
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.finishedBy = null;
    }

    // full constructor.
    public Task(String title, LocalDate date, String description,
                String priority, String status, String createdBy, String finishedBy) {
        super(title, date, description);
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.finishedBy = finishedBy;
    }

    public String getPriority() { 
        return this.priority; 
    }
    
    public String getStatus() { 
        return this.status; 
    }

    public String getCreatedBy() { 
        return this.createdBy; 
    }
    
    public String getFinishedBy() { 
        return this.finishedBy; 
    }

    @Override
    public String getType() {
        return "Task";
    }

    @Override
    public String toDisplayString() {
        return "[Task] " + title + " (" + priority + ", " + status + ")";
    }

    @Override
    public Entry copy() {
        return new Task(title, date, description, priority, status, createdBy, finishedBy);
    }
}
