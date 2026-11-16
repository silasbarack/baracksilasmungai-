import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class FileManager {
    private Path folder=Path.of(System.getProperty("user.home"));
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"Name","Type","Size (bytes)"},0){
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JLabel path=new JLabel();
    private final JFrame frame=new JFrame("Week 5: File Manager (read-only)");
    private FileManager(){
        JTable table=new JTable(model);table.setAutoCreateRowSorter(true);
        JButton choose=new JButton("Choose folder"),up=new JButton("Parent"),refresh=new JButton("Refresh");
        choose.addActionListener(e->{JFileChooser c=new JFileChooser(folder.toFile());c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(c.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){folder=c.getSelectedFile().toPath();reload();}});
        up.addActionListener(e->{if(folder.getParent()!=null){folder=folder.getParent();reload();}});
        refresh.addActionListener(e->reload());
        JPanel nav=new JPanel(new FlowLayout());nav.add(choose);nav.add(up);nav.add(refresh);
        frame.add(nav,BorderLayout.NORTH);frame.add(new JScrollPane(table),BorderLayout.CENTER);frame.add(path,BorderLayout.SOUTH);
        reload();frame.setSize(750,480);frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);frame.setVisible(true);
    }
    private void reload(){
        model.setRowCount(0);path.setText(folder.toString());
        try(java.util.stream.Stream<Path> entries=Files.list(folder)){
            entries.sorted().forEach(p->{try{model.addRow(new Object[]{p.getFileName(),
                Files.isDirectory(p)?"Folder":"File",Files.isDirectory(p)?"—":Files.size(p)});}
                catch(IOException ex){model.addRow(new Object[]{p.getFileName(),"Unreadable","—"});}});
        }catch(IOException ex){JOptionPane.showMessageDialog(frame,ex.getMessage(),"Cannot list directory",JOptionPane.ERROR_MESSAGE);}
    }
    public static void main(String[]args){SwingUtilities.invokeLater(FileManager::new);}
}