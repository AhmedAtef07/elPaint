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
        return new Rectangle2D.Float(x, y, width, height);
    }    
    
    @Override
    public elShape getCopy() {
        return new elRectangle(x, y, width, height, fillColor, borderColor,
                lineType);
    }    
}