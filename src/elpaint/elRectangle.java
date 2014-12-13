package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;


/**
 *
 * @author HackerGhost
 */
public class elRectangle extends elPolygon {
    
    public elRectangle (int x, int y, int width, int height) {
        super(new Point(x, y), new Point(x + width, y + height));
    }    
    
    public elRectangle (int x, int y, int width, int height, 
                Color FillColor, Color BorderColor ,Stroke LineType) {
        super(FillColor, BorderColor, LineType, 
                new Point(x, y), new Point(x + width, y + height));
    }
      
    @Override
    public Shape getShape() {
            int x = Math.min(getPoint(0).x, getPoint(1).x);
            int y = Math.min(getPoint(0).y, getPoint(0).y);
            int width = Math.abs(getPoint(1).x - getPoint(0).x);
            int height = Math.abs(getPoint(1).y - getPoint(0).y);
            return new Rectangle2D.Float(x, y, width, height);
    }
    
}