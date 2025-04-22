import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class MovieScheduleAdminPanel extends JFrame {

    private JComboBox<String> cinemaBox;
    private JTextField titleField, priceField, cinemaNumField;
    private JTextField[] hourFields;
    private JTextField[] minFields;
    private JComboBox<String>[] ampmBoxes;

    // Updated color scheme with red and black
    private final Color bgColor = new Color(0x1F1F1F);  // Dark background
    private final Color textWhite = new Color(0xF9FAFB); // White text
    private final Color accentRed = new Color(0x991B1B); // Red accents
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);

    public MovieScheduleAdminPanel() {
        setTitle("🎬 Movie, Schedule & Price Management");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(bgColor);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 520, 520);
        panel.setBackground(bgColor);
        add(panel);

        // Cinema selection label and combo box
        JLabel cinemaLabel = new JLabel("Select a Cinema:");
        cinemaLabel.setFont(labelFont);
        cinemaLabel.setForeground(textWhite);
        cinemaLabel.setBounds(20, 20, 120, 25);
        panel.add(cinemaLabel);

        String[] cinemas = {"Cinema 1", "Cinema 2", "Cinema 3", "Cinema 4", "Cinema 5"};
        cinemaBox = new JComboBox<>(cinemas);
        cinemaBox.setBounds(150, 20, 120, 25);
        panel.add(cinemaBox);

        // Movie title label and text field
        JLabel titleLabel = new JLabel("Movie Title:");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(textWhite);
        titleLabel.setBounds(20, 60, 100, 25);
        panel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(150, 60, 180, 25);
        panel.add(titleField);

        // Cinema number label and text field (non-editable)
        JLabel cinemaNumLabel = new JLabel("Cinema No:");
        cinemaNumLabel.setFont(labelFont);
        cinemaNumLabel.setForeground(textWhite);
        cinemaNumLabel.setBounds(20, 100, 100, 25);
        panel.add(cinemaNumLabel);

        cinemaNumField = new JTextField();
        cinemaNumField.setBounds(150, 100, 50, 25);
        cinemaNumField.setEditable(false);
        panel.add(cinemaNumField);

        // Ticket price label and text field
        JLabel priceLabel = new JLabel("Ticket Price (₱):");
        priceLabel.setFont(labelFont);
        priceLabel.setForeground(textWhite);
        priceLabel.setBounds(20, 140, 120, 25);
        panel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(150, 140, 100, 25);
        panel.add(priceField);

        // Schedule label
        JLabel scheduleLabel = new JLabel("Schedules:");
        scheduleLabel.setFont(labelFont);
        scheduleLabel.setForeground(textWhite);
        scheduleLabel.setBounds(20, 180, 100, 25);
        panel.add(scheduleLabel);

        // Schedule input fields for hours, minutes, and AM/PM
        hourFields = new JTextField[4];
        minFields = new JTextField[4];
        ampmBoxes = new JComboBox[4];

        for (int i = 0; i < 4; i++) {
            hourFields[i] = new JTextField();
            hourFields[i].setBounds(20, 220 + (i * 35), 40, 25);
            panel.add(hourFields[i]);

            minFields[i] = new JTextField();
            minFields[i].setBounds(70, 220 + (i * 35), 40, 25);
            panel.add(minFields[i]);

            ampmBoxes[i] = new JComboBox<>(new String[]{"AM", "PM"});
            ampmBoxes[i].setBounds(120, 220 + (i * 35), 60, 25);
            panel.add(ampmBoxes[i]);
        }

        // Save changes button
        JButton saveButton = createStyledButton("Save Changes", 290, 400);
        panel.add(saveButton);

        // Return button
        JButton returnButton = createStyledButton("Return", 290, 440);
        panel.add(returnButton);

        // Listeners
        cinemaBox.addActionListener(e -> loadSelectedCinema());
        saveButton.addActionListener(e -> saveCinema());
        returnButton.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        // Initial setup
        cinemaBox.setSelectedIndex(0);
        loadSelectedCinema();

        setVisible(true);
    }

    // Method for creating styled buttons with red/black theme
    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 140, 30);
        button.setForeground(textWhite);
        button.setBackground(accentRed);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0x7B1F1F)); // Darker red when hovered
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(accentRed); // Original red when mouse exits
            }
        });

        return button;
    }

    // Method to load the selected cinema data
    private void loadSelectedCinema() {
        int selectedIndex = cinemaBox.getSelectedIndex();
        cinemaNumField.setText(String.valueOf(selectedIndex + 1));
        loadCinemaData(selectedIndex + 1);
    }

    // Method to load data from the database for a selected cinema
    private void loadCinemaData(int cinemaId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT movie, price, schedule1, schedule2, schedule3, schedule4 FROM cinemas WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, cinemaId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                titleField.setText(rs.getString("movie"));
                priceField.setText(rs.getString("price"));

                for (int i = 0; i < 4; i++) {
                    String time = rs.getString("schedule" + (i + 1));
                    if (time != null) {
                        String[] split = time.split(" ");
                        String[] hm = split[0].split(":");
                        hourFields[i].setText(hm[0]);
                        minFields[i].setText(hm[1]);
                        ampmBoxes[i].setSelectedItem(split[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to save updated cinema data to the database
    private void saveCinema() {
        int cinemaId = cinemaBox.getSelectedIndex() + 1;
        String movie = titleField.getText().trim();
        String price = priceField.getText().trim();
        String[] schedules = new String[4];

        for (int i = 0; i < 4; i++) {
            String hr = hourFields[i].getText();
            String min = minFields[i].getText();
            String ampm = (String) ampmBoxes[i].getSelectedItem();
            schedules[i] = hr + ":" + min + " " + ampm;
        }

        saveCinemaData(cinemaId, movie, price, schedules);
        JOptionPane.showMessageDialog(this, "Changes saved for Cinema " + cinemaId + "!");
    }

    // Method to save updated cinema data into the database
    private void saveCinemaData(int cinemaId, String movie, String price, String[] schedules) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE cinemas SET movie = ?, price = ?, schedule1 = ?, schedule2 = ?, schedule3 = ?, schedule4 = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, movie);
            pst.setString(2, price);
            for (int i = 0; i < 4; i++) {
                pst.setString(3 + i, schedules[i]);
            }
            pst.setInt(7, cinemaId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieScheduleAdminPanel::new);
    }
}
