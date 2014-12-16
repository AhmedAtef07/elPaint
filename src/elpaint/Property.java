package elpaint;

import java.awt.Color;

/**
 *
 * @author Ahmed Atef
 */
public class Property {
    
    public enum PropertyType {
        INTEGER,
        COLOR,
    }
    
    public enum PropertyName {
        X(PropertyType.INTEGER),
        Y(PropertyType.INTEGER),
        WIDTH(PropertyType.INTEGER),
        HEIGHT(PropertyType.INTEGER),
        COLOR(PropertyType.COLOR),
        BORDER_COLOR(PropertyType.COLOR),
        STROKE_THICKNESS(PropertyType.INTEGER);
        
        PropertyType propertyType;                

        private PropertyName(PropertyType propertyType) {
            this.propertyType = propertyType;
        }

        public PropertyType getPropertyType() {
            return propertyType;
        }        
    }
    
    
    private PropertyType propertyType;
    private PropertyName propertyName;
    
    private String label;
    private Object value;
    boolean changed;        

    public Property(String label, Object value, PropertyName propertyName) {
        this.label = label;
        this.propertyName = propertyName;
        this.propertyType = propertyName.getPropertyType();
        
        this.value = value;
        this.changed = false;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public PropertyName getPropertyName() {
        return propertyName;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }    
}
