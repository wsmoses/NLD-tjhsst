import static java.lang.Math.*;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;
   public class Baseball extends JPanel
   { 
	   public static final boolean saveImage = false;
      private static volatile int threaddone = 1;
      private static final int xPanel = 50;
      private static final int yPanel = 50;
      public static final double dt = .01;
      private static final BufferedImage img = new BufferedImage(xPanel,yPanel,BufferedImage.TYPE_INT_RGB); 

 	 public static final WritableRaster mainR = ColorModel.getRGBdefault().createCompatibleWritableRaster(xPanel, yPanel);
 	static final int[] imagePixelData = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
  //   private static Graphics2D myBuffer = (Graphics2D)img.getGraphics();

     public static final double xmin = 0, xmax = 2*PI, ymin = 0, ymax =2*PI;
     //normal
     //public static final double xmin = 0.159, xmax = 0.16, ymin = -.01, ymax = .01;
    
     //zoomed in on wing thing
     //public static final double xmin = 0.025, xmax = 0.03, ymin = -.3, ymax = .3;
     public  static final double pix = (xmax-xmin)/xPanel, ypix = (ymax-ymin)/yPanel;
     public static final double xConvert = (xmax-xmin)/xPanel, yConvert = (ymax-ymin)/yPanel;
    

   	  final static JFrame fr = new JFrame("Repellor");
      public static final void main(String[] args)throws Exception
      {
    	  final Baseball graph = new Baseball();
    	  graph.setPreferredSize(new Dimension(xPanel, yPanel));
    	  fr.setContentPane(graph);
    	  fr.pack();
    	  fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	  fr.setVisible(true);
     for(int i = 1; i<portion.AMOUNT; i++)
     {
        try{
           new portion(i).start(); 
        }
           catch(Throwable yig)
           {
        	   yig.printStackTrace();
           }
     } 
     new portion(0).run(); 
     while((threaddone-1)*100.0/portion.AMOUNT<100)
     {
    //    System.out.println((threaddone-1)*100.0/xPanel+"%");
     }
     graph.repaint();
     if(saveImage) save("F"+xPanel+"x"+yPanel);
      }
      public final void paintComponent(Graphics g)
      {
         g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
      }
      public static final void save(String file)
      {
         try {
        	 File dir = new File(".");
        	 System.out.println(dir.getAbsolutePath());
        	 while(contains(dir.list(), file+".png"))
        		 file+="A";
            File outputfile = new File(file+".png");
            ImageIO.write(img, "png", outputfile);
            System.out.println("Saved: "+file+".png");
            fr.setTitle("Done - "+fr.getTitle());
         } 
            catch (IOException e) {
            	e.printStackTrace();
            }
      }	
      public static final boolean contains(String[] a, String b){
    	  for(String c:a)
    		  if(c.equalsIgnoreCase(b))
    			  return true;
    	  return false;
      }
      public static final double F(final double  v){
    	  return 0.0039+.0058/(1+Math.exp((v-35)/5));
      }
      public static final double rpmToRad(final double r){
    	  return r/60*2*PI;
      }
      public static final double mphToMps(final double r){
    	  return r/2.2369362920544;
      }
      public static double theta = toRadians(1), B = 1, omega = rpmToRad(1800);
      public static final double get(final double xS, final double yS) {
    	 double[] s = new double[]{0, 0, 0};
    	 double[] v = new double[]{85*Math.cos(yS), 0, 85*Math.sin(yS)};
    	 double[] vp = new double[3];
    	 int i;
    	for(i = 0; i<100000 && s[0]+v[0]*dt<18.4; i++){
    	 	s[0]+=v[0]*dt;
    		  s[1]+=v[1]*dt;
    		  s[2]+=v[2]*dt;
    		  double vMag = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
    		  vp[0]=v[0] +dt*( -F(vMag)*vMag*v[0]+B*omega*(v[0]*Math.sin(xS)-v[1]*Math.cos(xS)));
    		  vp[1]=v[1] +dt*(-F(vMag)*vMag*v[1]+B*omega*cos(xS));
    		  vp[2]=v[2] +dt*(-9.81-F(vMag)*vMag*v[2]-B*omega*v[0]*sin(xS));
    		  
    		  double[] temp = v;
    		  v = vp;
    		  vp = temp;
    	  }
    	 double vMag = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
//		  System.out.println(Arrays.toString(v)+Arrays.toString(s));
    	 // return s[1]/200;
    	 return i*dt;
    	 //return Math.abs(Math.atan2(self[1], self[0]))/Math.PI;
    	  }
   
      public static final class portion extends Thread
      {
    	  public static final int AMOUNT = 8;
         private final int xs;
         public portion(int xmin)
         {
            xs = xmin;
         }
         public final void run()
         {
            	for(int x = xs; x<xPanel; x+=AMOUNT){
            		final double myX = x*xConvert+xmin;
               for(int y = 0; y<yPanel; y++)
                	  imagePixelData[y*xPanel+x] = Hue.DecToRGB( get(myX, ymax-y*yConvert));
        	}     
                threaddone++;    	
         }
      
      }
   }
   