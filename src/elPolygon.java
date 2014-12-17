import java.awt.*;
import java.awt.geom.AffineTransform;
/**
 *
 * @author Hassan Rezk
 */

public class elPolygon extends ElShape {
    
    private int[] ptx;
    private int[] pty;
    private int CenterX;
    private int CenterY;
    private int radius, n;
    private double angel;
    public elPolygon(Point p1 , Point p2 ,int n , double angle){
        super();
        ptx = new int[n];
        pty = new int[n];   
        this.CenterX = p1.x;
        this.CenterY = p1.y;
        this.n = n;
        this.angel = angel ;
        this.radius = (int) Math.sqrt(Math.pow(p2.x - p1.x,2) + 
                Math.pow(p2.y-p1.y,2));
        this.width = 2*radius;
        this.height= 2*radius;
        System.out.println(radius);
        makePoints();
        getUpperleft();
    }
    
    public elPolygon(int[] x, int[] y) {
        this.ptx = x;
        this.pty = y;
        this.angel = 0.0;
        this.n = 5;
        this.radius = 0;
    }
    public elPolygon(int x, int y, int radius, int n, double angel) {
        super();
        this.x = x;
        this.y = y;
        this.CenterX = x ;
        this.CenterY = y ;
        this.radius = radius;
        this.width = 2*radius;
        this.height =2*radius;
        this.n = n;
        this.angel = angel;
        ptx = new int[n];
        pty = new int[n];
      //  cloneSelf();
        makeCenterX();
        makeCenterY();
        makePoints();
    }
    public elPolygon(int x,int y,int radius,int n,double angel,
            Color FillColor,Color StrokeColor,Stroke LineType){
        super(FillColor,StrokeColor,LineType);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.width = 2*radius;
        this.height =2*radius;
        this.n = n;
        this.angel = angel;
        ptx = new int[n];
        pty = new int[n];
        makeCenterX();
        makeCenterX();
        makePoints();
    }
    private void makeCenterX(){
        this.CenterX = x + (width/2);
    }
    
    private void makeCenterY(){
        this.CenterY = y + (height/2);
    }
    private void makePoints(){ 
        for (int i = 0; i < getN(); i++) {
            ptx[i] = (int)(this.getCenterX() + getRadius() * 
                    Math.cos(2 * Math.PI * i / getN()));
            pty[i] = (int)(this.getCenterY() + getRadius() * 
                    Math.sin(2 * Math.PI * i / getN()));
        }
    }
    
    public int[] getXpts() {
        int[] pt = new int[ptx.length];
        for(int i = 0 ; i < pt.length ; i ++)
            pt[i] = ptx[i];
        return pt;
    }
    
    public int[] getYpts() {
        int[] pt = new int[pty.length];
        for ( int i = 0 ; i < pt.length ; i ++ )
            pt[i] = pty[i];
        return pt;
    }
    
    private void getUpperleft(){
         this.x = 10000000;
         this.y = 10000000;
        for (int i = 0; i < getN(); i++) {
           if(ptx[i] < x) this.x=ptx[i];
           if(pty[i] < y) this.y=pty[i];
     
        }
    }
    @Override
    public Shape getShape() {
        
        AffineTransform at = AffineTransform.getRotateInstance(angel, 
                getCenterX(), getCenterY());
        Shape rotated = at.createTransformedShape(new Polygon(ptx, pty,
                ptx.length));
        return rotated;
    }
    
    /**
     *
     * @param width
     */
    @Override
    public void move(int xDisplacment, int yDisplacment){
            //cloneSelf();
//            for(int i = 0 ; i < ptx.length ; i ++) {
//                setX(ptx[i]+xDisplacment);
//                setY(pty[i]+yDisplacment);
//            }
     //   makePoints();
        setX(getClonedX()-xDisplacment);
        setY(getClonedY()-yDisplacment);
        makePoints();
    }
    @Override 
    public void setX(int x){
        this.x = x;
        this.CenterX = x ;
        //makePoints();
    }
    @Override
    public void setY(int y){
        this.y = y;
        this.CenterY = y;
        //makePoints();
    }
    @Override
    public ElShape getCopy() {
        return new elPolygon(getCenterX(), getCenterY(), getRadius(), getN(), 
                getAngel());
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getN() {
        return n;
    }

    public double getAngel() {
        return angel;
    }

    public void setAngel(double angel) {
        this.angel = angel;
    }

    public int getCenterX() {
        return CenterX;
    }

    public void setCenterX(int CenterX) {
        this.CenterX = CenterX;
    }

    public int getCenterY() {
        return CenterY;
    }

    public void setCenterY(int CenterY) {
        this.CenterY = CenterY;
    }
    @Override
    public void setWidth(int width) {
       this.radius = width/2;
       makePoints();
    }
    @Override        
    public void setHeight(int height) {
        this.height = Math.max(height, 3);
         makePoints();
    }
}