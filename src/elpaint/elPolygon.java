package elpaint;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Hassan Rezk
 */

public class elPolygon extends elShape {
    
    protected int[] x;
    protected int[] y;
    
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
        polygondraw = new Polygon(x, y, x.length);
    }
    
    public elPolygon(Point... p) {
        initialize(p);
    }

    public elPolygon(Color FillColor, 
            Color BorderColor, Stroke LineType, Point... p) {
        super(FillColor, BorderColor, LineType);
        initialize(p);
    }
    
    private Polygon polygondraw;
    
    @Override
    public Shape getShape() {
        return polygondraw;
    }

    @Override
    public void Rotate(double angle) {
        super.angle = angle;
        Rectangle rect = polygondraw.getBounds();
        AffineTransform transform = AffineTransform.getRotateInstance(angle, 
                rect.getCenterX(), rect.getCenterY());
        transform.createTransformedShape((Shape) this);
    }

    @Override
    public elShape getCopy() {
        Point[] p = new Point[x.length];
        for( int i = 0 ; i < p.length ; i ++ )
            p[i] = new Point(x[i],y[i]);
        return new elPolygon(getFillColor(), getBorderColor(), getLineType(), p);
    }
    @Override
    public int getX() {
        return polygondraw.getBounds().x;
    }

    @Override
    public int getY() {
        return polygondraw.getBounds().y;
    }

    @Override
    public void setX(int x) {
        for ( int i = 0 ; i < this.x.length ; i ++ )
            this.x[i] += x;
    }

    @Override
    public void setY(int y) {
        for ( int i = 0 ; i < this.y.length ; i ++ )
            this.y[i] += y;
    }
}
