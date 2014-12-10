package elpaint;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Ahmed Atef
 */
public class elEllipse extends elShape {

    public elEllipse(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
   
    public elEllipse (int x, int y, int width, int height, 
                Color FillColor, Color BorderColor ,Stroke LineType){
        super(FillColor, BorderColor, LineType);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public Ellipse2D.Float getFloat() {
        return new Ellipse2D.Float(x, y, width, height);
    }
    
        @Override
    public elShape getCopy() {
        return new elEllipse(x, y, width, height, FillColor, BorderColor,
                LineType);
    }
}
