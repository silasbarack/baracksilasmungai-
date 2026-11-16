import javax.swing.*;
import java.awt.*;
public class StudentPortal {
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> {
        JFrame f = new JFrame("Riara — Student Portal Mockup");
        f.add(new JLabel("STUDENT PORTAL",SwingConstants.CENTER),BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profile", new JLabel("Student: Silas Mungai",SwingConstants.CENTER));
        tabs.addTab("Units", new JScrollPane(new JList<>(new String[]{"Event Programming","Software Engineering","Wireless Networks"})));
        tabs.addTab("Dashboard",new JLabel("Welcome to the university portal",SwingConstants.CENTER));
        f.add(tabs,BorderLayout.CENTER);
        f.add(new JLabel("Static mockup • Week 3"),BorderLayout.SOUTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setSize(650,400);f.setLocationRelativeTo(null);f.setVisible(true);
    }); }
}