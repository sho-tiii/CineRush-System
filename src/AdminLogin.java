import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame {
    // Color scheme: black + red
    private final Color bgColor = new Color(0x1A1A1A);       // Black background
    private final Color cardColor = new Color(0x222222);     // Dark card background
    private final Color inputColor = new Color(0x333333);    // Input field background
    private final Color textWhite = new Color(0xF9FAFB);     // White text
    private final Color accentRed = new Color(0xE11D48);     // Vivid red
    private final Color buttonRed = new Color(0xB91C2B);     // Darker red for buttons
    private final Color labelGray = new Color(0xD1D5DB);     // Label text gray

    // Fonts
    private final Font headerFont = new Font("Segoe UI", Font.PLAIN, 28);
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);

    public AdminLogin() {
        setTitle("CineRush - Admin Login");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
        setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));

        // Left Panel - Red side
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 380, 550);
        leftPanel.setBackground(buttonRed);
        add(leftPanel);

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: left;'>Welcome back,<br>Admin!</div></html>");
        welcomeLabel.setForeground(textWhite);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        welcomeLabel.setBounds(60, 160, 300, 100);
        leftPanel.add(welcomeLabel);

        // Right Panel
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBounds(380, 0, 520, 550);
        rightPanel.setBackground(bgColor);
        add(rightPanel);

        // Login Card
        JPanel loginCard = new JPanel(null);
        loginCard.setBounds(75, 75, 370, 380);
        loginCard.setBackground(cardColor);
        loginCard.setBorder(BorderFactory.createLineBorder(accentRed, 1));
        rightPanel.add(loginCard);

        JLabel loginTitle = new JLabel("ADMIN LOGIN");
        loginTitle.setForeground(accentRed);
        loginTitle.setFont(headerFont);
        loginTitle.setBounds(95, 30, 200, 40);
        loginCard.add(loginTitle);

        JSeparator separator = new JSeparator();
        separator.setBounds(90, 75, 200, 1);
        separator.setForeground(accentRed);
        loginCard.add(separator);

        // Username
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setForeground(labelGray);
        usernameLabel.setFont(labelFont);
        usernameLabel.setBounds(70, 100, 100, 20);
        loginCard.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(70, 125, 230, 35);
        usernameField.setBackground(inputColor);
        usernameField.setForeground(textWhite);
        usernameField.setCaretColor(textWhite);
        usernameField.setFont(inputFont);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        loginCard.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setForeground(labelGray);
        passwordLabel.setFont(labelFont);
        passwordLabel.setBounds(70, 170, 100, 20);
        loginCard.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(70, 195, 230, 35);
        passwordField.setBackground(inputColor);
        passwordField.setForeground(textWhite);
        passwordField.setCaretColor(textWhite);
        passwordField.setFont(inputFont);
        passwordField.setEchoChar('•');
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        loginCard.add(passwordField);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setBounds(70, 240, 130, 20);
        showPass.setForeground(labelGray);
        showPass.setBackground(cardColor);
        showPass.setFont(labelFont);
        showPass.setFocusPainted(false);
        showPass.addActionListener(e -> {
            passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });
        loginCard.add(showPass);

        // Login Button
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(70, 280, 230, 40);
        loginBtn.setForeground(textWhite);
        loginBtn.setBackground(buttonRed);
        loginBtn.setFont(buttonFont);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(accentRed);
            }

            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(buttonRed);
            }
        });
        loginBtn.addActionListener(e -> {
            String adminUsername = "admin";
            String adminPassword = "admin123";

            if (usernameField.getText().equals(adminUsername) &&
                new String(passwordField.getPassword()).equals(adminPassword)) {
                new AdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid admin credentials. Please try again.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        loginCard.add(loginBtn);

        // Back to user login
        JLabel backLabel = new JLabel("BACK TO USER LOGIN");
        backLabel.setForeground(accentRed);
        backLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backLabel.setBounds(70, 330, 230, 20);
        backLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new LoginWindow();
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(accentRed.brighter());
            }

            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(accentRed);
            }
        });
        loginCard.add(backLabel);

        setVisible(true);
    }
}
