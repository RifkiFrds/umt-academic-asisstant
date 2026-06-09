package model;

import java.time.LocalDate;

public class Task {
    private int id;
    private int userId;
    private int courseId;
    private String title;
    private LocalDate deadline;
    private TaskStatus status;

    public Task() {
    }

    public Task(int id, int courseId, String title, LocalDate deadline, TaskStatus status) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.deadline = deadline;
        this.status = status;
    }

    public Task(int id, int userId, int courseId, String title, LocalDate deadline, TaskStatus status) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.title = title;
        this.deadline = deadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                '}';
    }
}
