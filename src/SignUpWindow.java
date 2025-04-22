import javax.swing.*;
import java.awt.*;

public class SignUpWindow extends JFrame {

    public SignUpWindow() {
        setTitle("Sign Up - CineRush");
        setSize(440, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        // Theme colors
        Color deepRed = new Color(0xC62828);
        Color bgBlack = new Color(0x1A1A1A);
        Color textWhite = new Color(0xF9FAFB);
        Color inputBg = new Color(0x333333);

        getContentPane().setBackground(bgBlack);

        JLabel title = new JLabel("Create Your CineRush Account");
        title.setBounds(90, 20, 300, 25);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(deepRed);
        add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 25);
        usernameLabel.setForeground(textWhite);
        add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(130, 70, 180, 25);
        usernameField.setBackground(inputBg);
        usernameField.setForeground(textWhite);
        usernameField.setCaretColor(textWhite);
        usernameField.setBorder(BorderFactory.createLineBorder(deepRed));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 110, 100, 25);
        passwordLabel.setForeground(textWhite);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 110, 150, 25);
        passwordField.setBackground(inputBg);
        passwordField.setForeground(textWhite);
        passwordField.setCaretColor(textWhite);
        passwordField.setBorder(BorderFactory.createLineBorder(deepRed));
        add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 150, 120, 25);
        confirmPasswordLabel.setForeground(textWhite);
        add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(170, 150, 150, 25);
        confirmPasswordField.setBackground(inputBg);
        confirmPasswordField.setForeground(textWhite);
        confirmPasswordField.setCaretColor(textWhite);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(deepRed));
        add(confirmPasswordField);

        JCheckBox showPassword = new JCheckBox("Show");
        showPassword.setBounds(285, 110, 60, 25);
        showPassword.setBackground(bgBlack);
        showPassword.setForeground(textWhite);
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        add(showPassword);

        JCheckBox showConfirm = new JCheckBox("Show");
        showConfirm.setBounds(325, 150, 60, 25);
        showConfirm.setBackground(bgBlack);
        showConfirm.setForeground(textWhite);
        showConfirm.addActionListener(e -> {
            confirmPasswordField.setEchoChar(showConfirm.isSelected() ? (char) 0 : '•');
        });
        add(showConfirm);

        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBounds(160, 200, 100, 30);
        signUpBtn.setBackground(deepRed);
        signUpBtn.setForeground(textWhite);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            } else if (!password.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
            } else {
                UserRegistration registration = new UserRegistration();
                boolean success = registration.register(username, password);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Account created successfully!");
                    new LoginWindow();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account. Username might already exist.");
                }
            }
        });
        add(signUpBtn);

        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(160, 240, 100, 25);
        returnBtn.setBackground(deepRed.darker());
        returnBtn.setForeground(textWhite);
        returnBtn.setFocusPainted(false);
        returnBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        returnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnBtn.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });
        add(returnBtn);

        setVisible(true);
    }

    public static void main(String[] args) {
        new SignUpWindow();
    }
}
