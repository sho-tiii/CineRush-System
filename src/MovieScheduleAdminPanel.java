import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MovieScheduleAdminPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("🎬 Movie, Schedule & Price Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 520);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel cinemaLabel = new JLabel("Select a Cinema:");
        cinemaLabel.setBounds(20, 20, 120, 25);
        panel.add(cinemaLabel);

        String[] cinemas = {"Cinema 1", "Cinema 2", "Cinema 3", "Cinema 4", "Cinema 5"};
        JComboBox<String> cinemaBox = new JComboBox<>(cinemas);
        cinemaBox.setBounds(150, 20, 120, 25);
        panel.add(cinemaBox);

        JLabel titleLabel = new JLabel("Movie Title:");
        titleLabel.setBounds(20, 60, 100, 25);
        panel.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(150, 60, 180, 25);
        panel.add(titleField);

        JLabel cinemaNumLabel = new JLabel("Cinema No:");
        cinemaNumLabel.setBounds(20, 100, 100, 25);
        panel.add(cinemaNumLabel);

        JTextField cinemaNumField = new JTextField();
        cinemaNumField.setBounds(150, 100, 50, 25);
        cinemaNumField.setEditable(false);
        panel.add(cinemaNumField);

        JLabel priceLabel = new JLabel("Ticket Price (₱):");
        priceLabel.setBounds(20, 140, 120, 25);
        panel.add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(150, 140, 100, 25);
        panel.add(priceField);

        JLabel scheduleLabel = new JLabel("Schedules:");
        scheduleLabel.setBounds(20, 180, 100, 25);
        panel.add(scheduleLabel);

        JTextField[] hourFields = new JTextField[4];
        JTextField[] minFields = new JTextField[4];
        JComboBox<String>[] ampmBoxes = new JComboBox[4];

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

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBounds(290, 400, 140, 30);
        panel.add(saveButton);

        JButton returnButton = new JButton("Return");
        returnButton.setBounds(290, 440, 140, 30);
        panel.add(returnButton);

        cinemaBox.addActionListener(e -> {
            int selectedIndex = cinemaBox.getSelectedIndex();
            cinemaNumField.setText(String.valueOf(selectedIndex + 1));
            loadCinemaData(selectedIndex + 1, titleField, priceField, hourFields, minFields, ampmBoxes);
        });

        saveButton.addActionListener(e -> {
            int cinemaId = cinemaBox.getSelectedIndex() + 1;
            String movie = titleField.getText().trim();
            String price = priceField.getText().trim();
            String[] scheduleTimes = new String[4];

            for (int i = 0; i < 4; i++) {
                String hr = hourFields[i].getText();
                String min = minFields[i].getText();
                String ampm = (String) ampmBoxes[i].getSelectedItem();
                scheduleTimes[i] = hr + ":" + min + " " + ampm;
            }

            saveCinemaData(cinemaId, movie, price, scheduleTimes);
            JOptionPane.showMessageDialog(frame, "Changes saved for Cinema " + cinemaId + "!");
        });

        returnButton.addActionListener(e -> {
            frame.dispose();
            AdminDashboard.main(null);
        });

        frame.add(panel);
        frame.setVisible(true);
        cinemaBox.setSelectedIndex(0); // Load first cinema on start
    }

    private static void loadCinemaData(int cinemaId, JTextField titleField, JTextField priceField,
                                       JTextField[] hourFields, JTextField[] minFields, JComboBox<String>[] ampmBoxes) {
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
                    String[] split = time.split(" ");
                    String[] hm = split[0].split(":");
                    hourFields[i].setText(hm[0]);
                    minFields[i].setText(hm[1]);
                    ampmBoxes[i].setSelectedItem(split[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveCinemaData(int cinemaId, String movie, String price, String[] schedules) {
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
}
