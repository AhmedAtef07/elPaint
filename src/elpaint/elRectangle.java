package elpaint;

import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Body1_1Impl;
import java.awt.Shape;
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
    
    
    public elRectangle(int x1,int y1,int x2,int y2){
    this.x1=x1;
    this.x2=x2;
    this.y1=y1;
    this.y2=y2;
    }
    
    @Override
    public Shape getFloat() {
            int x = Math.min(x2, x1);
            
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
    
}
