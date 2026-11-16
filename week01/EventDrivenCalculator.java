import javax.swing.*;
import java.awt.*;
public class EventDrivenCalculator extends JFrame {
    private final JTextField display = new JTextField("0");
    private Double first = null;
    private String operator;
    private boolean newNumber = true;
    public EventDrivenCalculator() {
        super("Week 1 — Event-Driven Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        display.setEditable(false); display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        add(display, BorderLayout.NORTH);
        JPanel buttons = new JPanel(new GridLayout(4, 4, 5, 5));
        for (String key : new String[]{"7","8","9","/","4","5","6","*","1","2","3","-","0","C","=","+"}) {
            JButton b = new JButton(key);
            b.addActionListener(e -> handle(key));
            buttons.add(b);
        }
        add(buttons, BorderLayout.CENTER);
        setSize(350, 380); setLocationRelativeTo(null);
    }
    private void handle(String key) {
        if (key.matches("[0-9]")) {
            display.setText(newNumber || display.getText().equals("0") ? key : display.getText() + key);
            newNumber = false; return;
        }
        if ("C".equals(key)) { display.setText("0"); first = null; operator = null; newNumber = true; return; }
        try {
            double current = Double.parseDouble(display.getText());
            if ("=".equals(key)) {
                if (first == null || operator == null) return;
                double result = ProceduralCalculator.calculate(first, operator, current);
                display.setText(Double.toString(result)); first = null; operator = null; newNumber = true;
            } else { first = current; operator = key; newNumber = true; }
        } catch (RuntimeException ex) { display.setText("Error"); first = null; operator = null; newNumber = true; }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new EventDrivenCalculator().setVisible(true)); }
}