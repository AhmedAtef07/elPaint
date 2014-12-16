package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Atef
 */

public class Layer extends JPanel {

    // 'elShapes' must be final to always have only one reference.
    private final LinkedList<ElShape> elShapes;
    private LinkedList<ElShape> redo;
    private ElShape holdedShape;
    Color color;

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
    
    public Layer(Point point, int width, int height) {
        setBounds(point.x, point.y, width, height);
        setBackground(Color.white);

        elShapes = new LinkedList<>();
        redo = new LinkedList<>();
        holdedShape = null;
        color = Color.WHITE;
    }

    void popLastShape() {
        if (elShapes.size() == 0) {
            return;
        }
        redo.add(elShapes.peekLast());
        elShapes.removeLast();
    }
    
    void unPopLastShape() {
        if (redo.size() == 0) {
            return;
        }        
        elShapes.add(redo.peekLast());
        redo.removeLast();
    }
    
    public void setHoldedShape(ElShape holdedShape) {
        this.holdedShape = holdedShape;
    }
    
    public LinkedList<ElShape> getElShapes() {
        return elShapes;
    }
    
    public void addShape(ElShape shape) {
        elShapes.add(shape);
        redo.clear();
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        //g.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (ElShape shape: elShapes) {
            g2d.setColor(shape.getFillColor());
            g2d.fill(shape.getShape());
            g2d.setColor(shape.getBorderColor());
            g2d.setStroke(shape.getLineType());
            g2d.draw(shape.getShape());
        }
        if (holdedShape != null) {
            g2d.setColor(Color.gray);
            g2d.draw(holdedShape.getShape());            
        } else {
            for (ElShape shape: elShapes) {
                if (shape.isSelected()) {                    
                    g2d.setColor(SelectionBox.boxColor);
                    for(SelectionBox.ResizeBox box: 
                            shape.getResizeBox().getBoxes()) {
                        if (box == null) {
                            continue;
                        }
                        g2d.fill(box.getRect().getShape());
                    }
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(shape.getShape().getBounds2D());                    
                }
            }
        }                  
    }
    
    public BufferedImage getPNG() {
        JFileChooser chooser = new JFileChooser();
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), 
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

        for (ElShape shape: elShapes) {
            g2d.setColor(shape.getFillColor());
            g2d.fill(shape.getShape());
            g2d.setColor(shape.getBorderColor());
            g2d.setStroke(shape.getLineType());
            g2d.draw(shape.getShape());
        }
        if (holdedShape != null) {
            g2d.setColor(Color.gray);
            g2d.draw(holdedShape.getShape());            
        } else {
            for (ElShape shape: elShapes) {
                if (shape.isSelected()) {                    
                    g2d.setColor(SelectionBox.boxColor);
                    for(SelectionBox.ResizeBox box: 
                            shape.getResizeBox().getBoxes()) {
                        if (box == null) {
                            continue;
                        }
                        g2d.fill(box.getRect().getShape());
                    }
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(shape.getShape().getBounds2D());                    
                }
            }
        }  
        return bi;
    }
    
}