package elpaint;

import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpSetProperty extends Operation {

    Property.PropertyName propertyName;
    LinkedList<Object> values;
    
    /**
     *
     * @param property Single property it's value will be set to all targeted
     * Shapes.
     * @param values List of same number of elements as targetShapes, as each 
     * corresponding value will be added to the shape.
     * @param targetedShapes Shapes whose values will change depending on the 
     * property passed.
     */
    public OpSetProperty(Property.PropertyName propertyName, 
            LinkedList<Object> values, LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public Operation perform() {
        LinkedList<Object> oldValues = new LinkedList<>();
            for (int i = 0; i < targetedShapes.size(); i++) {
                ElShape shape = targetedShapes.get(i);            
                switch (propertyName) {
                    case X:
                        oldValues.add(shape.getX());
                        shape.setX((int)values.get(i));                    
                        break;
                    case Y:
                        oldValues.add(shape.getY());
                        shape.setY((int)values.get(i));
                        break;
                    case WIDTH:
                        oldValues.add(shape.getWidth());
                        shape.setWidth((int)values.get(i));
                        break;
                    case HEIGHT:
                        oldValues.add(shape.getHeight());
                        shape.setHeight((int)values.get(i));                    
                        break;
                    case COLOR:
                        oldValues.add(shape.getFillColor());
                        shape.setFillColor((Color)values.get(i));                    
                        break;
                    case BORDER_COLOR:
                        oldValues.add(shape.getBorderColor());
                        shape.setBorderColor((Color)values.get(i));                    
                        break;
                }
            }
            
        return new OpSetProperty(propertyName, oldValues, targetedShapes);
    }

}
