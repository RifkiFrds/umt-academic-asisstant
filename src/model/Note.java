package model;

public class Note {
    private int id;
    private int userId;
    private int courseId;
    private String title;
    private String content;

    public Note() {
    }

    public Note(int id, int courseId, String title, String content) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
    }

    public Note(int id, int userId, int courseId, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
