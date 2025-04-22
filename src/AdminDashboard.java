import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private final Color bgColor = new Color(0x1A1A1A);
    private final Color textWhite = new Color(0xF9FAFB);
    private final Color accentRed = new Color(0xE11D48); 
    private final Color buttonRed = new Color(0xB91C2B);

    private final Font headerFont = new Font("Segoe UI", Font.PLAIN, 28);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);

    public AdminDashboard() {
        setTitle("CineRush - Admin Dashboard");
        setSize(350, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
        setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));

        JLabel dashboardTitle = new JLabel("<html><div style='text-align: center;'>ADMIN<br>DASHBOARD<br></div></html>");
        dashboardTitle.setForeground(textWhite);
        dashboardTitle.setFont(headerFont);
        dashboardTitle.setBounds(85, 35, 200, 100);
        add(dashboardTitle);

        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setBounds(50, 125, 250, 1);
        titleSeparator.setForeground(accentRed);
        add(titleSeparator);

        JButton manageMoviesButton = createStyledButton("Manage Movies", 50, 150);
        add(manageMoviesButton);
        manageMoviesButton.addActionListener(e -> {
            new MovieScheduleAdminPanel();  
            dispose();  
        });

        JButton transactionsButton = createStyledButton("View Transactions", 50, 220);
        add(transactionsButton);
        transactionsButton.addActionListener(e -> {
            new TransactionHistoryPanel();  
            dispose();
        });

       
        JButton manageSeatsButton = createStyledButton("Manage Seats", 50, 290);
        add(manageSeatsButton);
        manageSeatsButton.addActionListener(e -> {
            new ManageSeatsWindow();  
            dispose();  
        });

        JButton logoutButton = createStyledButton("Logout", 50, 360); 
        add(logoutButton);
        logoutButton.addActionListener(e -> {
          
            new AdminLogin();
            dispose();
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 250, 50);
        button.setForeground(textWhite);
        button.setBackground(buttonRed);
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
                button.setBackground(buttonRed); 
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
