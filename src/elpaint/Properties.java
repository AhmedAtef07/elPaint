package elpaint;

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
    private Property pr;
    int num = 0;
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
        panel = new JPanel(new GridLayout(propertyList.size(), 2));        
        for(Property p : propertyList) {
            pr = p;
            panel.add(new JLabel(p.getLabel()));
            switch (p.getPropertyType()) {
                case INTEGER :
                    num = (int)p.getValue();
                    JTextField t = new JTextField(Integer.toString(
                            (int)p.getValue()));
                    t.addKeyListener(new KeyAdapter() {            
                        public void keyReleased(KeyEvent e) {
                            JTextField textField = (JTextField) e.getSource();
                            String text = textField.getText();
                            if(e.getKeyChar() == '\n') {
                                try {
                                    num = Integer.parseInt(text);
                                } catch (NumberFormatException exep) {
                                    t.setText("");
                                }
                                p.setValue(num);  
                                stage.checkProperties();
                            }
                            try {
                                num = Integer.parseInt(text);
                            } catch (NumberFormatException exep) {
                                t.setText("");
                            }
                        }
                        public void keyTyped(KeyEvent e) {
                        }
                        public void keyPressed(KeyEvent e) {
                        }
                    });
                    p.setValue(num);
                    panel.add(t);
                    break;
                case COLOR :
                    JLabel l = new JLabel();
                    l.setBackground((Color)p.getValue());
                    l.setOpaque(true);
                    l.addMouseListener(new MouseAdapter()   {   
                    public void mouseClicked(MouseEvent e)   
                    {
                        Color color = JColorChooser.showDialog(
                                null, p.getLabel(), (Color)p.getValue());
                        p.setValue(color);
                        l.setBackground(color);
                        stage.checkProperties();
                    }   
                    });
                    panel.add(l);             
            }
        }
        return panel;
    }
}