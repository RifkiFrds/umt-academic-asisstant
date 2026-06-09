package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadDefaults();
        loadExternal();
    }

    /**
     * Loads default properties bundled inside the classpath
     * (src/main/resources/application.properties).
     */
    private static void loadDefaults() {
        try (InputStream in = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in != null) {
                properties.load(in);
            } else {
                System.err.println("[ConfigReader] application.properties not found in classpath.");
            }
        } catch (IOException e) {
            System.err.println("[ConfigReader] Failed to load classpath config: " + e.getMessage());
        }
    }

    /**
     * Loads an optional external application.properties from the working
     * directory.  Values here override classpath defaults, which is useful
     * for local development where credentials differ per machine.
     */
    private static void loadExternal() {
        try (InputStream in = new FileInputStream("application.properties")) {
            properties.load(in);
        } catch (IOException e) {
            // External file is optional — no warning needed.
        }
    }

    /**
     * Returns the value for the given key.
     * Resolution order: environment variable → external file → classpath default.
     */
    public static String get(String key) {
        String envKey = toEnvKey(key);
        if (envKey != null) {
            String envVal = System.getenv(envKey);
            if (envVal != null && !envVal.isBlank()) {
                return envVal;
            }
        }
        return properties.getProperty(key);
    }

    /**
     * Returns the value for the given key, or the supplied default if missing.
     */
    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return (value != null) ? value : defaultValue;
    }

    /**
     * Returns the value for the given key parsed as an int,
     * or the supplied default on missing / malformed values.
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Maps a property key to its corresponding environment variable name.
     * Only sensitive or deployment-specific keys are mapped.
     */
    private static String toEnvKey(String key) {
        return switch (key) {
            case "gemini.api.key" -> "GEMINI_API_KEY";
            case "REPLICATE_API_TOKEN" -> "REPLICATE_API_TOKEN";
            case "db.password"    -> "DB_PASSWORD";
            case "db.username"    -> "DB_USERNAME";
            case "db.host"        -> "DB_HOST";
            case "db.name"        -> "DB_NAME";
            default               -> null;
        };
    }
}
