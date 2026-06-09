package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import database.DBConnection;
import model.Task;
import model.TaskStatus;
import utils.SessionManager;

public class TaskDAO {

    public void insertTask(Task task) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "INSERT INTO tasks (user_id, course_id, title, deadline, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, task.getCourseId());
            stmt.setString(3, task.getTitle());
            stmt.setDate(4, Date.valueOf(task.getDeadline()));
            stmt.setString(5, statusToString(task.getStatus()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                    task.setUserId(userId);
                }
            }
        }
    }

    public void updateTask(Task task) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "UPDATE tasks SET course_id = ?, title = ?, deadline = ?, status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, task.getCourseId());
            stmt.setString(2, task.getTitle());
            stmt.setDate(3, Date.valueOf(task.getDeadline()));
            stmt.setString(4, statusToString(task.getStatus()));
            stmt.setInt(5, task.getId());
            stmt.setInt(6, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_id, title, deadline, status FROM tasks WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }
        }
        return tasks;
    }

    public Task getTaskById(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_id, title, deadline, status FROM tasks WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTask(rs);
                }
            }
        }
        return null;
    }

    // Bidirectional status mappings
    public String statusToString(TaskStatus status) {
        if (status == null) {
            return "Belum Dikerjakan";
        }
        return switch (status) {
            case PENDING     -> "Belum Dikerjakan";
            case IN_PROGRESS -> "Sedang Dikerjakan";
            case COMPLETED   -> "Selesai";
        };
    }

    public TaskStatus stringToStatus(String statusStr) {
        if (statusStr == null) {
            return TaskStatus.PENDING;
        }
        return switch (statusStr) {
            case "Sedang Dikerjakan" -> TaskStatus.IN_PROGRESS;
            case "Selesai"           -> TaskStatus.COMPLETED;
            default                  -> TaskStatus.PENDING;
        };
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("course_id"),
            rs.getString("title"),
            rs.getDate("deadline").toLocalDate(),
            stringToStatus(rs.getString("status"))
        );
    }
}
