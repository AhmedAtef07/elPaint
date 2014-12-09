package elpaint;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author HackerGhost
 */
public class elRectangle extends elShape {
    
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    /**
    * @return set Colors and Stroke with default value
    */
    
    public elRectangle(int x1,int y1,int x2,int y2){
       super();
    this.x1=x1;
    this.x2=x2;
    this.y1=y1;
    this.y2=y2;
    }
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param BorderColor
     * @param FillColor
     * @param LineType
     * @return make Rectangle2D.Float with specified Colors and Strokes 
     */
        public elRectangle (int x1,int y1,int x2,int y2 ,Color BorderColor,
            Color FillColor ,Stroke LineType){
        super(FillColor,BorderColor,LineType);
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }
    
    @Override
    public Shape getFloat() {
            int x = Math.min(x2, x1);
            int y = Math.min(y2,y1);
            int width = Math.abs(x2-x1);
            int height = Math.abs(y2-y1);
            return new Rectangle2D.Float(x,y,width,height);
    }

    /**
     * @return the x1
     */
    public int getX1() {
        return x1;
    }

    /**
     * @param x1 the x1 to set
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * @return the x2
     */
    public int getX2() {
        return x2;
    }

    /**
     * @param x2 the x2 to set
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * @return the y1
     */
    public int getY1() {
        return y1;
    }

    /**
     * @param y1 the y1 to set
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * @return the y2
     */
    public int getY2() {
        return y2;
    }

    /**
     * @param y2 the y2 to set
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    @Override
    public boolean hasStroke() {
        return true ;
    }

    @Override
    public boolean hasFillColor() {
        return true ;
    }

    @Override
    public boolean hasBorderColor() {
        return true ;
    }
    
}
