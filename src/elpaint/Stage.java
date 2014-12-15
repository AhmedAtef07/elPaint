package elpaint;
 
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; 
import java.util.LinkedList;
 

public final class Stage implements Triggable {
 
    enum Mode {
        DRAWING,
        EDITING,        
    }
 
    enum ShapeType {
        RECTANGLE,
        ELLIPSE,
        LINE,
        ISOSCELES_TRIANGLE,
        RIGHT_TRIANGLE,
        POLYGON
    }    

    private final UserInterface ui;
    private final Layer layer;
    private final OperationManager opManager;
    
    
    private Properties properties;
    ArrayList<Property> propertiesList = new ArrayList<Property>();
 
    private int startX, startY;
    private ElShape holdedShape;
    private ElShape resizingRelativeShape;
    private boolean multiSelectionActivated;
    private boolean isMoving, isDragging, isResizing;
    private SelectionBox.ResizeBoxType selectedResizeBoxType;
 
    private LinkedList<ElShape> elShapes;
    private LinkedList<ElShape> copiedShapes;
 
    private Mode currentMode;
    private ShapeType currentShapeType;
 
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
        opManager = new OperationManager(elShapes);
        
        properties = ui.getProperties();
        // Only need to be initialized once, as it will always keep the 
        // reference.
        elShapes = layer.getElShapes();
        
        properties = new Properties(propertiesList, this);
 
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
                holdedShape = new ElRectangle(
                        pressedX, pressedY, width, height);
                break;
            case ELLIPSE:
                holdedShape = new ElEllipse(pressedX, pressedY, width, height);
                break;
            case ISOSCELES_TRIANGLE:
                holdedShape = new ElTriangle(new Point(pressedX, pressedY), 
                        width, height, ElTriangle.Type.ISOSCELES);
                break;
            case RIGHT_TRIANGLE:
                holdedShape = new ElTriangle(new Point(pressedX, pressedY), 
                        width, height, ElTriangle.Type.RIGHT);
                break;
            case LINE:
//                holdedShape = new elLine(new Point(pressedX, pressedY), 
//                        new Point(pressedX + width,pressedY + height));
 
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
        for (ElShape shape: elShapes) {
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
 
        for (ElShape elshape: elShapes) {
            elshape.setSelected(false);            
        }
        layer.repaint();
    }
 
    void selectAll() {
 
        for (ElShape elshape: elShapes) {
            elshape.setSelected(true);            
        }
        layer.repaint();
    }
 
    void cloneShapesList() {
        for (ElShape elshape: layer.getElShapes()) {
            elshape.cloneSelf();
        }
    }
 
    void updateToEditingMode() {    
       cloneShapesList();        
    }
 
    private LinkedList<ElShape> getSelectedShapes() {
        LinkedList<ElShape> selectedShapes = new LinkedList<>();
        for (ElShape elshape: layer.getElShapes()) {
            if (elshape.isSelected()) {
                selectedShapes.add(elshape);
            }
        }
        return selectedShapes;
    }
 
    private void deleteSelectedShapes() {
        for (int i = elShapes.size() - 1; i > -1; --i) {
             if (elShapes.get(i).isSelected()) {
                elShapes.remove(elShapes.get(i));
                --i;
            }
        }
        layer.repaint();
        cloneShapesList();
    }  

