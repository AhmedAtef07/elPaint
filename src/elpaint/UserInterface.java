package elpaint;

import java.awt.Point;
import javax.swing.JFrame;

/**
 *
 * @author Ahmed Atef
 */
public class UserInterface extends JFrame {

    private Stage stage;
    private Layer layer;
    
    public UserInterface(Stage stage) {
        this.stage = stage;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        
        layer = new Layer(new Point(0, 0), 0, 0);
        add(layer);
        
        setVisible(true);
    }
    
    public Layer getLayer() {
        return layer;
    } 
}
