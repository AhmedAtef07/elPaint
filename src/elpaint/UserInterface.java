package elpaint;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    JPanel buttonsJPanel, propertiesJPanel;
    JToggleButton lineToggle, rectangleToggle, ellipseToggle, 
            rightTriangleToggle, isoTriangleToggle, polygonToggle, modeToggle;
    JButton saveButton, openButton, jpgButton;
    
    public UserInterface(final Stage stage) {
        this.stage = stage;
        
        setSystemLook();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                
        
        setButtonsJPanel();
        setPropertyJPanel();
        //setMenuBar();
        
        setSize(800, 650);
        setExtendedState(JFrame.MAXIMIZED_BOTH);                
        
        setButton(Button.RECTANGLE);
        
        InputHandler inputHandler = new InputHandler(stage); 
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        addKeyListener(inputHandler); 
        
        layer = new Layer(new Point(0, 0), 300, 300);
        add(layer, BorderLayout.CENTER);
        layer.setFocusable(false);
        
        this.requestFocus();                     
        setVisible(true);
    }

    public Properties getProperties() {
        return properties;
    }
    
    public Layer getLayer() {
        return layer;
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
        buttonsJPanel = new JPanel(new GridLayout(1, 11, 0, 0));
        buttonsJPanel.setBackground(Color.LIGHT_GRAY);                                                       
                
        Icon lineIcon = new ImageIcon("src/resources/line.png");
        Icon rectangleIcon = new ImageIcon("src/resources/rect.png");
        Icon ellipseIcon = new ImageIcon("src/resources/ellipse.png");
        Icon rightTriangleIcon = new ImageIcon("src/resources/tri.png");
        Icon isoTriangleIcon = new ImageIcon("src/resources/tri2.png");
        Icon polygonIcon = new ImageIcon("src/resources/polygon.png");
        Icon modeIcon = new ImageIcon("src/resources/cursor.png");
        ImageIcon saveIcon = new ImageIcon("src/resources/save.png");
        ImageIcon openIcon = new ImageIcon("src/resources/open.png");
        ImageIcon jpgIcon = new ImageIcon("src/resources/jpg.png");
        
        lineToggle = new JToggleButton(lineIcon);
        rectangleToggle = new JToggleButton(rectangleIcon);
        ellipseToggle = new JToggleButton(ellipseIcon);
        rightTriangleToggle = new JToggleButton(rightTriangleIcon);
        isoTriangleToggle = new JToggleButton(isoTriangleIcon);
        polygonToggle = new JToggleButton(polygonIcon);
        modeToggle = new JToggleButton(modeIcon);
        saveButton = new JButton(saveIcon);
        openButton = new JButton(openIcon);
        jpgButton = new JButton(jpgIcon);
        saveButton.setToolTipText("Export as XML");
        openButton.setToolTipText("Import XML");
        jpgButton.setToolTipText("Export JPG");

                
        buttonsJPanel.add(openButton);
        buttonsJPanel.add(saveButton);
        buttonsJPanel.add(jpgButton);
        buttonsJPanel.add(new JLabel());
        buttonsJPanel.add(new JLabel());
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
        saveButton.addActionListener(buttonPressed);
        openButton.addActionListener(buttonPressed);
        jpgButton.addActionListener(buttonPressed);
        
        lineToggle.setFocusable(false);
        rectangleToggle.setFocusable(false);
        ellipseToggle.setFocusable(false);
        rightTriangleToggle.setFocusable(false);
        isoTriangleToggle.setFocusable(false);
        polygonToggle.setFocusable(false);
        modeToggle.setFocusable(false);
        saveButton.setFocusable(false);
        openButton.setFocusable(false);
        jpgButton.setFocusable(false);
        //this.getContentPane().add(tools, BorderLayout.NORTH);  
        
        JPanel buttons = new JPanel();        
        buttons.add(buttonsJPanel);
        buttons.setBackground(Color.LIGHT_GRAY);
        add(buttons, BorderLayout.SOUTH);
    }        
    
    private void setPropertyJPanel() {               
        propertiesJPanel = new JPanel();
        propertiesJPanel.setBackground(Color.LIGHT_GRAY);
        propertiesJPanel.setMinimumSize(new Dimension(100, this.getHeight()));
        propertiesJPanel.setMaximumSize(new Dimension(100, this.getHeight()));
        this.add(propertiesJPanel, BorderLayout.EAST);
    }
    
    private void setMenuBar() {
        JMenuBar menuBar;
        JMenu file;        
        JMenuItem open;
        JMenuItem save;
        
        ImageIcon saveIcon = new ImageIcon("src/resources/save.png");
        menuBar = new JMenuBar();
        file = new JMenu(" File ");
        save = new JMenuItem("Save", saveIcon);
        save.addActionListener(buttonPressed);
        file.add(save);
        menuBar.add(file);
        setJMenuBar(menuBar);
        
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
                stage.setCurrentShapeType(ElShape.Type.RECTANGLE);
                break;
        }
    }
    
    AbstractAction buttonPressed = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == lineToggle) {               
                stage.setCurrentShapeType(ElShape.Type.LINE);
                setButton(Button.LINE);                
            }
            else if (e.getSource() == rectangleToggle) {
                stage.setCurrentShapeType(ElShape.Type.RECTANGLE);
                setButton(Button.RECTANGLE);
            }
            else if (e.getSource() == ellipseToggle) {
                stage.setCurrentShapeType(ElShape.Type.ELLIPSE);
                setButton(Button.ELLIPSE);
            }
            else if (e.getSource() == rightTriangleToggle) {               
                stage.setCurrentShapeType(ElShape.Type.RIGHT_TRIANGLE);
                setButton(Button.RIGHTTRIANGLE);
            }
            else if (e.getSource() == isoTriangleToggle) {               
                stage.setCurrentShapeType(ElShape.Type.ISOSCELES_TRIANGLE);
                setButton(Button.ISOTRIANGLE);
            }
            else if (e.getSource() == polygonToggle) {               
                stage.setCurrentShapeType(ElShape.Type.POLYGON);
                setButton(Button.POLYGON);
            }
            else if (e.getSource() == modeToggle) {
                if (modeToggle.isSelected()) {
                    modeToggle.setSelected(true);
                    stage.setCurrentMode(Stage.Mode.EDITING);
                    setButton(Button.RECTANGLE);
                    stage.setCurrentShapeType(ElShape.Type.RECTANGLE);                    
                } 
                else {
                    modeToggle.setSelected(false);
                    stage.setCurrentMode(Stage.Mode.DRAWING);
                }
            }
            else if (e.getSource() == saveButton) {                
                stage.save();
            }
            else if (e.getSource() == openButton) {
                stage.open();
            }
            else if (e.getSource() == jpgButton) {
                stage.image();
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
