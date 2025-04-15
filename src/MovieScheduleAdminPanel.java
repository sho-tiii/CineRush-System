import javax.swing.*;

public class MovieScheduleAdminPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("🎬 Movie, Schedule & Price Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 520);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Cinema Dropdown
        JLabel cinemaLabel = new JLabel("Select a Cinema:");
        cinemaLabel.setBounds(20, 20, 120, 25);
        panel.add(cinemaLabel);

        String[] cinemas = {"Cinema 1", "Cinema 2", "Cinema 3", "Cinema 4", "Cinema 5"};
        JComboBox<String> cinemaBox = new JComboBox<>(cinemas);
        cinemaBox.setBounds(150, 20, 120, 25);
        panel.add(cinemaBox);

        // Movie Title
        JLabel titleLabel = new JLabel("Movie Title:");
        titleLabel.setBounds(20, 60, 100, 25);
        panel.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(150, 60, 180, 25);
        panel.add(titleField);

        JButton editTitleBtn = new JButton("Edit");
        editTitleBtn.setBounds(340, 60, 70, 25);
        panel.add(editTitleBtn);

        // Cinema Number
        JLabel cinemaNumLabel = new JLabel("Cinema No:");
        cinemaNumLabel.setBounds(20, 100, 100, 25);
        panel.add(cinemaNumLabel);

        JTextField cinemaNumField = new JTextField();
        cinemaNumField.setBounds(150, 100, 50, 25);
        cinemaNumField.setEditable(false);
        panel.add(cinemaNumField);

        // Ticket Price
        JLabel priceLabel = new JLabel("Ticket Price (₱):");
        priceLabel.setBounds(20, 140, 120, 25);
        panel.add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(150, 140, 100, 25);
        panel.add(priceField);

        JButton editPriceBtn = new JButton("Edit");
        editPriceBtn.setBounds(260, 140, 70, 25);
        panel.add(editPriceBtn);

        // Schedule Label
        JLabel scheduleLabel = new JLabel("Schedules:");
        scheduleLabel.setBounds(20, 180, 100, 25);
        panel.add(scheduleLabel);

        // Schedule Inputs
        JTextField[] hourFields = new JTextField[4];
        JTextField[] minFields = new JTextField[4];
        @SuppressWarnings("unchecked")
        JComboBox<String>[] ampmBoxes = (JComboBox<String>[]) new JComboBox<?>[4];
        JButton[] editButtons = new JButton[4];

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

            editButtons[i] = new JButton("Edit");
            editButtons[i].setBounds(190, 220 + (i * 35), 70, 25);
            panel.add(editButtons[i]);
        }

        // Save Button
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBounds(290, 400, 140, 30);
        panel.add(saveButton);

        // Return Button
        JButton returnButton = new JButton("Return");
        returnButton.setBounds(290, 440, 140, 30);
        panel.add(returnButton);

        // Cinema Data: Storing movie titles, prices, and schedules for Cinema 1 to 5
        String[] movieTitles = {"Avengers", "Barbie", "Oppenheimer", "John Wick", "Spider Man"};
        String[] prices = {"350", "350", "350", "350", "350"};
        String[][] schedules = {
                {"10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM"},
                {"10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM"},
                {"10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM"},
                {"10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM"},
                {"10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM"}
        };

        // Update the movie, price, and schedule based on the selected cinema
        cinemaBox.addActionListener(e -> {
            int selectedIndex = cinemaBox.getSelectedIndex();

            cinemaNumField.setText(String.valueOf(selectedIndex + 1));
            titleField.setText(movieTitles[selectedIndex]);
            priceField.setText(prices[selectedIndex]);

            for (int i = 0; i < 4; i++) {
                String[] timeSplit = schedules[selectedIndex][i].split(" ");
                String[] time = timeSplit[0].split(":");
                hourFields[i].setText(time[0]);
                minFields[i].setText(time[1]);
                ampmBoxes[i].setSelectedItem(timeSplit[1]);
            }
        });

        // Initial load for Cinema 1
        cinemaBox.setSelectedIndex(0);

        // SAVE ACTION
        saveButton.addActionListener(e -> {
            int selectedIndex = cinemaBox.getSelectedIndex();

            movieTitles[selectedIndex] = titleField.getText();
            prices[selectedIndex] = priceField.getText();

            // Schedule gathering
            StringBuilder scheduleText = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                String hr = hourFields[i].getText();
                String min = minFields[i].getText();
                String ampm = (String) ampmBoxes[i].getSelectedItem();
                schedules[selectedIndex][i] = hr + ":" + min + " " + ampm;
                scheduleText.append("• ").append(hr).append(":").append(min).append(" ").append(ampm).append("\n");
            }

            // Show updated details
            String message = "Changes Saved!\n\n"
                    + "Movie Title: " + movieTitles[selectedIndex] + "\n"
                    + "Cinema " + (selectedIndex + 1) + "\n"
                    + "Price: ₱" + prices[selectedIndex] + "\n"
                    + "Schedules:\n" + scheduleText;

            JOptionPane.showMessageDialog(frame, message, "Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
        });

        // RETURN ACTION (go back to AdminDashboard)
        returnButton.addActionListener(e -> {
            frame.dispose(); // close current window
            AdminDashboard.main(null); // open Admin Dashboard again
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
