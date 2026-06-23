package src;

import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private String category;
    private String priority;
    private String dueDate;
    private boolean completed;

    public Task(String title, String description, String category, String priority, String dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.category = category == null || category.trim().isEmpty() ? "General" : category.trim();
        this.priority = priority == null ? "Medium" : priority;
        this.dueDate = dueDate == null || dueDate.trim().isEmpty() ? "No Date" : dueDate.trim();
        this.completed = false;
    }

    public Task(String id, String title, String description, String category, String priority, String dueDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", completed=" + completed +
                '}';
    }
}
