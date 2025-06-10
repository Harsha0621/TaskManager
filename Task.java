import java.util.Date;

//need to generate a unique ID for each task, using a static counter or similar mechanis

public class Task {
    private int id;
    private String description;
    private TaskStatusLookup status;
    private Date createdDate;
    private Date updatedDate;

    public Task(int id, String description, TaskStatusLookup status, Date createdDate, Date updatedDate) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatusLookup getStatus() {
        return status;
    }

    public String getCreatedDate() {
        return createdDate.toString();
    }

    public String getUpdatedDate() {
        return updatedDate.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatusLookup status) {
        this.status = status;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return String.format("%-5d | %-32s | %-15s | %-28s | %-28s",
                id, description, status, createdDate, updatedDate);
    }
}