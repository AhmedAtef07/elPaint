package elpaint;

import java.awt.*;

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
        x[3] = p3.x;
        
        y[0] = p1.y;
        y[1] = p2.y;
        y[3] = p3.y;
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
    
    @Override
    public Shape getFloat() {
        return super.getFloat();
    }
}
