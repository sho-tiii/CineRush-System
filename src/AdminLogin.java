import javax.swing.*;

public class AdminLogin {
    public AdminLogin() {
        JFrame frame = new JFrame("Admin Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        frame.add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 30, 130, 25);
        frame.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        frame.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 130, 25);
        frame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 80, 25);
        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals("admin") && new String(passwordField.getPassword()).equals("1234")) {
                new AdminDashboard(); 
                frame.setVisible(false); 
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.");
            }
        });

        frame.add(loginButton);
        

        frame.setVisible(true);
    }
}
