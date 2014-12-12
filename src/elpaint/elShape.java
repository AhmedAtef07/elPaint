package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author HackerGhost
 */
public abstract class elShape   {
 
 private boolean IsSelected;

    Stroke LineType;
    Color  FillColor;
    Color  BorderColor;
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    
     public elShape() {
        this.IsSelected = false;
        this.LineType = new BasicStroke(2);
        this.FillColor = Color.WHITE;
        this.BorderColor = Color.BLACK;
    }

    public Stroke getLineType() {
        return LineType;
    }

    public void setLineType(Stroke LineType) {
        this.LineType = LineType;
    }

    public Color getFillColor() {
        return FillColor;
    }

    public void setFillColor(Color FillColor) {
        this.FillColor = FillColor;
    }

    public Color getBorderColor() {
        return BorderColor;
    }

    public void setBorderColor(Color BorderColor) {
        this.BorderColor = BorderColor;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public ResizeBox getResizeBoxes() {
        return new ResizeBox(this);
    }
    public abstract elShape getCopy();
 
}
