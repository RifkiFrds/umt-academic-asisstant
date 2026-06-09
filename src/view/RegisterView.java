package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.RegisterController;

public class RegisterView extends JFrame {

    private final LoginView loginView;
    private final RegisterController registerController;

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JLabel lblError;
    private JButton btnBack;

    public RegisterView(LoginView loginView) {
        super("UMT Academic Assistant - Daftar Akun");
        this.loginView = loginView;
        this.registerController = new RegisterController();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 580);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel pnlCard = new JPanel(new GridBagLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        JLabel lblTitle = new JLabel("Daftar Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(17, 24, 39));
        pnlCard.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Lengkapi data diri Anda di bawah ini", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        pnlCard.add(lblSubtitle, gbc);

        pnlCard.add(Box.createVerticalStrut(10), gbc);

        // Full Name
        JLabel lblFullName = new JLabel("Nama Lengkap");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFullName.setForeground(new Color(75, 85, 99));
        pnlCard.add(lblFullName, gbc);

        txtFullName = new JTextField();
        txtFullName.setPreferredSize(new Dimension(0, 36));
        txtFullName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFullName.setBackground(Color.WHITE);
        txtFullName.setForeground(new Color(17, 24, 39));
        txtFullName.setCaretColor(Color.BLACK);
        txtFullName.putClientProperty("JTextField.placeholderText", "Nama Lengkap Anda");
        pnlCard.add(txtFullName, gbc);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(new Color(75, 85, 99));
        pnlCard.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(0, 36));
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setBackground(Color.WHITE);
        txtUsername.setForeground(new Color(17, 24, 39));
        txtUsername.setCaretColor(Color.BLACK);
        txtUsername.putClientProperty("JTextField.placeholderText", "Pilih username");
        pnlCard.add(txtUsername, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(75, 85, 99));
        pnlCard.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 36));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(new Color(17, 24, 39));
        txtPassword.setCaretColor(Color.BLACK);
        txtPassword.putClientProperty("JTextField.placeholderText", "Masukkan password");
        pnlCard.add(txtPassword, gbc);

        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Konfirmasi Password");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConfirmPassword.setForeground(new Color(75, 85, 99));
        pnlCard.add(lblConfirmPassword, gbc);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setPreferredSize(new Dimension(0, 36));
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConfirmPassword.setBackground(Color.WHITE);
        txtConfirmPassword.setForeground(new Color(17, 24, 39));
        txtConfirmPassword.setCaretColor(Color.BLACK);
        txtConfirmPassword.putClientProperty("JTextField.placeholderText", "Ulangi password");
        pnlCard.add(txtConfirmPassword, gbc);

        // Error Label
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblError.setForeground(new Color(220, 38, 38));
        pnlCard.add(lblError, gbc);

        // Register Button
        btnRegister = new JButton("Daftar Sekarang");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(139, 92, 246)); // Purple
        btnRegister.setPreferredSize(new Dimension(0, 38));
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> onRegister());
        pnlCard.add(btnRegister, gbc);

        // Back link button
        btnBack = new JButton("Sudah punya akun? Masuk");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(new Color(37, 99, 235)); // Link Blue
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            setVisible(false);
            loginView.setVisible(true);
            dispose();
        });
        pnlCard.add(btnBack, gbc);

        add(pnlCard, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblFooter = new JLabel("© 2026 UMT Academic Assistant");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(Color.GRAY);
        pnlFooter.add(lblFooter);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void onRegister() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

        lblError.setText("");

        try {
            boolean success = registerController.register(fullName, username, password, confirmPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan masuk menggunakan akun baru Anda.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                loginView.setVisible(true);
                dispose();
            } else {
                lblError.setText("Gagal mendaftarkan akun. Silakan coba kembali.");
            }
        } catch (IllegalArgumentException ex) {
            lblError.setText(ex.getMessage());
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
        }
    }
}
