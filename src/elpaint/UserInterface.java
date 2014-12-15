package elpaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ahmed Emad
 */

public class UserInterface extends JFrame {
    
    enum Button {
        RECTANGLE,
        ELLIPSE,
        LINE,
        RIGHTTRIANGLE,
        ISOTRIANGLE,
        POLYGON
    }
    
    private Stage stage;
    private Layer layer;
    private Properties properties;
    
    Thread thread;
    
    Color color;
    JPanel buttonsJPanel, propertiesJPanel, BackJPanel;
    JToggleButton lineToggle, rectangleToggle, ellipseToggle, 
            rightTriangleToggle, isoTriangleToggle, polygonToggle, modeToggle;
    JButton colorChooser;
    JTextField positionX, positionY, shapeHeight, shapeWidth,
               positionX2, positionY2;
    JLabel modeJLabel, colorLabel;
    
    public UserInterface(final Stage stage) {
        this.stage = stage;
        setSystemLook();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                
        
        setButtonsJPanel();
        setPropertyJPanel();
        
        setSize(800, 650);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel buttons = new JPanel();        
        buttons.add(buttonsJPanel);
        buttons.setBackground(Color.LIGHT_GRAY);
        add(buttons, BorderLayout.SOUTH);
        
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

    public Properties getProperties() {
        return properties;
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
        Icon polygonIcon = new ImageIcon("src/resources/polygon.png");
        Icon modeIcon = new ImageIcon("src/resources/cursor.png");
        
        lineToggle = new JToggleButton(lineIcon);
        rectangleToggle = new JToggleButton(rectangleIcon);
        ellipseToggle = new JToggleButton(ellipseIcon);
        rightTriangleToggle = new JToggleButton(rightTriangleIcon);
        isoTriangleToggle = new JToggleButton(isoTriangleIcon);
        polygonToggle = new JToggleButton(polygonIcon);
        modeToggle = new JToggleButton(modeIcon);
        
        buttonsJPanel.add(lineToggle);
        buttonsJPanel.add(rectangleToggle);
        buttonsJPanel.add(ellipseToggle);
        buttonsJPanel.add(rightTriangleToggle);
        buttonsJPanel.add(isoTriangleToggle);
        buttonsJPanel.add(polygonToggle);
        buttonsJPanel.add(modeToggle);
        
        lineToggle.addActionListener(buttonPressed);
        rectangleToggle.addActionListener(buttonPressed);
        ellipseToggle.addActionListener(buttonPressed);
        rightTriangleToggle.addActionListener(buttonPressed);
        isoTriangleToggle.addActionListener(buttonPressed);
        polygonToggle.addActionListener(buttonPressed);
        modeToggle.addActionListener(buttonPressed);
        
        lineToggle.setFocusable(false);
        rectangleToggle.setFocusable(false);
        ellipseToggle.setFocusable(false);
        rightTriangleToggle.setFocusable(false);
        isoTriangleToggle.setFocusable(false);
        polygonToggle.setFocusable(false);
        modeToggle.setFocusable(false);
        //this.getContentPane().add(tools, BorderLayout.NORTH);                
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    private void setPropertyJPanel() {               
        propertiesJPanel = new JPanel();
        propertiesJPanel.setBackground(Color.LIGHT_GRAY);
        propertiesJPanel.setMinimumSize(new Dimension(100, this.getHeight()));
        propertiesJPanel.setMaximumSize(new Dimension(100, this.getHeight()));
        this.add(propertiesJPanel, BorderLayout.EAST);
    }
    
    public void setButton(Button button) {
        switch (button) {
            case LINE :
                lineToggle.setSelected(true);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                polygonToggle.setSelected(false);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
            case RECTANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(true);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                polygonToggle.setSelected(false);
                break;
            case ELLIPSE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(true);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                polygonToggle.setSelected(false);
                break;
            case RIGHTTRIANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(true);
                isoTriangleToggle.setSelected(false);
                polygonToggle.setSelected(false);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
            case ISOTRIANGLE :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(true);
                polygonToggle.setSelected(false);
                modeToggle.setSelected(false);
                stage.setCurrentMode(Stage.Mode.DRAWING);
                break;
            case POLYGON :
                lineToggle.setSelected(false);
                rectangleToggle.setSelected(false);
                ellipseToggle.setSelected(false);
                rightTriangleToggle.setSelected(false);
                isoTriangleToggle.setSelected(false);
                polygonToggle.setSelected(true);
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
            else if (e.getSource() == polygonToggle) {               
                stage.setCurrentShapeType(Stage.ShapeType.POLYGON);
                setButton(Button.POLYGON);
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
    
    public void setProperties(Properties properties){
        remove(propertiesJPanel);
        propertiesJPanel = new JPanel();
        propertiesJPanel.setBackground(Color.LIGHT_GRAY);
        propertiesJPanel.setMinimumSize(new Dimension(150, this.getHeight()));
        propertiesJPanel.add(properties.getJPanel());
        this.add(propertiesJPanel, BorderLayout.EAST);
        revalidate();
        repaint();
        layer.repaint();
    }
}
