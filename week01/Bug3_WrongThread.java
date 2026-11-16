import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
public class Bug3_WrongThread extends JFrame {
    public Bug3_WrongThread() {
        super("Fixed: UI Clock"); setLayout(new FlowLayout());
        JLabel label = new JLabel("Time"); JButton start = new JButton("Start / Stop");
        Timer timer = new Timer(1000, e -> label.setText(LocalTime.now().toString()));
        start.addActionListener(e -> { if (timer.isRunning()) timer.stop(); else timer.start(); });
        add(label); add(start); setSize(320,150); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() { public void windowClosed(java.awt.event.WindowEvent e) { timer.stop(); }});
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Bug3_WrongThread().setVisible(true)); }
}