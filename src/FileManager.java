

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Hassan
 */

public class FileManager {
    
    public static void Export(String filepath, LinkedList<ElShape> elShapes) 
            throws FileNotFoundException, IOException, AWTException {
	XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
                new FileOutputStream(filepath)));
        
        for (ElShape shape : elShapes) {
            e.writeObject(shape.getShape());
            e.writeObject(shape.getFillColor());
            e.writeObject(shape.getBorderColor());
            e.writeObject(shape.getLineType());
        }
        
	e.flush();
	e.close();
    }
    
    public static void Import(String filepath, LinkedList<ElShape> elShapes) 
            throws FileNotFoundException {
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filepath)));
        Object result;
        int j = 0;
        try{
            while(true) {
                result = d.readObject();
                if ( result instanceof Rectangle2D.Float ) {
                    Rectangle2D.Float s = (Rectangle2D.Float) result;
                    elShapes.add(new ElRectangle(s.getBounds().x,
                            s.getBounds().y, s.getBounds().width, 
                            s.getBounds().height ) );
                } else if ( result instanceof Ellipse2D.Float ) {
                    Ellipse2D.Float s = (Ellipse2D.Float) result;
                    elShapes.add(new ElEllipse(s.getBounds().x,s.getBounds().y, 
                            s.getBounds().width, s.getBounds().height));
                } else if ( result instanceof Polygon) {
                    Polygon s = (Polygon) result;
                    int[] x = s.xpoints;
                    int[] y = s.ypoints;
                    Point[] p = new Point[x.length];
                    for( int i = 0 ; i < x.length ; i ++)
                        p[i] = new Point(x[i],y[i]);
                    if ( x.length == 3 ) 
                        elShapes.add(new ElTriangle(p[0],p[1],p[2]));
                    else if ( x.length == 2 )
                        elShapes.add(new ElLine(p[0], p[1]));
                }
                result = d.readObject();
                Color c = (Color)result;
                elShapes.get(elShapes.size()-1).setFillColor(c);
                result = d.readObject();
                c = (Color)result;
                elShapes.get(elShapes.size()-1).setBorderColor(c);
                result = d.readObject();
                Stroke s = (Stroke) result;
                elShapes.get(elShapes.size()-1).setLineType(s);
            }
        } catch(Exception e) {
            d.close();
        }
    }
}
