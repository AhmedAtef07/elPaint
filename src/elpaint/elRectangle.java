package elpaint;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author HackerGhost
 */
public class ElRectangle extends ElShape {    

    public ElRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
  
    public ElRectangle (int x, int y, int width, int height,
            Color fillColor, Color borderColor, Stroke lineType) {
        super(fillColor, borderColor, lineType);;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
        
    @Override
    public Shape getShape() {
        return new Rectangle2D.Float(x, y, width, height);
    }    
    
    @Override
    public ElShape getCopy() {
        return new ElRectangle(x, y, width, height, fillColor, borderColor,
                lineType);
    }    
}