import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
public class StudentTableDemo {
    public static void main(String[] args){SwingUtilities.invokeLater(()->{
        JFrame f=new JFrame("Week 5: Student Table");
        DefaultTableModel m=new DefaultTableModel(new Object[][]{
            {"S101","Anne",85},{"S102","Brian",74},{"S103","Carol",93}
        },new Object[]{"Reg No.","Name","Score"}){
            public Class<?> getColumnClass(int col){return col==2?Integer.class:String.class;}
        };
        JTable t=new JTable(m);t.setAutoCreateRowSorter(true);
        t.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focused,int row,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(table,value,selected,focused,row,col);
                if(!selected)l.setBackground(((Number)value).intValue()>=80?new Color(216,244,218):Color.WHITE);
                return l;
            }
        });
        f.add(new JScrollPane(t));f.setSize(500,280);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setVisible(true);
    });}
}