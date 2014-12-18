
 
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
 

public final class Stage implements Triggable {
 
    enum Mode {
        DRAWING,
        EDITING,        
    } 

    private enum MoveDir {
        UP, 
        DOWN, 
        LEFT, 
        RIGHT,
    }
    
    public enum SaveType {
        XML,
        PNG,
        JPG,
    }
    
    private final UserInterface ui;
    private final Layer layer;
    private final OperationManager opManager;
    
    
    private Properties properties;
    private ArrayList<Property> propertiesList = new ArrayList<Property>();
 
    private int startX, startY;
    private ElShape holdedShape;
    private ElShape resizingRelativeShape;
    private boolean multiSelectionActivated;
    private boolean isMoving, isDragging, isResizing;
    private SelectionBox.ResizeBoxType selectedResizeBoxType;
 
    private final LinkedList<ElShape> elShapes;
    private LinkedList<ElShape> copiedShapes;
 
    private Mode currentMode;
    private ElShape.Type currentShapeType;
    
    private final int[] magnetLineX;
    private final int[] magnetLineY;
    
    private final int magnetPadding = 20;
    private final int smallJump = 10;
    private final int bigJump = 35;
 
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
        elShapes = layer.getElShapes();
        opManager = new OperationManager(elShapes);
        
        properties = ui.getProperties();
        // Only need to be initialized once, as it will always keep the 
        // reference.
        
        properties = new Properties(propertiesList, this);
 
        multiSelectionActivated = false;
        isDragging = false;
        currentMode = Mode.DRAWING;
        currentShapeType = ElShape.Type.RECTANGLE;
        
        magnetLineX = new int[5000];
        magnetLineY = new int[5000];
        resetField();
        
