package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
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
            g2d.fill(shape.getFloat());
            g2d.setColor(shape.getBorderColor());
            g2d.setStroke(shape.getLineType());
            g2d.draw(shape.getFloat());
        }
        if (holdedShape != null) {
            g2d.setColor(Color.gray);
            g2d.draw(holdedShape.getFloat());            
        } else {
            for (elShape shape: elShapes) {
                if (shape.getIsSelected()) {
                    Rectangle boundry = shape.getFloat().getBounds();
//                    elShape selectionShape = 
                    g2d.setColor(Color.blue);
                    float dash[] = { 10.0f };
//                    g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
//                         BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(boundry);            
                    
                    
                    
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(Color.black);
                    int smallRectSize = 3;
                    elRectangle sbNW = new elRectangle(
                            boundry.x - smallRectSize, 
                            boundry.y - smallRectSize, 
                            2 * smallRectSize, 
                            2 * smallRectSize);
                    g2d.fill(sbNW.getFloat());    
                    
                    elRectangle sbSE = new elRectangle(
                            boundry.x + boundry.width - smallRectSize, 
                            boundry.y + boundry.height - smallRectSize, 
                            2 * smallRectSize, 
                            2 * smallRectSize);
                    g2d.fill(sbSE.getFloat());         
                }
            }
        }
    }
}
