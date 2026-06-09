package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.TaskDAO;
import model.Task;

public class TaskController {

    private final TaskDAO taskDAO;

    public TaskController() {
        this.taskDAO = new TaskDAO();
    }

    // Constructor allowing injection
    public TaskController(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean addTask(Task task) {
        validateTask(task);
        try {
            taskDAO.insertTask(task);
            return true;
        } catch (SQLException e) {
            System.err.println("[TaskController] Error adding task: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTask(Task task) {
        validateTask(task);
        try {
            taskDAO.updateTask(task);
            return true;
        } catch (SQLException e) {
            System.err.println("[TaskController] Error updating task: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTask(int id) {
        try {
            taskDAO.deleteTask(id);
            return true;
        } catch (SQLException e) {
            System.err.println("[TaskController] Error deleting task: " + e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks() {
        try {
            return taskDAO.getAllTasks();
        } catch (SQLException e) {
            System.err.println("[TaskController] Error fetching all tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Task getTaskById(int id) {
        try {
            return taskDAO.getTaskById(id);
        } catch (SQLException e) {
            System.err.println("[TaskController] Error fetching task by ID: " + e.getMessage());
            return null;
        }
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }
        if (task.getDeadline() == null) {
            throw new IllegalArgumentException("Task deadline cannot be null.");
        }
    }
}
