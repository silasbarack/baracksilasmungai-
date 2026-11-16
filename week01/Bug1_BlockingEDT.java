import javax.swing.*;
import java.awt.*;
public class Bug1_BlockingEDT extends JFrame {
    public Bug1_BlockingEDT() {
        super("Fixed: Blocking EDT"); setLayout(new FlowLayout());
        JLabel status = new JLabel("Ready"); JButton button = new JButton("Process");
        button.addActionListener(e -> {
            button.setEnabled(false); status.setText("Working...");
            new SwingWorker<Void,Void>() {
                protected Void doInBackground() throws Exception { Thread.sleep(1500); return null; }
                protected void done() { status.setText("Done!"); button.setEnabled(true); }
            }.execute();
        });
        add(status); add(button); setSize(320,150); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Bug1_BlockingEDT().setVisible(true)); }
}