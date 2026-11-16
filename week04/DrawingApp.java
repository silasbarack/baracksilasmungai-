import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class DrawingApp extends JPanel {
    private final java.util.List<Point> points=new ArrayList<>();
    private int x=100,y=100;
    public DrawingApp(){
        setBackground(Color.WHITE);setFocusable(true);
        addMouseMotionListener(new MouseMotionAdapter(){public void mouseDragged(MouseEvent e){points.add(e.getPoint());repaint();}});
        addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT->x-=10;case KeyEvent.VK_RIGHT->x+=10;case KeyEvent.VK_UP->y-=10;case KeyEvent.VK_DOWN->y+=10;
        }repaint();}});
        addMouseListener(new MouseAdapter(){public void mousePressed(MouseEvent e){requestFocusInWindow();}});
    }
    protected void paintComponent(Graphics g){super.paintComponent(g);g.setColor(Color.BLUE);for(Point p:points)g.fillOval(p.x,p.y,5,5);
        g.setColor(Color.RED);g.fillRect(x,y,30,30);}
    public static void main(String[] args){SwingUtilities.invokeLater(()->{JFrame f=new JFrame("Week 4: Draw with mouse, move red square with arrows");
        DrawingApp panel=new DrawingApp();f.add(panel);f.setSize(600,400);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);panel.requestFocusInWindow();});}
}