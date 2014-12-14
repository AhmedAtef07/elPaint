package elpaint;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Ahmed Atef
 */
public class ResizeBox {

    enum BoxType {
        NW, N, NE, E, W, SW, S, SE;
    }
    
    class Box {
        private elRectangle rect;
        private BoxType boxType;

        public Box(elRectangle rect, BoxType boxType) {
            this.rect = rect;
            this.boxType = boxType;
        }

        public elRectangle getRect() {
            return rect;
        }

        public BoxType getBoxType() {
            return boxType;
        }       
        
        public Cursor getCursor() {
            switch(boxType) {
            case NW:
                return new Cursor(Cursor.NW_RESIZE_CURSOR);
            case N:
                return new Cursor(Cursor.N_RESIZE_CURSOR);
            case NE:
                return new Cursor(Cursor.NE_RESIZE_CURSOR);
            case E:
                return new Cursor(Cursor.E_RESIZE_CURSOR);
            case W:
                return new Cursor(Cursor.W_RESIZE_CURSOR);
            case SW:
                return new Cursor(Cursor.SW_RESIZE_CURSOR);
            case S:
                return new Cursor(Cursor.S_RESIZE_CURSOR);
            case SE:
                return new Cursor(Cursor.SE_RESIZE_CURSOR);
            default:
                return null;
            }
        }
    }
    
    public static Color boxColor = new Color(0, 0, 255, 150);
    public static int boxHSize = 4; // Default by 3.
    
    private Rectangle2D bound;
    private Box[] boxes;
    private int minDimension = 30;
    
    public ResizeBox(elShape shape) {
        bound = shape.getShape().getBounds2D();
        
        int x = (int)bound.getX();
        int y = (int)bound.getY();
        int width = (int)bound.getWidth();
        int height = (int)bound.getHeight();
        
        // boxColor, boxColor, new BasicStroke());
        boxes = new Box[8];
        
        boxes[0] = new Box(new elRectangle(x - boxHSize, y - boxHSize, 
                boxHSize * 2, boxHSize * 2), BoxType.NW);
        boxes[2] = new Box(new elRectangle(x + width - boxHSize, y - boxHSize, 
                boxHSize * 2, boxHSize * 2), BoxType.NE);
        boxes[5] = new Box(new elRectangle(x - boxHSize, y + height - boxHSize, 
                boxHSize * 2, boxHSize * 2), BoxType.SW);
        boxes[7] = new Box(new elRectangle(x + width - boxHSize,
                y + height - boxHSize, boxHSize * 2, boxHSize * 2), BoxType.SE);

        if (bound.getWidth() > minDimension) {
          boxes[1] = new Box(new elRectangle(x + width / 2 - boxHSize, 
                  y - boxHSize, boxHSize * 2, boxHSize * 2), BoxType.N);
          boxes[6] = new Box(new elRectangle(x + width / 2 - boxHSize, 
                  y + height - boxHSize, boxHSize * 2, boxHSize * 2),
                  BoxType.S);            
        }

        if (bound.getHeight() > minDimension) {
          boxes[3] = new Box(new elRectangle(x - boxHSize, 
                  y + height / 2 - boxHSize, boxHSize * 2, boxHSize * 2), 
                  BoxType.W);
          boxes[4] = new Box(new elRectangle(x + width - boxHSize, 
                  y + height / 2 - boxHSize, boxHSize * 2, boxHSize * 2),
                  BoxType.E);            
        }
    }

    public Box[] getBoxes() {
        return boxes;
    }
    
    
}