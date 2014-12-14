package elpaint;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Hassan Rezk
 */
public class elTriangle extends elPolygon {
    
    public enum Type {
        ISOSCELES,
        RIGHT,
    }
    
    private void initialize(Point p1, Point p2, Type type) {
        Point p3;
        switch(type) {
            case RIGHT:
                p3 = new Point(p1.x, p2.y);
                break;
            default:
                p3 = new Point(p1.x + p2.x, p2.y);
                break;
        }
        x = new int[3];
        y = new int[3];
        
        x[0] = p1.x;
        x[1] = p2.x;
        x[2] = p3.x;
        
        y[0] = p1.y;
        y[1] = p2.y;
        y[2] = p3.y;
    }
    
    private Type type;
    private int width,height;
    public elTriangle(Point p, int width, int height, Type type) {
        Point p2,p3;
        switch(type) {
            case RIGHT:
                p2 = new Point(p.x,p.y+height);
                p3 = new Point(p.x+width,p.y+height);
                break;
            default:
                p.x = p.x + width/2;
                p2 = new Point(p.x+width/2,p.y+height);
                p3 = new Point(p.x-width/2,p.y+height);
                break;    
        }
        this.width = width;
        this.height = height;
        this.type = type;
        
        x = new int[3];
        y = new int[3];
        
        x[0] = p.x;
        x[1] = p2.x;
        x[2] = p3.x;
        
        y[0] = p.y;
        y[1] = p2.y;
        y[2] = p3.y;
                
    }
    
    public elTriangle(Point p, int width, int height, Type type,
            Color FillColor, Color BorderColor, 
            Stroke LineType) {
        setLineType(LineType);
        setFillColor(FillColor);
        setBorderColor(BorderColor);
        this.type = type;
        
        Point p2,p3;
        switch(type) {
            case RIGHT:
                p2 = new Point(p.x,p.y+height);
                p3 = new Point(p.x+width,p.y+height);
                break;
            default:
                p.x = p.x + width/2;
                p2 = new Point(p.x+width/2,p.y+height);
                p3 = new Point(p.x-width/2,p.y+height);
                break;    
        }
        this.width = width;
        this.height = height;
        this.type = type;
        
        x = new int[3];
        y = new int[3];
        
        x[0] = p.x;
        x[1] = p2.x;
        x[2] = p3.x;
        
        y[0] = p.y;
        y[1] = p2.y;
        y[2] = p3.y;
          
        
    }
    
    public elTriangle(Point p1, Point p2, Type type) {
        initialize(p1, p2, type);
    }
    
    public elTriangle(Color FillColor, Color BorderColor, 
            Stroke LineType, Point p1, Point p2, Type type) {
        initialize(p1, p2, type);
    }

    public elTriangle(Point p1, Point p2, Point p3) {
        super(p1,p2,p3);
    }

    public elTriangle(Color FillColor, Color BorderColor, 
            Stroke LineType, Point p1, Point p2, Point p3) {
        super(FillColor, BorderColor, LineType, p1, p2, p3);
    }
}
