import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author Hassan Rezk
 */

public class  elLine extends ElShape {

    @Override
    public ElShape getCopy() {
        return new elLine(linepoint1,linepoint2,thickness);
    }

    @Override
    public Shape getShape() {
        int[] tx = new int[4];
        int[] ty = new int[4];
        
        tx[0] = p1.x;
        ty[0] = p1.y;
        
        tx[1] = p2.x;
        ty[1] = p2.y;
        
        tx[2] = p4.x;
        ty[2] = p4.y;
        
        tx[3] = p3.x;
        ty[3] = p3.y;
        
        return new Polygon(tx,ty,4);
    }
    
    private Point p1,p2,p3,p4;
    private int thickness;
    private Point linepoint1, linepoint2;
    
    private void initialize( Point p1, Point p2, int thickness ) {
        linepoint1 = p1;
        linepoint2 = p2;
        
        width = Math.abs(p1.x-p2.x);
        height = Math.abs(p1.y-p2.y);
        
        this.p1 = new Point(p1.x + thickness,p1.y + thickness);
        this.p2 = new Point(p1.x - thickness,p1.y - thickness);
        p3 = new Point(p2.x + thickness,p2.y + thickness);
        p4 = new Point(p2.x - thickness,p2.y - thickness);

        this.x = Math.min(p1.x,p2.x);
        this.x = Math.min(x, p3.x);
        this.x = Math.min(x, p4.x);
        
        this.y = Math.min(p1.y,p2.y);
        this.y = Math.min(y, p3.y);
        this.y = Math.min(y, p4.y);
        
        this.thickness = thickness;
    }
    
    public elLine(Point p1, Point p2, int thickness) {
        initialize(p1, p2, thickness);
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
    
    @Override
    public Color getBorderColor() {
        return fillColor;
    }
    
    @Override
    public void move(int xDisplacment, int yDisplacment) {
        setX(getClonedX()-xDisplacment);
        setY(getClonedY()-yDisplacment);
//        linepoint2.x -= xDisplacment;
//        linepoint1.x -= xDisplacment;
//        linepoint2.y -= yDisplacment;
//        linepoint1.y -= yDisplacment;
//        
       // initialize(getClonedX(), getClonedY(), thickness);
    }
    
    @Override
    public void setX(int x) {
        linepoint2.x = x;
        linepoint1.x = linepoint2.x - width;
        initialize(linepoint1, linepoint2, thickness);
    }
    
    @Override
    public void setY(int y) {
        linepoint2.y = y;
        linepoint1.y = linepoint2.y - height;
        initialize(linepoint1, linepoint2, thickness);
    }
}
