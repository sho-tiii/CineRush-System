import javax.swing.*;
import java.awt.*;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(JFrame parent, String customerName, String selectedSeats, int totalAmount, int payment, int change, String cinema, String movie, String schedule) {
        super(parent, "Receipt", true);
        setSize(350, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        Color mainRed = new Color(0xC62828);
        Font mainFont = new Font("SansSerif", Font.BOLD, 16);
        Font subFont = new Font("SansSerif", Font.PLAIN, 14);

        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("🎬 CineRush Movie Ticketing", SwingConstants.CENTER);
        titleLabel.setFont(mainFont);
        titleLabel.setForeground(mainRed);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(titleLabel);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cinerush_logo.png")));


        receiptPanel.add(Box.createRigidArea(new Dimension(0, 15))); // spacing

        receiptPanel.add(createCenteredLabel("Customer: " + customerName, subFont));
        receiptPanel.add(createCenteredLabel("Cinema: " + cinema, subFont));
        receiptPanel.add(createCenteredLabel("Movie: " + movie, subFont));
        receiptPanel.add(createCenteredLabel("Schedule: " + schedule, subFont));
        receiptPanel.add(createCenteredLabel("Seats: " + selectedSeats, subFont));
        receiptPanel.add(createCenteredLabel("Total: ₱" + totalAmount, subFont));
        receiptPanel.add(createCenteredLabel("Payment: ₱" + payment, subFont));
        receiptPanel.add(createCenteredLabel("Change: ₱" + change, subFont));

        add(receiptPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        closeButton.setFocusPainted(false);
        closeButton.setBackground(mainRed);
        closeButton.setForeground(Color.WHITE);
        closeButton.setPreferredSize(new Dimension(100, 40));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }

    private JLabel createCenteredLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }
}
