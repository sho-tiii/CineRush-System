import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageSeatsWindow extends JFrame {
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cinemaComboBox;
    private JComboBox<String> scheduleComboBox;
    private JPanel seatVisualizationPanel;
    private String selectedCinema = "Cinema 1";
    private String selectedSchedule = "10:00 AM";
    private JButton deleteButton;
    private JButton refreshButton;
    private Map<String, JButton> seatButtons = new HashMap<>();
    
    // Colors
    private final Color redAccent = new Color(198, 40, 40);     
    private final Color softBlack = new Color(60, 60, 60);       
    private final Color lighterBlack = new Color(42, 42, 42);   
    private final Color textWhite = Color.WHITE;
    private final Color availableSeatColor = lighterBlack;
    private final Color takenSeatColor = Color.GRAY;
    private final Color selectedBookingColor = new Color(70, 130, 180); 

    public ManageSeatsWindow() {
        setTitle("CineRush - Manage Seats");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));
        } catch (Exception e) {
            System.out.println("Logo image not found");
        }

        // Header
        JLabel headerLabel = new JLabel("CineRush - Seat Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(textWhite);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(redAccent);
        headerLabel.setPreferredSize(new Dimension(getWidth(), 60));
        add(headerLabel, BorderLayout.NORTH);

        // Main content panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setBackground(softBlack);
        
        // Left panel for bookings table
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(softBlack);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(softBlack);
        filterPanel.add(new JLabel("Cinema:"));
        ((JLabel)filterPanel.getComponent(0)).setForeground(textWhite);
        
        cinemaComboBox = new JComboBox<>(new String[]{"All Cinemas", "Cinema 1", "Cinema 2", "Cinema 3"});
        cinemaComboBox.setBackground(lighterBlack);
        cinemaComboBox.setForeground(textWhite);
        cinemaComboBox.addActionListener(e -> {
            if (cinemaComboBox.getSelectedItem().equals("All Cinemas")) {
                selectedCinema = null;
            } else {
                selectedCinema = (String) cinemaComboBox.getSelectedItem();
            }
            refreshBookingsTable();
            updateSeatVisualization();
        });
        filterPanel.add(cinemaComboBox);
        
        filterPanel.add(new JLabel("Schedule:"));
        ((JLabel)filterPanel.getComponent(2)).setForeground(textWhite);
        
        scheduleComboBox = new JComboBox<>(new String[]{"All Schedules", "10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});
        scheduleComboBox.setBackground(lighterBlack);
        scheduleComboBox.setForeground(textWhite);
        scheduleComboBox.addActionListener(e -> {
            if (scheduleComboBox.getSelectedItem().equals("All Schedules")) {
                selectedSchedule = null;
            } else {
                selectedSchedule = (String) scheduleComboBox.getSelectedItem();
            }
            refreshBookingsTable();
            updateSeatVisualization();
        });
        filterPanel.add(scheduleComboBox);
        
        refreshButton = new JButton("Refresh");
        styleButton(refreshButton, redAccent, textWhite);
        refreshButton.addActionListener(e -> {
            refreshBookingsTable();
            updateSeatVisualization();
        });
        filterPanel.add(refreshButton);
        
        // Table for bookings
        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Customer Name", "Cinema", "Seats", "Total Price", "Schedule"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        bookingsTable.setBackground(lighterBlack);
        bookingsTable.setForeground(textWhite);
        bookingsTable.getTableHeader().setBackground(redAccent);
        bookingsTable.getTableHeader().setForeground(textWhite);
        bookingsTable.setSelectionBackground(selectedBookingColor);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.setRowHeight(25);
        bookingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookingsTable.getSelectedRow() != -1) {
                highlightSelectedBookingSeats();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(bookingsTable);
        tableScrollPane.setBackground(softBlack);
        tableScrollPane.getViewport().setBackground(lighterBlack);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(softBlack);
        
        deleteButton = new JButton("Reset Seats");
        styleButton(deleteButton, redAccent, textWhite);
        deleteButton.addActionListener(e -> deleteSelectedBooking());
        
        JButton backButton = new JButton("Back to Dashboard");
        styleButton(backButton, lighterBlack, textWhite);
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });
        
        actionPanel.add(deleteButton);
        actionPanel.add(backButton);
        
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        leftPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Right panel for seat visualization
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(softBlack);
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), "Seat Visualization"));
        setBorderTextColor(rightPanel, textWhite);
        
        JPanel cinemaInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cinemaInfoPanel.setBackground(softBlack);
        JLabel cinemaLabel = new JLabel("Cinema & Schedule View");
        cinemaLabel.setForeground(textWhite);
        cinemaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        cinemaInfoPanel.add(cinemaLabel);
        
        // Legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legendPanel.setBackground(softBlack);
        
        JPanel availableSwatch = new JPanel();
        availableSwatch.setBackground(availableSeatColor);
        availableSwatch.setPreferredSize(new Dimension(20, 20));
        legendPanel.add(availableSwatch);
        JLabel availableLabel = new JLabel("Available");
        availableLabel.setForeground(textWhite);
        legendPanel.add(availableLabel);
        
        JPanel takenSwatch = new JPanel();
        takenSwatch.setBackground(takenSeatColor);
        takenSwatch.setPreferredSize(new Dimension(20, 20));
        legendPanel.add(takenSwatch);
        JLabel takenLabel = new JLabel("Taken");
        takenLabel.setForeground(textWhite);
        legendPanel.add(takenLabel);
        
        JPanel selectedSwatch = new JPanel();
        selectedSwatch.setBackground(selectedBookingColor);
        selectedSwatch.setPreferredSize(new Dimension(20, 20));
        legendPanel.add(selectedSwatch);
        JLabel selectedLabel = new JLabel("Selected Booking");
        selectedLabel.setForeground(textWhite);
        legendPanel.add(selectedLabel);
        
        // Seat grid panel
        seatVisualizationPanel = new JPanel(new GridLayout(5, 5, 10, 10));
        seatVisualizationPanel.setBackground(softBlack);
        seatVisualizationPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setupSeatButtons();
        
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(softBlack);
        screenPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 30));
        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setForeground(textWhite);
        screenLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        screenPanel.add(screenLabel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(softBlack);
        centerPanel.add(screenPanel, BorderLayout.NORTH);
        centerPanel.add(seatVisualizationPanel, BorderLayout.CENTER);
        
        rightPanel.add(cinemaInfoPanel, BorderLayout.NORTH);
        rightPanel.add(centerPanel, BorderLayout.CENTER);
        rightPanel.add(legendPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        refreshBookingsTable();
        updateSeatVisualization();
        
        setVisible(true);
    }
    
    private void setupSeatButtons() {
        String[] seatNames = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5", 
                             "C1", "C2", "C3", "C4", "C5", "D1", "D2", "D3", "D4", "D5", 
                             "E1", "E2", "E3", "E4", "E5"};
        
        for (String name : seatNames) {
            JButton seatBtn = new JButton(name);
            styleButton(seatBtn, availableSeatColor, textWhite);
            seatBtn.setBorder(BorderFactory.createLineBorder(redAccent));
            seatButtons.put(name, seatBtn);
            seatVisualizationPanel.add(seatBtn);
        }
    }
    
    private void updateSeatVisualization() {
        // Reset all seats to available
        for (JButton button : seatButtons.values()) {
            button.setBackground(availableSeatColor);
            button.setForeground(textWhite);
        }
        
        if (selectedCinema == null || selectedSchedule == null) {
            return;
        }
        
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT seats FROM bookings WHERE cinema = ? AND schedule = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedCinema);
            pst.setString(2, selectedSchedule);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String[] seats = rs.getString("seats").split(" ");
                for (String seat : seats) {
                    JButton seatButton = seatButtons.get(seat.trim());
                    if (seatButton != null) {
                        seatButton.setBackground(takenSeatColor);
                        seatButton.setForeground(Color.DARK_GRAY);
                    }
                }
            }
            
            // Re-highlight selected booking if there is one
            if (bookingsTable.getSelectedRow() != -1) {
                highlightSelectedBookingSeats();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading seat information: " + e.getMessage());
        }
    }
    
    private void refreshBookingsTable() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.connect()) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT id, customer_name, cinema, seats, total_price, schedule FROM bookings WHERE 1=1");
            
            if (selectedCinema != null) {
                sqlBuilder.append(" AND cinema = ?");
            }
            
            if (selectedSchedule != null) {
                sqlBuilder.append(" AND schedule = ?");
            }
            
            sqlBuilder.append(" ORDER BY id DESC");
            
            PreparedStatement pst = conn.prepareStatement(sqlBuilder.toString());
            
            int paramIndex = 1;
            if (selectedCinema != null) {
                pst.setString(paramIndex++, selectedCinema);
            }
            
            if (selectedSchedule != null) {
                pst.setString(paramIndex, selectedSchedule);
            }
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("cinema"),
                    rs.getString("seats"),
                    "₱" + rs.getInt("total_price"),
                    rs.getString("schedule")
                });
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }
    
    private void highlightSelectedBookingSeats() {
        // First, update the visualization to reset colors
        updateSeatVisualization();
        
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String bookingSeats = tableModel.getValueAt(selectedRow, 3).toString();
        String[] seats = bookingSeats.split(" ");
        
        for (String seat : seats) {
            JButton seatButton = seatButtons.get(seat.trim());
            if (seatButton != null) {
                seatButton.setBackground(selectedBookingColor);
                seatButton.setForeground(textWhite);
            }
        }
    }
    
    private void deleteSelectedBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to delete");
            return;
        }
        
        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset booking #" + bookingId + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "DELETE FROM bookings WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, bookingId);
                int affected = pst.executeUpdate();
                
                if (affected > 0) {
                    JOptionPane.showMessageDialog(this, "Booking deleted successfully");
                    refreshBookingsTable();
                    updateSeatVisualization();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete booking");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting booking: " + e.getMessage());
            }
        }
    }
    
    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
    }
    
    private void setBorderTextColor(JComponent component, Color color) {
        if (component.getBorder() instanceof javax.swing.border.TitledBorder border) {
            border.setTitleColor(color);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ManageSeatsWindow());
    }
}