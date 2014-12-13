package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
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
}