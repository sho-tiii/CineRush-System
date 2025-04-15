import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    public LoginWindow() {
        setTitle("Login - CineRush");
        setSize(415, 300); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("CineRush Movie Ticketing");
        title.setBounds(100, 20, 250, 25); 
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

        JCheckBox showPass = new JCheckBox("Show");
        showPass.setBounds(285, 110, 60, 25);
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        add(showPass);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 160, 100, 30); 
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            // Authentication
            UserLogin login = new UserLogin();
            boolean isAuthenticated = login.authenticate(user, pass);

            if (isAuthenticated) {
                new DashboardWindow(); 
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login. Try again.");
            }
        });
        add(loginBtn);

        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBounds(150, 200, 100, 30); 
        signUpBtn.addActionListener(e -> {
            new SignUpWindow(); 
            dispose(); 
        });
        add(signUpBtn);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}
