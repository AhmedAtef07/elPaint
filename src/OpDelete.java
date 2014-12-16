

import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpDelete extends Operation {

    private LinkedList<ElShape> elShapes;
    
    /**
     * Must be called from Operation Manager, to re-call with passing a 
     * reference to list of ElShapes. Otherwise it will throw nullPointException
     * as elShapes will not be initialized.
     * @param targetedShapes List of ElShapes to be deleted from the Layer.
     */
    public OpDelete(LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
    }

    /**
     * Must be called from Operation Manager, to re-call with passing a 
     * reference to list of ElShapes. Otherwise it will throw nullPointException
     * as elShapes will not be initialized.
     * @param targetedShape ElShape to be deleted from the Layer.
     */
    public OpDelete(ElShape targetedShape) {
        super(new LinkedList<ElShape>() {{add(targetedShape);}});
    }
    
    /**
     * Can be called from outside Operation Manager.
     * @param opDelete Operation to take targetedShapes from.
     * @param elShapes Reference of ElShapes to delete targetedShapes from.
     */
    public OpDelete(Operation opDelete, LinkedList<ElShape> elShapes) {
        super(opDelete.targetedShapes);
        this.elShapes = elShapes;
    }
        
    @Override
    public Operation perform() {
        for (ElShape shape: targetedShapes) {
            elShapes.remove(shape);
        }
        return new OpAdd(this, elShapes);
    }

}
