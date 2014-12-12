package elpaint;

import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
        
        InputHandler stageInputHandler = new InputHandler(stage);
        addMouseListener(stageInputHandler);
        addMouseMotionListener(stageInputHandler);
        addKeyListener(stageInputHandler);
        
        layer = new Layer(new Point(0, 0), 70, 70);
        add(layer);
        
        setVisible(true);
    }
    
    public Layer getLayer() {
        return layer;
    } 
}
