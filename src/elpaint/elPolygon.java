package elpaint;

import java.awt.*;

/**
 *
 * @author Hassan Rezk
 */

public class elPolygon extends elShape {
    
    private int[] x;
    private int[] y;
    
    /**
     * @param index
     * [Important]
     * @return (-1,-1) in case of accessing out of bound element
     */ 
    public Point getPoint(int index) { 
        return index >= 0 && index < x.length ? 
                new Point(x[index],y[index]) : new Point(-1,-1);
    }
    
    /**
     * 
     * @param p
     * @param index 
     * do nothing in case of accessing out of bound element
     */
    public void setPoint(Point p, int index) {
        if(index >= 0 && index < x.length) {
            x[index] = p.x;
            y[index] = p.y;
        }
    }
    
    private void initialize(Point[] p) {
        x = new int[p.length];
        y = new int[p.length];
        for(int i = 0 ; i < p.length ; i ++ ) {
            x[i] = p[i].x;
            y[i] = p[i].y;
        }
    }
    
    public elPolygon(Point... p) {
        initialize(p);
    }

    public elPolygon(Color FillColor, 
            Color BorderColor, Stroke LineType, Point... p) {
        super(FillColor, BorderColor, LineType);
        initialize(p);
    }

    @Override
    public Shape getFloat() {
        return new Polygon(x, y, x.length);
    }

    @Override
    public elShape getCopy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
  
}
