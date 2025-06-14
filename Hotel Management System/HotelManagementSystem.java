// HotelManagementSystem.java (Main launcher class)
import javax.swing.*;

public class HotelManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}

// DBConnection.java (Database connection handler)
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
