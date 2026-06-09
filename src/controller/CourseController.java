package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.CourseDAO;
import model.Course;

public class CourseController {

    private final CourseDAO courseDAO;

    public CourseController() {
        this.courseDAO = new CourseDAO();
    }

    // Constructor allowing injection (e.g. for testing)
    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public boolean addCourse(Course course) {
        validateCourse(course);
        try {
            courseDAO.insertCourse(course);
            return true;
        } catch (SQLException e) {
            System.err.println("[CourseController] Error adding course: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        validateCourse(course);
        try {
            courseDAO.updateCourse(course);
            return true;
        } catch (SQLException e) {
            System.err.println("[CourseController] Error updating course: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        try {
            courseDAO.deleteCourse(id);
            return true;
        } catch (SQLException e) {
            System.err.println("[CourseController] Error deleting course: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses() {
        try {
            return courseDAO.getAllCourses();
        } catch (SQLException e) {
            System.err.println("[CourseController] Error fetching all courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Course getCourseById(int id) {
        try {
            return courseDAO.getCourseById(id);
        } catch (SQLException e) {
            System.err.println("[CourseController] Error fetching course by ID: " + e.getMessage());
            return null;
        }
    }

    private void validateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
    }
}
