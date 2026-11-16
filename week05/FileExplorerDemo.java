import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
public class FileExplorerDemo {
    public static void main(String[] args){SwingUtilities.invokeLater(()->{
        JFrame f=new JFrame("Week 5: JTree & Tabs");DefaultMutableTreeNode root=new DefaultMutableTreeNode("Projects");
        DefaultMutableTreeNode java=new DefaultMutableTreeNode("Java");
        java.add(new DefaultMutableTreeNode("Event Programming"));java.add(new DefaultMutableTreeNode("Swing"));
        root.add(java);root.add(new DefaultMutableTreeNode("JavaFX"));
        JTabbedPane tabs=new JTabbedPane();tabs.addTab("Explorer",new JScrollPane(new JTree(root)));
        JPanel settings=new JPanel(new FlowLayout());settings.add(new JCheckBox("Show hidden",false));
        settings.add(new JComboBox<>(new String[]{"List","Details"}));tabs.addTab("Settings",settings);
        f.add(tabs);f.setSize(500,360);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setVisible(true);
    });}
}