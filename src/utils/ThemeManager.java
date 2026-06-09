package utils;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.UIManager;

public class ThemeManager {

    private ThemeManager() {
        // Prevent instantiation
    }

    /**
     * Applies the FlatLaf Look and Feel based on the given theme name.
     * Supported values: "dark" (default), "light".
     * Must be called before any Swing component is created.
     */
    public static void init(String theme) {
        try {
            if ("light".equalsIgnoreCase(theme)) {
                FlatLightLaf.setup();
            } else {
                FlatDarkLaf.setup();
            }
        } catch (Exception e) {
            System.err.println("[ThemeManager] FlatLaf setup failed, falling back to system L&F.");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("[ThemeManager] System L&F fallback also failed: " + ex.getMessage());
            }
        }
    }
}
