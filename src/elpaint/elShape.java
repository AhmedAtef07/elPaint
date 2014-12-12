package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author HackerGhost
 */
public abstract class elShape  {
 
    private boolean IsSelected;
        
     Stroke LineType;
     Color  FillColor;
     Color  BorderColor;
    
     /**
      * 
      */
     public elShape() {

        this.IsSelected = false;
        this.LineType = new BasicStroke(2);
        this.FillColor = Color.WHITE;
        this.BorderColor = Color.BLACK;
    }
     /**
      * 
      * @param FillColor
      * @param BorderColor
      * @param LineType 
      */
    public elShape(Color FillColor,Color BorderColor,Stroke LineType){
        this.FillColor= FillColor;
        this.BorderColor= BorderColor;
        this.LineType = LineType ;
        this.IsSelected= false;
    }
    
    /**
     * 
     * 
     * @return object which has the implementation of shape 
     */
    public abstract Shape getShape();
    // For Line and text to know if exist 
    public abstract boolean hasStroke();
    public abstract boolean hasFillColor();
    public abstract boolean hasBorderColor();

    /**
     * @return the IsSelected
     */
    public boolean IsSelected() {
        return IsSelected;
    }
    

    /**
     * @param IsSelected the IsSelected to set
     */
    public void setIsSelected(boolean IsSelected) {
        this.IsSelected = IsSelected;
    }
    
    
 
}
