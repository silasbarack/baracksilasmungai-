import javax.swing.*;
import java.awt.*;
public class LibraryCheckoutDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Library checkout — event-driven prototype");
            JComboBox<String> books = new JComboBox<>(new String[]{"Java Fundamentals","Observer Pattern","JavaFX Guide"});
            JLabel status = new JLabel("Choose a book and reserve.");
            JButton reserve = new JButton("Reserve"); reserve.addActionListener(e -> status.setText("Reserved: " + books.getSelectedItem()));
            JButton clear = new JButton("Clear"); clear.addActionListener(e -> { books.setSelectedIndex(0); status.setText("Ready."); });
            JPanel p = new JPanel(new FlowLayout()); p.add(books); p.add(reserve); p.add(clear);
            frame.add(p, BorderLayout.CENTER); frame.add(status, BorderLayout.SOUTH);
            frame.setSize(550,150); frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); frame.setVisible(true);
        });
    }
}