package elpaint;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author HackerGhost
 */
public class elRectangle extends elShape {
    

    public elRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    /**
    * @return set Colors and Stroke with default value
    */
    

    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param BorderColor
     * @param FillColor
     * @param LineType
     * @return make Rectangle2D.Float with specified Colors and Strokes 
     */
        public elRectangle (int x, int y, int width, int height, 
                Color FillColor, Color BorderColor ,Stroke LineType){
        super(FillColor, BorderColor, LineType);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
        
    @Override
    public Shape getShape() {
//            int x = Math.min(x2, x1);
//            int y = Math.min(y2,y1);
//            int width = Math.abs(x2-x1);
//            int height = Math.abs(y2-y1);
            return new Rectangle2D.Float(x, y, width, height);
    }   
    

    public boolean hasStroke() {
        return true ;
    }


    public boolean hasFillColor() {
        return true ;
    }


    public boolean hasBorderColor() {
        return true ;
    }

    public elShape getCopy() {
        return new elRectangle(x, y, width, height, FillColor, BorderColor,
                LineType);
    }
    
}