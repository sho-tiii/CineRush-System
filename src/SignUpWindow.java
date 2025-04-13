import javax.swing.*;
import java.awt.*;

public class SignUpWindow extends JFrame {

    public SignUpWindow() {
        setTitle("Sign Up - CineRush");
        setSize(440, 350); // Updated window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Create Your CineRush Account");
        title.setBounds(90, 20, 250, 25); // Updated title
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

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 150, 120, 25);
        add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(170, 150, 150, 25);
        add(confirmPasswordField);

        // Show Password Checkbox
        JCheckBox showPassword = new JCheckBox("Show");
        showPassword.setBounds(285, 110, 60, 25);
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        add(showPassword);

        // Show Confirm Password Checkbox
        JCheckBox showConfirm = new JCheckBox("Show");
        showConfirm.setBounds(325, 150, 60, 25);
        showConfirm.addActionListener(e -> {
            confirmPasswordField.setEchoChar(showConfirm.isSelected() ? (char) 0 : '•');
        });
        add(showConfirm);

        // Sign Up Button
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBounds(160, 200, 100, 30);
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

        // Return Button
        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(160, 240, 100, 25);
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
