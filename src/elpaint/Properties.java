package elpaint;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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
    
    public void update() {
        getJPanel();
    }
    
    public JPanel getJPanel() {
        panel = new JPanel(new GridLayout(propertyList.size(), 2));
        for(Property p : propertyList) {
            panel.add(new JLabel(p.getLabel()));
            switch (p.getPropertyType()) {
                case INTEGER :
                    JTextField t = new JTextField(Integer.toString(
                            (int)p.getValue()).toString());
                    t.getDocument().addDocumentListener(documentListener);
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
    
      DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      public void insertUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      public void removeUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      private void printIt(DocumentEvent documentEvent) {
        DocumentEvent.EventType type = documentEvent.getType();
        String typeString = null;
        if (type.equals(DocumentEvent.EventType.CHANGE)) {
          typeString = "Change";
          
        }  else if (type.equals(DocumentEvent.EventType.INSERT)) {
          typeString = "Insert";
        }  else if (type.equals(DocumentEvent.EventType.REMOVE)) {
          typeString = "Remove";
        }
//        System.out.print("Type : " + typeString);
//        Document source = documentEvent.getDocument();
//        int length = source.getLength();
//        try {
//          System.out.println("Contents: " + source.getText(0, length));
//        } catch (BadLocationException badLocationException) {
//          System.out.println("Contents: Unknown");
//        }        
      }
    };
}
