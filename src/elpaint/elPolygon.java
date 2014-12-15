package elpaint;

import java.awt.*;
import java.awt.geom.AffineTransform;
/**
 *
 * @author Hassan Rezk
 */

public class elPolygon extends elShape {
    
    private int[] ptx;
    private int[] pty;
    private int radius, n;
    private double angel;
    
    public elPolygon(int x, int y, int radius, int n, double angel) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.width = 2*radius;
        this.height =2*radius;
        this.n = n;
        this.angel = angel;
        ptx = new int[n];
        pty = new int[n];
        makePoints();
    }
    public elPolygon(int x,int y,int radius,int n,double angel,
            Color FillColor,Color StrokeColor,Stroke LineType){
        super(FillColor,StrokeColor,LineType);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.width = 2*radius;
        this.height =2*radius;
        this.n = n;
        this.angel = angel;
        ptx = new int[n];
        pty = new int[n];
        makePoints();
    }
    
    private void makePoints(){
        for (int i = 0; i < getN(); i++) {
            ptx[i] = (int)(this.x + getRadius() * Math.cos(2 * Math.PI * i / getN()));
            pty[i] = (int)(this.y + getRadius() * Math.sin(2 * Math.PI * i / getN()));
            System.out.println(ptx[i] + " " + pty[i]);
        }
    }
    @Override
    public Shape getShape() {
        
        AffineTransform at = AffineTransform.getRotateInstance(angel, x, y);
        Shape rotated = at.createTransformedShape(new Polygon(ptx, pty, n));
       // Shape rotated = new Polygon(ptx , pty , getN());
        return new Polygon(ptx,pty,n);
    }
//    public void Move(int displacement ,){
//        switch(){
//            case NE : 
//                   
//        }
        
//    }
    @Override
    public elShape getCopy() {
        return new elPolygon(x, y, getRadius(), getN(), getAngel());
    }

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * @return the angel
     */
    public double getAngel() {
        return angel;
    }

    /**
     * @param angel the angel to set
     */
    public void setAngel(double angel) {
        this.angel = angel;
    }
    
}
