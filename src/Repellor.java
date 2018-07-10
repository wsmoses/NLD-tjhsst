
import javax.swing.*;
   import java.io.*;
   import java.awt.*;
   import java.awt.image.*;

import javax.imageio.*;
   public class Repellor extends JPanel
   { 
      private static volatile int threaddone = 1;
      private static final int xPanel = 12500;
      private static final int yPanel = 12500;
      private static final BufferedImage img = new BufferedImage(xPanel,yPanel,BufferedImage.TYPE_INT_RGB); 

 	 public static final WritableRaster mainR = ColorModel.getRGBdefault().createCompatibleWritableRaster(xPanel, yPanel);
 	static final int[] imagePixelData = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
  //   private static Graphics2D myBuffer = (Graphics2D)img.getGraphics();

     public static final double xmin = 0.025, xmax = 0.27, ymin = -3, ymax = 3;
     //normal
     //public static final double xmin = 0.159, xmax = 0.16, ymin = -.01, ymax = .01;
    
     //zoomed in on wing thing
     //public static final double xmin = 0.025, xmax = 0.03, ymin = -.3, ymax = .3;
     public  static final double pix = (xmax-xmin)/xPanel, ypix = (ymax-ymin)/yPanel;
     public static final double xConvert = (xmax-xmin)/xPanel, yConvert = (ymax-ymin)/yPanel;
     public static final double dT = .1, b = 0.25;
     public static final double dT2o2 = dT*dT/2, b2 = b*b;
    	 static final double[][] balls = {
//    		 {0, .5}
//    		 ,{0, -.5}
//    		 ,{.86, 0}
    		 {0, 1}
    		 ,{0, 0}
    		 ,{0, -1}
    		 ,{.86, .5}
    		 ,{.86, -.5}
    		 ,{1.72, 0}
    	 };

   	  final static JFrame fr = new JFrame("Repellor");
      public static final void main(String[] args)throws Exception
      {
    	  final Repellor graph = new Repellor();
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
     save("F"+xPanel+"x"+yPanel);
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
      public static final double get(final double xS, final double yS) {
    	 double[] self = {-3, yS, xS, 0}; 
    	 double t = 0;
    	  while(self[0]>=-3 && self[0]<=3 && self[1]>=-3 && self[1]<=3){
    		  for(int ball = 0; ball<balls.length; ball++){
    			  double r2 = (balls[ball][0]-self[0])*(balls[ball][0]-self[0])+(balls[ball][1]-self[1])*(balls[ball][1]-self[1]);
    			  double angleTo = Math.atan2((balls[ball][1]-self[1]), (balls[ball][0]-self[0]));
    			  double mR2oB2 = Math.exp(-r2/(b2));
    		  double xA = 
    				  Math.cos(angleTo)*mR2oB2;
    				 //Math.cos(angleTo)* b / r2;
    				  //Math.exp(mR2oB2*(self[0]-balls[ball][0]));
    				  //acc(self[0], self[2], t);
    		  self[0]+=self[2]*dT+xA*dT2o2;
    		  self[2]+=xA*dT;
    		  double yA = 
    				  Math.sin(angleTo)*mR2oB2;
     				// Math.sin(angleTo)* b / r2;
    				  //Math.exp(mR2oB2*(self[1]-balls[ball][1]));
    				  //acc(self[1], self[3], t);
    		  self[1]+=self[3]*dT+yA*dT2o2;
    		  self[3]+=yA*dT;
    		  }
    		  t+=dT;
    	  }
    	  return t/30;
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
   