package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.CourseController;
import controller.NoteController;
import controller.TaskController;
import model.Course;
import model.Task;
import model.TaskStatus;

public class DashboardView extends JPanel {

    private final CourseController courseController;
    private final TaskController taskController;
    private final NoteController noteController;

    // Stats labels
    private JLabel lblTotalCourses;
    private JLabel lblTotalTasks;
    private JLabel lblCompletedTasks;
    private JLabel lblPendingTasks;
    private JLabel lblTotalNotes;

    // Upcoming tasks panel/list
    private JPanel pnlUpcomingTasks;

    // Navigation callback
    private NavigationHandler navigationHandler;

    public interface NavigationHandler {
        void onNavigate(String viewName);
    }

    private JLabel lblWelcomeTitle;

    public DashboardView() {
        this.courseController = new CourseController();
        this.taskController = new TaskController();
        this.noteController = new NoteController();
        initComponents();
        refreshStatistics();
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setBackground(Color.WHITE);

        // ─── Header Panel (NORTH) ─────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);

        lblWelcomeTitle = new JLabel("Selamat Datang");
        lblWelcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        
        JLabel subtitleLabel = new JLabel("Semangat belajar hari ini! Kamu bisa melakukan yang terbaik.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        welcomePanel.add(lblWelcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(4));
        welcomePanel.add(subtitleLabel);


        // Date panel on the right
        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setBackground(new Color(243, 244, 246));
        datePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"));
        JLabel dateLabel = new JLabel(formattedDate);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLabel.setForeground(new Color(55, 65, 81));
        datePanel.add(dateLabel);

        headerPanel.add(welcomePanel, BorderLayout.WEST);
        headerPanel.add(datePanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ─── Center Main Panel ────────────────────────────────────
        JPanel centerPanel = new JPanel(new BorderLayout(0, 25));
        centerPanel.setOpaque(false);

        // 1. Cards Row (Top of Center Panel)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        cardsPanel.setOpaque(false);

        lblTotalCourses = new JLabel("0", SwingConstants.CENTER);
        lblTotalTasks = new JLabel("0", SwingConstants.CENTER);
        lblCompletedTasks = new JLabel("0", SwingConstants.CENTER);
        lblPendingTasks = new JLabel("0", SwingConstants.CENTER);
        lblTotalNotes = new JLabel("0", SwingConstants.CENTER);

        cardsPanel.add(createStatCard("Total Mata Kuliah", lblTotalCourses, new Color(37, 99, 235)));
        cardsPanel.add(createStatCard("Total Tugas", lblTotalTasks, new Color(139, 92, 246)));
        cardsPanel.add(createStatCard("Tugas Selesai", lblCompletedTasks, new Color(16, 185, 129)));
        cardsPanel.add(createStatCard("Tugas Tertunda", lblPendingTasks, new Color(239, 68, 68)));
        cardsPanel.add(createStatCard("Total Catatan", lblTotalNotes, new Color(245, 158, 11)));

        centerPanel.add(cardsPanel, BorderLayout.NORTH);

        // 2. Dashboard Body Split: Upcoming Tasks & AI Quick Actions
        JPanel bodySplitPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        bodySplitPanel.setOpaque(false);

        // Left: Upcoming Tasks
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 15));
        pnlLeft.setOpaque(false);
        pnlLeft.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlLeft.setBackground(Color.WHITE);

        JLabel lblUpcomingTitle = new JLabel("Tugas Mendatang");
        lblUpcomingTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlLeft.add(lblUpcomingTitle, BorderLayout.NORTH);

        pnlUpcomingTasks = new JPanel();
        pnlUpcomingTasks.setLayout(new BoxLayout(pnlUpcomingTasks, BoxLayout.Y_AXIS));
        pnlUpcomingTasks.setOpaque(false);
        
        JScrollPane scrollTasks = new JScrollPane(pnlUpcomingTasks);
        scrollTasks.setBorder(null);
        scrollTasks.setOpaque(false);
        scrollTasks.getViewport().setOpaque(false);
        pnlLeft.add(scrollTasks, BorderLayout.CENTER);

        // Bottom link for View Tasks
        JButton btnViewAllTasks = new JButton("Lihat Semua Tugas →");
        btnViewAllTasks.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnViewAllTasks.setForeground(new Color(37, 99, 235));
        btnViewAllTasks.setContentAreaFilled(false);
        btnViewAllTasks.setBorderPainted(false);
        btnViewAllTasks.setFocusPainted(false);
        btnViewAllTasks.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnViewAllTasks.addActionListener(e -> navigate("TASKS"));
        
        JPanel pnlLink = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlLink.setOpaque(false);
        pnlLink.add(btnViewAllTasks);
        pnlLeft.add(pnlLink, BorderLayout.SOUTH);

        bodySplitPanel.add(pnlLeft);

        // Right: AI Quick Actions
        JPanel pnlRight = new JPanel(new BorderLayout(10, 15));
        pnlRight.setOpaque(false);
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlRight.setBackground(Color.WHITE);

        JLabel lblAiTitle = new JLabel("AI Quick Actions");
        lblAiTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlRight.add(lblAiTitle, BorderLayout.NORTH);

        JPanel pnlAiActions = new JPanel(new GridLayout(3, 1, 0, 12));
        pnlAiActions.setOpaque(false);

        pnlAiActions.add(createAiActionCard("Smart Study Planner", "Buat rencana belajar cerdas berdasarkan matakuliah dan tugas.", new Color(37, 99, 235)));
        pnlAiActions.add(createAiActionCard("Academic Health Analyzer", "Analisis kebiasaan belajar dan status penyelesaian tugas akademik.", new Color(16, 185, 129)));
        pnlAiActions.add(createAiActionCard("Quiz Generator", "Buat kuis latihan otomatis dari isi catatan kuliah Anda.", new Color(139, 92, 246)));

        pnlRight.add(pnlAiActions, BorderLayout.CENTER);
        bodySplitPanel.add(pnlRight);

        centerPanel.add(bodySplitPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(18, 15, 18, 15)
        ));

