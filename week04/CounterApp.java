import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class CounterApp {
    private int count=0;
    public CounterApp() {
        JFrame f=new JFrame("Week 4: Counter");
        JLabel value=new JLabel("0",SwingConstants.CENTER);value.setFont(new Font("SansSerif",Font.BOLD,40));
        JPanel controls=new JPanel(new FlowLayout());
        JButton inc=new JButton("+"),dec=new JButton("-"),reset=new JButton("Reset");
        inc.addActionListener(e -> value.setText(""+(++count)));
        dec.addActionListener(e -> value.setText(""+(--count)));
        reset.addActionListener(e -> {count=0;value.setText("0");});
        controls.add(dec);controls.add(reset);controls.add(inc);
        f.add(value,BorderLayout.CENTER);f.add(controls,BorderLayout.SOUTH);
        JComponent root=f.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0),"inc");
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0),"dec");
        root.getActionMap().put("inc",new AbstractAction(){public void actionPerformed(ActionEvent e){inc.doClick();}});
        root.getActionMap().put("dec",new AbstractAction(){public void actionPerformed(ActionEvent e){dec.doClick();}});
        f.setSize(350,250);f.setLocationRelativeTo(null);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setVisible(true);
    }
    public static void main(String[] args){SwingUtilities.invokeLater(CounterApp::new);}
}