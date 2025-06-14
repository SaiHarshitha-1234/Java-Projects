// CheckoutUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CheckoutUI extends JFrame {
    private JTextField bookingIdField;
    private JTextField nameField;
    private JTextField roomTypeField;
    private JTextField daysStayedField;
    private JTextField rateField;
    private JTextField totalBillField;
    private JButton fetchBtn, checkoutBtn;

    public CheckoutUI() {
        setTitle("Customer Check-Out");
        setSize(400, 350);
        setLayout(new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(null);

        bookingIdField = new JTextField();
        nameField = new JTextField();
        roomTypeField = new JTextField();
        daysStayedField = new JTextField();
        rateField = new JTextField();
        totalBillField = new JTextField();

        nameField.setEditable(false);
        roomTypeField.setEditable(false);
        daysStayedField.setEditable(false);
        rateField.setEditable(false);
        totalBillField.setEditable(false);

        fetchBtn = new JButton("Fetch Booking Details");
        checkoutBtn = new JButton("Check Out");

        add(new JLabel("Booking ID:"));
        add(bookingIdField);

        add(new JLabel("Customer Name:"));
        add(nameField);

        add(new JLabel("Room Type:"));
        add(roomTypeField);

        add(new JLabel("Days Stayed:"));
        add(daysStayedField);

        add(new JLabel("Rate per Day:"));
        add(rateField);

        add(new JLabel("Total Bill:"));
        add(totalBillField);

        add(fetchBtn);
        add(checkoutBtn);

        // Fetch booking details by Booking ID
        fetchBtn.addActionListener(e -> {
            String bookingId = bookingIdField.getText();
            if (bookingId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Booking ID");
                return;
            }

            try (Connection con = DBConnection.getConnection()) {
                // Sample query: You might need to adapt to your schema
                String sql = "SELECT g.name, r.type, " +
                        "DATEDIFF(CURDATE(), g.checkin_date) AS daysStayed, " +
                        "r.rate_per_day " +
                        "FROM guests g JOIN rooms r ON g.room_id = r.id " +
                        "WHERE g.id = ? AND g.status = 'booked'";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(bookingId));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String roomType = rs.getString("type");
                    int daysStayed = rs.getInt("daysStayed");
                    double rate = rs.getDouble("rate_per_day");

                    nameField.setText(name);
                    roomTypeField.setText(roomType);
                    daysStayedField.setText(String.valueOf(daysStayed));
                    rateField.setText(String.format("%.2f", rate));

                    double total = daysStayed * rate;
                    totalBillField.setText(String.format("%.2f", total));
                } else {
                    JOptionPane.showMessageDialog(this, "No booking found or already checked out");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching booking details");
            }
        });

        // Checkout process
        checkoutBtn.addActionListener(e -> {
            String bookingId = bookingIdField.getText();
            if (bookingId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Booking ID");
                return;
            }
            try (Connection con = DBConnection.getConnection()) {
                String sql = "UPDATE guests SET status='checked-out', checkout_date=CURDATE() WHERE id=? AND status='booked'";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(bookingId));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Checked Out Successfully");
                    // Clear fields
                    bookingIdField.setText("");
                    nameField.setText("");
                    roomTypeField.setText("");
                    daysStayedField.setText("");
                    rateField.setText("");
                    totalBillField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Booking ID or already checked out");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during check-out");
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
