import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TransactionHistoryPanel extends JFrame {

    // Color scheme
    private final Color bgColor = new Color(0x1F1F1F); // Blackish background
    private final Color textWhite = new Color(0xF9FAFB);
    private final Color accentRed = new Color(0x991B1B); // Red accent
    private final Color panelBg = new Color(0xE11D48); // Button red

    private final Font headerFont = new Font("Segoe UI", Font.PLAIN, 28);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);

    private JTable table;
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel() {
        setTitle("CineRush - Transaction History");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
        setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        // Title
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>TRANSACTION<br>HISTORY</div></html>");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(textWhite);
        titleLabel.setBounds(330, 10, 250, 60);
        add(titleLabel);

        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setBounds(30, 75, 790, 1);
        titleSeparator.setForeground(accentRed);
        add(titleSeparator);

        // Cinema buttons
        String[] cinemaNames = {"Cinema 1", "Cinema 2", "Cinema 3", "Cinema 4", "Cinema 5"};
        for (int i = 0; i < cinemaNames.length; i++) {
            JButton cinemaButton = createStyledButton(cinemaNames[i], 30 + i * 155, 90);
            int finalI = i;
            cinemaButton.addActionListener(e -> loadTransactionData(cinemaNames[finalI]));
            add(cinemaButton);
        }

        // Table model setup
        tableModel = new DefaultTableModel(new String[]{
            "ID", "Customer", "Cinema", "Seats", "Price", "Payment", "Change", "Time", "Schedule"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table fully non-editable
            }
        };

        // Table setup
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(textWhite);
        table.setBackground(bgColor);
        table.setRowHeight(28);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0x4B5563));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(accentRed);
        header.setForeground(textWhite);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);

        // Adjust column widths for better visibility
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);   // ID
        columnModel.getColumn(1).setPreferredWidth(100);  // Customer
        columnModel.getColumn(2).setPreferredWidth(80);   // Cinema
        columnModel.getColumn(3).setPreferredWidth(100);  // Seats
        columnModel.getColumn(4).setPreferredWidth(60);   // Price
        columnModel.getColumn(5).setPreferredWidth(70);   // Payment
        columnModel.getColumn(6).setPreferredWidth(70);   // Change
        columnModel.getColumn(7).setPreferredWidth(150);  // Time - widened
        columnModel.getColumn(8).setPreferredWidth(100);  // Schedule

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 140, 790, 280);
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(accentRed, 1));
        add(scrollPane);

        // Return button
        JButton returnButton = createStyledButton("Return", 650, 440);
        returnButton.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });
        add(returnButton);

        setVisible(true);
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 140, 40);
        button.setForeground(textWhite);
        button.setBackground(panelBg);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentRed);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(panelBg);
            }
        });

        return button;
    }

    private void loadTransactionData(String cinemaName) {
        tableModel.setRowCount(0); // Clear table first

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM bookings WHERE cinema = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cinemaName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("cinema"),
                    rs.getString("seats"),
                    rs.getDouble("total_price"),
                    rs.getDouble("payment"),
                    rs.getDouble("change_amt"),
                    rs.getString("booking_time"),
                    rs.getString("schedule")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data for " + cinemaName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TransactionHistoryPanel::new);
    }
}
