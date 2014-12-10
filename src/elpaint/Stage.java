package elpaint;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; 

/**
 *
 * @author Ahmed Atef
 */
public class Stage implements Triggable {

    enum Mode {
        DRAWING,
        EDITING,        
    }
    
    enum ShapeType {
        RECTANGLE,
        ELLIPSE,
        LINE,
    }
    
    private UserInterface ui;
    private Layer layer;
    
    private int startX, startY;
    private elShape holdedShape;
    private boolean ctrlActived;
    
    private ArrayList<elShape> clonedShapes;
    private ArrayList<elShape> copiedShapes;
    
    Mode currentMode;
    ShapeType currentShapeType;
    
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
        
        ctrlActived = false;
        
        currentMode = Mode.DRAWING;
        currentShapeType = ShapeType.RECTANGLE;
        
//        elShape sh = new elEllipse(0, 0, 100, 100);
//        layer.addShape(sh);
        
        resetDrawingFactors();
        resetEditingFactors();
    }       

    void resetDrawingFactors() {        
        startX = -1;
        startY = -1;
    }

    void resetEditingFactors() {        
        // can't use -1 here, because the object might have these value in the 
        // beginning.
        startX = -1;
        startY = -1;
        cloneShapesList();
    }
             
    public void setSelectedShapes(Point p) {
        ArrayList<elShape> elShapes = layer.getElShapes();
        if (!ctrlActived) {
            unselectAll();
        }
        
        for (int i = elShapes.size() - 1; i != -1; --i) {
            if (elShapes.get(i).getFloat().contains(p)) {
                // Toggle selcetion.
                if (elShapes.get(i).getIsSelected()) {
                    elShapes.get(i).setIsSelected(false);
                } else {
                     elShapes.get(i).setIsSelected(true);
                }            
                break;
            } 
        }
    }
    
    
    private void drawHoldedShape(int pressedX, int pressedY, int width, int height) {
            
         switch (currentShapeType) {
            case RECTANGLE:   
                holdedShape = new elRectangle(
                        pressedX, pressedY, width, height);
                break;
            case ELLIPSE:
                holdedShape = new elEllipse(pressedX, pressedY, width, height);
                break;
         }
        holdedShape.setFillColor(Color.yellow);
        holdedShape.setBorderColor(Color.red);
        layer.setHoldedShape(holdedShape);
        layer.repaint();
    }
    
    void updateToDrawingMode() {
        // Remove everything related to editing.
        unselectAll();


        //
    }
    
    void setCursorOnAll(Cursor cursor) {
        ArrayList<elShape> elShapes = layer.getElShapes();
        for (elShape elshape: elShapes) {
            // add curosr in model.
        }
        layer.repaint();
    }
    
    void unselectAll() {
        ArrayList<elShape> elShapes = layer.getElShapes();
        for (elShape elshape: elShapes) {
            elshape.setIsSelected(false);            
        }
        layer.repaint();
    }
    
    void selectAll() {
        ArrayList<elShape> elShapes = layer.getElShapes();
        for (elShape elshape: elShapes) {
            elshape.setIsSelected(true);            
        }
        layer.repaint();
    }
    
    void cloneShapesList() {
        clonedShapes = new ArrayList<>();
        for (elShape elshape: layer.getElShapes()) {
            clonedShapes.add(elshape.getCopy());
        }
    }
    
    void updateToEditingMode() {    
       cloneShapesList();        
    }
    
    
    private void deleteSelectedShapes() {        
        ArrayList<elShape> elShapes = layer.getElShapes();
        ArrayList<elShape> newShapes = new ArrayList<>();
        for (elShape elshape: elShapes) {
            if (!elshape.getIsSelected()) {
                newShapes.add(elshape);
            }
        }
        layer.setElShapes(newShapes);
        layer.repaint();
    }
    
    private void copySelecetedShapes() {
        ArrayList<elShape> newCopiedShapes = new ArrayList<>();
        for (elShape elshape: layer.getElShapes()) {
            if (elshape.getIsSelected()) {
                newCopiedShapes.add(elshape.getCopy());
            }
        }
        if (!newCopiedShapes.isEmpty()) {
            copiedShapes = newCopiedShapes;
        }
    }
    
    private void pasteCopiedShaped() {
        unselectAll();
        for (elShape shape : copiedShapes) {
            shape.setX(shape.getX() + 10);
            shape.setY(shape.getY() + 10);
            shape.setIsSelected(true);
            layer.addShape(shape);
        }
        layer.repaint();
        cloneShapesList();
        copySelecetedShapes();
    }
    
    
    
    
    
    
    
    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = e.getPoint();
        int x = point.x - ui.getComponents()[0].getX();
        int y = point.y - ui.getComponents()[0].getY();
        
        switch (currentMode) {
            case DRAWING:    
                if (startX == -1) {
                    startX = x;
                    startY = y;
                } 
                int minX = Math.min(x, startX);
                int minY = Math.min(y,startY);
                int width = Math.abs(minX - Math.max(startX, x));
                int height = Math.abs(minY - Math.max(startY, y));
        
                drawHoldedShape(minX, minY, width, height); 
                break;
            case EDITING:      
                ArrayList<elShape> elShapes = layer.getElShapes();
                // Move shapes.
                for (int i = 0; i < elShapes.size(); i++) {
                    elShape elshape = elShapes.get(i);
                    elShape clonedShape = clonedShapes.get(i);
                    if (elshape.getIsSelected()) {
//                        elRectangle rect = (elRectangle)elshape;
                        if (startX  == -1) {
                            startX = x;
                            startY = y;
                        }                         
                        elshape.setX(clonedShape.getX() - (startX - x));
                        elshape.setY(clonedShape.getY() - (startY - y));
                    }        
                }
                layer.repaint();
                break;
        }        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:
                
                break;
        }
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {        
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:                
                Point point = e.getPoint();
                int x = point.x - ui.getComponents()[0].getX();
                int y = point.y - ui.getComponents()[0].getY();
                setSelectedShapes(new Point(x, y));
                layer.repaint();  
                break;
        }   
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (currentMode) {
            case DRAWING:
                resetDrawingFactors(); 
                System.out.println(holdedShape.toString());
                layer.addShape(holdedShape);
                layer.setHoldedShape(null);
                layer.repaint();
                break;
            case EDITING:   
            resetEditingFactors();
                break;
        } 
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
        if (e.getKeyCode() == KeyEvent.VK_1) {
            currentMode = Mode.DRAWING;
            updateToDrawingMode();
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            currentMode = Mode.EDITING;
            updateToEditingMode();
        }
        ui.setTitle(currentMode + "");

        
        if (e.getKeyCode() == KeyEvent.VK_R) {
            currentShapeType = ShapeType.RECTANGLE;
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            currentShapeType = ShapeType.ELLIPSE;
        } // add line with L later;
              
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            layer.popLastShape();
            layer.repaint();
        }
        
        if (currentMode == Mode.EDITING && e.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteSelectedShapes();
        }
        
        if (currentMode == Mode.EDITING && e.isControlDown() 
                && e.getKeyCode() == KeyEvent.VK_C) {
            copySelecetedShapes();
        }
        
        if (currentMode == Mode.EDITING && e.isControlDown() 
                && e.getKeyCode() == KeyEvent.VK_V) {
            pasteCopiedShaped();
        }
        
        if (currentMode == Mode.EDITING && e.isControlDown() 
                && e.getKeyCode() == KeyEvent.VK_A) {
            selectAll();
        }
        
        if (e.isControlDown()) {
            ctrlActived = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isControlDown()) {
            ctrlActived = false;
        }
    }
}
