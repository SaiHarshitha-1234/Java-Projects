// BillingUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BillingUI extends JFrame {
    private JTextField bookingIdField, customerNameField, roomNumField, roomTypeField;
    private JTextField checkinDateField, checkoutDateField, daysStayedField, roomChargeField;
    private JLabel taxLabel, totalLabel;
    private JButton fetchBtn, generateBillBtn;

    public BillingUI() {
        setTitle("Billing");
        setSize(600, 400);
        setLayout(new GridLayout(10, 2, 10, 10));
        setLocationRelativeTo(null);

        bookingIdField = new JTextField();
        customerNameField = new JTextField();
        customerNameField.setEditable(false);
        roomNumField = new JTextField();
        roomNumField.setEditable(false);
        roomTypeField = new JTextField();
        roomTypeField.setEditable(false);
        checkinDateField = new JTextField();
        checkinDateField.setEditable(false);
        checkoutDateField = new JTextField();
        checkoutDateField.setEditable(false);
        daysStayedField = new JTextField();
        daysStayedField.setEditable(false);
        roomChargeField = new JTextField();
        roomChargeField.setEditable(false);
        taxLabel = new JLabel("0.00");
        totalLabel = new JLabel("0.00");

        fetchBtn = new JButton("Fetch Booking Details");
        generateBillBtn = new JButton("Generate Bill");
        generateBillBtn.setEnabled(false);

        add(new JLabel("Booking ID:")); add(bookingIdField);
        add(new JLabel("")); add(fetchBtn);

        add(new JLabel("Customer Name:")); add(customerNameField);
        add(new JLabel("Room Number:")); add(roomNumField);
        add(new JLabel("Room Type:")); add(roomTypeField);
        add(new JLabel("Check-In Date:")); add(checkinDateField);
        add(new JLabel("Check-Out Date:")); add(checkoutDateField);
        add(new JLabel("Days Stayed:")); add(daysStayedField);
        add(new JLabel("Room Charge:")); add(roomChargeField);
        add(new JLabel("Tax (18%):")); add(taxLabel);
        add(new JLabel("Total Amount:")); add(totalLabel);
        add(new JLabel("")); add(generateBillBtn);

        fetchBtn.addActionListener(e -> fetchBookingDetails());
        generateBillBtn.addActionListener(e -> generateBill());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void fetchBookingDetails() {
        String bookingIdText = bookingIdField.getText().trim();
        if (bookingIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Booking ID.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // Query booking info joining guests and rooms (adjust table/column names accordingly)
            String sql = "SELECT g.name, g.room_id, r.type, g.checkin_date, g.checkout_date, r.rate_per_day " +
                         "FROM guests g JOIN rooms r ON g.room_id = r.id WHERE g.id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(bookingIdText));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                int roomNum = rs.getInt("room_id");
                String roomType = rs.getString("type");
                Date checkin = rs.getDate("checkin_date");
                Date checkout = rs.getDate("checkout_date");
                double ratePerDay = rs.getDouble("rate_per_day");

                customerNameField.setText(name);
                roomNumField.setText(String.valueOf(roomNum));
                roomTypeField.setText(roomType);
                checkinDateField.setText(checkin.toString());
                checkoutDateField.setText(checkout.toString());

                long daysStayed = ChronoUnit.DAYS.between(checkin.toLocalDate(), checkout.toLocalDate());
                if (daysStayed == 0) daysStayed = 1; // minimum 1 day charge

                daysStayedField.setText(String.valueOf(daysStayed));

                double roomCharge = daysStayed * ratePerDay;
                roomChargeField.setText(String.format("%.2f", roomCharge));

                double tax = 0.18 * roomCharge;
                taxLabel.setText(String.format("%.2f", tax));

                double total = roomCharge + tax;
                totalLabel.setText(String.format("%.2f", total));

                generateBillBtn.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Booking ID not found.");
                clearFields();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching booking details.");
            clearFields();
        }
    }

    private void clearFields() {
        customerNameField.setText("");
        roomNumField.setText("");
        roomTypeField.setText("");
        checkinDateField.setText("");
        checkoutDateField.setText("");
        daysStayedField.setText("");
        roomChargeField.setText("");
        taxLabel.setText("0.00");
        totalLabel.setText("0.00");
        generateBillBtn.setEnabled(false);
    }

    private void generateBill() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO billing (guest_id, room_id, amount, tax, total_amount, billing_date) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement ps = con.prepareStatement(sql);

            int guestId = Integer.parseInt(bookingIdField.getText());
            int roomId = Integer.parseInt(roomNumField.getText());
            double amount = Double.parseDouble(roomChargeField.getText());
            double tax = Double.parseDouble(taxLabel.getText());
            double total = Double.parseDouble(totalLabel.getText());

            ps.setInt(1, guestId);
            ps.setInt(2, roomId);
            ps.setDouble(3, amount);
            ps.setDouble(4, tax);
            ps.setDouble(5, total);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bill generated successfully.");
            generateBillBtn.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating bill.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingUI::new);
    }
}
