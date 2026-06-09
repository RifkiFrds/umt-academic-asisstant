package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import database.DBConnection;
import model.Course;
import utils.SessionManager;

public class CourseDAO {

    public void insertCourse(Course course) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "INSERT INTO courses (user_id, course_code, course_name, sks, lecturer) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getCourseName());
            stmt.setInt(4, course.getSks());
            stmt.setString(5, course.getLecturer());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                    course.setUserId(userId);
                }
            }
        }
    }

    public void updateCourse(Course course) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, sks = ?, lecturer = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getSks());
            stmt.setString(4, course.getLecturer());
            stmt.setInt(5, course.getId());
            stmt.setInt(6, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteCourse(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "DELETE FROM courses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_code, course_name, sks, lecturer FROM courses WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapRowToCourse(rs));
                }
            }
        }
        return courses;
    }

    public Course getCourseById(int id) throws SQLException {
        int userId = SessionManager.getCurrentUser().getId();
        String sql = "SELECT id, user_id, course_code, course_name, sks, lecturer FROM courses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCourse(rs);
                }
            }
        }
        return null;
    }

    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        return new Course(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("sks"),
            rs.getString("lecturer")
        );
    }
}
