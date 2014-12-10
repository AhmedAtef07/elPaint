package elpaint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Atef
 */
public class Layer extends JPanel {
    
    private ArrayList<elShape> elShapes;
    private elShape holdedShape;
    
    public Layer(Point point, int width, int height) {
            setBounds(point.x, point.y, width, height);
            setBackground(Color.white);
            
            elShapes = new ArrayList<>();
            holdedShape = null;
    }

    public void setHoldedShape(elShape holdedShape) {
        this.holdedShape = holdedShape;
    }
    
    public void addShape(elShape shape) {
        elShapes.add(shape);
    }
    
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        Graphics2D g2d = (Graphics2D)g;
        for (elShape shape: elShapes) {
            g2d.setColor(shape.getFillColor());
            g2d.fill(shape.getFloat());
            g2d.setColor(shape.getBorderColor());
            g2d.setStroke(shape.getLineType());
            g2d.draw(shape.getFloat());
        }
        if (holdedShape != null) {
            g2d.setColor(Color.gray);
            g2d.draw(holdedShape.getFloat());            
        }
    }
}
