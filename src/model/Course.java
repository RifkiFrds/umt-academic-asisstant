package model;

public class Course {
    private int id;
    private int userId;
    private String courseCode;
    private String courseName;
    private int sks;
    private String lecturer;

    public Course() {
    }

    public Course(int id, String courseCode, String courseName, int sks, String lecturer) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.sks = sks;
        this.lecturer = lecturer;
    }

    public Course(int id, int userId, String courseCode, String courseName, int sks, String lecturer) {
        this.id = id;
        this.userId = userId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.sks = sks;
        this.lecturer = lecturer;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", sks=" + sks +
                ", lecturer='" + lecturer + '\'' +
                '}';
    }
}
