//viewBookingUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ViewRoomsUI extends JFrame {

    private JList<String> availableRoomsList;
    private JList<String> bookedRoomsList;

    public ViewRoomsUI() {
        setTitle("View Rooms Status");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 10, 10));

        availableRoomsList = new JList<>();
        bookedRoomsList = new JList<>();

        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Available Rooms:"), BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableRoomsList), BorderLayout.CENTER);

        JPanel bookedPanel = new JPanel(new BorderLayout());
        bookedPanel.add(new JLabel("Booked Rooms:"), BorderLayout.NORTH);
        bookedPanel.add(new JScrollPane(bookedRoomsList), BorderLayout.CENTER);

        add(availablePanel);
        add(bookedPanel);

        loadRooms();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadRooms() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement psAvailable = con.prepareStatement("SELECT id FROM rooms WHERE status = ?");
            psAvailable.setString(1, "available");
            ResultSet rsAvailable = psAvailable.executeQuery();

            Vector<String> availableRooms = new Vector<>();
            while (rsAvailable.next()) {
                availableRooms.add("Room ID: " + rsAvailable.getInt("id"));
            }
            availableRoomsList.setListData(availableRooms);

            PreparedStatement psBooked = con.prepareStatement("SELECT id FROM rooms WHERE status = ?");
            psBooked.setString(1, "booked");
            ResultSet rsBooked = psBooked.executeQuery();

            Vector<String> bookedRooms = new Vector<>();
            while (rsBooked.next()) {
                bookedRooms.add("Room ID: " + rsBooked.getInt("id"));
            }
            bookedRoomsList.setListData(bookedRooms);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading rooms from database.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // For testing standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewRoomsUI::new);
    }
}
