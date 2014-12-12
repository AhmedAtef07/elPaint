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
    
    //Point[] x ;
    public elRectangle(Color FillColor,Color BorderColor,Stroke LineType
            ,Point p1 , Point p2){
        
        
        super(FillColor,BorderColor,LineType,p1,p2);
    }
    
    public elRectangle(Point p1 , Point p2){
        super(p1,p2);
    }
    
    
    @Override
    public Shape getShape() {
            int x = Math.min(getPoint(0).x, getPoint(1).x);
            int y = Math.min(getPoint(0).y,getPoint(0).y);
            int width = Math.abs(getPoint(1).x-getPoint(0).x);
            int height = Math.abs(getPoint(1).y-getPoint(0).y);
            return new Rectangle2D.Float(x,y,width,height);
    }

   

    @Override
    public boolean hasStroke() {
        return true ;
    }

    @Override
    public boolean hasFillColor() {
        return true ;
    }

    @Override
    public boolean hasBorderColor() {
        return true ;
    }
    
}
