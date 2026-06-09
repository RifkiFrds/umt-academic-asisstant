package controller;

import java.sql.SQLException;
import dao.UserDAO;
import model.User;

public class RegisterController {

    private final UserDAO userDAO;

    public RegisterController() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String fullName, String username, String password, String confirmPassword) throws SQLException {
        if (fullName == null || fullName.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mohon lengkapi seluruh field formulir");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password dan Konfirmasi Password tidak cocok");
        }

        if (userDAO.usernameExists(username)) {
            throw new SQLException("Username sudah terdaftar. Silakan pilih username lain");
        }

        User newUser = new User();
        newUser.setFullName(fullName.trim());
        newUser.setUsername(username.trim());
        newUser.setPassword(password.trim());

        return userDAO.register(newUser);
    }
}
