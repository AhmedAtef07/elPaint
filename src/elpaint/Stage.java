package elpaint;
 
import java.awt.AWTException;
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
    private ElShape.Type currentShapeType;
 
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
        width = Math.max(width, 5);
        height = Math.max(height, 5);
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
                if (pressedX == startX) {
                    pressedX += width;
                }
                if (pressedY == startY) {
                    pressedY += height;
                }
                holdedShape = new ElLine(new Point(startX, startY),
                        new Point(pressedX, pressedY));

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
        setProperties();
    }
 
    void selectAll() { 
        for (ElShape elshape: elShapes) {
            elshape.setSelected(true);            
        }
        layer.repaint();
        setProperties();
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
                opManager.execute(new OpResize(resizingRelativeShape, 
                        selectedResizeBoxType, new Point(x, y),
                        getSelectedShapes()), false);
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
                            selectedResizeBoxType, new Point(x, y),
                            getSelectedShapes()), true);
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
 
        if (e.isControlDown() && !e.isShiftDown() && 
                e.getKeyCode() == KeyEvent.VK_Z) {
//            layer.popLastShape();
            opManager.undo();
            setProperties();
            cloneShapesList();
            layer.repaint();
        } else if (e.isControlDown() && 
                (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) || 
                (e.getKeyCode() == KeyEvent.VK_Y)) {
//            layer.unPopLastShape();
            opManager.redo();
            setProperties();
            cloneShapesList();
            layer.repaint();
        }
 
        switch (currentMode) {
            case DRAWING:
                break;
            case EDITING:
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
 
    public void save() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(ui);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            String path = saveFile.getPath() + ".xml";
            try {
                FileManager.Export(path, layer.getElShapes());
            } catch (IOException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AWTException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void open() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
        "xml files (*.xml)", "xml");
        chooser.setFileFilter(xmlfilter);
        int returnVal = chooser.showOpenDialog(ui);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
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
    
    public void image() {        
        JFileChooser chooser = new JFileChooser();
        boolean isTransparence = false;
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter(
        "JPG files (*.jpg)", "jpg");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
        "PNG files (*.png)", "png");
        chooser.addChoosableFileFilter(jpgFilter);
        chooser.addChoosableFileFilter(pngFilter);
        int returnVal = chooser.showSaveDialog(ui);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            chooser.addChoosableFileFilter(jpgFilter);
            chooser.addChoosableFileFilter(pngFilter);
            File saveFile = chooser.getSelectedFile();            
            String path = saveFile.getPath() + ".png";
            BufferedImage bi = layer.getPNG();
            try {
                ImageIO.write(bi, "PNG", new File(path));
            } catch (IOException ex) {
                Logger.getLogger(Stage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}