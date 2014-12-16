

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpMove extends Operation {

    private Point startPoint, releasedPoint;
    private boolean withAspectRatio;
    
    public OpMove(Point startPoint, Point releasedPoint,
            boolean withAspectRatio, LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
        this.startPoint = startPoint;
        this.releasedPoint = releasedPoint;
        this.withAspectRatio = withAspectRatio;
    }

    @Override
    public Operation perform() {
        OpMove reverseOp = new OpMove(releasedPoint, startPoint,
                withAspectRatio, targetedShapes);
        int startX = startPoint.x;
        int startY = startPoint.y;
        int x = releasedPoint.x;
        int y = releasedPoint.y;
        for (ElShape elshape: targetedShapes) {
            if (withAspectRatio) {
                if (Math.abs(x - startX) < Math.abs(startY - y)) {
                    elshape.move(0, startY - y);                 
                } else {
                    elshape.move(startX - x, 0);
                }
            } else {
                elshape.move(startX - x, startY - y);
            }
        }
        return reverseOp;
    }

}
