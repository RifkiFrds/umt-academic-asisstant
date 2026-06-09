package view;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.AIController;
import controller.CourseController;
import controller.NoteController;
import controller.TaskController;
import model.Task;
import model.TaskStatus;

public class AcademicHealthPanel extends JPanel {

    private final AIController aiController;
    private final CourseController courseController;
    private final TaskController taskController;
    private final NoteController noteController;

    private CardLayout cardLayout;
    private JPanel pnlCards;

    // Initial View Components
    private JButton btnGenerateInitial;

    // Result View Components
    private JLabel lblScoreBadge;
    private JLabel lblTimestamp;
    private JTextArea txtResult;
    private JButton btnRegenerate;
    private JButton btnCopy;
    private JLabel lblLoading;

    public AcademicHealthPanel() {
        this.aiController = new AIController();
        this.courseController = new CourseController();
        this.taskController = new TaskController();
        this.noteController = new NoteController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.setOpaque(false);

        // ─── CARD 1: INITIAL STATE ────────────────────────────────
        JPanel pnlInitial = new JPanel(new GridBagLayout());
        pnlInitial.setBackground(Color.WHITE);
        pnlInitial.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblInitIcon = new JLabel("🩺", SwingConstants.CENTER);
        lblInitIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        pnlInitial.add(lblInitIcon, gbc);

        JLabel lblInitTitle = new JLabel("Academic Health Analyzer", SwingConstants.CENTER);
        lblInitTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblInitTitle.setForeground(new Color(17, 24, 39));
        pnlInitial.add(lblInitTitle, gbc);

        JTextArea txtInitDesc = new JTextArea("Fitur ini menganalisis beban mata kuliah, progres tugas, serta kuantitas catatan Anda untuk mendiagnosis kesehatan produktivitas akademik Anda beserta rekomendasi area perbaikan dari AI.");
        txtInitDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInitDesc.setForeground(Color.GRAY);
        txtInitDesc.setLineWrap(true);
        txtInitDesc.setWrapStyleWord(true);
        txtInitDesc.setEditable(false);
        txtInitDesc.setFocusable(false);
        txtInitDesc.setBackground(Color.WHITE);
        txtInitDesc.setColumns(30);
        txtInitDesc.setAlignmentX(CENTER_ALIGNMENT);
        pnlInitial.add(txtInitDesc, gbc);

        btnGenerateInitial = new JButton("Generate Analisis Kesehatan");
        btnGenerateInitial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerateInitial.setForeground(Color.WHITE);
        btnGenerateInitial.setBackground(new Color(16, 185, 129)); // Emerald Green
        btnGenerateInitial.setPreferredSize(new Dimension(240, 45));
        btnGenerateInitial.setFocusPainted(false);
        btnGenerateInitial.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGenerateInitial.addActionListener(e -> onGenerate());
        
        JPanel pnlBtnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnContainer.setOpaque(false);
        pnlBtnContainer.add(btnGenerateInitial);
        pnlInitial.add(pnlBtnContainer, gbc);

        // ─── CARD 2: LOADING STATE ────────────────────────────────
        JPanel pnlLoading = new JPanel(new GridBagLayout());
        pnlLoading.setBackground(Color.WHITE);
        
        lblLoading = new JLabel("Menganalisis Kesehatan Akademik Anda...", SwingConstants.CENTER);
        lblLoading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLoading.setForeground(new Color(75, 85, 99));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setForeground(new Color(16, 185, 129));
        progressBar.setBackground(new Color(243, 244, 246));
        progressBar.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0;
        gbcL.gridy = GridBagConstraints.RELATIVE;
        gbcL.insets = new Insets(15, 0, 15, 0);
        
        pnlLoading.add(lblLoading, gbcL);
        pnlLoading.add(progressBar, gbcL);

        // ─── CARD 3: RESULT STATE ─────────────────────────────────
        JPanel pnlResult = new JPanel(new BorderLayout(15, 15));
        pnlResult.setBackground(Color.WHITE);
        pnlResult.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Health Report Card
        JPanel cardHealth = new JPanel(new BorderLayout(0, 15));
        cardHealth.setBackground(Color.WHITE);
        cardHealth.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Header Panel inside card
        JPanel pnlCardHeader = new JPanel(new BorderLayout(15, 0));
        pnlCardHeader.setOpaque(false);

        JPanel pnlTextHeader = new JPanel(new BorderLayout());
        pnlTextHeader.setOpaque(false);

        JLabel lblCardTitle = new JLabel("Laporan Kesehatan Akademik");
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCardTitle.setForeground(new Color(17, 24, 39));

        lblTimestamp = new JLabel("Dianalisis pada: -");
        lblTimestamp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTimestamp.setForeground(Color.GRAY);

        pnlTextHeader.add(lblCardTitle, BorderLayout.NORTH);
        pnlTextHeader.add(lblTimestamp, BorderLayout.SOUTH);
        pnlCardHeader.add(pnlTextHeader, BorderLayout.CENTER);

        // Score Badge
        lblScoreBadge = new JLabel("80/100", SwingConstants.CENTER);
        lblScoreBadge.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblScoreBadge.setOpaque(true);
        lblScoreBadge.setBackground(new Color(209, 250, 229)); // light green
        lblScoreBadge.setForeground(new Color(5, 150, 105)); // dark green
        lblScoreBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pnlCardHeader.add(lblScoreBadge, BorderLayout.EAST);

        cardHealth.add(pnlCardHeader, BorderLayout.NORTH);

        // Result content area
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtResult.setBackground(Color.WHITE);
        txtResult.setForeground(new Color(17, 24, 39));
        txtResult.setCaretColor(Color.BLACK);
        txtResult.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollResult = new JScrollPane(txtResult);
        scrollResult.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        cardHealth.add(scrollResult, BorderLayout.CENTER);

        // Action Buttons
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        btnRegenerate = new JButton("Generate Ulang");
        btnRegenerate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegenerate.setForeground(Color.WHITE);
        btnRegenerate.setBackground(new Color(16, 185, 129));
        btnRegenerate.setPreferredSize(new Dimension(150, 38));
        btnRegenerate.setFocusPainted(false);
        btnRegenerate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegenerate.addActionListener(e -> onGenerate());

        btnCopy = new JButton("Salin Laporan");
        btnCopy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCopy.setForeground(new Color(75, 85, 99));
        btnCopy.setBackground(new Color(243, 244, 246));
        btnCopy.setPreferredSize(new Dimension(130, 38));
        btnCopy.setFocusPainted(false);
        btnCopy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCopy.addActionListener(e -> onCopy());

        pnlActions.add(btnCopy);
        pnlActions.add(btnRegenerate);
        cardHealth.add(pnlActions, BorderLayout.SOUTH);

        pnlResult.add(cardHealth, BorderLayout.CENTER);

        // Add cards to container
        pnlCards.add(pnlInitial, "INITIAL");
        pnlCards.add(pnlLoading, "LOADING");
        pnlCards.add(pnlResult, "RESULT");

        add(pnlCards, BorderLayout.CENTER);
        cardLayout.show(pnlCards, "INITIAL");
    }

