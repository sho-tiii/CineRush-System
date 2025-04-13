import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    public LoginWindow() {
        setTitle("Login - CineRush");
        setSize(415, 300); // Updated window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("CineRush Movie Ticketing");
        title.setBounds(100, 20, 250, 25); // Updated title position
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 25);
        add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(130, 70, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 110, 100, 25);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 110, 150, 25);
        add(passwordField);

        // Show Password checkbox
        JCheckBox showPass = new JCheckBox("Show");
        showPass.setBounds(285, 110, 60, 25);
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('•'); // Hide password
            }
        });
        add(showPass);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 160, 100, 30); // Updated button position
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            // Use UserLogin class for authentication
            UserLogin login = new UserLogin();
            boolean isAuthenticated = login.authenticate(user, pass);

            if (isAuthenticated) {
                new DashboardWindow(); // open dashboard
                dispose(); // close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login. Try again.");
            }
        });
        add(loginBtn);

        // Sign Up button
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBounds(150, 200, 100, 30); // Updated button position
        signUpBtn.addActionListener(e -> {
            new SignUpWindow(); // Open Sign Up window
            dispose(); // Close Login window
        });
        add(signUpBtn);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}
