import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JLabel;
import java.util.Map;
import java.util.ArrayList;
import java.sql.*;
import java.util.Vector;

public class ManageCinema extends JFrame {
    // Colors - matching the main app colors
    private final Color bgColor = new Color(0x800000);       // Maroon background
    private Color primaryRed = new Color(0xE11D48);    // Vibrant crimson
    private final Color accentGold = new Color(0xFACC15);    // Bright gold
    private final Color textWhite = new Color(0xF9FAFB);     // Off-white for text
    private final Color accentTeal = new Color(0x0EA5E9);    // Bright teal accent
    
    private Color panelBg = new Color(0x2F3B52);       // Lighter panel background
    private final Color fieldBg = new Color(0x374151);       // Input field background
    private final Color lightAccent = new Color(0xD1D5DB);   // Light accent for details
    private final Color cardBg = new Color(0x293548);        // Card background
    
    // Fonts
    private final Font headerFont = new Font("Segoe UI", Font.PLAIN, 25);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);

    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/cinerush_db";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private JTextField titleField;
    private JTextField cinemaNumField;
    private JTextField priceField;
    private JTextField[] hourFields;
    private JTextField[] minFields;
    private JComboBox<String>[] ampmBoxes;
    
    // Seat management components
    private final JPanel seatManagementPanel;
    private final JPanel seatGridPanel;
    private JComboBox<String> scheduleSelector;
    private final Map<String, JButton> seatButtons = new HashMap<>();
    private int currentCinemaId = 1;
    private String currentSchedule = "";

    public ManageCinema() {
        // Initialize database connection
        initDatabase();
        
        setTitle("CineRush - Manage Cinema ");
        setSize(900, 700);  // Increased height to accommodate new layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
        setLayout(null);  // Using null layout for simple positioning
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        // Main panel with all content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(50, 30, 800, 620);
        mainPanel.setBackground(cardBg);
        mainPanel.setBorder(BorderFactory.createLineBorder(textWhite, 1));
        add(mainPanel);

        // Panel title
        JLabel panelTitle = new JLabel("            MOVIE CINEMA ");
        panelTitle.setForeground(textWhite);
        panelTitle.setFont(headerFont);
        panelTitle.setBounds(250, 20, 320, 30);
        mainPanel.add(panelTitle);
        
        // Separator
        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setBounds(200, 55, 400, 1);
        titleSeparator.setForeground(accentTeal);
        mainPanel.add(titleSeparator);

        // Cinema selection
        JLabel cinemaLabel = new JLabel("SELECT CINEMA:");
        cinemaLabel.setForeground(lightAccent);
        cinemaLabel.setBounds(40, 80, 120, 25);
        cinemaLabel.setFont(smallFont);
        mainPanel.add(cinemaLabel);
        
        String[] cinemas = {"Cinema 1", "Cinema 2", "Cinema 3", "Cinema 4", "Cinema 5"};
        JComboBox<String> cinemaBox = new JComboBox<>(cinemas);
        cinemaBox.setBounds(170, 80, 160, 30);
        cinemaBox.setBackground(fieldBg);
        cinemaBox.setForeground(textWhite);
        cinemaBox.setFont(normalFont);
        mainPanel.add(cinemaBox);

        // Movie title
        JLabel titleLabel = new JLabel("MOVIE TITLE:");
        titleLabel.setForeground(lightAccent);
        titleLabel.setBounds(40, 120, 120, 25);
        titleLabel.setFont(smallFont);
        mainPanel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(170, 120, 400, 30);
        titleField.setCaretColor(textWhite);
        titleField.setForeground(textWhite);
        titleField.setBackground(fieldBg);
        titleField.setFont(normalFont);
        titleField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        mainPanel.add(titleField);

        // Cinema number
        JLabel cinemaNumLabel = new JLabel("CINEMA NO:");
        cinemaNumLabel.setForeground(lightAccent);
        cinemaNumLabel.setBounds(40, 160, 120, 25);
        cinemaNumLabel.setFont(smallFont);
        mainPanel.add(cinemaNumLabel);

        cinemaNumField = new JTextField();
        cinemaNumField.setBounds(170, 160, 60, 30);
        cinemaNumField.setCaretColor(textWhite);
        cinemaNumField.setForeground(textWhite);
        cinemaNumField.setBackground(fieldBg);
        cinemaNumField.setFont(normalFont);
        cinemaNumField.setEditable(false);
        cinemaNumField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        mainPanel.add(cinemaNumField);

        // Ticket price
        JLabel priceLabel = new JLabel("TICKET PRICE (₱):");
        priceLabel.setForeground(lightAccent);
        priceLabel.setBounds(40, 200, 120, 25);
        priceLabel.setFont(smallFont);
        mainPanel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(170, 200, 100, 30);
        priceField.setCaretColor(textWhite);
        priceField.setForeground(textWhite);
        priceField.setBackground(fieldBg);
        priceField.setFont(normalFont);
        priceField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        mainPanel.add(priceField);

        // Schedules
        JLabel schedulesLabel = new JLabel("SCHEDULES:");
        schedulesLabel.setForeground(lightAccent);
        schedulesLabel.setBounds(40, 240, 120, 25);
        schedulesLabel.setFont(smallFont);
        mainPanel.add(schedulesLabel);

        hourFields = new JTextField[4];
        minFields = new JTextField[4];
        ampmBoxes = new JComboBox[4];
        
        JLabel[] scheduleNumbers = new JLabel[4];
        String[] schedLabels = {"1st Show:", "2nd Show:", "3rd Show:", "4th Show:"};

        for (int i = 0; i < 4; i++) {
            scheduleNumbers[i] = new JLabel(schedLabels[i]);
            scheduleNumbers[i].setForeground(textWhite);
            scheduleNumbers[i].setBounds(60, 275 + (i * 35), 70, 25);
            scheduleNumbers[i].setFont(smallFont);
            mainPanel.add(scheduleNumbers[i]);
            
            hourFields[i] = new JTextField();
            hourFields[i].setBounds(140, 275 + (i * 35), 40, 30);
            hourFields[i].setCaretColor(textWhite);
            hourFields[i].setForeground(textWhite);
            hourFields[i].setBackground(fieldBg);
            hourFields[i].setFont(normalFont);
            hourFields[i].setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            mainPanel.add(hourFields[i]);
            
            JLabel colonLabel = new JLabel(":");
            colonLabel.setForeground(textWhite);
            colonLabel.setBounds(183, 275 + (i * 35), 10, 30);
            colonLabel.setFont(normalFont);
            mainPanel.add(colonLabel);

            minFields[i] = new JTextField();
            minFields[i].setBounds(190, 275 + (i * 35), 40, 30);
            minFields[i].setCaretColor(textWhite);
            minFields[i].setForeground(textWhite);
            minFields[i].setBackground(fieldBg);
            minFields[i].setFont(normalFont);
            minFields[i].setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            mainPanel.add(minFields[i]);

            ampmBoxes[i] = new JComboBox<>(new String[]{"AM", "PM"});
            ampmBoxes[i].setBounds(240, 275 + (i * 35), 60, 30);
            ampmBoxes[i].setBackground(fieldBg);
            ampmBoxes[i].setForeground(textWhite);
            ampmBoxes[i].setFont(smallFont);
            mainPanel.add(ampmBoxes[i]);
        }

        // ================== Seat Management Panel ======================
        seatManagementPanel = new JPanel();
        seatManagementPanel.setLayout(null);
        seatManagementPanel.setBounds(350, 265, 400, 240);
        seatManagementPanel.setBackground(fieldBg); // Darker red for contrast
        seatManagementPanel.setBorder(BorderFactory.createLineBorder(textWhite, 1));
        mainPanel.add(seatManagementPanel);
        
        JLabel seatManagementTitle = new JLabel("SEAT MANAGEMENT");
        seatManagementTitle.setForeground(textWhite);
        seatManagementTitle.setFont(headerFont);
        seatManagementTitle.setBounds(120, 10, 200, 30);
        seatManagementPanel.add(seatManagementTitle);
        
        // Schedule selector for seat management
        JLabel scheduleLabel = new JLabel("Select Schedule:");
        scheduleLabel.setForeground(textWhite);
        scheduleLabel.setFont(normalFont);
        scheduleLabel.setBounds(20, 50, 120, 25);
        seatManagementPanel.add(scheduleLabel);
        
        scheduleSelector = new JComboBox<>(new String[]{"Select Schedule"});
        scheduleSelector.setBounds(145, 50, 235, 25);
        scheduleSelector.setBackground(fieldBg);
        scheduleSelector.setForeground(textWhite);
        scheduleSelector.setFont(normalFont);
        seatManagementPanel.add(scheduleSelector);
        
        // Seat grid for visualization - now bigger
        seatGridPanel = new JPanel();
        seatGridPanel.setLayout(new GridLayout(5, 5, 5, 5));
        seatGridPanel.setBounds(20, 85, 360, 100);
        seatGridPanel.setBackground(new Color(0x420202));
        seatManagementPanel.add(seatGridPanel);
        
        // Reset seats button
        JButton resetSeatsButton = new JButton("RESET ALL SEATS");
        resetSeatsButton.setBounds(20, 195, 360, 30);
        resetSeatsButton.setBackground(primaryRed);
        resetSeatsButton.setForeground(textWhite);
        resetSeatsButton.setFont(normalFont);
        resetSeatsButton.setFocusPainted(false);
        resetSeatsButton.setBorderPainted(false);
        resetSeatsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetSeatsButton.addActionListener(e -> resetAllSeats());
        seatManagementPanel.add(resetSeatsButton);
        
        // Buttons at the bottom
        JButton saveButton = new JButton("SAVE CHANGES");
        saveButton.setBounds(250, 530, 150, 40);
        saveButton.setForeground(textWhite);
        saveButton.setBackground(primaryRed);  // Dark red
        saveButton.setFont(buttonFont);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(primaryRed);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(new Color(0x630403));
            }
        });
        mainPanel.add(saveButton);

        JButton returnButton = new JButton("RETURN TO DASHBOARD");
        returnButton.setBounds(420, 530, 200, 40);
        returnButton.setForeground(textWhite);
        returnButton.setBackground(panelBg);
        returnButton.setFont(normalFont);
        returnButton.setFocusPainted(false);
        returnButton.setBorderPainted(false);
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                returnButton.setBackground(accentTeal);  // Lighter shade on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                returnButton.setBackground(panelBg);
            }
        });
        mainPanel.add(returnButton);

        // Add action listeners
        cinemaBox.addActionListener(e -> {
            int selectedIndex = cinemaBox.getSelectedIndex();
            currentCinemaId = selectedIndex + 1;
            cinemaNumField.setText(String.valueOf(currentCinemaId));
            loadCinemaData(currentCinemaId);
            updateScheduleDropdown();
        });

        saveButton.addActionListener(e -> {
            int cinemaId = cinemaBox.getSelectedIndex() + 1;
            String movie = titleField.getText().trim();
            String price = priceField.getText().trim();
            String[] scheduleTimes = new String[4];

            for (int i = 0; i < 4; i++) {
                String hr = hourFields[i].getText();
                String min = minFields[i].getText();
                if (!hr.isEmpty() && !min.isEmpty()) {
                    String ampm = (String) ampmBoxes[i].getSelectedItem();
                    scheduleTimes[i] = hr + ":" + min + " " + ampm;
                } else {
                    scheduleTimes[i] = "";
                }
            }

            saveCinemaData(cinemaId, movie, price, scheduleTimes);
            updateScheduleDropdown();
            JOptionPane.showMessageDialog(this, "Changes saved for Cinema " + cinemaId + "!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        returnButton.addActionListener(e -> {
            dispose(); // Close current window
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.setVisible(true);
        });
        
        // Schedule selector for seat management action listener
        scheduleSelector.addActionListener(e -> {
            if (scheduleSelector.getSelectedIndex() > 0) {
                currentSchedule = (String) scheduleSelector.getSelectedItem();
                loadSeatData();
            }
        });

        // Initialize with first cinema
        cinemaBox.setSelectedIndex(0);
        initSeatGrid();
        
        setVisible(true);
    }
    
    private void initDatabase() {
    conn = DatabaseConnection.connect();
    if (conn == null) {
        JOptionPane.showMessageDialog(this, "Database connection failed.",
                                      "Database Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    createTablesIfNotExist();
}


    
    private void createTablesIfNotExist() {
        try {
            Statement stmt = conn.createStatement();
            
            // Create cinema table if it doesn't exist
            String createCinemaTable = "CREATE TABLE IF NOT EXISTS cinema (" +
                                       "id INT PRIMARY KEY," +
                                       "movie_title VARCHAR(255)," +
                                       "price DECIMAL(10,2)" +
                                       ")";
            stmt.executeUpdate(createCinemaTable);
            
            // Create cinema_schedule table if it doesn't exist
            String createScheduleTable = "CREATE TABLE IF NOT EXISTS cinema_schedule (" +
                                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                                         "cinema_id INT," +
                                         "schedule_time VARCHAR(50)," +
                                         "FOREIGN KEY (cinema_id) REFERENCES cinema(id)" +
                                         ")";
            stmt.executeUpdate(createScheduleTable);
            
            // Check if cinema data exists, insert default data if not
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM cinema");
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                // Insert default data for 5 cinemas
                for (int i = 1; i <= 5; i++) {
                    String defaultMovie = "Movie " + i;
                    String insertCinema = "INSERT INTO cinema (id, movie_title, price) VALUES (" + i + ", '" + defaultMovie + "', 350.00)";
                    stmt.executeUpdate(insertCinema);
                    
                    // Insert default schedules
                    String[] defaultSchedules = {"10:00 AM", "1:30 PM", "4:45 PM", "8:00 PM"};
                    for (String schedule : defaultSchedules) {
                        String insertSchedule = "INSERT INTO cinema_schedule (cinema_id, schedule_time) VALUES (" + i + ", '" + schedule + "')";
                        stmt.executeUpdate(insertSchedule);
                    }
                }
            }
            
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error creating database tables: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initSeatGrid() {
        seatGridPanel.removeAll();
        seatButtons.clear();
        
        String[] seatNames = {
            "A1", "A2", "A3", "A4", "A5",
            "B1", "B2", "B3", "B4", "B5",
            "C1", "C2", "C3", "C4", "C5",
            "D1", "D2", "D3", "D4", "D5",
            "E1", "E2", "E3", "E4", "E5"
        };
        
        for (String name : seatNames) {
            JButton seatBtn = new JButton();
            seatBtn.setPreferredSize(new Dimension(30, 20));
            seatBtn.setMargin(new Insets(1, 1, 1, 1));
            seatBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
            seatBtn.setText(name);
            seatBtn.setBackground(Color.WHITE);  // Available seats are WHITE
            seatBtn.setForeground(Color.BLACK);
            seatBtn.setBorderPainted(true);
            seatBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            seatBtn.setFocusPainted(false);
            
            // Add click listener to toggle seat status for demonstration
            seatBtn.addActionListener(e -> {
                if (currentSchedule.isEmpty() || currentSchedule.equals("Select Schedule")) {
                    JOptionPane.showMessageDialog(this, "Please select a schedule first!", 
                                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Toggle seat status (for admin purposes only, not affecting actual bookings)
                if (seatBtn.getBackground().equals(Color.WHITE)) {
                    // Mark as booked (simulating a manual bookings by admin)
                    int confirm = JOptionPane.showConfirmDialog(this, 
                          "Do you want to mark this seat as booked?", 
                          "Confirm Booking", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        seatBtn.setBackground(Color.RED);
                        markSeatAsBooked(name);
                    }
                } else {
                    // Mark as available (removing bookings)
                    int confirm = JOptionPane.showConfirmDialog(this, 
                          "Do you want to mark this seat as available?", 
                          "Confirm Unbookings", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        seatBtn.setBackground(Color.WHITE);
                        markSeatAsAvailable(name);
                    }
                }
            });
            
            seatButtons.put(name, seatBtn);
            seatGridPanel.add(seatBtn);
        }
        
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }
    
    private void markSeatAsBooked(String seat) {
        try {
            // Check if there's an existing bookings for this seat and schedule
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT id FROM bookings WHERE cinema = ? AND schedule = ? AND seats LIKE ?");
            checkStmt.setInt(1, currentCinemaId);
            checkStmt.setString(2, currentSchedule);
            checkStmt.setString(3, "%" + seat + "%");
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                // If no bookings exists, create one for admin purposes
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO bookings (customer_name, cinema, seats, total_price, payment, change_amt, booking_time, schedule) " +
                    "VALUES ('Admin Booking', ?, ?, ?, ?, 0, NOW(), ?)");
                insertStmt.setInt(1, currentCinemaId);
                insertStmt.setString(2, seat);
                
                // Get the price from cinema table
                double price = 0;
                PreparedStatement priceStmt = conn.prepareStatement("SELECT price FROM cinema WHERE id = ?");
                priceStmt.setInt(1, currentCinemaId);
                ResultSet priceRs = priceStmt.executeQuery();
                if (priceRs.next()) {
                    price = priceRs.getDouble("price");
                }
                priceRs.close();
                priceStmt.close();
                
                insertStmt.setDouble(3, price);
                insertStmt.setDouble(4, price);  // Payment = price
                insertStmt.setString(5, currentSchedule);
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            
            rs.close();
            checkStmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markSeatAsAvailable(String seat) {
        try {
            // Remove the bookings for this seat
            PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM bookings WHERE cinema = ? AND schedule = ? AND seats = ?");
            deleteStmt.setInt(1, currentCinemaId);
            deleteStmt.setString(2, currentSchedule);
            deleteStmt.setString(3, seat);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSeatData() {
        if (currentSchedule.isEmpty() || currentSchedule.equals("Select Schedule")) {
            return;
        }
        
        // Reset all seats to available first
        for (JButton btn : seatButtons.values()) {
            btn.setBackground(Color.WHITE);
        }
        
        try {
            // Get all booked seats for this cinema and schedule
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT seats FROM bookings WHERE cinema = ? AND schedule = ?");
            stmt.setInt(1, currentCinemaId);
            stmt.setString(2, currentSchedule);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String bookedSeats = rs.getString("seats");
                // Handle cases where seats might be stored as comma-separated list
                if (bookedSeats.contains(",")) {
                    String[] seats = bookedSeats.split(",");
                    for (String seat : seats) {
                        JButton seatBtn = seatButtons.get(seat.trim());
                        if (seatBtn != null) {
                            seatBtn.setBackground(Color.RED);
                        }
                    }
                } else {
                    // Single seat
                    JButton seatBtn = seatButtons.get(bookedSeats.trim());
                    if (seatBtn != null) {
                        seatBtn.setBackground(Color.RED);
                    }
                }
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading seat data: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetAllSeats() {
        if (currentSchedule.isEmpty() || currentSchedule.equals("Select Schedule")) {
            JOptionPane.showMessageDialog(this, "Please select a schedule first.", 
                                         "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                      "Are you sure you want to reset all seats for Cinema " + currentCinemaId + 
                      " - " + currentSchedule + "?\nThis will remove all bookings for this schedule.", 
                      "Confirm Reset", JOptionPane.YES_NO_OPTION);
                      
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete all bookings for this cinema and schedule
                PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM bookings WHERE cinema = ? AND schedule = ?");
                stmt.setInt(1, currentCinemaId);
                stmt.setString(2, currentSchedule);
                int rowsAffected = stmt.executeUpdate();
                stmt.close();
                
                // Reset seat display
                for (JButton btn : seatButtons.values()) {
                    btn.setBackground(Color.WHITE);
                }
                
                JOptionPane.showMessageDialog(this, 
                                            rowsAffected + " bookings deleted. All seats are now available.", 
                                            "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error resetting seats: " + e.getMessage(), 
                                             "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateScheduleDropdown() {
        scheduleSelector.removeAllItems();
        scheduleSelector.addItem("Select Schedule");
        
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT schedule_time FROM cinema_schedule WHERE cinema_id = ? ORDER BY schedule_time");
            stmt.setInt(1, currentCinemaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String schedule = rs.getString("schedule_time");
                if (schedule != null && !schedule.isEmpty()) {
                    scheduleSelector.addItem(schedule);
                }
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadCinemaData(int cinemaId) {
        try {
            // Get cinema details
            PreparedStatement cinemaStmt = conn.prepareStatement(
                "SELECT movie_title, price FROM cinema WHERE id = ?");
            cinemaStmt.setInt(1, cinemaId);
            ResultSet cinemaRs = cinemaStmt.executeQuery();
            
            if (cinemaRs.next()) {
                titleField.setText(cinemaRs.getString("movie_title"));
                priceField.setText(cinemaRs.getString("price"));
            }
            
            cinemaRs.close();
            cinemaStmt.close();
            
            // Get schedules
            PreparedStatement scheduleStmt = conn.prepareStatement(
                "SELECT schedule_time FROM cinema_schedule WHERE cinema_id = ? ORDER BY schedule_time");
            scheduleStmt.setInt(1, cinemaId);
            ResultSet scheduleRs = scheduleStmt.executeQuery();
            
            // Reset all schedule fields
            for (int i = 0; i < 4; i++) {
                hourFields[i].setText("");
                minFields[i].setText("");
                ampmBoxes[i].setSelectedItem("AM");
            }
            
            // Fill in schedule fields
            int index = 0;
            while (scheduleRs.next() && index < 4) {
                String scheduleTime = scheduleRs.getString("schedule_time");
                if (scheduleTime != null && !scheduleTime.isEmpty()) {
                    String[] parts = scheduleTime.split(" ");
                    if (parts.length >= 2) {
                        String[] timeParts = parts[0].split(":");
                        if (timeParts.length >= 2) {
                            hourFields[index].setText(timeParts[0]);
                            minFields[index].setText(timeParts[1]);
                            ampmBoxes[index].setSelectedItem(parts[1]);
                        }
                    }
                }
                index++;
            }
            
            scheduleRs.close();
            scheduleStmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cinema data: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCinemaData(int cinemaId, String movie, String price, String[] schedules) {
        try {
            // Begin transaction
            conn.setAutoCommit(false);
            
            try {
                // Update cinema information
                PreparedStatement updateCinema = conn.prepareStatement(
                    "UPDATE cinema SET movie_title = ?, price = ? WHERE id = ?");
                updateCinema.setString(1, movie);
                updateCinema.setString(2, price);
                updateCinema.setInt(3, cinemaId);
                updateCinema.executeUpdate();
                updateCinema.close();
                
                // Delete existing schedules
                PreparedStatement deleteSchedules = conn.prepareStatement(
                    "DELETE FROM cinema_schedule WHERE cinema_id = ?");
                deleteSchedules.setInt(1, cinemaId);
                deleteSchedules.executeUpdate();
                deleteSchedules.close();
                
                // Insert new schedules
                PreparedStatement insertSchedule = conn.prepareStatement(
                    "INSERT INTO cinema_schedule (cinema_id, schedule_time) VALUES (?, ?)");
                
                for (String schedule : schedules) {
                    if (schedule != null && !schedule.isEmpty()) {
                        insertSchedule.setInt(1, cinemaId);
                        insertSchedule.setString(2, schedule);
                        insertSchedule.executeUpdate();
                    }
                }
                insertSchedule.close();
                
                // Commit transaction
                conn.commit();
                
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                throw e;
            } finally {
                // Restore auto-commit
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving cinema data: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // This method helps view all current bookings for the selected cinema
    private void viewBookings() {
        if (currentCinemaId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a cinema first.", 
                                         "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            StringBuilder bookings = new StringBuilder();
            bookings.append("Bookings for Cinema ").append(currentCinemaId).append("\n\n");
            
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, customer_name, seats, total_price, booking_time, schedule " +
                "FROM bookings WHERE cinema = ? ORDER BY schedule, booking_time");
            stmt.setInt(1, currentCinemaId);
            ResultSet rs = stmt.executeQuery();
            
            boolean hasBookings = false;
            String currentSchedule = "";
            
            while (rs.next()) {
                hasBookings = true;
                String schedule = rs.getString("schedule");
                
                if (!schedule.equals(currentSchedule)) {
                    bookings.append("\n===== ").append(schedule).append(" =====\n");
                    currentSchedule = schedule;
                }
                
                bookings.append("ID: ").append(rs.getInt("id"))
                       .append(" | Name: ").append(rs.getString("customer_name"))
                       .append(" | Seats: ").append(rs.getString("seats"))
                       .append(" | Price: ₱").append(rs.getDouble("total_price"))
                       .append(" | Time: ").append(rs.getTimestamp("booking_time"))
                       .append("\n");
            }
            
            rs.close();
            stmt.close();
            
            if (!hasBookings) {
                bookings.append("No bookings found for this cinema.");
            }
            
            // Display bookings in a scrollable text area
            JTextArea textArea = new JTextArea(bookings.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                                        "Current Bookings", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving bookings: " + e.getMessage(), 
                                       "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Clean up database connection when window closes
    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageCinema());
    }
}