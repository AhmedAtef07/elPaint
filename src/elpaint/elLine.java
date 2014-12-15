package elpaint;

import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author Hassan Rezk
 */

public class  ElLine extends ElShape {

    @Override
    public ElShape getCopy() {
        return new ElLine(p1,p2);
    }

    @Override
    public Shape getShape() {
        int[] tx = new int[2];
        int[] ty = new int[2];
        tx[0] = p1.x;
        tx[1] = p2.x;
        ty[0] = p1.y;
        ty[1] = p2.y;
        return new Polygon(tx,ty,2);
    }
    
    private Point p1,p2;
    private int width,height;
    
    public ElLine(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.x = p1.x;
        this.y = p1.y;
        this.width = Math.abs(p2.x - p1.x);
        this.height = Math.abs(p2.y - p1.y);
    }
    
    @Override
    public void move(int xDisplacment, int yDisplacment) {
        setX(getClonedX()-xDisplacment);
        setY(getClonedY()-yDisplacment);
    }
    
    @Override
    public void setX(int x) {
        this.x = x;
        Point p = new Point(x, y);
        p1 = new Point(p);
        p2 = new Point(p1.x, p1.y + height);
        this.width = Math.abs(p2.x - p1.x);
        this.height = Math.abs(p2.y - p1.y);
    }
    
    @Override
    public void setY(int y) {
        this.y = y;
        Point p = new Point(x,y);
        p1 = new Point(p);
        p2 = new Point(p1.x, p1.y + height);
        this.width = Math.abs(p2.x - p1.x);
        this.height = Math.abs(p2.y - p1.y);
    }
}
