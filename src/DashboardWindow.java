import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class DashboardWindow extends JFrame {

    public DashboardWindow() {
        setTitle("CineRush - Dashboard");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));

        // Red + Black Theme
        Color deepRed = new Color(0xC62828);
        Color bgBlack = new Color(0x1A1A1A);
        Color darkGray = new Color(0x222222);
        Color textWhite = new Color(0xF9FAFB);

        // Heading
        JLabel cineRushLabel = new JLabel("CineRush", SwingConstants.CENTER);
        cineRushLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        cineRushLabel.setForeground(deepRed);
        cineRushLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel subHeading = new JLabel("Select a Cinema", SwingConstants.CENTER);
        subHeading.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subHeading.setForeground(textWhite);
        subHeading.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBackground(bgBlack);
        headingPanel.add(cineRushLabel);
        headingPanel.add(subHeading);
        add(headingPanel, BorderLayout.NORTH);

        // Cinema button panel
        JPanel cinemaPanel = new JPanel();
        cinemaPanel.setLayout(new GridLayout(0, 1, 10, 10));
        cinemaPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        cinemaPanel.setBackground(bgBlack);

        ArrayList<Cinema> cinemas = fetchCinemasFromDatabase();

        for (Cinema cinema : cinemas) {
            JButton button = new JButton(cinema.name + " - " + cinema.movie);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setFocusPainted(false);
            button.setBackground(deepRed);
            button.setForeground(textWhite);
            button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(deepRed.brighter());
                }

                public void mouseExited(MouseEvent e) {
                    button.setBackground(deepRed);
                }
            });
            button.addActionListener(e -> openSeatSelectionWindow(cinema.name, cinema.movie));
            cinemaPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(cinemaPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(bgBlack);
        add(scrollPane, BorderLayout.CENTER);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(deepRed.darker());
        logoutBtn.setForeground(textWhite);
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(deepRed);
            }

            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(deepRed.darker());
            }
        });
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginWindow();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(bgBlack);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(bgBlack);
        setVisible(true);
    }

    private void openSeatSelectionWindow(String cinema, String movie) {
        dispose();
        new SeatSelectionWindow(cinema, movie);
    }

    private ArrayList<Cinema> fetchCinemasFromDatabase() {
        ArrayList<Cinema> cinemaList = new ArrayList<>();
        Connection conn = DatabaseConnection.connect();

        try {
            String query = "SELECT * FROM cinemas";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String movie = rs.getString("movie");
                cinemaList.add(new Cinema(name, movie));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch cinemas.");
            e.printStackTrace();
        }

        return cinemaList;
    }

    class Cinema {
        String name;
        String movie;

        Cinema(String name, String movie) {
            this.name = name;
            this.movie = movie;
        }
    }

    public static void main(String[] args) {
        new DashboardWindow();
    }
}
