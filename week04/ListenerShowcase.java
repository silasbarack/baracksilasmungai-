import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class ListenerShowcase {
    private static final String[] FOOD = {"Margherita","Pepperoni","Vegetarian"};
    public static void main(String[] args) { SwingUtilities.invokeLater(ListenerShowcase::show); }
    private static void show() {
        JFrame f = new JFrame("Week 4: Action, Item & Document Listeners");
        JTabbedPane tabs = new JTabbedPane();
        JPanel actions = new JPanel(new FlowLayout()); JLabel actionStatus = new JLabel("Click any button");
        JButton lambda = new JButton("Lambda");
        lambda.addActionListener(e -> actionStatus.setText("Lambda source: "+((JButton)e.getSource()).getText()));
        JButton anonymous = new JButton("Anonymous"); anonymous.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) { actionStatus.setText("Anonymous listener"); }
        });
        JButton method = new JButton("Method reference"); method.addActionListener(e -> update(actionStatus,"Method-style handler"));
        actions.add(lambda);actions.add(anonymous);actions.add(method);actions.add(actionStatus); tabs.add("Actions",actions);
        JPanel pizza = new JPanel(new FlowLayout()); JComboBox<String> type = new JComboBox<>(FOOD);
        JCheckBox extra = new JCheckBox("Extra cheese (+100)"); JLabel total = new JLabel();
        Runnable recalc = () -> total.setText("Total KES "+(800 + type.getSelectedIndex()*200 + (extra.isSelected()?100:0)));
        type.addItemListener(e -> { if(e.getStateChange()==ItemEvent.SELECTED) recalc.run(); });
        extra.addItemListener(e -> recalc.run()); recalc.run();
        pizza.add(type);pizza.add(extra);pizza.add(total);tabs.add("Pizza Order",pizza);
        JPanel docs = new JPanel(new BorderLayout());
        JTextField search = new JTextField(); DefaultListModel<String> model = new DefaultListModel<>();
        String[] products = {"Laptop","Tablet","Phone","Mouse","Keyboard","Monitor"};
        JList<String> results = new JList<>(model); JLabel chars = new JLabel("0 characters");
        search.getDocument().addDocumentListener(new DocumentListener(){
            private void changed(){String q=search.getText().toLowerCase(Locale.ROOT); model.clear();
                for(String product:products) if(product.toLowerCase(Locale.ROOT).contains(q))model.addElement(product);
                chars.setText(search.getText().length()+" characters");
            }
            public void insertUpdate(DocumentEvent e){changed();}
            public void removeUpdate(DocumentEvent e){changed();}
            public void changedUpdate(DocumentEvent e){changed();}
        });
        for(String product:products)model.addElement(product);
        docs.add(search,BorderLayout.NORTH);docs.add(new JScrollPane(results),BorderLayout.CENTER);docs.add(chars,BorderLayout.SOUTH);
        tabs.add("Live Search",docs); f.add(tabs);
        f.setSize(650,400);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);f.setLocationRelativeTo(null);f.setVisible(true);
    }
    private static void update(JLabel label,String text){label.setText(text);}
}