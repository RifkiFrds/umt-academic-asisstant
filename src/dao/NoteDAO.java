package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import database.DBConnection;
import model.Note;
import utils.SessionManager;

public class NoteDAO {

    public void insertNote(Note note) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "INSERT INTO notes (user_id, course_id, title, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, note.getCourseId());
            stmt.setString(3, note.getTitle());
            stmt.setString(4, note.getContent());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    note.setId(generatedKeys.getInt(1));
                    note.setUserId(userId);
                }
            }
        }
    }

    public void updateNote(Note note) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "UPDATE notes SET course_id = ?, title = ?, content = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, note.getCourseId());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            stmt.setInt(4, note.getId());
            stmt.setInt(5, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteNote(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "DELETE FROM notes WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<Note> getAllNotes() throws SQLException {
        List<Note> notes = new ArrayList<>();
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_id, title, content FROM notes WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapRowToNote(rs));
                }
            }
        }
        return notes;
    }

    public Note getNoteById(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_id, title, content FROM notes WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToNote(rs);
                }
            }
        }
        return null;
    }

    private Note mapRowToNote(ResultSet rs) throws SQLException {
        return new Note(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("course_id"),
            rs.getString("title"),
            rs.getString("content")
        );
    }
}