        resetDrawingFactors();
        resetEditingFactors();
    }       
 
    private void resetField() {
        for (int x = 0; x < magnetLineX.length; x++) {
            magnetLineX[x] = x;            
        }
        for (int y = 0; y < magnetLineY.length; y++) {
            magnetLineY[y] = y;            
        }          
    }     
    
    private void updateField() {
        resetField();
        for (int x = 0; x < magnetLineX.length; x++) {
            magnetLineX[x] = x;            
        }
        for (int y = 0; y < magnetLineY.length; y++) {
            magnetLineY[y] = y;            
        }
        for (ElShape elShape : elShapes) {          
            if (elShape.isSelected()) {
                continue;
            }
            
            int x = elShape.getX();
            int y = elShape.getY();
            int width = elShape.getWidth();
            int height = elShape.getHeight();
            
            for (int i = - magnetPadding / 2; i != magnetPadding / 2 + 1; i++) {
                if (Math.min(x + i, x) >= 0) {
                    magnetLineX[x + i] = magnetLineX[x];                    
                }
                if (Math.min(x + width + i, x + width) >= 0) {
                    magnetLineX[x + width + i] = magnetLineX[x + width];  
                }
                if (Math.min(y + i, y) >= 0) {
                    magnetLineY[y + i] =  magnetLineY[y];   
                }
                if (Math.min(y + height + i, y + height) >= 0) {
                    magnetLineY[y + height + i] = magnetLineY[y + height];
                }
            }
        }
    }
    
    private void resetDrawingFactors() {        
        startX = -1;
        startY = -1;
    }
 
    private void resetEditingFactors() { 
        startX = -1;
        startY = -1;
        cloneShapesList();
    }
 
    private void setSelectedShapes(Point p) { 
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
        setProperties();
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
        setProperties();
    }    
 
    private void drawHoldedShape(int pressedX, int pressedY, int width, 
            int height) { 
        
        int attractedX = pressedX;
        int attractedY = pressedY;
        int attractedWidth = width;
        int attractedHeight = height;
        if (currentMode == Mode.DRAWING) {
            if (pressedX >= 0) {
                attractedX = magnetLineX[pressedX];            
            }
            if (pressedY >= 0) {
                attractedY = magnetLineY[pressedY];
            }
            attractedWidth = magnetLineX[width + pressedX] - attractedX;
            attractedHeight = magnetLineY[height + pressedY] - attractedY;            
        }

        attractedWidth = Math.max(attractedWidth, 5);
        attractedHeight = Math.max(attractedHeight, 5);
        
        switch (currentShapeType) {
            case RECTANGLE:   
                holdedShape = new ElRectangle(
                        attractedX, attractedY, attractedWidth, attractedHeight);
                break;
            case ELLIPSE:
                holdedShape = new ElEllipse(attractedX, attractedY, attractedWidth, attractedHeight);
                break;
            case ISOSCELES_TRIANGLE:
                holdedShape = new ElTriangle(new Point(attractedX, attractedY), 
                        attractedWidth, attractedHeight, ElTriangle.Type.ISOSCELES);
                break;
            case RIGHT_TRIANGLE:
                holdedShape = new ElTriangle(new Point(attractedX, attractedY), 
                        attractedWidth, attractedHeight, ElTriangle.Type.RIGHT);
                break;
            case LINE:
                if (attractedX == startX) {
                    attractedX += attractedWidth;
                }
                if (attractedY == startY) {
                    attractedY += attractedHeight;
                }
                holdedShape = new elLine(new Point(startX, startY),
                        new Point(attractedX, attractedY), 1);
                break;        
            case POLYGON:
                if ( attractedY != startY ){
                double angle = (attractedX - startX) / (attractedY - startY);
                holdedShape = new elPolygon(new Point(startX, startY),
                        new Point(attractedX, attractedY), 5, angle);
                }
                break;        
        }
        switch (currentMode) {
            case DRAWING:
                holdedShape.setFillColor(Color.yellow);
                holdedShape.setBorderColor(Color.red);
                holdedShape.setLineType(new BasicStroke(2));
                break;
            case EDITING:
                holdedShape.setFillColor(Color.lightGray);
                holdedShape.setBorderColor(Color.gray);
                holdedShape.setLineType(new BasicStroke(1));
                break;
        }
        layer.setHoldedShape(holdedShape);
        layer.repaint();
        cloneShapesList();
    }
 
    private void updateToDrawingMode() {
        // Remove everything related to editing.
        unselectAll();
        layer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
 
 
        //
    }
 
    private void setCursorOnAll(Point point) {         
        boolean foundAny = false;
        for (int i = elShapes.size() - 1; i != -1; --i) {
            ElShape shape = elShapes.get(i);  
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
 
    private void unselectAll() { 
        for (ElShape elshape: elShapes) {
            elshape.setSelected(false);            
        }
        layer.repaint();
        setProperties();
    }
 
    private void selectAll() { 
        for (ElShape elshape: elShapes) {
            elshape.setSelected(true);            
        }
        layer.repaint();
        setProperties();
    }
 
    private void cloneShapesList() {
        resetField();
        for (ElShape elshape: layer.getElShapes()) {
            elshape.cloneSelf();
        }
        updateField();
        setProperties();
    }
 
    private void updateToEditingMode() {    
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
        opManager.execute(new OpDelete(getSelectedShapes()), true);
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
        LinkedList<ElShape> paddedShapes = new LinkedList<>();
        for (ElShape shape : copiedShapes) {
            shape.setX(shape.getX() + 10);
            shape.setY(shape.getY() + 10);
            shape.setSelected(true);
            paddedShapes.add(shape);
        }
        opManager.execute(new OpAdd(paddedShapes), true);
        layer.repaint();
        copySelecetedShapes();
        cloneShapesList();
    }
 
    private void duplicateSelectedShapes() {
        LinkedList<ElShape> newCopiedShapes = new LinkedList<>();
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
            }
            opManager.execute(new OpAdd(newCopiedShapes), true);
            layer.repaint();
            cloneShapesList();
        }        
    }    
    
    private void moveSelectedInDir(MoveDir moveDir, boolean biggerJump) {
        int displacment;
        if (biggerJump) {
            displacment = bigJump;
        } else {
            displacment = smallJump;
        }
        int x = 0;
        int y = 0;
        switch (moveDir) {
            case LEFT:
                x = -displacment;
                break;
            case UP:
                y = -displacment;
                break;
            case RIGHT:
                x = displacment;
                break;
            case DOWN:
                y = displacment;
                break;
        }
        opManager.execute(new OpMove(new Point(0, 0), new Point(x, y), 
                false, getSelectedShapes()), true);
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
                                updateField();
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
                opManager.execute(new OpResize(resizingRelativeShape, 
                        selectedResizeBoxType, 
                        new Point(magnetLineX[x], magnetLineY[y]),
                        getSelectedShapes(), e.isShiftDown()), false);
                layer.repaint();
            } else if (isMoving) {
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
        ui.requestFocus();
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
                    opManager.execute(new OpAdd(holdedShape), true);
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
                    } else if (isResizing) {
                        opManager.execute(new OpResize(resizingRelativeShape, 
                                selectedResizeBoxType, 
                                new Point(magnetLineX[x], magnetLineY[y]),
                                getSelectedShapes(), e.isShiftDown()), true);
                    } else {
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
            currentShapeType = ElShape.Type.RECTANGLE;
            ui.setButton(UserInterface.Button.RECTANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            currentShapeType = ElShape.Type.ELLIPSE;
            ui.setButton(UserInterface.Button.ELLIPSE);
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            currentShapeType = ElShape.Type.ISOSCELES_TRIANGLE;
            ui.setButton(UserInterface.Button.ISOTRIANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            currentShapeType = ElShape.Type.RIGHT_TRIANGLE;
            ui.setButton(UserInterface.Button.RIGHTTRIANGLE);
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            currentShapeType = ElShape.Type.LINE;
            ui.setButton(UserInterface.Button.LINE);
        }  
 
        if (e.isControlDown()) {        
            if (!e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                opManager.undo();
                setProperties();
                cloneShapesList();
                layer.repaint();
            } else if ((e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) || 
                    (e.getKeyCode() == KeyEvent.VK_Y)) {
                opManager.redo();
                setProperties();
                cloneShapesList();
                layer.repaint();
            }
            
            if (e.getKeyCode() == KeyEvent.VK_S) {
                save(SaveType.JPG);
            } else if (e.getKeyCode() == KeyEvent.VK_E) {
                save(SaveType.XML);
            } else if (e.getKeyCode() == KeyEvent.VK_O) {
                open();
            }
    
        }
        
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    moveSelectedInDir(MoveDir.UP, e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveSelectedInDir(MoveDir.DOWN, e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveSelectedInDir(MoveDir.RIGHT, e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveSelectedInDir(MoveDir.LEFT, e.isShiftDown());
                }
                
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    unselectAll();
                }
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
                cloneShapesList();
                layer.repaint();
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
 
    public ElShape.Type getCurrentShapeType() {
        return currentShapeType;
    }
 
    public void setCurrentShapeType(ElShape.Type currentShapeType) {
        this.currentShapeType = currentShapeType;
    }   
 
    public void setProperties() {
        LinkedList<ElShape> selectedShapes = getSelectedShapes();
        propertiesList.clear();
        for (ElShape elShape : selectedShapes) {
            if (propertiesList.size() == 0) {
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
                propertiesList.add(new Property(
                      " Stroke Collor : ", elShape.getBorderColor(), 
                        Property.PropertyName.BORDER_COLOR));
            }
            else {
                for(Property p : propertiesList) {
                    if(p.getValue() == null) {
                            continue;
                    }
                    switch (p.getPropertyName()) {                                            
                        case X:
                            if ((int)p.getValue() != elShape.getX()) {
                                p.setValue(null);
                            }
                            break;
                        case Y:
                            if ((int)p.getValue() != elShape.getY()) {
                                p.setValue(null);
                            }
                            break;
                        case WIDTH:
                            if ((int)p.getValue() != elShape.getWidth()) {
                                p.setValue(null);
                            }
                            break;
                        case HEIGHT:
                            if ((int)p.getValue() != elShape.getHeight()) {
                                p.setValue(null);
                            }
                            break;
                        case COLOR:
                            if ((Color)p.getValue() 
                                    != elShape.getFillColor()) {
                                p.setValue(null);
                            }
                            break;
                        case BORDER_COLOR:
                            if ((Color)p.getValue() 
                                    != elShape.getBorderColor()) {
                                p.setValue(null);
                            }
                            break;
                    }
                }
            }
        }

        ui.setProperties(properties);
    }
    
    public void checkProperties() {
        LinkedList<ElShape> selectedShapes = getSelectedShapes();
        for(Property p : propertiesList) {
            if(p.getValue() == null || !p.isChanged()) {
                continue;
            }
            LinkedList<Object> values = new LinkedList<>();
            for(int i = 0; i < selectedShapes.size(); ++i) {
                values.add(p.getValue());
            }            
            opManager.execute(new OpSetProperty(p.getPropertyName(), values, 
                    selectedShapes), true); 
            cloneShapesList();
        }
        
        layer.repaint();
    }
    
    public void open() {
        JFileChooser chooser = new JFileChooser();
        // Remove 'All Files' option from chooser.
        chooser.removeChoosableFileFilter(chooser.getChoosableFileFilters()[0]);  
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
        "xml files (*.xml)", "xml");
        chooser.setFileFilter(xmlfilter);
        if(chooser.showOpenDialog(ui) == JFileChooser.APPROVE_OPTION) {
            chooser.setFileFilter(xmlfilter);
            File openFile = chooser.getSelectedFile();
            String path = openFile.getPath();
            //System.out.println("" + path);
            try {
                FileManager.Import(path, layer.getElShapes());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            layer.repaint();
        }
    }
    
    public void save(SaveType desiredSaveType) {        
        JFileChooser chooser = new JFileChooser();
        // Remove 'All Files' option from chooser.
        chooser.removeChoosableFileFilter(chooser.getChoosableFileFilters()[0]);  
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
        "PNG files (*.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter(
        "JPG files (*.jpg)", "jpg");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(
        "XML files (*.xml)", "xml");
        
        if (desiredSaveType != SaveType.XML) {
            chooser.addChoosableFileFilter(pngFilter);
            chooser.addChoosableFileFilter(jpgFilter);
            chooser.addChoosableFileFilter(xmlFilter);
        } else {
            chooser.addChoosableFileFilter(xmlFilter);
            chooser.setDialogTitle("Export");
        }
              
        if(chooser.showSaveDialog(ui) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();  
            SaveType saveType;
            String path = saveFile.getPath();
            
            if (chooser.getFileFilter() == pngFilter) {
                saveType = SaveType.PNG;
                path += ".png";
            } else if (chooser.getFileFilter() == jpgFilter) {
                saveType = SaveType.JPG;                
                path += ".jpg";
            } else {
                saveType = SaveType.XML;                
                path += ".xml";
            }
            
            if (saveType == SaveType.XML) {
                try {
                    FileManager.Export(path, layer.getElShapes());
                } catch (IOException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(
                            Level.SEVERE, null, ex);
                } catch (AWTException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            } else {            
                BufferedImage bi = layer.getImage(saveType);
                try {
                    ImageIO.write(bi, "PNG", new File(path));
                } catch (IOException ex) {
                    Logger.getLogger(Stage.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
    }
}