import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class DashboardWindow extends JFrame {

    public DashboardWindow() {
        setTitle("CineRush - Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Choose a Cinema:");
        label.setBounds(130, 20, 150, 25);
        add(label);

        // Load cinemas from database
        ArrayList<Cinema> cinemas = fetchCinemasFromDatabase();

        int y = 60;
        for (Cinema cinema : cinemas) {
            JButton button = new JButton(cinema.name + " - " + cinema.movie);
            button.setBounds(100, y, 200, 30);
            button.addActionListener(e -> openSeatSelectionWindow(cinema.name, cinema.movie));
            add(button);
            y += 40;
            
        }

        setVisible(true);
    }

    // Method to open seat selection
    private void openSeatSelectionWindow(String cinema, String movie) {
        this.dispose();
        new SeatSelectionWindow(cinema, movie);
    }

    // Method to fetch cinema data from DB
    private ArrayList<Cinema> fetchCinemasFromDatabase() {
        ArrayList<Cinema> cinemaList = new ArrayList<>();
        Connection conn = DatabaseConnection.connect();

        try {
            String query = "SELECT * FROM cinemas";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String movie = rs.getString("movie");
                cinemaList.add(new Cinema(name, movie));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch cinemas.");
            e.printStackTrace();
        }

        return cinemaList;
    }

    // Helper class to hold cinema info
    class Cinema {
        String name;
        String movie;

        Cinema(String name, String movie) {
            this.name = name;
            this.movie = movie;
        }
    }

    public static void main(String[] args) {
        new DashboardWindow();
    }
}
