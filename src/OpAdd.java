

import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpAdd extends Operation {

    private LinkedList<ElShape> elShapes;
    
    /**
     * Must be called from Operation Manager, to re-call with passing a 
     * reference to list of ElShapes. Otherwise it will throw nullPointException
     * as elShapes will not be initialized.
     * @param targetedShapes List of ElShapes to be added to the Layer.
     */
    public OpAdd(LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
    }

    /**
     * Must be called from Operation Manager, to re-call with passing a 
     * reference to list of ElShapes. Otherwise it will throw nullPointException
     * as elShapes will not be initialized.
     * @param targetedShape ElShape to be added to the Layer.
     */
    public OpAdd(ElShape targetedShape) {
        super(new LinkedList<ElShape>() {{add(targetedShape);}});
    }
    
    /**
     * Can be called from outside Operation Manager.
     * @param opAdd Operation to take targetedShapes from.
     * @param elShapes Reference of ElShapes to delete targetedShapes from.
     */
    public OpAdd(Operation opAdd, LinkedList<ElShape> elShapes) {
        super(opAdd.targetedShapes);
        this.elShapes = elShapes;
    }
        
    @Override
    public Operation perform() {
        for (ElShape shape: targetedShapes) {
            elShapes.add(shape);
        }
        return new OpDelete(this, elShapes);
    }

}
