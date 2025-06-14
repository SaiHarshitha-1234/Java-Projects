// CheckinUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CheckInUI extends JFrame {
    public CheckInUI() {
        setTitle("Customer Check-In");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel bookingIdLabel = new JLabel("Booking ID:");
        JTextField bookingIdField = new JTextField();

        JLabel nameLabel = new JLabel("Guest Name:");
        JTextField nameField = new JTextField();

        JLabel roomLabel = new JLabel("Room Assigned:");
        JTextField roomField = new JTextField();

        JLabel checkinDateLabel = new JLabel("Check-In Date (YYYY-MM-DD):");
        JTextField checkinDateField = new JTextField();

        JButton checkInBtn = new JButton("Check In");

        add(bookingIdLabel); add(bookingIdField);
        add(nameLabel); add(nameField);
        add(roomLabel); add(roomField);
        add(checkinDateLabel); add(checkinDateField);
        add(new JLabel()); add(checkInBtn);

        checkInBtn.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "UPDATE guests SET status = 'checked-in', checkin_date = ?, room_id = ? WHERE id = ? AND name = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setDate(1, Date.valueOf(checkinDateField.getText()));
                ps.setInt(2, Integer.parseInt(roomField.getText()));
                ps.setInt(3, Integer.parseInt(bookingIdField.getText()));
                ps.setString(4, nameField.getText());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Guest Checked In Successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Check-In Failed. Please verify details.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during check-in.");
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CheckInUI::new);
    }
}
