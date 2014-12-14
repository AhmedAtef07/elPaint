package elpaint;

import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author Hassan Rezk
 */

public class  elLine extends elComponent {

    private Point p1, p2;
    private Color color;
    
    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Color getColor() {
        return color;
    }

    public double getAngle() {
        return angle;
    }

    public elLine(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = Color.BLACK;
        linedraw = new Line2D.Float(p1, p2); 
    }

    public elLine(Point p1, Point p2, Color color) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        linedraw = new Line2D.Float(p1, p2); 
    }
    
    public Shape getShape() {
        return new Line2D.Float(p1, p2);
    }
    
    private Line2D.Float linedraw;
    private double angle;
    
    public void Rotate(double angle) {
        this.angle = angle;
        Rectangle rect = linedraw.getBounds();
        AffineTransform transform = AffineTransform.getRotateInstance(angle, 
                rect.getCenterX(), rect.getCenterY());
        transform.createTransformedShape((Shape) this);
    }
    
    public int getX() {
        return linedraw.getBounds().x;
    }

    public int getY() {
        return linedraw.getBounds().y;
    }
    
    public void setX(int x) {
        p1.x += x;
        p2.x += x;
    }

    public void setY(int y) {
        p1.y += y;
        p2.y += y;
    }
}
