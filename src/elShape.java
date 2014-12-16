

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;


public abstract class ElShape {
    
    enum Type {
        RECTANGLE,
        ELLIPSE,
        LINE,
        ISOSCELES_TRIANGLE,
        RIGHT_TRIANGLE,
        POLYGON,
    }
        
    protected Stroke lineType;
    protected Color fillColor;
    protected Color borderColor;   
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    private int clonedX;
    private int clonedY;
    private int clonedWidth;
    private int clonedHeight;
    
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
        this.width = Math.max(width, 3);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = Math.max(height, 3);
    }
    
    public SelectionBox getResizeBox() {
        return new SelectionBox(this);
    }
     
    public abstract ElShape getCopy();
    public abstract Shape getShape();
 
    public void cloneSelf() {
        clonedX = x;
        clonedY = y;
        clonedWidth = width;
        clonedHeight = height;
    }

    public int getClonedX() {
        return clonedX;
    }

    public int getClonedY() {
        return clonedY;
    }

    public int getClonedWidth() {
        return clonedWidth;
    }

    public int getClonedHeight() {
        return clonedHeight;
    }
    
    public void move(int xDisplacment, int yDisplacment) {
        x = clonedX - xDisplacment;
        y = clonedY - yDisplacment;
    }
    
    /**
     * Change the reference point and dimension of the shape in relative to the 
     * relative shape.
     * 
     * @param relativeShape Shape in which all selected shapes will be resized 
     * in relative to it's dimension.
     * @param selectedResizeBoxType Type of resize box used in resizing.
     * @param draggedPoint The point the cursor points to in this instance.
     */
    public void resize(ElShape relativeShape, 
            SelectionBox.ResizeBoxType selectedResizeBoxType, 
            Point draggedPoint) {
        double xR = relativeShape.getClonedX();
        double yR = relativeShape.getClonedY();
        double wR = relativeShape.getClonedWidth();
        double hR = relativeShape.getClonedHeight(); 
        int draggedX = draggedPoint.x;
        int draggedY = draggedPoint.y;
        
        double r;
        switch(selectedResizeBoxType) {
        case NW:
            if (draggedY <= yR + hR && draggedX <= xR + wR) {
                r = (double)(((yR - draggedY)) / hR);
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight - getHeight());
                r = (double)(((xR - draggedX)) / wR);
                setWidth(clonedWidth + (int) (clonedWidth * r)); 
                setX(clonedX + clonedWidth - getWidth());
            }
            else if (draggedY > yR + hR && draggedX <= xR + wR) {
                r = (double)(((draggedY - (yR + hR))) / hR);
                setHeight((int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight);
                r = (double)(((xR - draggedX)) / wR);
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth - getWidth());
            }
            else if (draggedY <= yR + hR && draggedX > xR + wR) {                                    
                r = (double)(((yR - draggedY)) / hR);
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY +
                        clonedHeight - 
                        getHeight());
                r = (double)(((draggedX - (xR + wR))) / wR);
                setWidth((int) (clonedWidth * r)); 
                setX(clonedX + clonedWidth); 
            }
            else if (draggedY > yR + hR && draggedX > xR + wR) {
                r = (double) (((draggedY - (yR + hR))) / hR);
                setHeight((int)(clonedHeight * r)); 
                setY(clonedY +
                        clonedHeight); 
                r = (double) (((draggedX - (xR + wR))) / wR);
                setWidth((int) (clonedWidth * r)); 
                setX(clonedX + clonedWidth); 
            }
            break;
        case N:
            if (draggedY <= yR + hR) {                                    
                r = (double)(((yR - draggedY)) / hR);
                System.out.print(height + " " );
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight - getHeight());
            }
            else if (draggedY > yR + hR) {
                r = (double)(((draggedY - (yR + hR))) / hR);
                setHeight((int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight);                                    
            }
            System.out.println(height);
            break;
        case NE:
            if (draggedY <= yR + hR && draggedX >= xR) {                                    
                r = (double)(((yR - draggedY)) / hR);
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight - getHeight());
                r = (double)((draggedX - xR - wR) / wR);                                                           
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX);
            }
            else if (draggedY > yR + hR && draggedX >= xR) {
                r = (double)(((draggedY - (yR + hR))) / hR);
                setHeight(
                        (int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight); 
                r = (double)((draggedX - xR - wR) / wR);                                                           
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX);
            }
            else if (draggedY <= yR + hR && draggedX < xR) {                                    
                r = (double)(((yR - draggedY)) / hR);
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight - getHeight());
                r = (double)((xR - draggedX) / wR);                                                           
                setWidth((int)(clonedWidth * r));
                setX(clonedX - getWidth());
            }
            else if (draggedY > yR + hR && draggedX < xR) {
                r = (double)(((draggedY - (yR + hR))) / hR);
                setHeight((int)(clonedHeight * r)); 
                setY(clonedY + clonedHeight); 
                r = (double)((xR - draggedX) / wR);                                                           
                setWidth((int)(clonedWidth * r));
                setX(clonedX - getWidth());
            }
            break;
        case E:  
            if (draggedX >= xR) {                                    
                r = (double)((draggedX - xR - wR) / wR);                                                           
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX);
            }
            else if (draggedX < xR) {
                r = (double)((xR - draggedX) / wR);                                                           
                setWidth((int)(clonedWidth * r));
                setX(clonedX - getWidth());
            }                                  
            break;
        case W:                                
            if (draggedX <= xR + wR) {                                    
                r = (double)(((xR - draggedX)) / wR);
                setWidth(clonedWidth + (int) (clonedWidth * r)); 
                setX(clonedX + clonedWidth - getWidth());
            }
            else if (draggedX > xR + wR) {
                r = (double)(((draggedX - (xR + wR))) / wR);
                setWidth((int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth);                                    
            }                              
            break;
        case SW:
            if (draggedY >= yR && draggedX <= xR + wR) {                                    
                r = (double)((draggedY - yR - hR) / hR);                                                           
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY);
                r = (double)(((xR - draggedX)) / wR);
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth - getWidth());
            }
            else if (draggedY < yR && draggedX <= xR + wR) {
                r = (double)((yR - draggedY) / hR);                                                           
                setHeight((int)(clonedHeight * r));
                setY(clonedY - getHeight());
                r = (double)(((xR - draggedX)) / wR);
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth - getWidth());
            }
            else if (draggedY >= yR && draggedX > xR + wR) {                                    
                r = (double)((draggedY - yR - hR) / hR);                                                           
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY);
                r = (double)(((draggedX - (xR + wR))) / wR);
                setWidth((int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth);
            }
            else if (draggedY < yR && draggedX > xR + wR) {
                r = (double)((yR - draggedY) / hR);                                                           
                setHeight((int)(clonedHeight * r));
                setY(clonedY - getHeight());
                r = (double)(((draggedX - (xR + wR))) / wR);
                setWidth((int)(clonedWidth * r)); 
                setX(clonedX + clonedWidth);
            }
            break;
        case S:
            if (draggedY >= yR) {                                    
                r = (double)((draggedY - yR - hR) / hR);                                                           
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY);
            }
            else if (draggedY < yR) {
                r = (double)((yR - draggedY) / hR);                                                           
                setHeight((int)(clonedHeight * r));
                setY(clonedY - getHeight());
            }
            break;
        case SE:
            if (draggedY >= yR && draggedX >= xR) {                                    
                r = (double)((draggedY - yR - hR) / hR);                                                           
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY);
                r = (double)((draggedX - xR - wR) / wR);                                                           
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX);
            }
            else if (draggedY < yR && draggedX >= xR) {
                r = (double)((yR - draggedY) / hR);                                                           
                setHeight((int)(clonedHeight * r));
                setY(clonedY - getHeight());
                r = (double)((draggedX - xR - wR) / wR);                                                           
                setWidth(clonedWidth + (int)(clonedWidth * r)); 
                setX(clonedX);
            }
            else if (draggedY >= yR && draggedX < xR) {                                    
                r = (double)((draggedY - yR - hR) / hR);                                                           
                setHeight(clonedHeight + (int)(clonedHeight * r)); 
                setY(clonedY);
                r = (double)((xR - draggedX) / wR);                                                           
                setWidth((int)(clonedWidth * r));
                setX(clonedX - getWidth());
            }
            else if (draggedY < yR && draggedX < xR) {
                r = (double)((yR - draggedY) / hR);                                                           
                setHeight((int)(clonedHeight * r));
                setY(clonedY - getHeight());
                r = (double)((xR - draggedX) / wR);                                                           
                setWidth((int)(clonedWidth * r));
                setX(clonedX - getWidth());
            }
            break;
        default:
            break;
        }
    }
    
}