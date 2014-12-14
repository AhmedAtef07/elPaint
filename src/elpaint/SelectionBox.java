package elpaint;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Ahmed Atef
 */
public class SelectionBox {

    public enum ResizeBoxType {
        NW, N, NE, E, W, SW, S, SE;
    }    
    
    public static Color boxColor = new Color(0, 0, 255, 150);
    public static int boxHSize = 4; // Default by 3.
    
    private final Rectangle2D bound;
    private final ResizeBox[] boxes;
    // Minumum dimension in which middle boxes appear.
    private final int minDimension = 30;
    
    public SelectionBox(elShape shape) {
        bound = shape.getShape().getBounds2D();
        
        int x = (int)bound.getX();
        int y = (int)bound.getY();
        int width = (int)bound.getWidth();
        int height = (int)bound.getHeight();
        
        boxes = new ResizeBox[8];
        
        boxes[0] = new ResizeBox(x, y, ResizeBoxType.NW);
        boxes[2] = new ResizeBox(x + width, y, ResizeBoxType.NE);
        boxes[5] = new ResizeBox(x, y + height, ResizeBoxType.SW);
        boxes[7] = new ResizeBox(x + width, y + height, ResizeBoxType.SE);

        if (bound.getWidth() > minDimension) {
            boxes[1] = new ResizeBox(x + width / 2, y, ResizeBoxType.N);
            boxes[6] = new ResizeBox(x + width / 2, y + height, 
                    ResizeBoxType.S);            
        }

        if (bound.getHeight() > minDimension) {
            boxes[3] = new ResizeBox(x, y + height / 2, ResizeBoxType.W);
            boxes[4] = new ResizeBox(x + width, y + height / 2,
                    ResizeBoxType.E);            
        }
    }

    public ResizeBox[] getBoxes() {
        return boxes;
    }
    
    public class ResizeBox {
        private elRectangle rect;
        private ResizeBoxType boxType;

        public ResizeBox(elRectangle rect, ResizeBoxType boxType) {
            this.rect = rect;
            this.boxType = boxType;
        }

        public ResizeBox(int x, int y, ResizeBoxType boxType) {
            rect = new elRectangle(x - boxHSize, y - boxHSize, 
                    boxHSize * 2, boxHSize * 2);
            this.boxType = boxType;
        }
        
        public elRectangle getRect() {
            return rect;
        }

        public ResizeBoxType getBoxType() {
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
}