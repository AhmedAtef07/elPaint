package elpaint;

import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public abstract class Operation {
    protected final LinkedList<ElShape> targetedShapes;

    public Operation(LinkedList<ElShape> targetedShapes) {
        this.targetedShapes = targetedShapes;
    }
    
    /**
     *
     * @return A reverse operation to revert the changes this operation 
     * performs.
     */
    public abstract Operation perform();
    
}
