package view;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.AIController;
import controller.CourseController;
import controller.TaskController;
import model.Course;
import model.Task;

public class StudyPlannerPanel extends JPanel {

    private final AIController aiController;
    private final CourseController courseController;
    private final TaskController taskController;

    private CardLayout cardLayout;
    private JPanel pnlCards;

    // Initial View Components
    private JButton btnGenerateInitial;

    // Result View Components
    private JLabel lblTimestamp;
    private JTextArea txtResult;
    private JButton btnRegenerate;
    private JButton btnCopy;
    private JLabel lblLoading;

    public StudyPlannerPanel() {
        this.aiController = new AIController();
        this.courseController = new CourseController();
        this.taskController = new TaskController();
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

        JLabel lblInitIcon = new JLabel("📅", SwingConstants.CENTER);
        lblInitIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        pnlInitial.add(lblInitIcon, gbc);

        JLabel lblInitTitle = new JLabel("Smart Study Planner", SwingConstants.CENTER);
        lblInitTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblInitTitle.setForeground(new Color(17, 24, 39));
        pnlInitial.add(lblInitTitle, gbc);

        JTextArea txtInitDesc = new JTextArea("Fitur ini akan menganalisis data mata kuliah dan tugas aktif Anda secara otomatis, lalu merumuskan strategi rencana belajar mingguan yang personal dan optimal.");
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

        btnGenerateInitial = new JButton("Generate Rencana Belajar");
        btnGenerateInitial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerateInitial.setForeground(Color.WHITE);
        btnGenerateInitial.setBackground(new Color(37, 99, 235)); // Primary Blue
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
        
        lblLoading = new JLabel("Menyusun Rencana Belajar Terbaik Anda...", SwingConstants.CENTER);
        lblLoading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLoading.setForeground(new Color(75, 85, 99));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setForeground(new Color(37, 99, 235));
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

        // Result Card (Styled Container)
        JPanel cardStudyPlan = new JPanel(new BorderLayout(0, 10));
        cardStudyPlan.setBackground(Color.WHITE);
        cardStudyPlan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Header inside result card
        JPanel pnlResultHeader = new JPanel(new BorderLayout());
        pnlResultHeader.setOpaque(false);

        JLabel lblResultTitle = new JLabel("Rencana Belajar Mingguan");
        lblResultTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResultTitle.setForeground(new Color(17, 24, 39));

        lblTimestamp = new JLabel("Dibuat pada: -");
        lblTimestamp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTimestamp.setForeground(Color.GRAY);

        pnlResultHeader.add(lblResultTitle, BorderLayout.NORTH);
        pnlResultHeader.add(lblTimestamp, BorderLayout.SOUTH);
        cardStudyPlan.add(pnlResultHeader, BorderLayout.NORTH);

        // Content
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
        cardStudyPlan.add(scrollResult, BorderLayout.CENTER);

        // Actions at bottom
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        btnRegenerate = new JButton("Generate Ulang");
        btnRegenerate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegenerate.setForeground(Color.WHITE);
        btnRegenerate.setBackground(new Color(37, 99, 235));
        btnRegenerate.setPreferredSize(new Dimension(150, 38));
        btnRegenerate.setFocusPainted(false);
        btnRegenerate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegenerate.addActionListener(e -> onGenerate());

        btnCopy = new JButton("Salin Hasil");
        btnCopy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCopy.setForeground(new Color(75, 85, 99));
        btnCopy.setBackground(new Color(243, 244, 246));
        btnCopy.setPreferredSize(new Dimension(120, 38));
        btnCopy.setFocusPainted(false);
        btnCopy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCopy.addActionListener(e -> onCopy());

        pnlActions.add(btnCopy);
        pnlActions.add(btnRegenerate);
        cardStudyPlan.add(pnlActions, BorderLayout.SOUTH);

        pnlResult.add(cardStudyPlan, BorderLayout.CENTER);

        // Add to main cards container
        pnlCards.add(pnlInitial, "INITIAL");
        pnlCards.add(pnlLoading, "LOADING");
        pnlCards.add(pnlResult, "RESULT");

        add(pnlCards, BorderLayout.CENTER);
        cardLayout.show(pnlCards, "INITIAL");
    }

    private void onGenerate() {
        List<Course> courses = courseController.getAllCourses();
        List<Task> tasks = taskController.getAllTasks();

        if (courses.isEmpty() && tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada data mata kuliah atau tugas. Silakan tambahkan mata kuliah dan tugas terlebih dahulu.",
                    "Data Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cardLayout.show(pnlCards, "LOADING");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return aiController.generateStudyPlan(courses, tasks);
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    txtResult.setText(result);
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
                    lblTimestamp.setText("Dibuat pada: " + LocalDateTime.now().format(formatter) + " WIB");
                    
                    cardLayout.show(pnlCards, "RESULT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudyPlannerPanel.this,
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

    private void onCopy() {
        String content = txtResult.getText();
        if (content != null && !content.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
            JOptionPane.showMessageDialog(this, "Hasil rencana belajar berhasil disalin ke clipboard!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
