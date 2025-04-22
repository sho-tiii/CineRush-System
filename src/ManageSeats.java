import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageSeats extends JFrame {

    private String selectedCinema = "Cinema 1";
    private String selectedSchedule = "10:00 AM";
    private JPanel gridPanel;

    public ManageSeats() {
        setTitle("Manage Seats");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));

        JLabel titleLabel = new JLabel("Seat Management Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(178, 34, 34));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));
        add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout());
        JComboBox<String> cinemaBox = new JComboBox<>(new String[]{"Cinema 1", "Cinema 2", "Cinema 3"});
        JComboBox<String> scheduleBox = new JComboBox<>(new String[]{"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});

        cinemaBox.addActionListener(e -> {
            selectedCinema = (String) cinemaBox.getSelectedItem();
            updateSeatAvailability();
        });

        scheduleBox.addActionListener(e -> {
            selectedSchedule = (String) scheduleBox.getSelectedItem();
            updateSeatAvailability();
        });

        topPanel.add(new JLabel("Cinema:"));
        topPanel.add(cinemaBox);
        topPanel.add(new JLabel("Schedule:"));
        topPanel.add(scheduleBox);
        add(topPanel, BorderLayout.SOUTH);

        gridPanel = new JPanel(new GridLayout(5, 5, 10, 10));
        setupSeatButtons();
        add(gridPanel, BorderLayout.CENTER);

        updateSeatAvailability();
        setVisible(true);
    }

    private void setupSeatButtons() {
        String[] seats = {
            "A1", "A2", "A3", "A4", "A5",
            "B1", "B2", "B3", "B4", "B5",
            "C1", "C2", "C3", "C4", "C5",
            "D1", "D2", "D3", "D4", "D5",
            "E1", "E2", "E3", "E4", "E5"
        };

        for (String seat : seats) {
            JButton btn = new JButton(seat);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.addActionListener(e -> toggleSeat(btn));
            gridPanel.add(btn);
        }
    }

    private void updateSeatAvailability() {
        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
                btn.setEnabled(true);

                if (isSeatTaken(btn.getText())) {
                    btn.setBackground(Color.GRAY);
                }
            }
        }
    }

    private boolean isSeatTaken(String seatName) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM bookings WHERE cinema = ? AND schedule = ? AND seats = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedCinema);
            pst.setString(2, selectedSchedule);
            pst.setString(3, seatName);

            ResultSet rs = pst.executeQuery();
            boolean taken = rs.next();

            rs.close();
            pst.close();
            return taken;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void toggleSeat(JButton btn) {
        String seat = btn.getText();

        if (isSeatTaken(seat)) {
            // Unblock the seat (delete from bookings)
            if (removeSeat(seat)) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
        } else {
            // Block the seat (add to bookings)
            if (addSeat(seat)) {
                btn.setBackground(Color.GRAY);
                btn.setForeground(Color.WHITE);
            }
        }
    }

    private boolean addSeat(String seat) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO bookings (customer_name, cinema, seats, total_price, payment, change_amt, schedule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "Admin");
            pst.setString(2, selectedCinema);
            pst.setString(3, seat);
            pst.setInt(4, 0); 
            pst.setInt(5, 0); 
            pst.setInt(6, 0); 
            pst.setString(7, selectedSchedule);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean removeSeat(String seat) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "DELETE FROM bookings WHERE customer_name = 'Admin' AND cinema = ? AND schedule = ? AND seats = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedCinema);
            pst.setString(2, selectedSchedule);
            pst.setString(3, seat);
            int rows = pst.executeUpdate();
            pst.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new ManageSeats();
    }
}
