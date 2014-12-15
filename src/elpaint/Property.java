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
        X,
        Y,
        WIDTH,
        HEIGHT,
        COLOR,
        STROKE_COLOR,
        STROKE_THICKNESS,
    }

    PropertyType propertyType;
    
    String label;
    int intValue;
    Color colorValue;

    public Property(String label, int intValue) {
        this.label = label;
        this.intValue = intValue;
        propertyType = PropertyType.INTEGER;
    }
 
    public Property(String label, Color colorValue) {
        this.label = label;
        this.colorValue = colorValue;
        propertyType = PropertyType.COLOR;
    }
    
    
    
}
