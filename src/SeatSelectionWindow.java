import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeatSelectionWindow extends JFrame {

    private ArrayList<JButton> selectedSeats = new ArrayList<>();
    private JLabel seatInfoLabel;
    private JLabel totalPriceLabel;
    private JLabel seatInfoLabelRight;
    private JLabel totalPriceLabelRight;
    private JTextField nameField;
    private JTextField paymentField;
    private JLabel changeLabel;
    private final int seatPrice = 350;
    private String cinema;
    private String movie;

    public SeatSelectionWindow(String cinema, String movie) {
        this.cinema = cinema;
        this.movie = movie;
        setTitle("CineRush - Seat Selection");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Seat Panel
        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(null);
        seatPanel.setBounds(10, 10, 360, 380);

        JLabel titleLabel = new JLabel(cinema + " - " + movie);
        titleLabel.setBounds(100, 20, 200, 25);
        seatPanel.add(titleLabel);

        String[] seatNames = {
            "A1", "A2", "A3", "A4", "A5",
            "B1", "B2", "B3", "B4", "B5",
            "C1", "C2", "C3", "C4", "C5",
            "D1", "D2", "D3", "D4", "D5",
            "E1", "E2", "E3", "E4", "E5"
        };

        int x = 20, y = 60;
        for (int i = 0; i < seatNames.length; i++) {
            JButton seatBtn = new JButton(seatNames[i]);
            seatBtn.setBounds(x, y, 60, 30);
            seatBtn.addActionListener(e -> toggleSeat(seatBtn));
            seatPanel.add(seatBtn);

            x += 65;
            if ((i + 1) % 5 == 0) {
                x = 20;
                y += 40;
            }
        }

        seatInfoLabel = new JLabel("Selected: ");
        seatInfoLabel.setBounds(20, 250, 300, 25);
        seatPanel.add(seatInfoLabel);

        totalPriceLabel = new JLabel("Total: ₱0");
        totalPriceLabel.setBounds(20, 280, 150, 25);
        seatPanel.add(totalPriceLabel);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(null);
        paymentPanel.setBounds(390, 10, 330, 380);

        seatInfoLabelRight = new JLabel("Selected: ");
        seatInfoLabelRight.setBounds(20, 20, 300, 25);
        paymentPanel.add(seatInfoLabelRight);

        totalPriceLabelRight = new JLabel("Total: ₱0");
        totalPriceLabelRight.setBounds(20, 50, 150, 25);
        paymentPanel.add(totalPriceLabelRight);

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setBounds(20, 90, 100, 25);
        paymentPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 90, 180, 25);
        paymentPanel.add(nameField);

        JLabel paymentLabel = new JLabel("Payment (₱):");
        paymentLabel.setBounds(20, 130, 100, 25);
        paymentPanel.add(paymentLabel);

        paymentField = new JTextField();
        paymentField.setBounds(120, 130, 180, 25);
        paymentPanel.add(paymentField);

        changeLabel = new JLabel("Change: ₱0");
        changeLabel.setBounds(20, 170, 200, 25);
        paymentPanel.add(changeLabel);

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setBounds(100, 220, 120, 30);
        checkoutBtn.addActionListener(e -> checkout());
        paymentPanel.add(checkoutBtn);

        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(100, 270, 120, 30);
        returnBtn.addActionListener(e -> {
            dispose();
            new DashboardWindow();
        });
        paymentPanel.add(returnBtn);

        add(seatPanel);
        add(paymentPanel);
        setVisible(true);
    }

    private void toggleSeat(JButton seatBtn) {
        if (selectedSeats.contains(seatBtn)) {
            selectedSeats.remove(seatBtn);
            seatBtn.setBackground(null);
        } else {
            if (selectedSeats.size() < 5) {
                selectedSeats.add(seatBtn);
                seatBtn.setBackground(Color.LIGHT_GRAY);
            } else {
                JOptionPane.showMessageDialog(this, "Maximum of 5 seats only!");
                return;
            }
        }
        updateSeatInfo();
    }

    private void updateSeatInfo() {
        StringBuilder info = new StringBuilder("Selected: ");
        for (JButton btn : selectedSeats) {
            info.append(btn.getText()).append(" ");
        }

        int total = selectedSeats.size() * seatPrice;
        seatInfoLabel.setText(info.toString());
        seatInfoLabelRight.setText(info.toString());
        totalPriceLabel.setText("Total: ₱" + total);
        totalPriceLabelRight.setText("Total: ₱" + total);
    }

    private void checkout() {
        String name = nameField.getText();
        String paymentText = paymentField.getText();

        if (name.isEmpty() || paymentText.isEmpty() || selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete all fields and select at least one seat.");
            return;
        }

        try {
            int payment = Integer.parseInt(paymentText);
            int total = selectedSeats.size() * seatPrice;

            if (payment < total) {
                JOptionPane.showMessageDialog(this, "Insufficient payment!");
                return;
            }

            int change = payment - total;
            changeLabel.setText("Change: ₱" + change);

            saveBookingToDatabase(name, cinema, getSelectedSeatsString(), total, payment, change);

            // Updated: Pass cinema and movie to receipt
            new ReceiptDialog(this, name, getSelectedSeatsString(), total, payment, change, cinema, movie);

            JOptionPane.showMessageDialog(this, "Transaction successful!\nThank you, " + name + "!");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for payment.");
        }
    }

    private void saveBookingToDatabase(String customerName, String cinema, String selectedSeats, int total, int payment, int change) {
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
            return;
        }

        try {
            String sql = "INSERT INTO bookings (customer_name, cinema, seats, total_price, payment, change_amt) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, customerName);
            pst.setString(2, cinema);
            pst.setString(3, selectedSeats);
            pst.setInt(4, total);
            pst.setInt(5, payment);
            pst.setInt(6, change);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving booking.");
        }
    }

    private String getSelectedSeatsString() {
        StringBuilder seats = new StringBuilder();
        for (JButton btn : selectedSeats) {
            seats.append(btn.getText()).append(" ");
        }
        return seats.toString().trim();
    }

    public static void main(String[] args) {
        new SeatSelectionWindow("Cinema 1", "Avengers");
    }

    // ✅ Receipt Dialog updated to include Cinema and Movie
    public class ReceiptDialog extends JDialog {

        public ReceiptDialog(JFrame parent, String customerName, String selectedSeats, int totalAmount, int payment, int change, String cinema, String movie) {
            super(parent, "Receipt", true);
            setSize(300, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel receiptPanel = new JPanel();
            receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));

            receiptPanel.add(new JLabel("🎬 CineRush Movie Ticketing"));
            receiptPanel.add(new JLabel("Customer: " + customerName));
            receiptPanel.add(new JLabel("Cinema: " + cinema));
            receiptPanel.add(new JLabel("Movie: " + movie));
            receiptPanel.add(new JLabel("Seats: " + selectedSeats));
            receiptPanel.add(new JLabel("Total: ₱" + totalAmount));
            receiptPanel.add(new JLabel("Payment: ₱" + payment));
            receiptPanel.add(new JLabel("Change: ₱" + change));

            add(receiptPanel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            add(closeButton, BorderLayout.SOUTH);

            setVisible(true);
        }
    }
}
