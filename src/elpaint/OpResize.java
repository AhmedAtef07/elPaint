package elpaint;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpResize extends Operation {

    private ElShape relativeShape;
    private SelectionBox.ResizeBoxType selectedResizeBoxType;
    private Point draggedPoint;

    public OpResize(ElShape relativeShape,
            SelectionBox.ResizeBoxType selectedResizeBoxType,
            Point draggedPoint, LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
        this.relativeShape = relativeShape;
        this.selectedResizeBoxType = selectedResizeBoxType;
        this.draggedPoint = draggedPoint;
    }    
    

    @Override
    public Operation perform() {
        int padX = 0;
        int padY = 0;
        switch (selectedResizeBoxType) {
            case N:
                padX = relativeShape.getWidth() / 2;
                break;
            case NE:
                padX = relativeShape.getWidth();
                break;
            case E:
                padX = relativeShape.getWidth();
                padY = relativeShape.getHeight() / 2;
                break;
            case W:
                padY = relativeShape.getHeight() / 2;
                break;
            case SW:
                padX = relativeShape.getWidth();
                padY = relativeShape.getHeight(); 
                break;
            case S:
                padX = relativeShape.getWidth() / 2;
                padY = relativeShape.getHeight();
                break;
            case SE:
                padY = relativeShape.getHeight();
                break;
            }
        Point startResizePoint = new Point(relativeShape.getClonedX() + padX, 
                relativeShape.getClonedY() + padY);
        for (ElShape elShape: targetedShapes) {
            elShape.resize(relativeShape, selectedResizeBoxType, draggedPoint);
        }
        return new OpResize(relativeShape, selectedResizeBoxType, 
                startResizePoint, targetedShapes);
    }

}