    private void copySelecetedShapes() {
        LinkedList<ElShape> newCopiedShapes = new LinkedList<>();
        for (ElShape elshape: layer.getElShapes()) {
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
        for (ElShape shape : copiedShapes) {
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
        ArrayList<ElShape> newCopiedShapes = new ArrayList<>();
        for (ElShape elshape: layer.getElShapes()) {
            if (elshape.isSelected()) {
                newCopiedShapes.add(elshape.getCopy());
            }
        }
        if (!newCopiedShapes.isEmpty()) {
            unselectAll();
            for (ElShape shape : newCopiedShapes) {
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
            // startX == 1 means you just started to drag.
            if (startX  == -1) {
                startX = x;
                startY = y;
                isMoving = false;
                isResizing = false;
                for (ElShape shape: elShapes) {
                    Rectangle bound = shape.getShape().getBounds();
                    bound.grow(SelectionBox.boxHSize * 2, 
                            SelectionBox.boxHSize * 2);
                    if (shape.isSelected() && bound.contains(new Point(x, y))) {
                        for(SelectionBox.ResizeBox box: 
                                shape.getResizeBox().getBoxes()) {
                            if (box == null) {
                                continue;
                            }
                            if (box.getRect().getShape().contains(
                                    new Point(x, y))) {
                                resizingRelativeShape = shape;
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
                    for (ElShape shape: elShapes) {
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
                for (ElShape elShape : elShapes) {           
                    if (elShape.isSelected()) {
                        elShape.resize(resizingRelativeShape, 
                                selectedResizeBoxType, new Point(x, y));
                    }        
                }
                layer.repaint();
            } else if (isMoving) {
                // Move shapes.
                opManager.execute(new OpMove(new Point(startX, startY), 
                        new Point(x, y), e.isShiftDown(), getSelectedShapes()),
                        false);
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
                    if (isMoving) {
                        opManager.execute(new OpMove(new Point(startX, startY), 
                        new Point(x, y), e.isShiftDown(), getSelectedShapes()),
                        true);
                    } else if (!isMoving && !isResizing) {
                        setSelectedShapes();
                        layer.setHoldedShape(null);
                    }                    
                    layer.repaint();
                    resetEditingFactors();                    
                }
                setProperties();
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
            ui.setCurrentMode(currentMode);
            updateToDrawingMode();
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            currentMode = Mode.EDITING;
            ui.setCurrentMode(currentMode);
            updateToEditingMode();
        }
        ui.setTitle(currentMode + "");
 
        if (e.getKeyCode() == KeyEvent.VK_R) {
            currentShapeType = ShapeType.RECTANGLE;
            ui.setButton(UserInterface.Button.RECTANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            currentShapeType = ShapeType.ELLIPSE;
            ui.setButton(UserInterface.Button.ELLIPSE);
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            currentShapeType = ShapeType.ISOSCELES_TRIANGLE;
            ui.setButton(UserInterface.Button.ISOTRIANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            currentShapeType = ShapeType.RIGHT_TRIANGLE;
            ui.setButton(UserInterface.Button.RIGHTTRIANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            currentShapeType = ShapeType.LINE;
            ui.setButton(UserInterface.Button.LINE);
        }  
 
        if (e.isControlDown() && !e.isShiftDown() && 
                e.getKeyCode() == KeyEvent.VK_Z) {
//            layer.popLastShape();
            opManager.undo();
            cloneShapesList();
            layer.repaint();
        } else if (e.isControlDown() && 
                (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) || 
                (e.getKeyCode() == KeyEvent.VK_Y)) {
//            layer.unPopLastShape();
            opManager.redo();
            cloneShapesList();
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
 
    public Mode getCurrentMode() {
        return currentMode;
    }
 
    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
        if (currentMode ==  Mode.DRAWING) {
            updateToDrawingMode();
        }
        else {
            updateToEditingMode();
        }
    }
 
    public ShapeType getCurrentShapeType() {
        return currentShapeType;
    }
 
    public void setCurrentShapeType(ShapeType currentShapeType) {
        this.currentShapeType = currentShapeType;
    }
 
    public void setProperties() {  
        propertiesList.clear();
        System.out.println("Set : " + propertiesList);
        for (ElShape elShape : elShapes) {
            if (elShape.isSelected()) {
                System.out.println("??");
                propertiesList.add(new Property(
                        " X : ", elShape.getX(), 
                        Property.PropertyName.X));
                propertiesList.add(new Property(
                        " Y : ", elShape.getY(), 
                        Property.PropertyName.Y));
                propertiesList.add(new Property(
                        " WIDTH : ", elShape.getWidth(), 
                        Property.PropertyName.WIDTH));
                propertiesList.add(new Property(
                        " HEIGHT : ", elShape.getHeight(), 
                        Property.PropertyName.HEIGHT));
                propertiesList.add(new Property(
                        " Fill Color : ", elShape.getFillColor(), 
                        Property.PropertyName.COLOR));
                System.out.println("Add :" + elShape.getFillColor());
                propertiesList.add(new Property(
                      " Stroke Collor : ", elShape.getBorderColor(), 
                        Property.PropertyName.STROKE_COLOR));
            }
        }
        //properties.update();
        ui.setProperties(properties);
    }
    
    public void checkProperties() {
        for (ElShape elShape : elShapes) {
            if (elShape.isSelected()) {   
                for(Property p : propertiesList) {
                    System.out.println("Check: "  + propertiesList);
                    //System.out.println("C " + p.getValue());
                    switch (p.getPropertyName()) {
                        case X:
                            elShape.setX((int)p.getValue());
                            break;
                        case Y:
                            elShape.setY((int)p.getValue());
                            break;
                        case WIDTH:
                            elShape.setWidth((int)p.getValue());
                            break;
                        case HEIGHT:
                            elShape.setHeight((int)p.getValue());
                            break;
                        case COLOR:
                            elShape.setFillColor((Color)p.getValue());
                            System.out.println("Ch " + (Color)p.getValue());
                            break;
                        case STROKE_COLOR:
                            elShape.setBorderColor((Color)p.getValue());
                            break;
                    }
                }
            }
        }
        layer.repaint();
    }
 
}