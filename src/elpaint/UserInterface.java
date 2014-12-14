package elpaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ahmed Emad
 */

public class UserInterface extends JFrame {

    enum property {
        elComponents,
        elShape,
        elLine,
        elText,        
    }
    
    enum Button {
        RECTANGLE,
        ELLIPSE,
        LINE,
        RIGHTTRIANGLE,
        ISOTRIANGLE,
    }
    
    private Layer layer;
    private Stage stage;
    private int propertiesWidth = 0;
    private int x , y, elWidth, elHeight;
    
    Thread thread;
    
    Color color;
    JPanel buttonsJPanel, elComponentsJPanel, elShapeJPanel,
           elLineJPanel, elTextJPanel, propertiesJPanel, BackJPanel;
    JToggleButton lineToggle, rectangleToggle, ellipseToggle, 
            rightTriangleToggle, isoTriangleToggle, modeToggle;
    JButton colorChooser;
    JTextField positionX, positionY, shapeHeight, shapeWidth,
               positionX2, positionY2;
    JLabel modeJLabel, colorLabel;
    
    public UserInterface(final Stage stage) {
        this.stage = stage;
        setSystemLook();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);                
        
        setButtonsJPanel();
        setPropertyJPanel();
        this.add(propertiesJPanel, BorderLayout.EAST);
        setSize(700, 800);
        
        JPanel a = new JPanel();        
        a.add(buttonsJPanel);
        a.setBackground(Color.LIGHT_GRAY);
        add(a, BorderLayout.SOUTH);
        
        setButton(Button.RECTANGLE);
        
        InputHandler inputHandler = new InputHandler(stage); 
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        addKeyListener(inputHandler); 
        
        layer = new Layer(new Point(0, 0), 300, 300);
        add(layer, BorderLayout.CENTER);
        layer.requestFocusInWindow();        
        
