

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Atef
 */

public class Layer extends JPanel {

    // 'elShapes' must be final to always have only one reference.
    private final LinkedList<ElShape> elShapes;
    private final LinkedList<Integer> magnetLineX;
    private final LinkedList<Integer> magnetLineY;
    private ElShape holdedShape;
    Color color;
    
    public Layer(Point point, int width, int height) {
        setBounds(point.x, point.y, width, height);
        setBackground(Color.white);

        elShapes = new LinkedList<>();
        magnetLineX = new LinkedList<>();
        magnetLineY = new LinkedList<>();
        
        holdedShape = null;
        color = Color.WHITE;
    }
    
    public void setHoldedShape(ElShape holdedShape) {
        this.holdedShape = holdedShape;
    }
    
    public LinkedList<ElShape> getElShapes() {
        return elShapes;
    }
    
    public void addShape(ElShape shape) {
        elShapes.add(shape);
    }    
    
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void addMagnetLineOnX(int xPosition) {
        magnetLineX.add(xPosition);
    }

    public void addMagnetLineOnY(int yPosition) {
        magnetLineY.add(yPosition);
    }
    
    public void clearMagnetLines() {
        magnetLineX.clear();
        magnetLineY.clear();
    }
    public BufferedImage getImage(Stage.SaveType saveType) {
        if (saveType == Stage.SaveType.JPG) {            
            elShapes.addFirst(new ElRectangle(-7, -7, getWidth() + 17, 
                    getHeight() + 17, Color.WHITE, Color.WHITE, 
                    new BasicStroke()));
            repaint();
        }
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
        
        if (saveType == Stage.SaveType.JPG) {            
            elShapes.removeFirst();
        }
        return bi;
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
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
            holdedShape.setAlphaFactor(0.65);
            g2d.setColor(holdedShape.getFillColor());
            g2d.fill(holdedShape.getShape());
            g2d.setColor(holdedShape.getBorderColor());
            g2d.setStroke(holdedShape.getLineType());
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
        
        g2d.setColor(new Color(255, 0, 0, 130));
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 10.0f, new float[] {5.0f}, 0.0f));
        for (Integer x : magnetLineX) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (Integer y : magnetLineY) {
            g2d.drawLine(0, y, getWidth(), y);            
        }
        
        // g2d.scale(2, 1.5);
    }
    
    
}