package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Atef
 */
public class Layer extends JPanel {

//    private ArrayList<elShape> elShapes;
    private LinkedList<elShape> elShapes;
    private LinkedList<elShape> redo;
    private elShape holdedShape;
    
    public Layer(Point point, int width, int height) {
            setBounds(point.x, point.y, width, height);
            setBackground(Color.white);
            
//            elShapes = new ArrayList<>();
            elShapes = new LinkedList<>();
            redo = new LinkedList<>();
            holdedShape = null;
    }

    void popLastShape() {
        if (elShapes.size() == 0) {
            return;
        }
//        elShapes.remove(elShapes.size() - 1);
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
    
    public void setHoldedShape(elShape holdedShape) {
        this.holdedShape = holdedShape;
    }
    
    public LinkedList<elShape> getElShapes() {
        return elShapes;
    }

    public void setElShapes(LinkedList<elShape> elShapes) {
        this.elShapes = elShapes;
    }
    
    public void addShape(elShape shape) {
        elShapes.add(shape);
        redo.clear();
    }
    
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        Graphics2D g2d = (Graphics2D)g;
        for (elShape shape: elShapes) {
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
            for (elShape shape: elShapes) {
                if (shape.isSelected()) {                    
                    g2d.setColor(ResizeBox.boxColor);
                    for(ResizeBox.Box box: shape.getResizeBoxes().getBoxes()) {
                        if (box == null) {
                            continue;
                        }
//                        g2d.setColor(ResizeBox.boxColor);
                        g2d.fill(box.getRect().getShape());
                    }
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(shape.getShape().getBounds2D());
                    
                    
                }
            }
        }
    }
}