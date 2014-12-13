package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Ahmed Atef
 */
public class Stage implements Triggable {
    
    private UserInterface ui;
    private Layer layer;
    
    private int startX, startY;
    private elShape holdedShape;
    
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
        
        resetDrawingFactors();
    }       

    void resetDrawingFactors() {        
        startX = -1;
        startY = -1;
    }
         
    private void drawHoldedShape(int pressedX, int pressedY, int width,
            int height) {            
        holdedShape = new elRectangle(pressedX, pressedY, width, height);         
        holdedShape.setFillColor(Color.yellow);
        holdedShape.setBorderColor(Color.red);
        layer.setHoldedShape(holdedShape);
        layer.repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = e.getPoint();
        int x = point.x - ui.getComponents()[0].getX();
        int y = point.y - ui.getComponents()[0].getY();
        if (startX == -1) {
            startX = x;
            startY = y;
        } 
        int minX = Math.min(x, startX);
        int minY = Math.min(y,startY);
        int width = Math.abs(minX - Math.max(startX, x));
        int height = Math.abs(minY - Math.max(startY, y));
        drawHoldedShape(minX, minY, width, height); 
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        resetDrawingFactors();
        layer.addShape(holdedShape);
        layer.setHoldedShape(null);
        layer.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}