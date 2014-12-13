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

    public elLine(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = Color.BLACK;
    }

    public elLine(Point p1, Point p2, Color color) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
    }
    
    public Shape getFloat() {
        return new Line2D.Float(p1, p2);
    }
}
