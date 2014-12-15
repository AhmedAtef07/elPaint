package elpaint;

import java.awt.*;
/**
 *
 * @author Hassan Rezk
 */

public class ElPolygon extends ElShape {
    
    private int[] ptx;
    private int[] pty;
    
    @Override
    public Shape getShape() {
        return new Polygon(ptx, pty, ptx.length);
    }

    @Override
    public ElShape getCopy() {
        return new ElPolygon();
    }
   
}