        // Top line accent
        JPanel topAccent = new JPanel();
        topAccent.setPreferredSize(new Dimension(0, 4));
        topAccent.setBackground(accentColor);
        card.add(topAccent, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(107, 114, 128)); // Soft grey
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(titleLabel, BorderLayout.CENTER);
        card.add(valueLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createAiActionCard(String title, String desc, Color btnColor) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(new Color(249, 250, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(17, 24, 39));

        JLabel descLabel = new JLabel("<html>" + desc + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(107, 114, 128));

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(descLabel);

        card.add(textPanel, BorderLayout.CENTER);

        JButton btnAction = new JButton("Buka");
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAction.setForeground(Color.WHITE);
        btnAction.setBackground(btnColor);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAction.addActionListener(e -> navigate("AI"));

        JPanel btnWrapper = new JPanel(new GridBagLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnAction);
        card.add(btnWrapper, BorderLayout.EAST);

        return card;
    }

    public void refreshStatistics() {
        if (utils.SessionManager.isLoggedIn()) {
            lblWelcomeTitle.setText("Selamat Datang, " + utils.SessionManager.getCurrentUser().getFullName());
        } else {
            lblWelcomeTitle.setText("Selamat Datang di Academic Assistant");
        }

        int totalCourses = courseController.getAllCourses().size();
        List<Task> tasks = taskController.getAllTasks();
        int totalTasks = tasks.size();

        
        long completedTasks = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();

        long pendingTasks = tasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.COMPLETED)
                .count();

        int totalNotes = noteController.getAllNotes().size();

        lblTotalCourses.setText(String.valueOf(totalCourses));
        lblTotalTasks.setText(String.valueOf(totalTasks));
        lblCompletedTasks.setText(String.valueOf(completedTasks));
        lblPendingTasks.setText(String.valueOf(pendingTasks));
        lblTotalNotes.setText(String.valueOf(totalNotes));

        // Refresh upcoming tasks list (up to 4 pending tasks sorted by deadline)
        pnlUpcomingTasks.removeAll();
        List<Task> upcoming = tasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.COMPLETED)
                .sorted(Comparator.comparing(Task::getDeadline))
                .limit(4)
                .collect(Collectors.toList());

        if (upcoming.isEmpty()) {
            JLabel lblNoTasks = new JLabel("Tidak ada tugas mendatang.");
            lblNoTasks.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblNoTasks.setForeground(Color.GRAY);
            lblNoTasks.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnlUpcomingTasks.add(lblNoTasks);
        } else {
            for (Task t : upcoming) {
                pnlUpcomingTasks.add(createUpcomingTaskRow(t));
                pnlUpcomingTasks.add(Box.createVerticalStrut(10));
            }
        }
        pnlUpcomingTasks.revalidate();
        pnlUpcomingTasks.repaint();
    }

    private JPanel createUpcomingTaskRow(Task t) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(243, 244, 246), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Accent indicator based on deadline proximity
        JPanel indicator = new JPanel();
        indicator.setPreferredSize(new Dimension(5, 0));
        LocalDate now = LocalDate.now();
        if (t.getDeadline().isBefore(now.plusDays(2))) {
            indicator.setBackground(new Color(239, 68, 68)); // Urgent - Red
        } else if (t.getDeadline().isBefore(now.plusDays(5))) {
            indicator.setBackground(new Color(245, 158, 11)); // Orange
        } else {
            indicator.setBackground(new Color(37, 99, 235)); // Blue
        }
        row.add(indicator, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(t.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(31, 41, 55));

        Course course = courseController.getCourseById(t.getCourseId());
        String courseText = (course != null) ? course.getCourseCode() + " - " + course.getCourseName() : "Unknown Course";
        JLabel lblCourse = new JLabel(courseText);
        lblCourse.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCourse.setForeground(Color.GRAY);

        infoPanel.add(lblTitle);
        infoPanel.add(lblCourse);
        row.add(infoPanel, BorderLayout.CENTER);

        JLabel lblDeadline = new JLabel(t.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")));
        lblDeadline.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDeadline.setForeground(new Color(107, 114, 128));
        
        row.add(lblDeadline, BorderLayout.EAST);

        return row;
    }

    private void navigate(String target) {
        if (navigationHandler != null) {
            navigationHandler.onNavigate(target);
        }
    }
}
