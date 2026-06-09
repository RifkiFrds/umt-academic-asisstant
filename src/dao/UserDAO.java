package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DBConnection;
import model.User;

public class UserDAO {

    public User login(String username, String password) throws SQLException {
        createDefaultAdminIfNotExists();

        String sql = "SELECT id, full_name, username, password, created_at FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(User user) throws SQLException {
        createDefaultAdminIfNotExists();

        if (usernameExists(user.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO users (full_name, username, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public User getById(int id) throws SQLException {
        String sql = "SELECT id, full_name, username, password, created_at FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public void createDefaultAdminIfNotExists() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "full_name VARCHAR(100) NOT NULL,"
                + "username VARCHAR(50) NOT NULL UNIQUE,"
                + "password VARCHAR(255) NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);

            // Dynamically add created_at if it was missing in the existing table
            try {
                stmt.execute("ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                System.out.println("[UserDAO] Added missing created_at column to users table.");
            } catch (SQLException ex) {
                // Ignore if column already exists
            }

            // Dynamically add user_id column and foreign key constraint to courses table
            try {
                stmt.execute("ALTER TABLE courses ADD COLUMN user_id INT DEFAULT 1");
                stmt.execute("ALTER TABLE courses ADD CONSTRAINT fk_courses_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE");
                System.out.println("[UserDAO] Added user_id and foreign key to courses table.");
            } catch (SQLException ex) {
                // Ignore if column/constraint already exists
            }

            // Dynamically add user_id column and foreign key constraint to tasks table
            try {
                stmt.execute("ALTER TABLE tasks ADD COLUMN user_id INT DEFAULT 1");
                stmt.execute("ALTER TABLE tasks ADD CONSTRAINT fk_tasks_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE");
                System.out.println("[UserDAO] Added user_id and foreign key to tasks table.");
            } catch (SQLException ex) {
                // Ignore if column/constraint already exists
            }

            // Dynamically add user_id column and foreign key constraint to notes table
            try {
                stmt.execute("ALTER TABLE notes ADD COLUMN user_id INT DEFAULT 1");
                stmt.execute("ALTER TABLE notes ADD CONSTRAINT fk_notes_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE");
                System.out.println("[UserDAO] Added user_id and foreign key to notes table.");
            } catch (SQLException ex) {
                // Ignore if column/constraint already exists
            }

            // Check if empty
            String checkSql = "SELECT COUNT(*) FROM users";
            boolean isEmpty = true;
            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) > 0) {
                    isEmpty = false;
                }
            }

            if (isEmpty) {
                String insertSql = "INSERT INTO users (id, full_name, username, password) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, 1);
                    ps.setString(2, "Administrator");
                    ps.setString(3, "admin");
                    ps.setString(4, "admin123");
                    ps.executeUpdate();
                    System.out.println("[UserDAO] Default admin account created: admin/admin123");
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error initializing user table/default admin: " + e.getMessage());
        }
    }
}
