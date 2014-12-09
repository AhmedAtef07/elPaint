package elpaint;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

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
    
    public void setPoint(Point p, int index) {
        if(index >= 0 && index < x.length) {
            x[index] = p.x;
            y[index] = p.y;
        }
    }
    
    private void initialize(ArrayList<Point> p) {
        x = new int[p.size()];
        y = new int[p.size()];
        for(int i = 0 ; i < p.size() ; i ++ ) {
            x[i] = p.get(i).x;
            y[i] = p.get(i).y;
        }
    }
    
    public elPolygon(ArrayList<Point> p) {
        initialize(p);
    }

    public elPolygon(ArrayList<Point> p, Color FillColor, 
            Color BorderColor, Stroke LineType) {
        super(FillColor, BorderColor, LineType);
        initialize(p);
    }

    @Override
    public Shape getFloat() {
        return new Polygon(x, y, x.length);
    }
    
}
