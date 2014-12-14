package elpaint;

import java.awt.*;
import javafx.scene.shape.TriangleMesh;
/**
 *
 * @author Hassan Rezk
 */
public class elTriangle extends elShape {
    
    public enum Type {
        ISOSCELES,
        RIGHT,
    }
    
    private Type type;
    private Point p1,p2,p3;
    
    public elTriangle(Point p, int width, int height, Type type) {
        this.x = p.x;
        this.y = p.y;
        this.width = width;
        this.height = height;
        this.type = type;
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x,p1.y+height);
                p3 = new Point(p1.x+width,p1.y+height);
                break;
            default:
                p1 = new Point(p.x+width/2,p.y);
                p2 = new Point(p1.x+width/2,p1.y+height);
                p3 = new Point(p1.x-width/2,p1.y+height);
                break;    
        }
    }

    @Override
    public elShape getCopy() {
        return new elTriangle(new Point(this.x,this.y), width, height, type);
    }

    @Override
    public Shape getShape() {
        int[] ptx = new int[3];
        int[] pty = new int[3];
        
        ptx[0] = p1.x;
        ptx[1] = p2.x;
        ptx[2] = p3.x;
        
        pty[0] = p1.y;
        pty[1] = p2.y;
        pty[2] = p3.y;
        
        return new Polygon(ptx, pty, 3);
    }

    @Override
    public void setX(int x) {
        //int width = this.width + this.x - x;
        this.x = x;
        Point p = new Point(x,y);
        this.width = width;
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x,p1.y+height);
                p3 = new Point(p1.x+width,p1.y+height);
                break;
            default:
                p1 = new Point(p.x+width/2,p.y);
                p2 = new Point(p1.x+width/2,p1.y+height);
                p3 = new Point(p1.x-width/2,p1.y+height);
                break;    
        }
    }

    @Override
    public void setY(int y) {
        //int height = this.height + this.y - y;
        this.y = y;
        Point p = new Point(x,y);
        this.height = height;
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x,p1.y+height);
                p3 = new Point(p1.x+width,p1.y+height);
                break;
            default:
                p1 = new Point(p.x+width/2,p.y);
                p2 = new Point(p1.x+width/2,p1.y+height);
                p3 = new Point(p1.x-width/2,p1.y+height);
                break;    
        }
    }
        
}
