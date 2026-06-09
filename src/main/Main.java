package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.ConfigReader;
import utils.ThemeManager;
import view.AIView;
import view.AcademicHealthPanel;
import view.CourseView;
import view.DashboardView;
import view.LoginView;
import view.NoteView;
import view.QuizGeneratorPanel;
import view.StudyPlannerPanel;
import view.TaskView;

public class Main {
    public static void main(String[] args) {
        // 1. Copy logo dynamically at runtime if resource doesn't exist
        initializeLogoResource();

        // 2. Initialize UI theme via ThemeManager
        String theme = ConfigReader.getOrDefault("app.theme", "dark");
        ThemeManager.init(theme);

        // 3. Launch the Login window first
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(user -> {
                // When successfully logged in, boot the main app frame
                launchMainDashboard();
            });
            loginView.setVisible(true);
        });
    }

    private static void initializeLogoResource() {
        try {
            java.io.File dest = new java.io.File("src/main/resources/images/logo.png");
            if (!dest.exists()) {
                dest.getParentFile().mkdirs();
                java.io.File source = new java.io.File("C:\\Users\\Rifki\\.gemini\\antigravity-ide\\brain\\9b5f2b25-c44b-49fb-a1d9-df144424aa19\\logo_1781018438860.png");
                if (source.exists()) {
                    java.nio.file.Files.copy(source.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[Main] Copied logo to resources folder dynamically.");
                }
            }
        } catch (Exception e) {
            System.err.println("[Main] Failed to copy logo: " + e.getMessage());
        }
    }

    private static void launchMainDashboard() {
        JFrame frame = new JFrame("UMT Academic Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set size from configuration
        int width = ConfigReader.getInt("app.window.width", 1280);
        int height = ConfigReader.getInt("app.window.height", 720);
        frame.setSize(width, height);
        frame.setMinimumSize(new Dimension(1000, 650));
        frame.setLocationRelativeTo(null); // Center on screen

        // Main application panel holding sidebar and content
        JPanel mainAppPanel = new JPanel(new BorderLayout());
        mainAppPanel.setBackground(Color.WHITE);

        // Container for views using CardLayout
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(null);

        // Instantiate Views
        DashboardView dashboardView = new DashboardView();
        CourseView courseView = new CourseView();
        TaskView taskView = new TaskView();
        NoteView noteView = new NoteView();
        AIView aiView = new AIView();
        StudyPlannerPanel studyPlannerPanel = new StudyPlannerPanel();
        AcademicHealthPanel healthAnalyzerPanel = new AcademicHealthPanel();
        QuizGeneratorPanel quizGeneratorPanel = new QuizGeneratorPanel();

        // Wrap panels with navigation header
        JPanel studyPlannerWrapper = createAiSubmoduleWrapper(studyPlannerPanel, cardLayout, contentPanel);
        JPanel healthAnalyzerWrapper = createAiSubmoduleWrapper(healthAnalyzerPanel, cardLayout, contentPanel);
        JPanel quizGeneratorWrapper = createAiSubmoduleWrapper(quizGeneratorPanel, cardLayout, contentPanel);

        // Add panels to CardLayout container directly
        contentPanel.add(dashboardView, "DASHBOARD");
        contentPanel.add(courseView, "COURSES");
        contentPanel.add(taskView, "TASKS");
        contentPanel.add(noteView, "NOTES");
        contentPanel.add(aiView, "AI");
        contentPanel.add(studyPlannerWrapper, "STUDY_PLANNER");
        contentPanel.add(healthAnalyzerWrapper, "HEALTH_ANALYZER");
        contentPanel.add(quizGeneratorWrapper, "QUIZ_GENERATOR");

        // Setup AIView navigation handler
        aiView.setNavigationHandler(target -> {
            if ("QUIZ_GENERATOR".equals(target)) {
                quizGeneratorPanel.loadNotes();
            }
            cardLayout.show(contentPanel, target);
        });

        // Setup Sidebar
        JPanel sidebar = createSidebar(cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);
        mainAppPanel.add(sidebar, BorderLayout.WEST);
        mainAppPanel.add(contentPanel, BorderLayout.CENTER);

        // Setup Dashboard View inner navigation callback
        dashboardView.setNavigationHandler(target -> {
            // Find and trigger the active button state in sidebar
            Component[] comps = sidebar.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JPanel) {
                    for (Component child : ((JPanel) comp).getComponents()) {
                        if (child instanceof SidebarButton) {
                            SidebarButton btn = (SidebarButton) child;
                            if (btn.targetName.equals(target)) {
                                btn.setActive(true);
                            } else {
                                btn.setActive(false);
                            }
                        }
                    }
                }
            }
            
            // Refresh statistics when showing views or leaving
            if ("COURSES".equals(target)) {
                courseView.loadTableData();
            } else if ("TASKS".equals(target)) {
                taskView.loadCourses();
                taskView.loadTableData();
            } else if ("NOTES".equals(target)) {
                noteView.loadCourses();
                noteView.loadTableData();
            }
            cardLayout.show(contentPanel, target);
        });

        frame.add(mainAppPanel);
        frame.setVisible(true);
    }

    private static JPanel createSidebar(CardLayout cardLayout, JPanel contentPanel, 
                                        DashboardView dashboardView, CourseView courseView, 
                                        TaskView taskView, NoteView noteView) {
        
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(17, 24, 39)); // Deep dark navy/slate (#111827)
        sidebar.setBorder(null);

        // 1. Logo / Title Section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel titleLabel = new JLabel("UMT ACADEMIC");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("ASSISTANT");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        subtitleLabel.setForeground(new Color(59, 130, 246)); // Vibrant blue
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("Sistem Manajemen Akademik Mahasiswa");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        descLabel.setForeground(new Color(156, 163, 175)); // Soft grey
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(descLabel);
        sidebar.add(headerPanel, BorderLayout.NORTH);

        // 2. Navigation Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        SidebarButton btnDashboard = new SidebarButton("Dashboard", "DASHBOARD", true, cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);
        SidebarButton btnCourses = new SidebarButton("Mata Kuliah", "COURSES", false, cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);
        SidebarButton btnTasks = new SidebarButton("Tugas", "TASKS", false, cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);
        SidebarButton btnNotes = new SidebarButton("Catatan Kuliah", "NOTES", false, cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);
        SidebarButton btnAi = new SidebarButton("AI Assistant", "AI", false, cardLayout, contentPanel, dashboardView, courseView, taskView, noteView);

        // Place all buttons in a list to manage active state together
        SidebarButton[] allButtons = new SidebarButton[]{btnDashboard, btnCourses, btnTasks, btnNotes, btnAi};
        for (SidebarButton btn : allButtons) {
            btn.setPeerButtons(allButtons);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(8));
        }

        // Add custom Logout button
        JButton btnLogout = new JButton("Keluar / Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setForeground(new Color(239, 68, 68)); // Red-500
        btnLogout.setBackground(new Color(17, 24, 39));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(210, 40));
        btnLogout.setMinimumSize(new Dimension(210, 40));
        btnLogout.setPreferredSize(new Dimension(210, 40));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setContentAreaFilled(true);
                btnLogout.setBackground(new Color(31, 41, 55));
                btnLogout.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setContentAreaFilled(false);
                btnLogout.setBackground(new Color(17, 24, 39));
                btnLogout.repaint();
            }
        });
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(contentPanel, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                utils.SessionManager.clearSession();
                Window window = SwingUtilities.getWindowAncestor(contentPanel);
                if (window != null) {
                    window.dispose();
                }
                SwingUtilities.invokeLater(() -> {
                    LoginView lv = new LoginView(user -> {
                        launchMainDashboard();
                    });
                    lv.setVisible(true);
                });
            }
        });
        
        menuPanel.add(Box.createVerticalStrut(15));
        menuPanel.add(btnLogout);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // 3. User Profile Card at bottom
        JPanel profileCard = new JPanel(new BorderLayout(12, 0));
        profileCard.setBackground(new Color(31, 41, 55)); // Gray-800
        profileCard.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Rounded Avatar Placeholder
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(59, 130, 246)); // Blue
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "U";
                if (utils.SessionManager.isLoggedIn()) {
                    String name = utils.SessionManager.getCurrentUser().getFullName();
                    if (name != null && !name.isEmpty()) {
                        text = name.substring(0, 1).toUpperCase();
                    }
                }
                
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setOpaque(false);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        String displayUserName = "User";
        if (utils.SessionManager.isLoggedIn()) {
            displayUserName = utils.SessionManager.getCurrentUser().getFullName();
        }
        JLabel nameLabel = new JLabel(displayUserName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel("Mahasiswa");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(156, 163, 175));

        details.add(nameLabel);
        details.add(roleLabel);


        profileCard.add(avatar, BorderLayout.WEST);
        profileCard.add(details, BorderLayout.CENTER);

        // Footer panel below the profile card
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(8, 15, 8, 15));

        JLabel copyrightLabel = new JLabel("© 2026 UMT Academic Assistant");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        copyrightLabel.setForeground(new Color(156, 163, 175));
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel developerLabel = new JLabel("Developed by Muhamad Rifki Firdaus");
        developerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        developerLabel.setForeground(new Color(107, 114, 128));
        developerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(copyrightLabel);
        footerPanel.add(Box.createVerticalStrut(2));
        footerPanel.add(developerLabel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(profileCard, BorderLayout.CENTER);
        southPanel.add(footerPanel, BorderLayout.SOUTH);

        sidebar.add(southPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private static class SidebarButton extends JButton {
        private final String targetName;
        private boolean isActive;
        private SidebarButton[] peers;
        
        private final Color activeBg = new Color(37, 99, 235); // Blue-600
        private final Color hoverBg = new Color(31, 41, 55);  // Gray-800
        private final Color normalBg = new Color(17, 24, 39); // Gray-900 (transparent look)

        public SidebarButton(String text, String targetName, boolean active,
                             CardLayout cardLayout, JPanel contentPanel,
                             DashboardView dashboardView, CourseView courseView, 
                             TaskView taskView, NoteView noteView) {
            super(text);
            this.targetName = targetName;
            this.isActive = active;
            
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(active ? Color.WHITE : new Color(209, 213, 219));
            setBackground(active ? activeBg : normalBg);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(210, 40));
            setMinimumSize(new Dimension(210, 40));
            setPreferredSize(new Dimension(210, 40));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isActive) {
                        setBackground(hoverBg);
                        setForeground(Color.WHITE);
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isActive) {
                        setBackground(normalBg);
                        setForeground(new Color(209, 213, 219));
                        repaint();
                    }
                }
            });

            addActionListener(e -> {
                // Reset peers
                if (peers != null) {
                    for (SidebarButton peer : peers) {
                        peer.setActive(false);
                    }
                }
                setActive(true);

                // Reload views as we navigate to them
                if ("DASHBOARD".equals(targetName)) {
                    dashboardView.refreshStatistics();
                } else if ("COURSES".equals(targetName)) {
                    courseView.loadTableData();
                } else if ("TASKS".equals(targetName)) {
                    taskView.loadCourses();
                    taskView.loadTableData();
                } else if ("NOTES".equals(targetName)) {
                    noteView.loadCourses();
                    noteView.loadTableData();
                }

                cardLayout.show(contentPanel, targetName);
            });
        }

        public void setPeerButtons(SidebarButton[] peers) {
            this.peers = peers;
        }

        public void setActive(boolean active) {
            this.isActive = active;
            setForeground(active ? Color.WHITE : new Color(209, 213, 219));
            setBackground(active ? activeBg : normalBg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            // Text placement
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = 15; // Left padding
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), x, y);
            
            g2.dispose();
        }
    }

    private static JPanel createAiSubmoduleWrapper(JPanel viewPanel, CardLayout cardLayout, JPanel container) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        JButton btnBack = new JButton("← Kembali ke AI Assistant");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(new Color(75, 85, 99));
        btnBack.setBackground(new Color(243, 244, 246));
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(190, 30));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            cardLayout.show(container, "AI");
        });

        navBar.add(btnBack);
        wrapper.add(navBar, BorderLayout.NORTH);
        wrapper.add(viewPanel, BorderLayout.CENTER);
        return wrapper;
    }
}
