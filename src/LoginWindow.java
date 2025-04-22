import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {
    // Colors (Red & Black theme)
    private Color bgColor = new Color(0x121212);          // Very dark background
    private Color primaryRed = new Color(0xD32F2F);       // Strong red
    private Color darkRed = new Color(0x8B0000);          // Dark red for left panel
    private Color textWhite = new Color(0xF9FAFB);        // Off-white for text
    private Color lightGray = new Color(0xB0B0B0);        // Light gray for labels
    private Color panelBg = new Color(0x1E1E1E);          // Right panel background
    private Color fieldBg = new Color(0x2C2C2C);          // Input field background
    private Color borderAccent = new Color(0xFF5252);     // Border/hover accent
    private Color cardBg = new Color(0x1A1A1A);           // Card background

    // Fonts
    private Font headerFont = new Font("Segoe UI Light", Font.PLAIN, 28);
    private Font normalFont = new Font("Segoe UI Light", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Font smallFont = new Font("Segoe UI Light", Font.PLAIN, 12);

    public LoginWindow() {
        setTitle("CineRush - Movie Ticketing System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
        setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, 380, 550);
        leftPanel.setBackground(darkRed);
        add(leftPanel);

        JLabel taglineLabel = new JLabel("PREMIERE MOVIE EXPERIENCE");
        taglineLabel.setForeground(new Color(255, 255, 255, 220));
        taglineLabel.setFont(smallFont);
        taglineLabel.setBounds(110, 40, 250, 20);
        leftPanel.add(taglineLabel);

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>Welcome to<br>the Ultimate Booking<br>Experience</div></html>");
        welcomeLabel.setForeground(textWhite);
        welcomeLabel.setFont(headerFont);
        welcomeLabel.setBounds(70, 150, 280, 120);
        leftPanel.add(welcomeLabel);

        JLabel ctaLabel = new JLabel("Reserve Your Seat Today!");
        ctaLabel.setForeground(borderAccent);
        ctaLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
        ctaLabel.setBounds(110, 360, 220, 30);
        leftPanel.add(ctaLabel);

        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBounds(380, 0, 520, 550);
        rightPanel.setBackground(panelBg);
        add(rightPanel);

        JLabel closeLabel = new JLabel("×");
        closeLabel.setForeground(lightGray);
        closeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        closeLabel.setBounds(480, 10, 20, 20);
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(primaryRed);
            }

            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(lightGray);
            }
        });
        rightPanel.add(closeLabel);

        // Login Card
        JPanel loginCard = new JPanel();
        loginCard.setLayout(null);
        loginCard.setBounds(75, 45, 370, 410);
        loginCard.setBackground(cardBg);
        loginCard.setBorder(BorderFactory.createLineBorder(borderAccent, 1));
        rightPanel.add(loginCard);

        JLabel loginTitle = new JLabel("User Login");
        loginTitle.setForeground(textWhite);
        loginTitle.setFont(headerFont);
        loginTitle.setBounds(120, 30, 150, 40);
        loginCard.add(loginTitle);

        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setBounds(110, 75, 150, 2);
        titleSeparator.setForeground(borderAccent);
        loginCard.add(titleSeparator);

        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setForeground(lightGray);
        usernameLabel.setBounds(70, 100, 100, 25);
        usernameLabel.setFont(smallFont);
        loginCard.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(70, 125, 230, 35);
        usernameField.setCaretColor(textWhite);
        usernameField.setForeground(textWhite);
        usernameField.setBackground(fieldBg);
        usernameField.setFont(normalFont);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        loginCard.add(usernameField);

        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setForeground(lightGray);
        passwordLabel.setBounds(70, 170, 100, 25);
        passwordLabel.setFont(smallFont);
        loginCard.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(70, 195, 230, 35);
        passwordField.setCaretColor(textWhite);
        passwordField.setForeground(textWhite);
        passwordField.setBackground(fieldBg);
        passwordField.setFont(normalFont);
        passwordField.setEchoChar('•');
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        loginCard.add(passwordField);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setBounds(70, 240, 120, 25);
        showPass.setForeground(lightGray);
        showPass.setBackground(cardBg);
        showPass.setFocusPainted(false);
        showPass.setFont(smallFont);
        showPass.addActionListener(e -> {
            passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });
        loginCard.add(showPass);

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(70, 290, 230, 40);
        loginBtn.setForeground(textWhite);
        loginBtn.setBackground(primaryRed);
        loginBtn.setFont(buttonFont);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(borderAccent);
            }

            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(primaryRed);
            }
        });
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            UserLogin login = new UserLogin();
            boolean isAuthenticated = login.authenticate(user, pass);
            if (isAuthenticated) {
                new DashboardWindow();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials. Please try again.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        loginCard.add(loginBtn);

        JSeparator leftDivider = new JSeparator();
        leftDivider.setBounds(70, 350, 90, 1);
        leftDivider.setForeground(lightGray);
        loginCard.add(leftDivider);

        JLabel orLabel = new JLabel("OR");
        orLabel.setForeground(lightGray);
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orLabel.setBounds(160, 340, 40, 20);
        orLabel.setFont(smallFont);
        loginCard.add(orLabel);

        JSeparator rightDivider = new JSeparator();
        rightDivider.setBounds(200, 350, 100, 1);
        rightDivider.setForeground(lightGray);
        loginCard.add(rightDivider);

        JLabel createAccountLabel = new JLabel("CREATE AN ACCOUNT");
        createAccountLabel.setForeground(borderAccent);
        createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createAccountLabel.setBounds(70, 370, 230, 25);
        createAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new SignUpWindow();
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                createAccountLabel.setForeground(primaryRed);
            }

            public void mouseExited(MouseEvent e) {
                createAccountLabel.setForeground(borderAccent);
            }
        });
        loginCard.add(createAccountLabel);

        JLabel adminLabel = new JLabel("ADMIN ACCESS");
        adminLabel.setForeground(borderAccent);
        adminLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
        adminLabel.setBounds(215, 465, 150, 25);
        adminLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new AdminLogin().setVisible(true);
                dispose(); // close the LoginWindow
            }

            public void mouseEntered(MouseEvent e) {
                adminLabel.setForeground(primaryRed);
            }

            public void mouseExited(MouseEvent e) {
                adminLabel.setForeground(borderAccent);
            }
        });

        rightPanel.add(adminLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}
