package elpaint;

import java.awt.*;
/**
 *
 * @author Hassan Rezk
 */

public class elPolygon extends elShape {
    
    private int[] ptx;
    private int[] pty;
    
    @Override
    public Shape getShape() {
        return new Polygon(ptx,pty,ptx.length);
    }

    @Override
    public elShape getCopy() {
        return new elPolygon();
    }
   
}
