  import javax.swing.*;
   import java.util.*;
   import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
   public class StatsGraph extends JPanel
   {
   
   /**
    * 
    */
      private static final long serialVersionUID = 1L;
      private JFrame f = new JFrame("");
      static 
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	    static int windowX = screenSize.width;
	    static int windowY = screenSize.height;
      private int xPanel = screenSize.width;
      private int yPanel = screenSize.height;
      private BufferedImage img = new BufferedImage(xPanel,yPanel,BufferedImage.TYPE_INT_RGB); 
      private Graphics2D myBuffer = (Graphics2D)img.getGraphics();
      public StatsGraph()
      {
    	  myBuffer.setColor(Color.black);
    		f.setUndecorated(true);
    	    f.setSize(windowX, windowY);
    	    f.setLocation(0, 0);
    		addKeyListener(new KeyAdapter(){
    			@Override
    			public void keyPressed(KeyEvent e){
    				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE){
    					System.exit(0);
    				}
    			}
    		});
    		setFocusable(true);
    		requestFocusInWindow();
         maker();
      }
      public void paintComponent(Graphics g)
      {
         g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
      }
      public void maker()
      {
     //    f = new JFrame("StatsGraph");
      //   f.setSize(xPanel, yPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         f.setContentPane(this);
         f.setVisible(true);
         myBuffer.setColor(Color.WHITE);
         myBuffer.fillRect(0, 0, xPanel, yPanel);
         repaint();
      }
      
      public static double minimum(double[] data){
    	  double a = data[0];
    	  for(double e:data)
    		  a = Math.max(a,  e);
    	  return a;
      }
      public static double maximum(double[] data){
    	  double a = data[0];
    	  for(double e:data)
    		  a = Math.max(a,  e);
    	  return a;
      }
      
      public void xAxis(int xMin,int xMax, int y)
      {
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawLine(xMin, y, xMax, y);
      }
      public void yAxis(int yMin, int yMax, int x)
      {
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawLine(x, yMax, x, yMin);
      }
      public void pixel(int x, int y, Color c)
   	{
   	myBuffer.setColor(c);
   	myBuffer.fillRect(x, y, 1, 1);
   	}
     
      public void graph(Function f, double xMin, double xMax, double yMin, double yMax, int color){
    	  for(int x = 0; x<img.getWidth(); x++)
    	  {
    		  double xx = xMin+x*(xMax-xMin)/img.getWidth();
    		  double yy = f.get(xx);
    		  int y = (int)((yy-yMin)/(yMax-yMin)*img.getHeight());
    		  if(img.getHeight()-y-1<img.getHeight()  && img.getHeight()-y-1>=0)
    		  img.setRGB(x, img.getHeight()-y-1, color);
    	  }
    	  repaint();
      }
      public void plot(double xx, double yy, double xMin, double xMax, double yMin, double yMax, int color){
		  int x = (int)((xx-xMin)/(xMax-xMin)*img.getWidth());
		  int y = (int)((yy-yMin)/(yMax-yMin)*img.getHeight());
		  if(y<img.getHeight() && x<img.getWidth())
		  img.setRGB(x, img.getHeight()-y-1, color);
      }
      public void fillVLine(double xx, double y1, double y2, double xMin, double xMax, double yMin, double yMax, int color){
		  int x = (int)((xx-xMin)/(xMax-xMin)*img.getWidth());
		  int y = (int)((1-(y1-yMin)/(yMax-yMin))*img.getHeight());
		  int yw = (int)((1-(y2-yMin)/(yMax-yMin))*img.getHeight());
		  myBuffer.setColor(new Color(color));
		  myBuffer.drawLine(x, y, x, yw);
		//  if(yw<y){
		//	for(int i = yw; i<=y; i++)
		//		  if(img.getHeight()-i-1<img.getHeight() && img.getHeight()-i-1>=0 && x<img.getWidth() && x>=0)
		//			  img.setRGB(x, img.getHeight()-i-1, color);
		 // }
		  //else{
		//		for(int i = y; i<=yw; i++)
		//			  if(img.getHeight()-i-1<img.getHeight() && img.getHeight()-i-1>=0 && x<img.getWidth() && x>=0)
		//				  img.setRGB(x, img.getHeight()-i-1, color);
		 // }
      }
      public void fillHLine(double x1, double x2, double yy, double xMin, double xMax, double yMin, double yMax, int color){
		  int x = (int)((x1-xMin)/(xMax-xMin)*img.getWidth());
		  int xw = (int)((x2-xMin)/(xMax-xMin)*img.getWidth());
		  int y = (int)((1-(yy-yMin)/(yMax-yMin))*img.getHeight());
		  myBuffer.setColor(new Color(color));
		  myBuffer.drawLine(x, y, xw, y);
		  //if(xw<x){
		//	for(int i = xw; i<=x; i++)
		//		  if(img.getHeight()-y-1<img.getHeight() && img.getHeight()-y-1>=0 && i<img.getWidth() && y>=0)
		//			  img.setRGB(i, img.getHeight()-y-1, color);
		 // }
		  //else{
		//		for(int i = x; i<=xw; i++)
		//			  if(img.getHeight()-y-1<img.getHeight() && img.getHeight()-y-1>=0 && i<img.getWidth() && y>=0)
		//				  img.setRGB(i, img.getHeight()-y-1, color);
		 // }
      }
      public void cob(Function f, double xMin, double xMax, double yMin, double yMax, int color, double startX, double startY, int bounces){
    	  myBuffer.setColor(new Color(color));
    	  for(int i = 0; i<bounces; i++){
    		//  if(startX> xMax || startX<xMin || startY>yMax || startY<yMin)
    		//	  break;
    		  switch(i%2){
    		  case 0:
    			  double temp = f.get(startX);
    				fillVLine(startX, temp, startY, xMin, xMax, yMin, yMax, color);  
    				startY = temp;
    			  break;
    		  case 1:
    				fillHLine(startX, startY, startY, xMin, xMax, yMin, yMax, color);  
    				startX = startY;
    			  break;
    		  }
        	  repaint();
    	  }
      }
      public void axes(double xMin, double xMax, double yMin, double yMax, int color){
    	  myBuffer.setColor(new Color(color));
    	  int yy = (int)((1-(-yMin)/(yMax-yMin))*img.getHeight());
    	  int xx =(int)((-xMin)/(xMax-xMin)*img.getWidth());
    	  myBuffer.drawLine(0, yy,  img.getWidth(), yy);
    	  myBuffer.drawLine( xx, 0, xx, img.getHeight());
      }
      public void cobweb(Function f, double xMin, double xMax, double yMin, double yMax, int pix){
    	  axes(xMin, xMax, yMin, yMax, Color.black.getRGB());
    	  graph(f, xMin, xMax, yMin, yMax, Color.black.getRGB());
    	  graph(Function.YeX, xMin, xMax, yMin, yMax,  Color.green.getRGB());
    	  for(int i = 0; i<img.getWidth(); i+=pix)
    		  cob(f, xMin, xMax, yMin, yMax,  Color.blue.getRGB(), xMin+(xMax-xMin)*i/img.getWidth(), 0, 20);
      }
      public static void main(String[] args){
    	  StatsGraph gr = new StatsGraph();
    	  Function f = new Function(){
    		  public double get(double x){
    			  return 
    					//  1/2.*Math.exp(x)-x*Math.sqrt(x)
    			//(1-x)/(3*x+1)
    			  //x*x-1
    					  -x*x+5
    					  ;
    		  }
    	  };
    	  double xMin = -3.2, xMax = 3, yMin = -5, yMax = 6, x = .3, y = 0;
    	 // double xMin = -3, xMax = 3, yMin = -3, yMax = 3, x = .3, y = 0;

    	 // double xMin = 0, xMax = 1.5, yMin = 0, yMax = 1, x = .3, y = 0;
    	  gr.cobweb(f, xMin, xMax, yMin, yMax, 5);
    	  System.out.println("Done");
      }

   }

