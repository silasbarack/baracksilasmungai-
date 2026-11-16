import javax.swing.*;
import java.awt.*;
public class SwingTour {
    public static void main(String[] args) { SwingUtilities.invokeLater(SwingTour::show); }
    private static void show() {
        JFrame frame = new JFrame("Week 3: Swing Components & Layouts");
        JTabbedPane tabs = new JTabbedPane();
        JPanel components = new JPanel(new FlowLayout());
        components.add(new JLabel("Name:")); components.add(new JTextField(12));
        components.add(new JButton("Button")); components.add(new JCheckBox("Subscribe"));
        components.add(new JRadioButton("Option")); components.add(new JComboBox<>(new String[]{"BBIT","Computer Science","IT"}));
        components.add(new JScrollPane(new JTextArea("Text area",3,12)));
        tabs.addTab("Components",components);
        JPanel border = new JPanel(new BorderLayout());
        for (String side : new String[]{BorderLayout.NORTH,BorderLayout.SOUTH,BorderLayout.EAST,BorderLayout.WEST,BorderLayout.CENTER})
            border.add(new JButton(side),side);
        tabs.addTab("BorderLayout",border);
        JPanel grid = new JPanel(new GridLayout(3,3,4,4));
        for (int i=1;i<=9;i++) grid.add(new JButton(Integer.toString(i)));
        tabs.addTab("GridLayout",grid);
        JPanel combined = new JPanel(new BorderLayout()); combined.add(new JLabel("Header"),BorderLayout.NORTH);
        JPanel main = new JPanel(new FlowLayout()); main.add(new JButton("Overview")); main.add(new JButton("Settings"));
        combined.add(main,BorderLayout.CENTER); combined.add(new JLabel("Status: Ready"),BorderLayout.SOUTH);
        tabs.addTab("Combined",combined);
        frame.add(tabs); frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650,450); frame.setLocationRelativeTo(null); frame.setVisible(true);
    }
}