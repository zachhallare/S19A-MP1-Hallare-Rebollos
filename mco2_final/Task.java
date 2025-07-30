package mco2_final;

import java.time.LocalDate;


/**
 * Represents a Task entry with priority, status, creator, and an optional finisher.
 * A Task does not have a start or end time.
 */
public class Task extends Entry {

    /** The priority level of the task (e.g., High, Medium, Low). */
    final private String priority; 
    
    /** The current status of the task (e.g., Pending, Done). */
    final private String status;    
    
    /** The username of the account that created this task. */
    final private String createdBy;

    /** The username of the account that finished this task (optional). */
    final private String finishedBy;  

    
    /**
     * Constructs a Task with no finishedBy user.
     * @param title       the title of the task
     * @param date        the date of the task
     * @param description the description of the task
     * @param priority    the priority level (High, Medium, Low)
     * @param status      the task status (Pending, Done)
     * @param createdBy   the username of the creator
     */
    public Task(String title, LocalDate date, String description,
                String priority, String status, String createdBy) {
        super(title, date, description);
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.finishedBy = null;
    }

    
    /**
     * Constructs a Task with a specified finishedBy user.
     * @param title       the title of the task
     * @param date        the date of the task
     * @param description the description of the task
     * @param priority    the priority level (High, Medium, Low)
     * @param status      the task status (Pending, Done)
     * @param createdBy   the username of the creator
     * @param finishedBy  the username of the user who finished the task
     */
    public Task(String title, LocalDate date, String description,
                String priority, String status, String createdBy, String finishedBy) {
        super(title, date, description);
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.finishedBy = finishedBy;
    }


    /**
     * Returns the priority level of the task.
     *
     * @return the priority
     */
    public String getPriority() { 
        return this.priority; 
    }
    

    /**
     * Returns the current status of the task.
     *
     * @return the status
     */
    public String getStatus() { 
        return this.status; 
    }


    /**
     * Returns the username of the account that created this task.
     * @return the creator's username
     */
    public String getCreatedBy() { 
        return this.createdBy; 
    }
    

    /**
     * Returns the username of the account that finished this task, if any.
     * @return the finisher's username or null
     */
    public String getFinishedBy() { 
        return this.finishedBy; 
    }


    /**
     * Returns the type of this entry.
     * @return "Task"
     */
    @Override
    public String getType() {
        return "Task";
    }


    /**
     * Returns a short formatted string for display purposes.
     * @return a display string showing type, title, priority, and status
     */
    @Override
    public String toDisplayString() {
        return "[Task] " + title + " (" + priority + ", " + status + ")";
    }


    /**
     * Creates and returns a copy of this Task.
     * @return a new Task object with the same field values
     */
    @Override
    public Entry copy() {
        return new Task(title, date, description, priority, status, createdBy, finishedBy);
    }
}
