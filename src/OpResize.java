
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
    private boolean withAspectRatio;

    public OpResize(ElShape relativeShape,
            SelectionBox.ResizeBoxType selectedResizeBoxType,
            Point draggedPoint, LinkedList<ElShape> targetedShapes, 
            boolean withAspectRatio) {
        super(targetedShapes);
        this.relativeShape = relativeShape;
        this.selectedResizeBoxType = selectedResizeBoxType;
        this.draggedPoint = draggedPoint;
        this.withAspectRatio = withAspectRatio;
    }    
    

    @Override
    public Operation perform() {
        int padX = 0;
        int padY = 0;
        SelectionBox.ResizeBoxType selectedResizeBoxTypeNew = 
                selectedResizeBoxType;
        
        if (withAspectRatio) {
            double m = (double)(relativeShape.getClonedHeight())
                        / (double)(relativeShape.getClonedWidth());
            int y1 = relativeShape.getClonedY();
            int y2 = relativeShape.getClonedY();
            int x1 = relativeShape.getClonedX();            
            int x2 = relativeShape.getClonedX()+relativeShape.getClonedWidth();
            int x, y;
            switch (selectedResizeBoxType) {
            case NW:  
            case SE:
                y = (int)(m * (draggedPoint.getX() - x1) + y1);
                x = (int)(((draggedPoint.getY() - y1) / m) + x1); 
                if(Math.abs(draggedPoint.getX() - x) < Math.abs(draggedPoint.getY() - y)) {
                    draggedPoint.x = x;
                }
                else {
                    draggedPoint.y = y;
                }
                break;
            case NE:  
            case SW:
                y = (int)(-m * (draggedPoint.getX() - x2) + y2);
                x = (int)(((draggedPoint.getY() - y2) / -m) + x2); 
                if(Math.abs(draggedPoint.getX() - x) < Math.abs(draggedPoint.getY() - y)) {
                    draggedPoint.x = x;
                }
                else {
                    draggedPoint.y = y;
                }
                break;                        
            }
        }
        switch (selectedResizeBoxType) {
            case NW:      
                if (draggedPoint.getY() > relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() < relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SW;
                } else if (draggedPoint.getY() > relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() > relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SE;
                } else if (draggedPoint.getY() < relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() > relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NE;
                }
                break;
            case N:
                if (draggedPoint.getY() > relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.S;
                }
                padX = relativeShape.getClonedWidth() / 2;
                break;
            case NE:      
                if (draggedPoint.getY() > relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() < relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SW;
                } else if (draggedPoint.getY() > relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() > relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SE;
                } else if (draggedPoint.getY() < relativeShape.getClonedY() 
                        + relativeShape.getClonedHeight() 
                        && draggedPoint.getX() < relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NW;
                }
                padX = relativeShape.getClonedWidth();
                break;
            case E:
                if (draggedPoint.getX() < relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.W;
                }
                padX = relativeShape.getClonedWidth();
                padY = relativeShape.getClonedHeight() / 2;
                break;
            case W:
                if (draggedPoint.getX() > relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.E;
                }
                padY = relativeShape.getClonedHeight() / 2;
                break;
            case SW:
                if (draggedPoint.getY() < relativeShape.getClonedY() 
                        && draggedPoint.getX() > relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NE;
                } else if (draggedPoint.getY() > relativeShape.getClonedY()
                        && draggedPoint.getX() > relativeShape.getClonedX()
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SE;
                } else if (draggedPoint.getY() < relativeShape.getClonedY() 
                        && draggedPoint.getX() < relativeShape.getClonedX() 
                        + relativeShape.getClonedWidth()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NW;
                }
                padY = relativeShape.getClonedHeight(); 
                break;
            case S:
                if (draggedPoint.getY() < relativeShape.getClonedY()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.N;
                }
                padX = relativeShape.getClonedWidth() / 2;
                padY = relativeShape.getClonedHeight();
                break;
            case SE:
                if (draggedPoint.getY() > relativeShape.getClonedY() 
                        && draggedPoint.getX() < relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.SW;
                } else if (draggedPoint.getY() < relativeShape.getClonedY() 
                        && draggedPoint.getX() > relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NE;
                } else if (draggedPoint.getY() < relativeShape.getClonedY() 
                        && draggedPoint.getX() < relativeShape.getClonedX()) {
                    selectedResizeBoxTypeNew = SelectionBox.ResizeBoxType.NW;
                }
                padX = relativeShape.getClonedWidth();
                padY = relativeShape.getClonedHeight();
                break;
            }
        Point startResizePoint = new Point(relativeShape.getClonedX() + padX, 
                relativeShape.getClonedY() + padY);
        for (ElShape elShape: targetedShapes) {
            elShape.resize(relativeShape, selectedResizeBoxType, draggedPoint);
        }
        return new OpResize(relativeShape, selectedResizeBoxTypeNew, 
                startResizePoint, targetedShapes, withAspectRatio);
    }

}
