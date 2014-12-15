package elpaint;

import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author Ahmed Atef
 */
public class OpSetProperty extends Operation {

    Property property;
    LinkedList<Object> values;
    
    /**
     *
     * @param property Single property it's value will be set to all targeted
     * Shapes.
     * @param targetedShapes Shapes whose values will change depending on the 
     * property passed.
     */
    public OpSetProperty(Property property, 
            LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
        this.property = property;
        values = null;
    }
    
    /**
     *
     * @param property Single property it's value will be set to all targeted
     * Shapes.
     * @param values List of same number of elements as targetShapes, as each 
     * corresponding value will be added to the shape.
     * @param targetedShapes Shapes whose values will change depending on the 
     * property passed.
     */
    public OpSetProperty(Property property, LinkedList<Object> values, 
            LinkedList<ElShape> targetedShapes) {
        super(targetedShapes);
        this.property = property;
        this.values = values;
    }

    @Override
    public Operation perform() {
        LinkedList<Object> oldValues = new LinkedList<>();
        if (values == null) {
            for (ElShape shape: targetedShapes) {
                oldValues.add(property.getValue());
                if (property.getValue() == null) {
                    continue;
                }
                switch (property.getPropertyName()) {
                    case X:
                        shape.setX((int)property.getValue());                    
                        break;
                    case Y:
                        shape.setY((int)property.getValue());
                        break;
                    case WIDTH:
                        shape.setWidth((int)property.getValue());
                        break;
                    case HEIGHT:                     
                        shape.setHeight((int)property.getValue());                    
                        break;
                    case COLOR:                    
                        shape.setFillColor((Color)property.getValue());                    
                        break;
                    case BORDER_COLOR:
                        shape.setBorderColor((Color)property.getValue());                    
                        break;
                }
            }
        } else {
            for (int i = 0; i < targetedShapes.size(); i++) {
                ElShape shape = targetedShapes.get(i);
                oldValues.add(values.get(i));
                if (property.getValue() == null) {
                    continue;
                }
                switch (property.getPropertyName()) {
                    case X:
                        shape.setX((int)values.get(i));                    
                        break;
                    case Y:
                        shape.setY((int)values.get(i));           
                        break;
                    case WIDTH:
                        shape.setWidth((int)values.get(i));           
                        break;
                    case HEIGHT:                     
                        shape.setHeight((int)values.get(i));                       
                        break;
                    case COLOR:                    
                        shape.setFillColor((Color)values.get(i));                    
                        break;
                    case BORDER_COLOR:
                        shape.setBorderColor((Color)values.get(i));                           
                        break;
                }
            }            
        }
        return new OpSetProperty(property, oldValues, targetedShapes);
    }

}
