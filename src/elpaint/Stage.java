package elpaint;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; 
import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public final class Stage implements Triggable {

    enum Mode {
        DRAWING,
        EDITING,        
    }
    
    enum ShapeType {
        RECTANGLE,
        ELLIPSE,
        LINE,
    }
    
    private final UserInterface ui;
    private final Layer layer;
    
    private int startX, startY;
    private elShape holdedShape;
    private boolean multiSelectionActivated;
    private boolean isMoving, isDragging, isResizing;
    SelectionBox.ResizeBoxType selectedResizeBoxType;
    
    private LinkedList<elShape> elShapes;
    private LinkedList<elShape> clonedShapes;
    private LinkedList<elShape> copiedShapes;
    
    Mode currentMode;
    ShapeType currentShapeType;
    
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
        // Only need to be initialized once, as it will always keep the 
        // reference.
        elShapes = layer.getElShapes();
        
        multiSelectionActivated = false;
        isDragging = false;
        currentMode = Mode.DRAWING;
        currentShapeType = ShapeType.RECTANGLE;
        
        resetDrawingFactors();
        resetEditingFactors();
    }       

    void resetDrawingFactors() {        
        startX = -1;
        startY = -1;
    }

    void resetEditingFactors() {        

        startX = -1;
        startY = -1;
        cloneShapesList();
    }
             
    public void setSelectedShapes(Point p) {
         
        if (!multiSelectionActivated) {
            unselectAll();
        }
        
        for (int i = elShapes.size() - 1; i != -1; --i) {
            if (elShapes.get(i).getShape().contains(p)) {
                // Toggle selcetion.
                if (elShapes.get(i).isSelected()) {
                    elShapes.get(i).setSelected(false);
                } else {
                     elShapes.get(i).setSelected(true);
                }            
                break;
            } 
        }
    }
    
    /**
     * Add shapes which are totally contained in selection region.
     */
    public void setSelectedShapes() {
        if (!multiSelectionActivated) {
            unselectAll();
        }        
        for (int i = elShapes.size() - 1; i != -1; --i) {
            if (holdedShape.getShape().contains(
                    elShapes.get(i).getShape().getBounds2D())) {
                // Toggle selcetion.
                if (elShapes.get(i).isSelected()) {
                    elShapes.get(i).setSelected(false);
                } else {
                     elShapes.get(i).setSelected(true);
                }      
            } 
        }
    }    
    
    private void drawHoldedShape(int pressedX, int pressedY, int width, 
            int height) {            
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
        layer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


        //
    }
    
    void setCursorOnAll(Point point) {         
        boolean foundAny = false;
        for (elShape shape: elShapes) {
            Rectangle bound = shape.getShape().getBounds();
            bound.grow(SelectionBox.boxHSize * 2, SelectionBox.boxHSize * 2);
            if (shape.isSelected() && bound.contains(point)) {                    
                for(SelectionBox.ResizeBox box: 
                        shape.getResizeBox().getBoxes()) {
                    if (box == null) {
                        continue;
                    }
                    if (box.getRect().getShape().contains(point)) {
                        layer.setCursor(box.getCursor());                            
                        foundAny = true;
                        break;
                    }
                }
            }
            if (foundAny) {
                break;
            }
            if (shape.getShape().contains(point) && !foundAny) {
                    if (shape.isSelected()) {
                        layer.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    } else {
                        layer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    foundAny = true;
                    break;
                }                   
        }
        if (!foundAny) {
            layer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    void unselectAll() {
         
        for (elShape elshape: elShapes) {
            elshape.setSelected(false);            
        }
        layer.repaint();
    }
    
    void selectAll() {
         
        for (elShape elshape: elShapes) {
            elshape.setSelected(true);            
        }
        layer.repaint();
    }
    
    void cloneShapesList() {
        clonedShapes = new LinkedList<>();
        for (elShape elshape: layer.getElShapes()) {
            clonedShapes.add(elshape.getCopy());
        }
    }
    
    void updateToEditingMode() {    
       cloneShapesList();        
    }
    
    
    private void deleteSelectedShapes() {              
        LinkedList<elShape> newShapes = new LinkedList<>();
        for (elShape elshape: elShapes) {
            if (!elshape.isSelected()) {
                newShapes.add(elshape);
            }
        }
        setNewShapesReference(newShapes);
        layer.repaint();
        cloneShapesList();
    }
    
    void setNewShapesReference(LinkedList<elShape> newShapesListRef) {
        layer.setElShapes(newShapesListRef);
        elShapes = layer.getElShapes();
    }
    private void copySelecetedShapes() {
        LinkedList<elShape> newCopiedShapes = new LinkedList<>();
        for (elShape elshape: layer.getElShapes()) {
            if (elshape.isSelected()) {
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
            shape.setSelected(true);
            layer.addShape(shape);
        }
        layer.repaint();
        copySelecetedShapes();
        cloneShapesList();
    }
    
    private void duplicateSelectedShapes() {
        ArrayList<elShape> newCopiedShapes = new ArrayList<>();
        for (elShape elshape: layer.getElShapes()) {
            if (elshape.isSelected()) {
                newCopiedShapes.add(elshape.getCopy());
            }
        }
        if (!newCopiedShapes.isEmpty()) {
            unselectAll();
            for (elShape shape : newCopiedShapes) {
                shape.setX(shape.getX() + 37);
                shape.setY(shape.getY() + 17);
                shape.setSelected(true);
                layer.addShape(shape);
            }
            layer.repaint();
            cloneShapesList();
        }        
    }    
    
    @Override
    public void mouseDragged(MouseEvent e) {
        isDragging = true;
        Point point = e.getPoint();
        int x = point.x - ui.getComponents()[0].getX();
        int y = point.y - ui.getComponents()[0].getY();
        switch (currentMode) {
            case DRAWING:    
                if (startX == -1) {
                    startX = x;
                    startY = y;
                } 
                int minX, minY, width, height;
                if (e.isShiftDown()) {
                    minX = Math.min(x, startX);
                    minY = Math.min(y,startY);
                    width = Math.max(Math.abs(minX - Math.max(startX, x)), 
                            Math.abs(minY - Math.max(startY, y)));
                    height = width;
                    if (x >= startX && y >= startY) {
                    } else if (x > startX && y < startY) {
                        minY = Math.min(y, startY - height);              
                    } else if (x < startX && y > startY) {
                       minX = Math.min(x, startX - width);
                    } else if (x < startX && y < startY) {
                       minX = Math.min(x, startX - width);
                       minY = Math.min(y, startY - height);
                    }
                } else {
                    minX = Math.min(x, startX);
                    minY = Math.min(y,startY);
                    width = Math.abs(minX - Math.max(startX, x));
                    height = Math.abs(minY - Math.max(startY, y));
                }        
                drawHoldedShape(minX, minY, width, height); 
                break;
            case EDITING:
                // startX != -1 means you are curruntly drawing the selection 
                // region.
                if (startX  == -1) {
                    startX = x;
                    startY = y;
                    isMoving = false;
                    isResizing = false;
                    for (elShape shape: elShapes) {
                        Rectangle bound = shape.getShape().getBounds();
                        bound.grow(SelectionBox.boxHSize * 2, 
                                SelectionBox.boxHSize * 2);
                        if (shape.isSelected() && bound.contains(
                                new Point(x, y))) {
                            for(SelectionBox.ResizeBox box: 
                                    shape.getResizeBox().getBoxes()) {
                                if (box == null) {
                                    continue;
                                }
                                if (box.getRect().getShape().contains(
                                        new Point(x, y))) {
                                    selectedResizeBoxType = box.getBoxType();
                                    isResizing = true;
                                    break;
                                }
                            }
                        }
                        if (isResizing) {
                            break;
                        }
                    }
                    // If resizing it will never be moving.
                    if (!isResizing) {                        
                        for (elShape shape: elShapes) {
                            // A selected objectd and started dragging over it.
                            if (shape.isSelected() && shape.getShape().contains(
                                    new Point(x, y))) {
                                isMoving = true;
                                break;
                            }                   
                        }
                    }
                } 
                
                if (isResizing) {
                    for (int i = 0; i < elShapes.size(); i++) {
                        elShape elshape = elShapes.get(i);
                        elShape clonedShape = clonedShapes.get(i);
                        if (elshape.isSelected()) {
                            switch(selectedResizeBoxType) {
                            case NW:
                                elshape.setY(Math.min(y, clonedShape.getY() + 
                                        clonedShape.getHeight())); 
                                elshape.setHeight(Math.abs((
                                        clonedShape.getHeight() + 
                                                clonedShape.getY()) - y));
                                elshape.setX(Math.min(x, clonedShape.getX() + 
                                        clonedShape.getWidth())); 
                                elshape.setWidth(Math.abs((
                                        clonedShape.getWidth() + 
                                                clonedShape.getX()) - x));                                
                                break;
                            case N:
                                elshape.setY(Math.min(y, clonedShape.getY() + 
                                        clonedShape.getHeight())); 
                                elshape.setHeight(Math.abs((
                                        clonedShape.getHeight() + 
                                                clonedShape.getY()) - y));
                                break;
                            case NE:
                                elshape.setY(Math.min(y, clonedShape.getY() + 
                                        clonedShape.getHeight())); 
                                elshape.setHeight(Math.abs((
                                        clonedShape.getHeight() + 
                                                clonedShape.getY()) - y));
                                elshape.setX(Math.min(x, clonedShape.getX()));                            
                                elshape.setWidth(
                                        Math.abs(x - clonedShape.getX()));
                                break;
                            case E:
                                elshape.setX(Math.min(x, clonedShape.getX()));                            
                                elshape.setWidth(
                                        Math.abs(x - clonedShape.getX()));
                                break;
                            case W:
                                elshape.setX(Math.min(x, clonedShape.getX() + 
                                        clonedShape.getWidth())); 
                                elshape.setWidth(Math.abs((
                                        clonedShape.getWidth() + 
                                                clonedShape.getX()) - x));
                                break;
                            case SW:
                                elshape.setY(Math.min(y, clonedShape.getY()));                            
                                elshape.setHeight(
                                        Math.abs(y - clonedShape.getY()));                               
                                elshape.setX(Math.min(x, clonedShape.getX() + 
                                        clonedShape.getWidth())); 
                                elshape.setWidth(Math.abs((
                                        clonedShape.getWidth() + 
                                                clonedShape.getX()) - x));                                
                                break;
                            case S:
                                elshape.setY(Math.min(y, clonedShape.getY()));                            
                                elshape.setHeight(
                                        Math.abs(y - clonedShape.getY()));
                                break;
                            case SE:
                                elshape.setY(Math.min(y, clonedShape.getY()));                            
                                elshape.setHeight(
                                        Math.abs(y - clonedShape.getY()));                               
                                elshape.setX(Math.min(x, clonedShape.getX()));                            
                                elshape.setWidth(
                                        Math.abs(x - clonedShape.getX()));                                
                                break;
                            default:
                                break;
                            }
                        }        
                    }
                    layer.repaint();
                } else if (isMoving) {                    
                    // Move shapes.
                    for (int i = 0; i < elShapes.size(); i++) {
                        elShape elshape = elShapes.get(i);
                        elShape clonedShape = clonedShapes.get(i);
                        if (elshape.isSelected()) {
                            if (e.isShiftDown()) {
                                System.out.println(x + " " + y);
                                if (Math.abs(x - startX) < 
                                        Math.abs(startY - y)) {
                                    elshape.setX(clonedShape.getX());
                                    elshape.setY(clonedShape.getY() - (
                                            startY - y));                                      
                                } else {
                                    elshape.setX(clonedShape.getX() - (
                                            startX - x));                                    
                                    elshape.setY(clonedShape.getY());
                                }
                            } else {                                
                                elshape.setX(clonedShape.getX() - (startX - x));
                                elshape.setY(clonedShape.getY() - (startY - y));
                            }
                        }        
                    }
                    layer.repaint();
                } else {
                    minX = Math.min(x, startX);
                    minY = Math.min(y,startY);
                    width = Math.abs(minX - Math.max(startX, x));
                    height = Math.abs(minY - Math.max(startY, y));
        
                    drawHoldedShape(minX, minY, width, height); 
                }
                break;
            default:
                break;
        }        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() - ui.getComponents()[0].getX();
        int y = e.getY() - ui.getComponents()[0].getY();
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:  
                setCursorOnAll(new Point(x, y));                
                break;
        }
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {          
        int x = e.getX() - ui.getComponents()[0].getX();
        int y = e.getY() - ui.getComponents()[0].getY();
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:                
                Point point = e.getPoint();
                setSelectedShapes(new Point(x, y));
                layer.repaint();  
                break;
        }   
        setCursorOnAll(new Point(x, y));    
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {        
        int x = e.getX() - ui.getComponents()[0].getX();
        int y = e.getY() - ui.getComponents()[0].getY();  
        switch (currentMode) {
            case DRAWING:
                if (isDragging) {
                    resetDrawingFactors(); 
                    cloneShapesList();
                    layer.addShape(holdedShape);
                    layer.setHoldedShape(null);
                    layer.repaint();
                }
                break;
            case EDITING: 
                if (isDragging) {
                    if (!isMoving && !isResizing) {
                        setSelectedShapes();
                        layer.setHoldedShape(null);
                    }
                    layer.repaint();
                    resetEditingFactors();
                }
                break;
        } 
        isDragging = false;
        setCursorOnAll(new Point(x, y));  
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
              
        if (e.isControlDown() && !e.isShiftDown() && 
                e.getKeyCode() == KeyEvent.VK_Z) {
            layer.popLastShape();
            layer.repaint();
        } else if (e.isControlDown() && 
                (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) || 
                (e.getKeyCode() == KeyEvent.VK_Y)) {
            layer.unPopLastShape();
            layer.repaint();
        }
        
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedShapes();
                }        
                if (e.isControlDown() 
                        && e.getKeyCode() == KeyEvent.VK_C) {
                    copySelecetedShapes();
                }
                if (e.isControlDown() 
                        && e.getKeyCode() == KeyEvent.VK_V) {
                    pasteCopiedShaped();
                }
                if (e.isControlDown() 
                        && e.getKeyCode() == KeyEvent.VK_D) {
                    duplicateSelectedShapes();
                }
                if (e.isControlDown() 
                        && e.getKeyCode() == KeyEvent.VK_A) {
                    selectAll();
                }
                if (e.isControlDown() || e.isShiftDown()) {
                    multiSelectionActivated = true;
                }
                break;
            default:
                break;
        }      
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isControlDown() && !e.isShiftDown() ) {
            multiSelectionActivated = false;
        }
    }
}