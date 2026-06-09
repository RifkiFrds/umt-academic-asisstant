package controller;

import java.sql.SQLException;
import dao.UserDAO;
import model.User;

public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Mohon lengkapi username dan password");
        }
        User user = userDAO.login(username, password);
        if (user == null) {
            throw new SQLException("Username atau password salah");
        }
        return user;
    }
    
    public void initDefaultAdmin() {
        userDAO.createDefaultAdminIfNotExists();
    }
}
