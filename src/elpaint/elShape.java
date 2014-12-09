package elpaint;

import java.awt.Shape;

/**
 *
 * @author HackerGhost
 */
public abstract class elShape implements Shape {
 
 private boolean IsSelected;

     
    public elShape() {

        this.IsSelected = false;
    }

    /**
     * @return the IsSelected
     */
    public boolean isIsSelected() {
        return IsSelected;
    }

    /**
     * @param IsSelected the IsSelected to set
     */
    public void setIsSelected(boolean IsSelected) {
        this.IsSelected = IsSelected;
    }
    
    
 
}
