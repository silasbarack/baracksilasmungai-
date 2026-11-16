import javax.swing.*;
import java.awt.*;
public class Bug2_MissingListener extends JFrame {
    private int count;
    public Bug2_MissingListener() {
        super("Fixed: Missing Listener"); setLayout(new FlowLayout());
        JLabel label = new JLabel("Count: 0"); JButton button = new JButton("Increment");
        button.addActionListener(e -> label.setText("Count: " + (++count)));
        add(label); add(button); setSize(320,150); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Bug2_MissingListener().setVisible(true)); }
}