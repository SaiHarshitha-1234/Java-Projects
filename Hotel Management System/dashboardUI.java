// dashboardUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardUI extends JFrame {

    public DashboardUI() {
        setTitle("Hotel Management Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 20, 20)); // 4 rows, 2 columns with gaps

        // Create buttons
        JButton searchRoomBtn = new JButton("Search Room");
        JButton checkInBtn = new JButton("Check-In");
        JButton checkOutBtn = new JButton("Check-Out");
        JButton viewBookingsBtn = new JButton("View Bookings");
        JButton billingBtn = new JButton("Billing");
        JButton staffMgmtBtn = new JButton("Staff Management");
        JButton logoutBtn = new JButton("Logout");

        // Add buttons to frame
        add(searchRoomBtn);
        add(checkInBtn);
        add(checkOutBtn);
        add(viewBookingsBtn);
        add(billingBtn);
        add(staffMgmtBtn);
        add(logoutBtn);

        //  action listeners 
        searchRoomBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open Search Room Module");
             // new SearchRoomUI(); 
        });

        checkInBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open Check-In Module");
            // new CheckInUI();
        });

        checkOutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open Check-Out Module");
            // new CheckOutUI();
        });

        viewBookingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open View Bookings Module");
            // new ViewBookingsUI();
        });

        billingBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open Billing Module");
            // new BillingUI();
        });

        staffMgmtBtn.addActionListener(e -> {
           // new StaffManagementUI();  
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Close dashboard window
                // Show login window here if exists
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardUI::new);
    }
}
