// LoginUI.java
import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    public LoginUI() {
        setTitle("Hotel Management - Login");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        JLabel roleLabel = new JLabel("Login As:");
        String[] roles = {"Admin", "Manager", "Receptionist"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String username = userField.getText();
            String password = new String(passField.getPassword());

            boolean valid = false;

            // Sample login criteria (you can link to DB later)
            if (role.equals("Admin") && username.equals("admin") && password.equals("admin123")) {
                valid = true;
            } else if (role.equals("Manager") && username.equals("manager") && password.equals("manager123")) {
                valid = true;
            } else if (role.equals("Receptionist") && username.equals("reception") && password.equals("reception123")) {
                valid = true;
            }

            if (valid) {
                JOptionPane.showMessageDialog(this, "Login successful as " + role);
                dispose();
                new DashboardUI(); // Later you can load role-specific dashboards
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials for " + role);
            }
        });

        add(roleLabel); add(roleBox);
        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(new JLabel()); add(loginBtn);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
   public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
