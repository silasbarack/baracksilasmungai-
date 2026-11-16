import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.*;
public class MiniEditor {
    private Path currentFile;
    private final JTextArea area=new JTextArea();
    private final JFrame frame=new JFrame("Week 5: Mini Text Editor");
    private MiniEditor(){
        JMenuBar bar=new JMenuBar();JMenu file=new JMenu("File");
        JMenuItem open=new JMenuItem("Open"),save=new JMenuItem("Save As"),clear=new JMenuItem("New");
        open.addActionListener(e->{
            JFileChooser c=new JFileChooser();if(c.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION)
                try{currentFile=c.getSelectedFile().toPath();area.setText(Files.readString(currentFile));}
                catch(IOException ex){showError(ex);}
        });
        save.addActionListener(e->{
            JFileChooser c=new JFileChooser();if(currentFile!=null)c.setSelectedFile(currentFile.toFile());
            if(c.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION)
                try{currentFile=c.getSelectedFile().toPath();Files.writeString(currentFile,area.getText());}
                catch(IOException ex){showError(ex);}
        });
        clear.addActionListener(e->{currentFile=null;area.setText("");});
        file.add(clear);file.add(open);file.add(save);bar.add(file);
        JToolBar tools=new JToolBar();JButton openBtn=new JButton("Open"),saveBtn=new JButton("Save As");
        openBtn.addActionListener(e->open.doClick());saveBtn.addActionListener(e->save.doClick());tools.add(openBtn);tools.add(saveBtn);
        frame.setJMenuBar(bar);frame.add(tools,BorderLayout.NORTH);frame.add(new JScrollPane(area));
        frame.setSize(700,500);frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);frame.setVisible(true);
    }
    private void showError(IOException ex){JOptionPane.showMessageDialog(frame,ex.getMessage(),"File error",JOptionPane.ERROR_MESSAGE);}
    public static void main(String[]args){SwingUtilities.invokeLater(MiniEditor::new);}
}