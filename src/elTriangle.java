

import java.awt.*;
/**
 *
 * @author Hassan Rezk
 */
public class ElTriangle extends ElShape {
    
    public enum Type {
        ISOSCELES,
        RIGHT,
    }
    
    private Type type;
    private Point p1, p2, p3;
    
    public ElTriangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        
        if ( p1.x == p2.x ) {
            this.type = Type.RIGHT;
            this.x = p1.x;
            this.y = p1.y;
            this.height = p2.y - p1.y;
            this.width = p3.x - p1.x;
        } else {
            this.type = Type.ISOSCELES;
            this.x = p2.x;
            this.y = p1.y;
            this.height = p2.y - p1.y;
            this.width = p3.x - p2.x;
        }
    }
    
    public ElTriangle(Point p, int width, int height, Type type) {
        this.x = p.x;
        this.y = p.y;
        this.width = width;
        this.height = height;
        this.type = type;
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x, p1.y + height);
                p3 = new Point(p1.x + width, p1.y + height);
                break;
            default:
                p1 = new Point(p.x + width / 2, p.y);
                p2 = new Point(p1.x + width / 2, p1.y + height);
                p3 = new Point(p1.x - width / 2, p1.y + height);
                break;    
        }
    }
    
    public ElTriangle(Point p, int width, int height, Type type, 
            Color fillColor, Color borderColor ,Stroke lineType) {
        super(fillColor, borderColor, lineType);
        this.x = p.x;
        this.y = p.y;
        this.width = width;
        this.height = height;
        this.type = type;
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x, p1.y + height);
                p3 = new Point(p1.x + width, p1.y + height);
                break;
            default:
                p1 = new Point(p.x + width / 2, p.y);
                p2 = new Point(p1.x + width / 2, p1.y + height);
                p3 = new Point(p1.x - width / 2, p1.y + height);
                break;    
        }
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
        this.x = x;
        Point p = new Point(x, y);
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x, p1.y + height);
                p3 = new Point(p1.x + width, p1.y + height);
                break;
            default:
                p1 = new Point(p.x + width / 2, p.y);
                p2 = new Point(p1.x + width / 2, p1.y + height);
                p3 = new Point(p1.x - width / 2, p1.y + height);
                break;    
        }
    }

    @Override
    public void setY(int y) {
        this.y = y;
        Point p = new Point(x,y);
        switch(type) {
            case RIGHT:
                p1 = new Point(p);
                p2 = new Point(p1.x, p1.y + height);
                p3 = new Point(p1.x + width, p1.y + height);
                break;
            default:
                p1 = new Point(p.x + width / 2, p.y);
                p2 = new Point(p1.x + width / 2, p1.y + height);
                p3 = new Point(p1.x - width / 2, p1.y + height);
                break;    
        }
    }


    @Override
    public void move(int xDisplacment, int yDisplacment) {
        setX(getClonedX()-xDisplacment);
        setY(getClonedY()-yDisplacment);
    }
       
    @Override
    public ElShape getCopy() {
        return new ElTriangle(new Point(this.x, this.y), width, height, type, 
                fillColor, borderColor, lineType);
    } 
}
