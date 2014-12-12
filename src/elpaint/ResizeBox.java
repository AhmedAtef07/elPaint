package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Ahmed Atef
 */
public class ResizeBox {

    public static Color boxColor = Color.BLACK;
    
    private Rectangle2D bound;
    private elRectangle[] boxes;
    private int boxHSize = 3;
    private int minDimension = 30;
    
    public ResizeBox(elShape shape) {
        bound = shape.getFloat().getBounds2D();
        
        int x = (int)bound.getX();
        int y = (int)bound.getY();
        int width = (int)bound.getWidth();
        int height = (int)bound.getHeight();
        
        // boxColor, boxColor, new BasicStroke());
        boxes = new elRectangle[8];
        
        boxes[0] = new elRectangle(x - boxHSize, y - boxHSize, 
                boxHSize * 2, boxHSize * 2);
        boxes[2] = new elRectangle(x + width - boxHSize, y - boxHSize, 
                boxHSize * 2, boxHSize * 2);
        boxes[5] = new elRectangle(x - boxHSize, y + height - boxHSize, 
                boxHSize * 2, boxHSize * 2);
        boxes[7] = new elRectangle(x + width - boxHSize, y + height - boxHSize, 
                boxHSize * 2, boxHSize * 2);
        
        if (bound.getWidth() > minDimension) {
          boxes[1] = new elRectangle(x + width / 2 - boxHSize, y - boxHSize, 
                  boxHSize * 2, boxHSize * 2);
          boxes[6] = new elRectangle(x + width / 2 - boxHSize, 
                  y + height - boxHSize, boxHSize * 2, boxHSize * 2);            
        }
        
        if (bound.getHeight() > minDimension) {
          boxes[3] = new elRectangle(x - boxHSize, y + height / 2 - boxHSize, 
                  boxHSize * 2, boxHSize * 2);
          boxes[4] = new elRectangle(x + width - boxHSize, 
                  y + height / 2 - boxHSize, boxHSize * 2, boxHSize * 2);            
        }
    }

    public elRectangle[] getBoxes() {
        return boxes;
    }
    
    
}
