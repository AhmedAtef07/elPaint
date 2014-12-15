package elpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author HackerGhost
 */
public abstract class ElShape {
    
    protected Stroke lineType;
    protected Color fillColor;
    protected Color borderColor;   
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    private boolean isSelected;

    
    public ElShape() {
        this.isSelected = false;
        this.lineType = new BasicStroke(2);
        this.fillColor = Color.WHITE;
        this.borderColor = Color.BLACK;
    }   
    
    public ElShape(Color fillColor, Color borderColor, Stroke lineType) {
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.lineType = lineType ;
        this.isSelected = false;
    }    
    
    public Stroke getLineType() {
        return lineType;
    }

    public void setLineType(Stroke lineType) {
        this.lineType = lineType;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }  

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
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
    
    public SelectionBox getResizeBox() {
        return new SelectionBox(this);
    }
     
    public abstract ElShape getCopy();
    public abstract Shape getShape();
 
}