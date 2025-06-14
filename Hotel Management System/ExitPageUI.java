import javax.swing.*;
import java.awt.*;

public class ExitPage extends JFrame {

    public ExitPage() {
        setTitle("Exit Page");
        setSize(450, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Thank you message
        JLabel thanksLabel = new JLabel("Thank you for using the Hotel Management System!", JLabel.CENTER);
        thanksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        thanksLabel.setForeground(new Color(60, 60, 60));
        add(thanksLabel, BorderLayout.CENTER);

        // Subtext
        JLabel exitMsg = new JLabel("The application will close automatically...", JLabel.CENTER);
        exitMsg.setFont(new Font("Arial", Font.ITALIC, 13));
        exitMsg.setForeground(Color.GRAY);
        add(exitMsg, BorderLayout.SOUTH);

        setVisible(true);

        // Close the application after 3 seconds
        Timer timer = new Timer(3000, e -> System.exit(0));
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        new ExitPage();
    }
}