    private void onGenerate() {
        int totalCourses = courseController.getAllCourses().size();
        List<Task> tasks = taskController.getAllTasks();
        int totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        long pendingTasks = tasks.stream().filter(t -> t.getStatus() != TaskStatus.COMPLETED).count();
        int totalNotes = noteController.getAllNotes().size();

        if (totalCourses == 0 && totalTasks == 0 && totalNotes == 0) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada data akademik (mata kuliah, tugas, atau catatan). Silakan isi data terlebih dahulu.",
                    "Data Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cardLayout.show(pnlCards, "LOADING");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return aiController.generateAcademicHealthAnalysis(
                        totalCourses, totalTasks, completedTasks, pendingTasks, totalNotes
                );
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    txtResult.setText(result);

                    // Extract score
                    int score = parseScore(result);
                    updateScoreBadge(score);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
                    lblTimestamp.setText("Dianalisis pada: " + LocalDateTime.now().format(formatter) + " WIB");

                    cardLayout.show(pnlCards, "RESULT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AcademicHealthPanel.this,
                            "Terjadi kesalahan saat menghubungi AI: " + ex.getMessage(),
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(pnlCards, "INITIAL");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private int parseScore(String text) {
        if (text == null) return 80;
        // Search for Skor Produktivitas followed by number, optionally /100
        Pattern pattern = Pattern.compile("Skor\\s+Produktivitas:\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception e) {
                // fallback
            }
        }
        // General search for any number like "85/100"
        Pattern pattern2 = Pattern.compile("(\\d+)/100");
        Matcher matcher2 = pattern2.matcher(text);
        if (matcher2.find()) {
            try {
                return Integer.parseInt(matcher2.group(1));
            } catch (Exception e) {
                // fallback
            }
        }
        return 80; // default safe fallback score
    }

    private void updateScoreBadge(int score) {
        lblScoreBadge.setText(score + "/100");
        if (score < 50) {
            // High Risk - Red
            lblScoreBadge.setBackground(new Color(254, 226, 226)); // light red
            lblScoreBadge.setForeground(new Color(220, 38, 38)); // dark red
            lblScoreBadge.setBorder(BorderFactory.createLineBorder(new Color(239, 68, 68), 1));
        } else if (score < 75) {
            // Medium Risk - Orange/Yellow
            lblScoreBadge.setBackground(new Color(254, 243, 199)); // light amber
            lblScoreBadge.setForeground(new Color(217, 119, 6)); // dark amber
            lblScoreBadge.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 1));
        } else {
            // Good - Green
            lblScoreBadge.setBackground(new Color(209, 250, 229)); // light green
            lblScoreBadge.setForeground(new Color(5, 150, 105)); // dark green
            lblScoreBadge.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 1));
        }
    }

    private void onCopy() {
        String content = txtResult.getText();
        if (content != null && !content.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
            JOptionPane.showMessageDialog(this, "Laporan kesehatan akademik berhasil disalin!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
