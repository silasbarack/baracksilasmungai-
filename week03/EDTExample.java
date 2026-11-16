import javax.swing.*;
import java.awt.*;
public class EDTExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Week 3: Responsive EDT");
            JLabel status = new JLabel("EDT? "+SwingUtilities.isEventDispatchThread());
            JButton run = new JButton("Run long task"); run.addActionListener(e -> {
                run.setEnabled(false); status.setText("Working in background...");
                new SwingWorker<String,Void>() {
                    protected String doInBackground() throws Exception { Thread.sleep(1200); return "Finished without blocking EDT"; }
                    protected void done() {
                        try { status.setText(get()); } catch (Exception ex) { status.setText(ex.getMessage()); }
                        run.setEnabled(true);
                    }
                }.execute();
            });
            frame.setLayout(new FlowLayout()); frame.add(run); frame.add(status);
            frame.setSize(450,150); frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); frame.setVisible(true);
        });
    }
}