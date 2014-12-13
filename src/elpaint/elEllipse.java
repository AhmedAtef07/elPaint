package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author HackerGhost
 */
public class elEllipse extends elShape {
    private Point point1 ;
    private Point point2 ;
    /**
     * 
     * @param point1
     * @param point2 
     */
    public elEllipse(Point point1 ,Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        ellipsedraw = new Ellipse2D.Float(point1.x, point1.y, 
                point2.x - point1.x, point2.y-point1.y);
    }
    /**
     * 
     * @param Point1
     * @param Point2
     * @param FillColor
     * @param BorderColor
     * @param LineType 
     */
    public elEllipse(Point Point1 , Point Point2 , Color FillColor ,
            Color BorderColor ,Stroke LineType){
        super(FillColor,BorderColor,LineType);
        this.point1 = Point1;
        this.point2 = Point2; 
        ellipsedraw = new Ellipse2D.Float(Point1.x, Point1.y, 
                Point2.x - Point1.x, Point2.y-Point1.y);
    }
    private Point temp ;
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public elEllipse(int x1,int y1,int x2 ,int y2){
        super();
        temp = new Point(x1,y1);
        this.point1 = temp;
        temp = new Point (x2,y2);
        this.point2 = temp;
        ellipsedraw = new Ellipse2D.Float(point1.x, point1.y, 
                point2.x - point1.x, point2.y-point1.y);
    }
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param FillColor
     * @param BorderColor
     * @param LineType 
     */
    public elEllipse(int x1,int y1,int x2 ,int y2,Color FillColor ,
            Color BorderColor , Stroke LineType){
        super(FillColor,BorderColor,LineType);
        temp = new Point(x1,y1);
        this.point1 = temp;
        temp = new Point (x2,y2);
        this.point2 = temp;
        ellipsedraw = new Ellipse2D.Float(point1.x, point1.y, 
                point2.x - point1.x, point2.y-point1.y);
    }
    /**
     * 
     * @return Ellipse2D.Float object can be draw in graphics 
     */
    @Override
    public Shape getShape() {
        return ellipsedraw;
    } 
    
    private Ellipse2D.Float ellipsedraw;
    
    @Override
    public void Rotate(double angle) {
        super.angle = angle;
        Rectangle rect = ellipsedraw.getBounds();
        AffineTransform transform = AffineTransform.getRotateInstance(angle, 
                rect.getCenterX(), rect.getCenterY());
        transform.createTransformedShape((Shape) this);
    }

    @Override
    public elShape getCopy() {
        return new elEllipse(point1.x, point1.y, point2.x, point2.y, 
                getFillColor(), getBorderColor(), getLineType());
    }

    @Override
    public int getX() {
        return ellipsedraw.getBounds().x;
    }

    @Override
    public int getY() {
        return ellipsedraw.getBounds().y;
    }

    @Override
    public void setX(int x) {
        point1.x += x;
        point2.x += x;
    }

    @Override
    public void setY(int y) {
        point1.y += y;
        point2.y += y;
    }
}
