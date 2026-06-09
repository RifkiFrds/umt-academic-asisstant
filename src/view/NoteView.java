package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.CourseController;
import controller.NoteController;
import model.Course;
import model.Note;

public class NoteView extends JPanel {

    private final NoteController noteController;
    private final CourseController courseController;

    // Form fields
    private JComboBox<CourseWrapper> cbCourse;
    private JTextField txtTitle;
    private JTextArea txtContent;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;

    // Tracks selected note ID
    private int selectedNoteId = -1;

    public NoteView() {
        this.noteController = new NoteController();
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
        JLabel lblTitle = new JLabel("Manajemen Catatan Kuliah");
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

        JLabel lblDetail = new JLabel("Detail Catatan");
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
        formCard.add(createStyledLabel("Judul Catatan"), gbc);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtTitle = new JTextField();
        txtTitle.setBackground(Color.WHITE);
        txtTitle.setForeground(new Color(17, 24, 39));
        txtTitle.setCaretColor(Color.BLACK);
        txtTitle.putClientProperty("JTextField.placeholderText", "Contoh: Rumus Turunan");
        formCard.add(txtTitle, gbc);

        // Row 3 — Content
        gbc.gridwidth = 1;
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Isi Catatan"), gbc);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtContent = new JTextArea(8, 20);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setBackground(Color.WHITE);
        txtContent.setForeground(new Color(17, 24, 39));
        txtContent.setCaretColor(Color.BLACK);
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane scrollContent = new JScrollPane(txtContent);
        scrollContent.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        formCard.add(scrollContent, gbc);

        // Reset constraints for the main container layout
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        String[] columns = {"ID", "Course ID", "Mata Kuliah", "Judul Catatan", "Isi Catatan"};
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
        List<Note> notes = noteController.getAllNotes();
        for (Note n : notes) {
            Course c = courseController.getCourseById(n.getCourseId());
            String courseName = (c != null) ? c.getCourseCode() + " - " + c.getCourseName() : "Unknown";
            tableModel.addRow(new Object[]{
                n.getId(),
                n.getCourseId(),
                courseName,
                n.getTitle(),
                n.getContent()
            });
        }
        clearForm();
    }

    private void onAdd() {
        CourseWrapper selectedCourse = (CourseWrapper) cbCourse.getSelectedItem();
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Mata Kuliah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Note note = new Note();
        note.setCourseId(selectedCourse.getCourse().getId());
        note.setTitle(title);
        note.setContent(content);

        try {
            if (noteController.addNote(note)) {
                JOptionPane.showMessageDialog(this, "Catatan berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan catatan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onUpdate() {
        if (selectedNoteId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih catatan yang akan diperbarui.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CourseWrapper selectedCourse = (CourseWrapper) cbCourse.getSelectedItem();
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Mata Kuliah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Note note = new Note();
        note.setId(selectedNoteId);
        note.setCourseId(selectedCourse.getCourse().getId());
        note.setTitle(title);
        note.setContent(content);

        try {
            if (noteController.updateNote(note)) {
                JOptionPane.showMessageDialog(this, "Catatan berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui catatan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDelete() {
        if (selectedNoteId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih catatan yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus catatan ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (noteController.deleteNote(selectedNoteId)) {
                JOptionPane.showMessageDialog(this, "Catatan berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus catatan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedNoteId = (int) tableModel.getValueAt(selectedRow, 0);
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
            txtContent.setText((String) tableModel.getValueAt(selectedRow, 4));
        }
    }

    private void clearForm() {
        if (cbCourse.getItemCount() > 0) {
            cbCourse.setSelectedIndex(0);
        }
        txtTitle.setText("");
        txtContent.setText("");
        selectedNoteId = -1;
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
}
