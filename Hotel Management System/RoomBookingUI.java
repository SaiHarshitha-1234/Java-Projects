// RoomBookingUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomBookingUI extends JFrame {
    public RoomBookingUI() {
        setTitle("Room Booking");
        setSize(500, 500);
        setLayout(new GridLayout(10, 2, 10, 10));
        setLocationRelativeTo(null);

        // Input fields
        JTextField nameField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField aadharField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField roomIdField = new JTextField();
        JTextField checkinField = new JTextField();
        JTextField checkoutField = new JTextField();

        JButton bookBtn = new JButton("Book Room");

        // Adding UI Components
        add(new JLabel("Guest Name:")); add(nameField);
        add(new JLabel("Gender:")); add(genderField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Aadhar No:")); add(aadharField);
        add(new JLabel("Contact:")); add(contactField);
        add(new JLabel("Room ID:")); add(roomIdField);
        add(new JLabel("Check-in Date (YYYY-MM-DD):")); add(checkinField);
        add(new JLabel("Check-out Date (YYYY-MM-DD):")); add(checkoutField);
        add(new JLabel()); add(bookBtn);

        // Action to book room
        bookBtn.addActionListener(e -> {
            synchronized(this){
            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO guests (name, gender, address, aadhar_no, contact, room_id, checkin_date, checkout_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'booked')";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, genderField.getText());
                ps.setString(3, addressField.getText());
                ps.setString(4, aadharField.getText());
                ps.setString(5, contactField.getText());
                ps.setInt(6, Integer.parseInt(roomIdField.getText()));
                ps.setDate(7, Date.valueOf(checkinField.getText()));
                ps.setDate(8, Date.valueOf(checkoutField.getText()));

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Room Booked Successfully");

                // Optional: update room status to 'booked' in rooms table
                PreparedStatement ps2 = con.prepareStatement("UPDATE rooms SET status='booked' WHERE id=?");
                ps2.setInt(1, Integer.parseInt(roomIdField.getText()));
                ps2.executeUpdate();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error booking room");
            }
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
