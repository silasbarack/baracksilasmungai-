import javax.swing.*;
import java.awt.*;
public class ContactListDemo {
    public static void main(String[] args){SwingUtilities.invokeLater(()->{
        JFrame f=new JFrame("Week 5: Contact List");DefaultListModel<String> names=new DefaultListModel<>();
        for(String s:new String[]{"Alice Wanjiku","Brian Otieno","Cynthia Mwangi","David Njoroge"})names.addElement(s);
        JList<String> list=new JList<>(names);
        list.setCellRenderer((l,value,index,selected,focus)->{
            JLabel label=new JLabel("●  "+value);label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
            label.setBackground(selected?l.getSelectionBackground():Color.WHITE);
            label.setForeground(selected?l.getSelectionForeground():Color.DARK_GRAY);return label;
        });
        f.add(new JScrollPane(list));f.setSize(350,300);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setVisible(true);
    });}
}