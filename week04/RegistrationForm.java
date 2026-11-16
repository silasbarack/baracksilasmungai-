import javax.swing.*;
import java.awt.*;
public class RegistrationForm {
    public static void main(String[] args){SwingUtilities.invokeLater(()->{
        JFrame f=new JFrame("Week 4: Student Registration");JPanel form=new JPanel(new GridLayout(4,2,8,8));
        JTextField name=new JTextField(),number=new JTextField(),email=new JTextField();
        JComboBox<String> program=new JComboBox<>(new String[]{"BBIT","Computer Science","IT"});
        form.add(new JLabel("Full name"));form.add(name);form.add(new JLabel("Admission number"));form.add(number);
        form.add(new JLabel("Email"));form.add(email);form.add(new JLabel("Programme"));form.add(program);
        JLabel status=new JLabel("Ready");JPanel buttons=new JPanel();
        JButton submit=new JButton("Submit"),clear=new JButton("Clear"),exit=new JButton("Exit");
        submit.addActionListener(e->{if(name.getText().isBlank()||number.getText().isBlank()||!email.getText().matches("[^@ ]+@[^@ ]+\\.[^@ ]+"))
            status.setText("Enter valid name, admission number and email");else status.setText("Submitted: "+name.getText());});
        clear.addActionListener(e->{name.setText("");number.setText("");email.setText("");program.setSelectedIndex(0);status.setText("Cleared");});
        exit.addActionListener(e->f.dispose());buttons.add(submit);buttons.add(clear);buttons.add(exit);
        f.add(form,BorderLayout.CENTER);f.add(buttons,BorderLayout.NORTH);f.add(status,BorderLayout.SOUTH);
        f.setSize(550,280);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setVisible(true);
    });}
}