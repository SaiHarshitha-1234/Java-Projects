// StaffManagementUI.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffManagementUI extends JFrame {
    private DefaultTableModel model;
    private JTable staffTable;
    private JTextField nameField, roleField, contactField, shiftField, salaryField;
    private JButton addBtn, updateBtn, deleteBtn, refreshBtn;

    public StaffManagementUI() {
        setTitle("Staff Management");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Table with staff data
        model = new DefaultTableModel(new String[]{"ID", "Name", "Role", "Contact", "Shift", "Salary"}, 0);
        staffTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel (2 rows, 6 columns)
        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 10, 10));

        nameField = new JTextField();
        roleField = new JTextField();
        contactField = new JTextField();
        shiftField = new JTextField();
        salaryField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);

        inputPanel.add(new JLabel("Shift:"));
        inputPanel.add(shiftField);
        inputPanel.add(new JLabel("Salary:"));
        inputPanel.add(salaryField);

        // Buttons panel
        addBtn = new JButton("Add Staff");
        updateBtn = new JButton("Update Selected");
        deleteBtn = new JButton("Delete Selected");
        refreshBtn = new JButton("Refresh List");

        inputPanel.add(addBtn);
        inputPanel.add(updateBtn);
        inputPanel.add(deleteBtn);
        inputPanel.add(refreshBtn);

        add(inputPanel, BorderLayout.SOUTH);

        // Load data initially
        loadStaffData();

        // Button listeners
        addBtn.addActionListener(e -> addStaff());
        updateBtn.addActionListener(e -> updateStaff());
        deleteBtn.addActionListener(e -> deleteStaff());
        refreshBtn.addActionListener(e -> loadStaffData());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadStaffData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM staff");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("contact"),
                        rs.getString("shift"),
                        rs.getDouble("salary")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff data.");
            e.printStackTrace();
        }
    }

    private void addStaff() {
        String name = nameField.getText().trim();
        String role = roleField.getText().trim();
        String contact = contactField.getText().trim();
        String shift = shiftField.getText().trim();
        String salaryText = salaryField.getText().trim();

        if (name.isEmpty() || role.isEmpty() || contact.isEmpty() || shift.isEmpty() || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a number.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO staff (name, role, contact, shift, salary) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setString(3, contact);
            ps.setString(4, shift);
            ps.setDouble(5, salary);

            int inserted = ps.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Staff added successfully.");
                loadStaffData();
                clearInputFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding staff.");
            e.printStackTrace();
        }
    }

    private void updateStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a staff member to update.");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);

        String name = nameField.getText().trim();
        String role = roleField.getText().trim();
        String contact = contactField.getText().trim();
        String shift = shiftField.getText().trim();
        String salaryText = salaryField.getText().trim();

        if (name.isEmpty() || role.isEmpty() || contact.isEmpty() || shift.isEmpty() || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a number.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE staff SET name=?, role=?, contact=?, shift=?, salary=? WHERE id=?");
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setString(3, contact);
            ps.setString(4, shift);
            ps.setDouble(5, salary);
            ps.setInt(6, id);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Staff updated successfully.");
                loadStaffData();
                clearInputFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating staff.");
            e.printStackTrace();
        }
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a staff member to delete.");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this staff member?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM staff WHERE id=?");
            ps.setInt(1, id);

            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Staff deleted successfully.");
                loadStaffData();
                clearInputFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting staff.");
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        roleField.setText("");
        contactField.setText("");
        shiftField.setText("");
        salaryField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffManagementUI::new);
    }
}
