package elpaint;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Ahmed Atef
 */
public class InputHandler implements MouseMotionListener, MouseListener,
        KeyListener{
    
    Triggable triggable;
    public InputHandler(Triggable triggable) {
        this.triggable = triggable;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        triggable.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        triggable.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        triggable.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        triggable.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        triggable.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        triggable.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        triggable.mouseExited(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        triggable.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        triggable.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        triggable.keyReleased(e);
    }
}
