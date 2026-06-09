package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.CourseController;
import controller.TaskController;
import model.Course;
import model.Task;
import model.TaskStatus;

public class TaskView extends JPanel {

    private final TaskController taskController;
    private final CourseController courseController;

    // Form fields
    private JComboBox<CourseWrapper> cbCourse;
    private JTextField txtTitle;
    private JTextField txtDeadline;
    private JComboBox<TaskStatus> cbStatus;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;

    // Tracks selected task ID
    private int selectedTaskId = -1;

    public TaskView() {
        this.taskController = new TaskController();
        this.courseController = new CourseController();
        initComponents();
        loadCourses();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setBackground(Color.WHITE);

        // ─── Header Panel ─────────────────────────────────────────
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Manajemen Tugas Kuliah");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(17, 24, 39));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // ─── Form Panel (WEST) ────────────────────────────────────
        JPanel formPanel = createModernFormPanel();
        add(formPanel, BorderLayout.WEST);

        // ─── Table Panel (CENTER) ─────────────────────────────────
        JPanel tablePanel = createModernTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createModernFormPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(320, 0));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblDetail = new JLabel("Detail Tugas");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDetail.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblDetail, gbc);

        gbc.gridwidth = 1;

        // Row 1 — Course
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Mata Kuliah"), gbc);
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        cbCourse = new JComboBox<>();
        formCard.add(cbCourse, gbc);

        // Row 2 — Title
        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Judul Tugas"), gbc);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtTitle = new JTextField();
        txtTitle.setBackground(Color.WHITE);
        txtTitle.setForeground(new Color(17, 24, 39));
        txtTitle.setCaretColor(Color.BLACK);
        txtTitle.putClientProperty("JTextField.placeholderText", "Contoh: Laporan Bab 1");
        formCard.add(txtTitle, gbc);

        // Row 3 — Deadline
        gbc.gridwidth = 1;
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Tenggat Waktu (YYYY-MM-DD)"), gbc);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JPanel deadlinePanel = new JPanel(new BorderLayout(5, 0));
        deadlinePanel.setOpaque(false);
        txtDeadline = new JTextField();
        txtDeadline.setBackground(Color.WHITE);
        txtDeadline.setForeground(new Color(17, 24, 39));
        txtDeadline.setCaretColor(Color.BLACK);
        txtDeadline.putClientProperty("JTextField.placeholderText", "Contoh: 2026-06-30");
        
        JButton btnDatePicker = new JButton("📅");
        btnDatePicker.setFocusPainted(false);
        btnDatePicker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDatePicker.addActionListener(e -> showDatePicker(txtDeadline));
        
        deadlinePanel.add(txtDeadline, BorderLayout.CENTER);
        deadlinePanel.add(btnDatePicker, BorderLayout.EAST);
        formCard.add(deadlinePanel, gbc);

        // Row 4 — Status
        gbc.gridwidth = 1;
        gbc.gridy = 7; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Status"), gbc);
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(TaskStatus.values());
        cbStatus.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TaskStatus) {
                    switch ((TaskStatus) value) {
                        case PENDING: label.setText("Belum Dikerjakan"); break;
                        case IN_PROGRESS: label.setText("Sedang Dikerjakan"); break;
                        case COMPLETED: label.setText("Selesai"); break;
                    }
                }
                return label;
            }
        });
        formCard.add(cbStatus, gbc);

        container.add(formCard, BorderLayout.CENTER);

        // Action Buttons stacked vertically under form
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        btnPanel.setOpaque(false);

        btnAdd = createPrimaryButton("Tambah", new Color(37, 99, 235));
        btnUpdate = createPrimaryButton("Perbarui", new Color(16, 185, 129));
        btnDelete = createPrimaryButton("Hapus", new Color(239, 68, 68));
        btnClear = createSecondaryButton("Bersihkan");
        btnRefresh = createSecondaryButton("Segarkan");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        container.add(btnPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> {
            loadCourses();
            loadTableData();
        });

        return container;
    }

    private JPanel createModernTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        String[] columns = {"ID", "Course ID", "Mata Kuliah", "Judul Tugas", "Tenggat Waktu", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(new Color(37, 99, 235));

        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(249, 250, 251));
        header.setForeground(new Color(75, 85, 99));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 40));

        // Center / Left align columns
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Customize Status column renderer for badge styling
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                
                if (value instanceof TaskStatus) {
                    TaskStatus status = (TaskStatus) value;
                    String text = "";
                    Color bg = Color.WHITE;
                    Color fg = Color.WHITE;
                    
                    switch (status) {
                        case PENDING:
                            text = "Belum Dikerjakan";
                            bg = new Color(254, 226, 226);
                            fg = new Color(220, 38, 38);
                            break;
                        case IN_PROGRESS:
                            text = "Sedang Dikerjakan";
                            bg = new Color(254, 243, 199);
                            fg = new Color(217, 119, 6);
                            break;
                        case COMPLETED:
                            text = "Selesai";
                            bg = new Color(209, 250, 229);
                            fg = new Color(5, 150, 105);
                            break;
                    }
                    label.setText(text);
                    
                    if (!isSelected) {
                        label.setBackground(bg);
                        label.setForeground(fg);
                    } else {
                        label.setBackground(fg);
                        label.setForeground(Color.WHITE);
                    }
                    label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                }
                return label;
            }
        });

        // Hide ID and Course ID columns
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(0);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(75, 85, 99));
        return lbl;
    }

    private JButton createPrimaryButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(100, 36));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(75, 85, 99));
        btn.setBackground(new Color(243, 244, 246));
        btn.setPreferredSize(new Dimension(100, 36));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void loadCourses() {
        cbCourse.removeAllItems();
        List<Course> courses = courseController.getAllCourses();
        for (Course c : courses) {
            cbCourse.addItem(new CourseWrapper(c));
        }
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Task> tasks = taskController.getAllTasks();
        for (Task t : tasks) {
            Course c = courseController.getCourseById(t.getCourseId());
            String courseName = (c != null) ? c.getCourseCode() + " - " + c.getCourseName() : "Unknown";
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getCourseId(),
                courseName,
                t.getTitle(),
                t.getDeadline().toString(),
                t.getStatus()
            });
        }
        clearForm();
    }

    private void onAdd() {
        CourseWrapper selectedCourse = (CourseWrapper) cbCourse.getSelectedItem();
        String title = txtTitle.getText().trim();
        String deadlineText = txtDeadline.getText().trim();
        TaskStatus status = (TaskStatus) cbStatus.getSelectedItem();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Mata Kuliah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate deadline;
        try {
            deadline = LocalDate.parse(deadlineText);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Task task = new Task();
        task.setCourseId(selectedCourse.getCourse().getId());
        task.setTitle(title);
        task.setDeadline(deadline);
        task.setStatus(status);

        try {
            if (taskController.addTask(task)) {
                JOptionPane.showMessageDialog(this, "Tugas berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan tugas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onUpdate() {
        if (selectedTaskId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih tugas yang akan diperbarui.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CourseWrapper selectedCourse = (CourseWrapper) cbCourse.getSelectedItem();
        String title = txtTitle.getText().trim();
        String deadlineText = txtDeadline.getText().trim();
        TaskStatus status = (TaskStatus) cbStatus.getSelectedItem();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Mata Kuliah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate deadline;
        try {
            deadline = LocalDate.parse(deadlineText);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Task task = new Task();
        task.setId(selectedTaskId);
        task.setCourseId(selectedCourse.getCourse().getId());
        task.setTitle(title);
        task.setDeadline(deadline);
        task.setStatus(status);

        try {
            if (taskController.updateTask(task)) {
                JOptionPane.showMessageDialog(this, "Tugas berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui tugas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDelete() {
        if (selectedTaskId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih tugas yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus tugas ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (taskController.deleteTask(selectedTaskId)) {
                JOptionPane.showMessageDialog(this, "Tugas berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus tugas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedTaskId = (int) tableModel.getValueAt(selectedRow, 0);
            int courseId = (int) tableModel.getValueAt(selectedRow, 1);
            
            // Set Course
            for (int i = 0; i < cbCourse.getItemCount(); i++) {
                CourseWrapper cw = cbCourse.getItemAt(i);
                if (cw.getCourse().getId() == courseId) {
                    cbCourse.setSelectedIndex(i);
                    break;
                }
            }

            txtTitle.setText((String) tableModel.getValueAt(selectedRow, 3));
            txtDeadline.setText((String) tableModel.getValueAt(selectedRow, 4));
            cbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void clearForm() {
        if (cbCourse.getItemCount() > 0) {
            cbCourse.setSelectedIndex(0);
        }
        txtTitle.setText("");
        txtDeadline.setText("");
        cbStatus.setSelectedIndex(0);
        selectedTaskId = -1;
        table.clearSelection();
    }

    private static class CourseWrapper {
        private final Course course;

        public CourseWrapper(Course course) {
            this.course = course;
        }

        public Course getCourse() {
            return course;
        }

        @Override
        public String toString() {
            return course.getCourseCode() + " - " + course.getCourseName();
        }
    }

    private void showDatePicker(JTextField targetField) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pilih Tanggal", true);
        dialog.setSize(300, 320);
        dialog.setLocationRelativeTo(targetField);
        dialog.setLayout(new BorderLayout(5, 5));

        JPanel pnlHeader = new JPanel(new BorderLayout(5, 5));
        pnlHeader.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JButton btnPrev = new JButton("◀");
        JButton btnNext = new JButton("▶");
        JLabel lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pnlHeader.add(btnPrev, BorderLayout.WEST);
        pnlHeader.add(lblMonthYear, BorderLayout.CENTER);
        pnlHeader.add(btnNext, BorderLayout.EAST);
        dialog.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlGrid = new JPanel(new GridLayout(0, 7, 2, 2));
        pnlGrid.setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.add(pnlGrid, BorderLayout.CENTER);

        String[] daysOfWeek = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String day : daysOfWeek) {
            JLabel lblDay = new JLabel(day, SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", Font.BOLD, 11));
            pnlGrid.add(lblDay);
        }

        final java.util.Calendar cal = java.util.Calendar.getInstance();
        try {
            LocalDate currentVal = LocalDate.parse(targetField.getText().trim());
            cal.set(currentVal.getYear(), currentVal.getMonthValue() - 1, currentVal.getDayOfMonth());
        } catch (Exception ex) {
            // fallback
        }

        Runnable redrawCalendar = new Runnable() {
            @Override
            public void run() {
                while (pnlGrid.getComponentCount() > 7) {
                    pnlGrid.remove(7);
                }

                int month = cal.get(java.util.Calendar.MONTH);
                int year = cal.get(java.util.Calendar.YEAR);
                String[] monthNames = {
                    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                };
                lblMonthYear.setText(monthNames[month] + " " + year);

                java.util.Calendar firstOf = (java.util.Calendar) cal.clone();
                firstOf.set(java.util.Calendar.DAY_OF_MONTH, 1);
                int startDayOfWeek = firstOf.get(java.util.Calendar.DAY_OF_WEEK) - 1;
                
                int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

                for (int i = 0; i < startDayOfWeek; i++) {
                    pnlGrid.add(new JLabel(""));
                }

                for (int day = 1; day <= maxDay; day++) {
                    final int finalDay = day;
                    JButton btnDay = new JButton(String.valueOf(day));
                    btnDay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btnDay.setFocusPainted(false);
                    btnDay.setBackground(Color.WHITE);
                    btnDay.setMargin(new Insets(2, 2, 2, 2));
                    
                    if (day == cal.get(java.util.Calendar.DAY_OF_MONTH)) {
                        btnDay.setBackground(new Color(239, 246, 255));
                        btnDay.setForeground(new Color(37, 99, 235));
                    }

                    btnDay.addActionListener(evt -> {
                        LocalDate selected = LocalDate.of(year, month + 1, finalDay);
                        targetField.setText(selected.toString());
                        dialog.dispose();
                    });
                    pnlGrid.add(btnDay);
                }

                pnlGrid.revalidate();
                pnlGrid.repaint();
            }
        };

        btnPrev.addActionListener(evt -> {
            cal.add(java.util.Calendar.MONTH, -1);
            redrawCalendar.run();
        });
        btnNext.addActionListener(evt -> {
            cal.add(java.util.Calendar.MONTH, 1);
            redrawCalendar.run();
        });

        redrawCalendar.run();
        dialog.setVisible(true);
    }
}
