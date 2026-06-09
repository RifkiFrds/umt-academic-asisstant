package utils;

import model.User;

public class SessionManager {

    private static User currentUser;

    private SessionManager() {
        // Private constructor
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    public static synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public static synchronized void clearSession() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
