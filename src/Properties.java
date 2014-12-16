

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 *
 * @author Ahmed Emad
 */
public class Properties extends JPanel {
    
    private ArrayList<Property> propertyList;
    private Stage stage;
    JPanel panel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPropertyList(ArrayList<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public ArrayList<Property> getPropertyList() {
        return propertyList;
    }    
    
    public Properties(ArrayList<Property> propertyList, Stage stage) {
        this.propertyList = propertyList;
        this.stage = stage;
    }
    
    public JPanel getJPanel() {
        panel = new JPanel(new GridLayout(propertyList.size(), 2, 15, 15));  
        panel.setBackground(Color.LIGHT_GRAY);
        for(Property p : propertyList) {
            panel.add(new JLabel(p.getLabel()));
            switch (p.getPropertyType()) {
                case INTEGER :
                    JTextField t;
                    if (p.getValue() != null) {
                        t = new JTextField(Integer.toString((int)p.getValue()));  
                    }
                    else {
                        t = new JTextField("");
                    }
                    t.addKeyListener(new KeyAdapter() {            
                        public void keyReleased(KeyEvent e) {
                            p.setChanged(true);
                            int number = 0;
                            JTextField textField = (JTextField) e.getSource();
                            String text = textField.getText();
                            try {
                                number = Integer.parseInt(text);
                            } catch (NumberFormatException exep) {
                                t.setText("");
                            }
                            if(e.getKeyChar() == '\n') {
                                if(text.length() > 0) {
                                    try {
                                        number = Integer.parseInt(text);
                                    } catch (NumberFormatException exep) {
                                        t.setText("");
                                        p.setValue(null);
                                        stage.checkProperties();
                                    }
                                    p.setValue(number);  
                                    stage.checkProperties();
                                }
                                else {
                                    p.setValue(null);  
                                    stage.checkProperties();
                                }
                            }
                        }
                        public void keyTyped(KeyEvent e) {
                        }
                        public void keyPressed(KeyEvent e) {
                        }
                    });                    
                    panel.add(t);
                    break;
                case COLOR :
                    JLabel label =  new JLabel();
                    Color defaultColor;
                    if (p.getValue() != null) {
                        defaultColor = (Color)p.getValue();
                    }
                    else {
                        defaultColor = Color.WHITE;                        
                        label.setAlignmentX(CENTER_ALIGNMENT);
                        label.setText("multi");
                    }
                    label.setBackground(defaultColor);
                    label.setOpaque(true);
                    label.addMouseListener(new MouseAdapter()   {   
                    public void mouseClicked(MouseEvent e)   
                    {        
                        p.setChanged(true);
                        Color color = JColorChooser.showDialog(
                                null, p.getLabel(), defaultColor);
                        label.setBackground(color);
                        p.setValue(color);
                        stage.checkProperties();
                    }   
                    });
                    panel.add(label);             
            }
        }
        return panel;
    }
}