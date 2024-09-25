package utiles;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    int radian = 15;

    public MyButton(String label) {
        super(label);
        setFocusable(false);
        Dimension size = getPreferredSize();
        setPreferredSize(size);
        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(new Color(65, 105, 225));
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, radian, radian);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, radian, radian);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds()))
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }
}
