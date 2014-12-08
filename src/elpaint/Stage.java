package elpaint;

/**
 *
 * @author Ahmed Atef
 */
public class Stage {
    
    private UserInterface ui;
    private Layer layer;
    
    public Stage() {
        ui = new UserInterface(this);
        layer = ui.getLayer();
    }       
}
