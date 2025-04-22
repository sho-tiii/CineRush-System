import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

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
    private JLabel scheduleLabelLeft;
    private JLabel scheduleLabelRight;
    private String selectedSchedule = "10:00 AM";
    private JPanel gridPanel;

    // Colors
    private final Color redAccent = new Color(198, 40, 40);     // Red
    private final Color softBlack = new Color(60, 60, 60);       // Background
    private final Color lighterBlack = new Color(42, 42, 42);    // Panel
    private final Color textWhite = Color.WHITE;

    public SeatSelectionWindow(String cinema, String movie) {
        this.cinema = cinema;
        this.movie = movie;
        setTitle("CineRush - Seat Selection");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        // Header
        JLabel headerLabel = new JLabel("CineRush", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(textWhite);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(redAccent);
        headerLabel.setPreferredSize(new Dimension(getWidth(), 60));
        add(headerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(softBlack);

        // Seat Panel
        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
        seatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), cinema + " - " + movie));
        seatPanel.setBackground(softBlack);
        seatPanel.setPreferredSize(new Dimension(400, 400));
        setBorderTextColor(seatPanel, textWhite);

        // Grid for seats
        gridPanel = new JPanel(new GridLayout(5, 5, 10, 10));
        gridPanel.setBackground(softBlack);
        setupSeatButtons(gridPanel);

        // Info panel
        seatInfoLabel = createLabel("Selected: ");
        totalPriceLabel = createLabel("Total: ₱0");
        scheduleLabelLeft = createLabel("Schedule: " + selectedSchedule);
        JComboBox<String> scheduleComboBox = new JComboBox<>(new String[]{"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});
        scheduleComboBox.setBackground(lighterBlack);
        scheduleComboBox.setForeground(textWhite);
        scheduleComboBox.addActionListener(e -> {
            selectedSchedule = (String) scheduleComboBox.getSelectedItem();
            scheduleLabelLeft.setText("Schedule: " + selectedSchedule);
            scheduleLabelRight.setText("Schedule: " + selectedSchedule);
            updateSeatAvailability();
        });

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBackground(softBlack);
        infoPanel.add(seatInfoLabel);
        infoPanel.add(totalPriceLabel);
        infoPanel.add(scheduleLabelLeft);
        infoPanel.add(scheduleComboBox);

        seatPanel.add(gridPanel);
        seatPanel.add(Box.createVerticalStrut(10));
        seatPanel.add(infoPanel);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), "Payment"));
        paymentPanel.setBackground(softBlack);
        paymentPanel.setPreferredSize(new Dimension(400, 400));
        setBorderTextColor(paymentPanel, textWhite);

        seatInfoLabelRight = createLabel("Selected: ");
        totalPriceLabelRight = createLabel("Total: ₱0");
        scheduleLabelRight = createLabel("Schedule: " + selectedSchedule);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(softBlack);
        inputPanel.add(createLabel("Your Name:"));
        nameField = new JTextField();
        nameField.setBackground(lighterBlack);
        nameField.setForeground(textWhite);
        inputPanel.add(nameField);
        inputPanel.add(createLabel("Payment (₱):"));
        paymentField = new JTextField();
        paymentField.setBackground(lighterBlack);
        paymentField.setForeground(textWhite);
        inputPanel.add(paymentField);
        inputPanel.add(createLabel("Change:"));
        changeLabel = createLabel("₱0");
        inputPanel.add(changeLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(softBlack);
        JButton checkoutBtn = new JButton("Checkout");
        JButton returnBtn = new JButton("Return");
        styleButton(checkoutBtn, redAccent, textWhite);
        styleButton(returnBtn, lighterBlack, textWhite);
        checkoutBtn.addActionListener(e -> checkout());
        returnBtn.addActionListener(e -> {
            dispose();
            new DashboardWindow();
        });

        buttonPanel.add(checkoutBtn);
        buttonPanel.add(returnBtn);

        paymentPanel.add(seatInfoLabelRight);
        paymentPanel.add(totalPriceLabelRight);
        paymentPanel.add(scheduleLabelRight);
        paymentPanel.add(Box.createVerticalStrut(10));
        paymentPanel.add(inputPanel);
        paymentPanel.add(Box.createVerticalGlue());
        paymentPanel.add(buttonPanel);

        mainPanel.add(seatPanel);
        mainPanel.add(paymentPanel);
        add(mainPanel, BorderLayout.CENTER);

        updateSeatAvailability();
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textWhite);
        return label;
    }

    private void setBorderTextColor(JComponent component, Color color) {
        if (component.getBorder() instanceof javax.swing.border.TitledBorder border) {
            border.setTitleColor(color);
        }
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
    }

    private void setupSeatButtons(JPanel gridPanel) {
        String[] seatNames = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5", "C1", "C2", "C3", "C4", "C5", "D1", "D2", "D3", "D4", "D5", "E1", "E2", "E3", "E4", "E5"};
        for (String name : seatNames) {
            JButton seatBtn = new JButton(name);
            styleButton(seatBtn, lighterBlack, textWhite);
            seatBtn.setBorder(BorderFactory.createLineBorder(redAccent));
            seatBtn.addActionListener(e -> toggleSeat(seatBtn));
            gridPanel.add(seatBtn);
        }
    }

    private void updateSeatAvailability() {
        for (JButton btn : selectedSeats) {
            styleButton(btn, lighterBlack, textWhite);
        }
        selectedSeats.clear();
        updateSeatInfo();

        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton seatBtn) {
                seatBtn.setEnabled(true);
                styleButton(seatBtn, lighterBlack, textWhite);
                if (isSeatTaken(seatBtn.getText())) {
                    seatBtn.setBackground(Color.GRAY);
                    seatBtn.setForeground(Color.DARK_GRAY);
                    seatBtn.setEnabled(false);
                }
            }
        }
    }

    private boolean isSeatTaken(String seatName) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM bookings WHERE cinema = ? AND schedule = ? AND seats LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, cinema);
            pst.setString(2, selectedSchedule);
            pst.setString(3, "%" + seatName + "%");
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void toggleSeat(JButton seatBtn) {
        if (selectedSeats.contains(seatBtn)) {
            selectedSeats.remove(seatBtn);
            styleButton(seatBtn, lighterBlack, textWhite);
        } else {
            if (selectedSeats.size() < 5) {
                selectedSeats.add(seatBtn);
                styleButton(seatBtn, redAccent, textWhite);
            } else {
                JOptionPane.showMessageDialog(this, "Maximum of 5 seats only!");
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
            changeLabel.setText("₱" + change);
            saveBookingToDatabase(name, cinema, getSelectedSeatsString(), total, payment, change);
            updateSeatAvailabilityInDatabase();
            new ReceiptDialog(this, name, getSelectedSeatsString(), total, payment, change, cinema, movie, selectedSchedule);
            JOptionPane.showMessageDialog(this, "Transaction successful!\nThank you, " + name + "!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for payment.");
        }
    }

    private void saveBookingToDatabase(String customerName, String cinema, String selectedSeats, int total, int payment, int change) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO bookings (customer_name, cinema, seats, total_price, payment, change_amt, schedule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, customerName);
            pst.setString(2, cinema);
            pst.setString(3, selectedSeats);
            pst.setInt(4, total);
            pst.setInt(5, payment);
            pst.setInt(6, change);
            pst.setString(7, selectedSchedule);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving booking.");
        }
    }

    private void updateSeatAvailabilityInDatabase() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE seats_availability SET available = false WHERE seat_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            for (JButton seatBtn : selectedSeats) {
                pst.setString(1, seatBtn.getText());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating seat availability.");
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
}
