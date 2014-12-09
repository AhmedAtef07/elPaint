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
    
     
     public elShape() {

        this.IsSelected = false;
        this.LineType = new BasicStroke(2);
        this.FillColor = Color.WHITE;
        this.BorderColor = Color.BLACK;
    }
    public elShape(Color FillColor,Color BorderColor,Stroke LineType){
        this.FillColor= FillColor;
        this.BorderColor= BorderColor;
        this.LineType = LineType ;
        this.IsSelected= false;
    }
    
    /**
     * @param mask a mask within the type of object to be returned
     * @
     * @return object which has the implementation of shape 
     */
    public abstract Shape getFloat();

    /**
     * @return the IsSelected
     */
    public boolean getIsSelected() {
        return IsSelected;
    }
    

    /**
     * @param IsSelected the IsSelected to set
     */
    public void setIsSelected(boolean IsSelected) {
        this.IsSelected = IsSelected;
    }
    
    
 
}
