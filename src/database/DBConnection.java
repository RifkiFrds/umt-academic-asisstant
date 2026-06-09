package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.ConfigReader;

public class DBConnection {

    private static Connection connection;

    private DBConnection() {
        // Prevent instantiation
    }

    /**
     * Returns a singleton JDBC connection to the MySQL database.
     * The connection is lazily created and automatically re-opened
     * if the previous one was closed.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String host     = ConfigReader.getOrDefault("db.host", "localhost");
            String port     = ConfigReader.getOrDefault("db.port", "3306");
            String dbName   = ConfigReader.getOrDefault("db.name", "umt_academic_assistant");
            String username = ConfigReader.getOrDefault("db.username", "root");
            String password = ConfigReader.getOrDefault("db.password", "");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("[DBConnection] Connection established.");
        }
        return connection;
    }

    /**
     * Closes the current connection if it is open.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DBConnection] Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("[DBConnection] Failed to close: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
