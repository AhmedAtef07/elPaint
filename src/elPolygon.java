

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
        this.radius = (int) Math.sqrt(Math.pow(p2.x - p1.x,2) + Math.pow(p2.y-p1.y,2));
        this.width = 2*radius;
        this.height= 2*radius;
        System.out.println(radius);
        makePoints();
        getUpperleft();
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
            ptx[i] = (int)(this.getCenterX() + getRadius() * Math.cos(2 * Math.PI * i / getN()));
            pty[i] = (int)(this.getCenterY() + getRadius() * Math.sin(2 * Math.PI * i / getN()));
        }
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
        
        AffineTransform at = AffineTransform.getRotateInstance(angel, getCenterX(), getCenterY());
        Shape rotated = at.createTransformedShape(new Polygon(ptx, pty, n));
        return rotated;
    }
    @Override
    public void move(int xDisplacment, int yDisplacment){
            //cloneSelf();
            setX(xDisplacment);
            setY(yDisplacment);
            makePoints();
     //   makePoints();
    }
    @Override 
    public void setX(int x){
        //cloneSelf();
        this.x = this.x - x ;
        this.CenterX = this.CenterX - x  ;
       // makePoints();
    }
    @Override
    public void setY(int y){
        //cloneSelf();
        this.y=this.y - y;
        this.CenterY = this.CenterY - y;
       // makePoints();
    }
    @Override
    public ElShape getCopy() {
        return new elPolygon(getCenterX(), getCenterY(), getRadius(), getN(), getAngel());
    }

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * @return the angel
     */
    public double getAngel() {
        return angel;
    }

    /**
     * @param angel the angel to set
     */
    public void setAngel(double angel) {
        this.angel = angel;
    }

    /**
     * @return the CenterX
     */
    public int getCenterX() {
        return CenterX;
    }

    /**
     * @param CenterX the CenterX to set
     */
    public void setCenterX(int CenterX) {
        this.CenterX = CenterX;
    }

    /**
     * @return the CenterY
     */
    public int getCenterY() {
        return CenterY;
    }

    /**
     * @param CenterY the CenterY to set
     */
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
        
    }
}

//    public Shape move(int displacement , SelectionBox.ResizeBoxType selectedResizeBoxType){
//        switch(selectedResizeBoxType){
//            case NW:
//                for(int i=0;i<n;i++){
//                    ptx[i] -= displacement;
//                    pty[i] -= displacement;
//                }
//                break;
//            case N:
//                for(int i=0;i<n;i++){
//                  //  ptx[i] += displacement;
//                    pty[i] -= displacement;
//                }
//                break;
//            case NE:
//                for(int i=0;i<n;i++){
//                    ptx[i] += displacement;
//                    pty[i] -= displacement;
//                }
//                break;
//            case E:
//                for(int i=0;i<n;i++){
//                    ptx[i] += displacement;
//                   // pty[i] += displacement;
//                }
//                break;
//            case W:
//                for(int i=0;i<n;i++){
//                    ptx[i] -= displacement;
//                   // pty[i] += displacement;
//                }
//                break;
//                
//            case S:
//                for(int i=0;i<n;i++){
//                //    ptx[i] -= displacement;
//                    pty[i] += displacement;
//                }
//                break;
//            case SE:
//                for(int i=0;i<n;i++){
//                    ptx[i] += displacement;
//                    pty[i] += displacement;
//                }
//                break;
//            case SW:
//                for(int i=0;i<n;i++){
//                    ptx[i] -= displacement;
//                    pty[i] += displacement;
//                }
//                break;    
//        }
//        return getShape();
//    }
//    public void Move(int displacement ,){
//        switch(){
//            case NE : 
//                   
//        }
        
//    }