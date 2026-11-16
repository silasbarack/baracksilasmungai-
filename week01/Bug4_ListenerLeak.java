import javax.swing.*;
import java.awt.*;
public class Bug4_ListenerLeak extends JFrame {
    private final Timer timer;
    public Bug4_ListenerLeak() {
        super("Fixed: Listener Lifecycle"); setLayout(new FlowLayout());
        JLabel label = new JLabel("0"); final int[] ticks = {0};
        timer = new Timer(500, e -> label.setText(Integer.toString(++ticks[0])));
        add(label); timer.start(); setSize(300,120); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { timer.stop(); }
            public void windowClosed(java.awt.event.WindowEvent e) { timer.stop(); }
        });
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Bug4_ListenerLeak().setVisible(true)); }
}