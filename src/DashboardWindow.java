import javax.swing.*;

public class DashboardWindow extends JFrame {

    public DashboardWindow() {
        setTitle("CineRush - Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Choose a Cinema:");
        label.setBounds(130, 20, 150, 25);
        add(label);

        // Button for Cinema 1 - Avengers
        JButton cinema1 = new JButton("Cinema 1 - Avengers");
        cinema1.setBounds(100, 60, 200, 30);
        cinema1.addActionListener(e -> openSeatSelectionWindow("Cinema 1", "Avengers"));
        add(cinema1);

        // Button for Cinema 2 - Barbie
        JButton cinema2 = new JButton("Cinema 2 - Barbie");
        cinema2.setBounds(100, 100, 200, 30);
        cinema2.addActionListener(e -> openSeatSelectionWindow("Cinema 2", "Barbie"));
        add(cinema2);

        // Button for Cinema 3 - Oppenheimer
        JButton cinema3 = new JButton("Cinema 3 - Oppenheimer");
        cinema3.setBounds(100, 140, 200, 30);
        cinema3.addActionListener(e -> openSeatSelectionWindow("Cinema 3", "Oppenheimer"));
        add(cinema3);

        // Button for Cinema 4 - John Wick
        JButton cinema4 = new JButton("Cinema 4 - John Wick");
        cinema4.setBounds(100, 180, 200, 30);
        cinema4.addActionListener(e -> openSeatSelectionWindow("Cinema 4", "John Wick"));
        add(cinema4);

        // Button for Cinema 5 - Spider-Man
        JButton cinema5 = new JButton("Cinema 5 - Spider-Man");
        cinema5.setBounds(100, 220, 200, 30);
        cinema5.addActionListener(e -> openSeatSelectionWindow("Cinema 5", "Spider-Man"));
        add(cinema5);

        setVisible(true);
    }

    // Method to open the SeatSelectionWindow and close the current window
    private void openSeatSelectionWindow(String cinema, String movie) {
        this.dispose(); // Close the current Dashboard window
        new SeatSelectionWindow(cinema, movie); // Open the SeatSelectionWindow
    }

    public static void main(String[] args) {
        new DashboardWindow();
    }
}
