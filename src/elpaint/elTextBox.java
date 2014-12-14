package elpaint;

import java.awt.*;

/**
 *
 * @author Hassan Rezk
 */
public class elTextBox extends elComponent {
    
    private String text;
    private Font font;
    private Color color;
    private int x, y; // lower left corner of the enclosing textbox
    
    public elTextBox(String text, Font font, Color color, int x, int y) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
