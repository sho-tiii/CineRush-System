import javax.swing.*;

public class MainScreen {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cinema Ticketing System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Admin Button
        JButton adminButton = new JButton("I AM ADMIN");
        adminButton.setBounds(130, 100, 150, 40);
        frame.add(adminButton);

        // User Button
        JButton userButton = new JButton("I AM USER");
        userButton.setBounds(130, 160, 150, 40);  // Adjust position to avoid overlap
        frame.add(userButton);

        // Action listener for adminButton
        adminButton.addActionListener(e -> {
            new AdminLogin();  // Open Admin Login form
            frame.setVisible(false);  // Close the main screen
        });

        frame.setVisible(true);
    }
}
