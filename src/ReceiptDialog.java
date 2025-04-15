import javax.swing.*;
import java.awt.*;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(JFrame parent, String customerName, String selectedSeats, int totalAmount, int payment, int change) {
        super(parent, "Receipt", true); 
        setSize(300, 250);
        setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        // Create a panel for the receipt content
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        
        receiptPanel.add(new JLabel("CineRush Movie Ticketing"));
        receiptPanel.add(new JLabel("Customer: " + customerName));
        receiptPanel.add(new JLabel("Selected Seats: " + selectedSeats));
        receiptPanel.add(new JLabel("Total: ₱" + totalAmount));
        receiptPanel.add(new JLabel("Payment: ₱" + payment));
        receiptPanel.add(new JLabel("Change: ₱" + change));

        // Add the panel to the dialog
        add(receiptPanel, BorderLayout.CENTER);

        // Button to close the receipt dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}

