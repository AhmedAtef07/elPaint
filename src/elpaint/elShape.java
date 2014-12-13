package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author HackerGhost
 */
public abstract class elShape extends elComponent  {
 
    private boolean selected = false;
 
    private Stroke lineType;
    private Color  fillColor;
    private Color  borderColor;
    
    protected double angle = 0.0;

    public double getAngle() {
        return angle;
    }
    
     /**
      * 
      */
     public elShape() {
        this.lineType = new BasicStroke(2);
        this.fillColor = Color.WHITE;
        this.borderColor = Color.BLACK;
    }
     /**
      * 
      * @param FillColor
      * @param BorderColor
      * @param LineType 
      */
    public elShape(Color FillColor,Color BorderColor,Stroke LineType){
        this.fillColor= FillColor;
        this.borderColor= BorderColor;
        this.lineType = LineType;
    }
    
    /**
     * 
     * 
     * @return object which has the implementation of shape 
     */
    public abstract Shape getShape();
    // For Line and text to know if exist 
    public abstract void Rotate(double angle);

    public abstract elShape getCopy();
    
    /**
     * @return the IsSelected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param IsSelected the IsSelected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLineType(Stroke lineType) {
        this.lineType = lineType;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Stroke getLineType() {
        return lineType;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}
