// SearchRoomUI.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SearchRoomUI extends JFrame {
    public SearchRoomUI() {
        setTitle("Search Room");
        setSize(500, 400);
        setLayout(new GridLayout(6, 2));

        String[] roomTypes = {"Single", "Double", "Deluxe", "Family"};
        String[] bedTypes = {"Single Bed", "Double Bed"};
        String[] acTypes = {"AC", "Non-AC"};
        String[] familyOptions = {"Yes", "No"};

        JComboBox<String> roomTypeBox = new JComboBox<>(roomTypes);
        JComboBox<String> bedTypeBox = new JComboBox<>(bedTypes);
        JComboBox<String> acTypeBox = new JComboBox<>(acTypes);
        JComboBox<String> familyBox = new JComboBox<>(familyOptions);
        JButton searchBtn = new JButton("Search Rooms");

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JLabel("Room Type:")); add(roomTypeBox);
        add(new JLabel("Bed Type:")); add(bedTypeBox);
        add(new JLabel("AC/Non-AC:")); add(acTypeBox);
        add(new JLabel("Family Room:")); add(familyBox);
        add(new JLabel()); add(searchBtn);
        add(new JLabel("Matching Room IDs:")); add(new JScrollPane(resultArea));

        searchBtn.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String query = "SELECT id FROM rooms WHERE type=? AND bed_type=? AND ac_type=? AND family=? AND status='available'";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, roomTypeBox.getSelectedItem().toString());
                ps.setString(2, bedTypeBox.getSelectedItem().toString());
                ps.setString(3, acTypeBox.getSelectedItem().toString());
                ps.setString(4, familyBox.getSelectedItem().toString());

                ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("Room ID: ").append(rs.getInt("id")).append("\n");
                }
                if (sb.length() == 0) {
                    resultArea.setText("No matching rooms found.");
                } else {
                    resultArea.setText(sb.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                resultArea.setText("Error retrieving room information.");
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