        setVisible(true);
    }
    
    private void setSystemLook() {
        try {
            UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
        }
        catch (ClassNotFoundException e) {
        }
        catch (InstantiationException e) {
        }
        catch (IllegalAccessException e) {
        }
    }   
    
    private void setButtonsJPanel() {
        buttonsJPanel = new JPanel(new GridLayout(1, 5, 0, 0));
        buttonsJPanel.setBackground(Color.LIGHT_GRAY);                                                       
                
        Icon lineIcon = new ImageIcon("src/resources/line.png");
        Icon rectangleIcon = new ImageIcon("src/resources/rect.png");
        Icon ellipseIcon = new ImageIcon("src/resources/ellipse.png");
        Icon rightTriangleIcon = new ImageIcon("src/resources/tri.png");
        Icon isoTriangleIcon = new ImageIcon("src/resources/tri2.png");
        Icon modeIcon = new ImageIcon("src/resources/cursor.png");
        
        lineToggle = new JToggleButton(lineIcon);
        rectangleToggle = new JToggleButton(rectangleIcon);
        ellipseToggle = new JToggleButton(ellipseIcon);
        rightTriangleToggle = new JToggleButton(rightTriangleIcon);
        isoTriangleToggle = new JToggleButton(isoTriangleIcon);
        modeToggle = new JToggleButton(modeIcon);
        lineToggle.setBackground(Color.BLUE);
        
        buttonsJPanel.add(lineToggle);
        buttonsJPanel.add(rectangleToggle);
        buttonsJPanel.add(ellipseToggle);
        buttonsJPanel.add(rightTriangleToggle);
        buttonsJPanel.add(isoTriangleToggle);
        buttonsJPanel.add(modeToggle);
        
        lineToggle.addActionListener(buttonPressed);
        rectangleToggle.addActionListener(buttonPressed);
        ellipseToggle.addActionListener(buttonPressed);
        rightTriangleToggle.addActionListener(buttonPressed);
        isoTriangleToggle.addActionListener(buttonPressed);
        modeToggle.addActionListener(buttonPressed);
        
        lineToggle.setFocusable(false);
        rectangleToggle.setFocusable(false);
        ellipseToggle.setFocusable(false);
        rightTriangleToggle.setFocusable(false);
        isoTriangleToggle.setFocusable(false);
        modeToggle.setFocusable(false);
        //this.getContentPane().add(tools, BorderLayout.NORTH);                
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    private void setPropertyJPanel() {
        positionX = new JTextField();
        positionY = new JTextField();
        positionX2 = new JTextField();
        positionY2 = new JTextField();
        shapeWidth = new JTextField();
        shapeHeight = new JTextField();
        colorChooser = new JButton();
        colorChooser.setFocusable(false);
        colorChooser.setBackground(Color.BLUE);
        colorChooser.addActionListener(buttonPressed);
        
        propertiesJPanel = new JPanel();
        propertiesJPanel.setBackground(Color.LIGHT_GRAY);
        
        elComponentsJPanel = new JPanel(new GridLayout(5, 2, 0, 0));
        elComponentsJPanel.setBackground(Color.LIGHT_GRAY);
        elComponentsJPanel.add(new JLabel(" X : "));
        elComponentsJPanel.add(positionX);
        elComponentsJPanel.add(new JLabel(" Y : "));
        elComponentsJPanel.add(positionY);
        elComponentsJPanel.add(new JLabel(" Color : "));
        elComponentsJPanel.add(colorChooser);
        
        elShapeJPanel = new JPanel(new GridLayout(5, 2, 2, 2));
        elShapeJPanel.setBackground(Color.LIGHT_GRAY);
        elShapeJPanel.add(new JLabel(" X : "));
        elShapeJPanel.add(positionX2);
        elShapeJPanel.add(new JLabel(" Y : "));
        elShapeJPanel.add(positionY2);
        elShapeJPanel.add(new JLabel(" Width : "));
        elShapeJPanel.add(shapeWidth);
        elShapeJPanel.add(new JLabel(" Height : "));
        elShapeJPanel.add(shapeHeight);
        elShapeJPanel.add(new JLabel(" Color : "));
        
        colorLabel = new JLabel();        
        colorLabel.setOpaque(false);
        color = new Color(0);
        color = Color.WHITE;
        color = this.getBackground();
        colorLabel.setBackground(color);
        colorLabel.setOpaque(true);
        colorLabel.addMouseListener(new MouseAdapter()   {   
        public void mouseClicked(MouseEvent e)   
        {   
              color = JColorChooser.showDialog(null, "Choose Color", 
                        color); 
              layer.setColor(color);
              colorLabel.setBackground(color);
        }   
        });
        
        BackJPanel = new JPanel(new GridLayout(1,2,2,2));
        BackJPanel.setBackground(Color.LIGHT_GRAY);
        BackJPanel.add(new JLabel(" Color : "));
        BackJPanel.add(colorLabel);    
        
        propertiesJPanel.add(BackJPanel);
    }
    
    private void deleteProperties(boolean all) {
        if (all)
            this.remove(propertiesJPanel);
        else {
            try {
                propertiesJPanel.remove(elComponentsJPanel);
            } catch (Exception e) {
            }
            try {
                propertiesJPanel.remove(elShapeJPanel);
            } catch (Exception e) {
            }
            try {
                propertiesJPanel.remove(elLineJPanel);
            } catch (Exception e) {
            }
            try {
                propertiesJPanel.remove(elTextJPanel);
            } catch (Exception e) {
            }
        }
        positionX.setFocusable(false);
        positionY.setFocusable(false);
        positionX2.setFocusable(false);
        positionY2.setFocusable(false);
        shapeWidth.setFocusable(false);
        shapeHeight.setFocusable(false);
    }
    
    public void setProperty(property p) {      
        if (p == null) {
            deleteProperties(true);
            this.setSize(this.getWidth()-propertiesWidth, this.getHeight());
            propertiesWidth = 0;         
        }            
        else {
            deleteProperties(false);
            switch(p) {
                case elComponents :
                    propertiesJPanel.add(elComponentsJPanel);
                    this.add(propertiesJPanel, BorderLayout.EAST);
                    if (propertiesWidth != 150) {
                        this.setSize(this.getWidth()-propertiesWidth, 
                                this.getHeight());
                        propertiesWidth = 150;
                        this.setSize(this.getWidth() + propertiesWidth, 
                                this.getHeight());                        
                    }
                    break;
                case elShape : 
                    propertiesJPanel.add(elShapeJPanel);
                    this.add(propertiesJPanel, BorderLayout.EAST);
                    if (propertiesWidth != 152) {
                        this.setSize(this.getWidth()-propertiesWidth, 
                                this.getHeight());
                        propertiesWidth = 152;
                        this.setSize(this.getWidth() + propertiesWidth, 
                                this.getHeight());                        
                    }                    
                    break;
            }
        }            
    }        
    
    public void setButton(Button button) {
        switch (button) {
            case LINE :
                lineToggle.setSelected(true);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
            case RECTANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(true);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                break;
            case ELLIPSE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(true);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                break;
            case RIGHTTRIANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(true);
                isoTriangleToggle.setSelected(false);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
            case ISOTRIANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(true);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
                
        }
    }
    
    public void setCurrentMode(Stage.Mode mode) {
        switch (mode) {
            case DRAWING :
                modeToggle.setSelected(false);                
                break;
            case EDITING :
                modeToggle.setSelected(true);
                setButton(Button.RECTANGLE);
                stage.setCurrentShapeType(Stage.ShapeType.RECTANGLE);
                break;
        }
    }
    
    AbstractAction buttonPressed = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == lineToggle) {               
                stage.setCurrentShapeType(Stage.ShapeType.LINE);
                setButton(Button.LINE);                
            }
            else if (e.getSource() == rectangleToggle) {
                stage.setCurrentShapeType(Stage.ShapeType.RECTANGLE);
                setButton(Button.RECTANGLE);
            }
            else if (e.getSource() == ellipseToggle) {
                stage.setCurrentShapeType(Stage.ShapeType.ELLIPSE);
                setButton(Button.ELLIPSE);
            }
            else if (e.getSource() == rightTriangleToggle) {               
                stage.setCurrentShapeType(Stage.ShapeType.RIGHT_TRIANGLE);
                setButton(Button.RIGHTTRIANGLE);
            }
            else if (e.getSource() == isoTriangleToggle) {               
                stage.setCurrentShapeType(Stage.ShapeType.ISOSCELES_TRIANGLE);
                setButton(Button.ISOTRIANGLE);
            }
            else if (e.getSource() == colorChooser) {
                color = JColorChooser.showDialog(null, "Choose Color", 
                        colorChooser.getBackground());             
                colorChooser.setBackground(color);
            }
            else if (e.getSource() == modeToggle) {
                if (modeToggle.isSelected()) {
                    modeToggle.setSelected(true);
                    stage.setCurrentMode(Stage.Mode.EDITING);
                    setButton(Button.RECTANGLE);
                    stage.setCurrentShapeType(Stage.ShapeType.RECTANGLE);                    
                } 
                else {
                    modeToggle.setSelected(false);
                    stage.setCurrentMode(Stage.Mode.DRAWING);
                }
            }
        }
    };
}
