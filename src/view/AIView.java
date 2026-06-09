package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AIView extends JPanel {

    private NavigationHandler navigationHandler;

    public interface NavigationHandler {
        void onNavigate(String target);
    }

    public AIView() {
        initComponents();
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(Color.WHITE);

        // ─── Header Panel ─────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("AI Assistant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(17, 24, 39));

        JLabel subtitleLabel = new JLabel("Pilih fitur AI yang ingin Anda gunakan untuk meningkatkan produktivitas.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ─── Cards Grid (CENTER) ──────────────────────────────────
        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 25, 0));
        cardsGrid.setOpaque(false);

        cardsGrid.add(createFeatureCard(
                "Smart Study Planner",
                "Buat rencana belajar mingguan otomatis secara cerdas berdasarkan mata kuliah, daftar tugas, dan tanggal tenggat waktu.",
                "Buat Rencana",
                "STUDY_PLANNER",
                new Color(37, 99, 235), // Primary Blue
                new Color(239, 246, 255) // Soft light blue bg
        ));

        cardsGrid.add(createFeatureCard(
                "Academic Health Analyzer",
                "Analisis performa dan kebiasaan belajar Anda, dapatkan metrik kesehatan akademik, serta saran rekomendasi personal.",
                "Analisis Sekarang",
                "HEALTH_ANALYZER",
                new Color(16, 185, 129), // Emerald Green
                new Color(240, 253, 244) // Soft light green bg
        ));

        cardsGrid.add(createFeatureCard(
                "Quiz Generator",
                "Hasilkan kuis latihan pilihan ganda secara instan dari kumpulan ringkasan materi dan catatan kuliah Anda.",
                "Buat Kuis",
                "QUIZ_GENERATOR",
                new Color(139, 92, 246), // Purple
                new Color(245, 243, 255) // Soft light purple bg
        ));

        add(cardsGrid, BorderLayout.CENTER);
    }

    private JPanel createFeatureCard(String title, String desc, String btnText, String targetView, Color accentColor, Color cardBg) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(25, 20, 25, 20)
        ));

        // Decorative top bar
        JPanel topAccent = new JPanel();
        topAccent.setPreferredSize(new Dimension(0, 5));
        topAccent.setBackground(accentColor);
        card.add(topAccent, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(17, 24, 39));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(75, 85, 99));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setPreferredSize(new Dimension(200, 100));

        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(lblDesc);
        card.add(contentPanel, BorderLayout.CENTER);

        JButton btnAction = new JButton(btnText);
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAction.setForeground(Color.WHITE);
        btnAction.setBackground(accentColor);
        btnAction.setPreferredSize(new Dimension(0, 40));
        btnAction.setFocusPainted(false);
        btnAction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAction.addActionListener(e -> {
            if (navigationHandler != null) {
                navigationHandler.onNavigate(targetView);
            }
        });

        card.add(btnAction, BorderLayout.SOUTH);

        return card;
    }
}
