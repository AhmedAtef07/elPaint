package elpaint;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Atef
 */
public class Layer extends JPanel {
    
    public Layer(Point point, int width, int height) {
            setBounds(point.x, point.y, width, height);
            setBackground(Color.white);
    }
}
