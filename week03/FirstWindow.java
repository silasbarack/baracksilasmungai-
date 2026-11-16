import javax.swing.*;
import java.awt.*;
public class FirstWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Week 3: First Swing Window");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new JLabel("Hello, Swing!",SwingConstants.CENTER),BorderLayout.CENTER);
            frame.setSize(400,250); frame.setLocationRelativeTo(null); frame.setVisible(true);
        });
    }
}