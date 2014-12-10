package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author HackerGhost
 */
public class elEllipse extends elShape {
    private Point Point1 ;
    private Point Point2 ;

    public elEllipse(Point Point1 ,Point Point2) {
        super();
        this.Point1 = Point1;
        this.Point2 = Point2;
    }
    
    public elEllipse(Point Point1 , Point Point2 , Color FillColor , Color BorderColor ,Stroke LineType){
        super(FillColor,BorderColor,LineType);
        this.Point1 = Point1;
        this.Point2 = Point2;    
    }
    Point temp ;
    public elEllipse(int x1,int y1,int x2 ,int y2){
        super();
        temp = new Point(x1,y1);
        this.Point1 = temp;
        temp = new Point (x2,y2);
        this.Point2 = temp;
    }
    public elEllipse(int x1,int y1,int x2 ,int y2,Color FillColor , Color BorderColor , Stroke LineType){
        super(FillColor,BorderColor,LineType);
        temp = new Point(x1,y1);
        this.Point1 = temp;
        temp = new Point (x2,y2);
        this.Point2 = temp;
    }
   
    @Override
    public Shape getFloat() {
        
        return new Ellipse2D.Float();
    }

    @Override
    public boolean hasStroke() {
        return true;
    }

    @Override
    public boolean hasFillColor() {
        return true ;
    }

    @Override
    public boolean hasBorderColor() {
        return true;
    }

    
   
}
