package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.CourseController;
import model.Course;

public class CourseView extends JPanel {

    private final CourseController courseController;

    // Form fields
    private JTextField txtCourseCode;
    private JTextField txtCourseName;
    private JTextField txtSks;
    private JTextField txtLecturer;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;

    // Tracks selected course ID for update/delete
    private int selectedCourseId = -1;

    public CourseView() {
        this.courseController = new CourseController();
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setBackground(Color.WHITE);

        // ─── Header Panel ─────────────────────────────────────────
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Manajemen Mata Kuliah");
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

        JLabel lblDetail = new JLabel("Detail Mata Kuliah");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDetail.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblDetail, gbc);

        gbc.gridwidth = 1;
        
        // Row 1: Kode MK
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Kode MK"), gbc);
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtCourseCode = new JTextField();
        txtCourseCode.setBackground(Color.WHITE);
        txtCourseCode.setForeground(new Color(17, 24, 39));
        txtCourseCode.setCaretColor(Color.BLACK);
        txtCourseCode.putClientProperty("JTextField.placeholderText", "Contoh: IF-101");
        formCard.add(txtCourseCode, gbc);

        // Row 2: Nama MK
        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Nama Mata Kuliah"), gbc);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtCourseName = new JTextField();
        txtCourseName.setBackground(Color.WHITE);
        txtCourseName.setForeground(new Color(17, 24, 39));
        txtCourseName.setCaretColor(Color.BLACK);
        txtCourseName.putClientProperty("JTextField.placeholderText", "Contoh: Algoritma");
        formCard.add(txtCourseName, gbc);

        // Row 3: SKS
        gbc.gridwidth = 1;
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("SKS"), gbc);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtSks = new JTextField();
        txtSks.setBackground(Color.WHITE);
        txtSks.setForeground(new Color(17, 24, 39));
        txtSks.setCaretColor(Color.BLACK);
        txtSks.putClientProperty("JTextField.placeholderText", "Jumlah SKS (contoh: 3)");
        formCard.add(txtSks, gbc);

        // Row 4: Dosen
        gbc.gridwidth = 1;
        gbc.gridy = 7; gbc.gridx = 0; gbc.weightx = 0;
        formCard.add(createStyledLabel("Dosen Pengampu"), gbc);
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtLecturer = new JTextField();
        txtLecturer.setBackground(Color.WHITE);
        txtLecturer.setForeground(new Color(17, 24, 39));
        txtLecturer.setCaretColor(Color.BLACK);
        txtLecturer.putClientProperty("JTextField.placeholderText", "Nama Dosen");
        formCard.add(txtLecturer, gbc);

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
        btnRefresh.addActionListener(e -> loadTableData());

        return container;
    }

    private JPanel createModernTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        String[] columns = {"ID", "Kode MK", "Nama Mata Kuliah", "SKS", "Dosen Pengampu"};
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

        // Align columns nicely
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Hide the ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

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

    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Course> courses = courseController.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getCourseCode(),
                c.getCourseName(),
                c.getSks(),
                c.getLecturer()
            });
        }
        clearForm();
    }

    private void onAdd() {
        String courseCode = txtCourseCode.getText().trim();
        String courseName = txtCourseName.getText().trim();
        String sksText = txtSks.getText().trim();
        String lecturer = txtLecturer.getText().trim();

        if (courseCode.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode MK dan Nama MK harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sks;
        try {
            sks = Integer.parseInt(sksText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "SKS harus berupa angka.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setSks(sks);
        course.setLecturer(lecturer);

        try {
            if (courseController.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan mata kuliah.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onUpdate() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan diperbarui.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseCode = txtCourseCode.getText().trim();
        String courseName = txtCourseName.getText().trim();
        String sksText = txtSks.getText().trim();
        String lecturer = txtLecturer.getText().trim();

        if (courseCode.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode MK dan Nama MK harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sks;
        try {
            sks = Integer.parseInt(sksText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "SKS harus berupa angka.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Course course = new Course();
        course.setId(selectedCourseId);
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setSks(sks);
        course.setLecturer(lecturer);

        try {
            if (courseController.updateCourse(course)) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui mata kuliah.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDelete() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus mata kuliah ini?\nTindakan ini juga menghapus semua tugas dan catatan terkait.",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (courseController.deleteCourse(selectedCourseId)) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus mata kuliah.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedCourseId = (int) tableModel.getValueAt(selectedRow, 0);
            txtCourseCode.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtCourseName.setText((String) tableModel.getValueAt(selectedRow, 2));
            txtSks.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            txtLecturer.setText((String) tableModel.getValueAt(selectedRow, 4));
        }
    }

    private void clearForm() {
        txtCourseCode.setText("");
        txtCourseName.setText("");
        txtSks.setText("");
        txtLecturer.setText("");
        selectedCourseId = -1;
        table.clearSelection();
    }
}
