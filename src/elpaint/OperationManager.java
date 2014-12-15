package elpaint;

import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Ahmed Atef
 */
public class OperationManager {
    private final LinkedList<ElShape> elShapes;
    private final Stack<Operation> undoOperations;
    private final Stack<Operation> redoOperations;

    public OperationManager(final LinkedList<ElShape> elShapes) {
        this.elShapes = elShapes;
        undoOperations = new Stack<>();
        redoOperations = new Stack<>();
    }   
    
    public void execute(Operation operation, boolean addInHistory) {
        if (addInHistory) {
            undoOperations.add(operation.perform());        
            redoOperations.clear();
            return;
        }
        operation.perform();
        
    }
    
    public boolean undo() {
        if (undoOperations.isEmpty()) {
            return false;
        }        
        redoOperations.add(undoOperations.pop().perform());        
        return true;
    }
    
    public boolean redo() {
        if (redoOperations.isEmpty()) {
            return false;
        }
        undoOperations.add(redoOperations.pop().perform());        
        return true;
    }
}
