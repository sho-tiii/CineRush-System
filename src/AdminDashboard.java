import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard {
    public AdminDashboard() {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setSize(400, 500); // Increased window size for better spacing
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton manageMoviesButton = new JButton("Manage Movies");
        manageMoviesButton.setBounds(130, 30, 150, 40);
        frame.add(manageMoviesButton);

        manageMoviesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageMovies(); // Opens the Manage Movies window
                frame.dispose(); // Optional: closes the current dashboard window
            }
        });
        
        JButton manageSeatsButton = new JButton("View Seats");
        manageSeatsButton.setBounds(130, 80, 150, 40);
        frame.add(manageSeatsButton);

        JButton manageSchedulesButton = new JButton("Manage Schedules");
        manageSchedulesButton.setBounds(130, 130, 150, 40);
        frame.add(manageSchedulesButton);

        JButton setPricesButton = new JButton("Set Prices");
        setPricesButton.setBounds(130, 180, 150, 40);
        frame.add(setPricesButton);

        // Adjusting the last three buttons for more space
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(130, 230, 150, 40);
        frame.add(changePasswordButton);

        JButton transactionsButton = new JButton("Transactions");
        transactionsButton.setBounds(130, 280, 150, 40); // Adjusted spacing
        frame.add(transactionsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(130, 330, 150, 40); // Adjusted position
        frame.add(logoutButton);

        // Adding action listener for logoutButton inside the constructor
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MainScreen(); // Go back to the main screen
                frame.setVisible(false); // Close Admin Dashboard
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard(); // Create and display the Admin Dashboard
    }
}
