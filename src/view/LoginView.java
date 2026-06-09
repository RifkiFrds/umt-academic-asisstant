package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.LoginController;
import model.User;
import utils.SessionManager;

public class LoginView extends JFrame {

    private final LoginController loginController;
    private final LoginSuccessListener successListener;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JButton btnRegisterLink;

    public interface LoginSuccessListener {
        void onLoginSuccess(User user);
    }

    public LoginView(LoginSuccessListener successListener) {
        super("UMT Academic Assistant - Login");
        this.loginController = new LoginController();
        this.successListener = successListener;
        
        // Setup initial default admin user in DB
        loginController.initDefaultAdmin();
        
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 540);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Center card wrapper
        JPanel pnlCard = new JPanel(new GridBagLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 1. Logo Loading with Fallback
        JLabel lblLogo = new JLabel("🎓", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        
        java.net.URL logoUrl = getClass().getResource("/images/logo.png");
        if (logoUrl != null) {
            try {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image scaledImg = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImg));
                lblLogo.setText(""); // Remove fallback text
            } catch (Exception e) {
                // Fallback to emoji
            }
        }
        pnlCard.add(lblLogo, gbc);

        // 2. Title
        JLabel lblTitle = new JLabel("UMT ACADEMIC ASSISTANT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(17, 24, 39)); // Gray-900
        pnlCard.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Silakan masuk ke akun Anda", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        pnlCard.add(lblSubtitle, gbc);

        // Spacer
        pnlCard.add(Box.createVerticalStrut(8), gbc);

        // 3. Username Label & Field
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(new Color(75, 85, 99)); // Gray-600
        pnlCard.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(0, 36));
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setBackground(Color.WHITE);
        txtUsername.setForeground(new Color(17, 24, 39));
        txtUsername.setCaretColor(Color.BLACK);
        txtUsername.putClientProperty("JTextField.placeholderText", "Masukkan username");
        pnlCard.add(txtUsername, gbc);

        // 4. Password Label & Field
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

        // Enter key action handlers
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);

        // 5. Error Label
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblError.setForeground(new Color(220, 38, 38)); // Red-600
        pnlCard.add(lblError, gbc);

        // 6. Login Button
        btnLogin = new JButton("Masuk");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(37, 99, 235)); // Primary Blue
        btnLogin.setPreferredSize(new Dimension(0, 38));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> onLogin());
        pnlCard.add(btnLogin, gbc);

        // 7. Register link button
        btnRegisterLink = new JButton("Belum punya akun? Daftar");
        btnRegisterLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegisterLink.setForeground(new Color(37, 99, 235)); // Link Blue
        btnRegisterLink.setBorderPainted(false);
        btnRegisterLink.setContentAreaFilled(false);
        btnRegisterLink.setFocusPainted(false);
        btnRegisterLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegisterLink.addActionListener(e -> {
            setVisible(false);
            new RegisterView(this).setVisible(true);
        });
        pnlCard.add(btnRegisterLink, gbc);

        add(pnlCard, BorderLayout.CENTER);

        // 8. Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(0, 0, 15, 0));
        JLabel lblFooter = new JLabel("© 2026 UMT Academic Assistant");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(Color.GRAY);
        pnlFooter.add(lblFooter);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        lblError.setText("");

        try {
            User authenticatedUser = loginController.authenticate(username, password);
            
            // Set User Session
            SessionManager.setCurrentUser(authenticatedUser);

            // Hide and dispose login frame
            setVisible(false);
            dispose();
            
            // Invoke callback to boot dashboard
            if (successListener != null) {
                successListener.onLoginSuccess(authenticatedUser);
            }
        } catch (IllegalArgumentException ex) {
            lblError.setText(ex.getMessage());
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
        }
    }
}
