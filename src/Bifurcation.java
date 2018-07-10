

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class Bifurcation extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	    static int windowX = screenSize.width;
	    static int windowY = screenSize.height;
	public static void main(String[] args){
		Bifurcation can = new Bifurcation();
        can.plot(new Func1(),1, 0, 4, 1);
        JFrame f = new JFrame("");
		f.setUndecorated(true);
	    f.setSize(windowX, windowY);
	    f.setLocation(0, 0);
		can.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE){
					System.exit(0);
				}
			}
		});
		can.setFocusable(true);
		can.requestFocusInWindow();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(can);
        f.setVisible(true);
	}
  static final int initIter = 20;
  static final int plottedIter = 50;
int height = windowX, width = windowY;
  private BufferedImage source = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
Graphics2D g = (Graphics2D)source.getGraphics();
  /*
  public void plot(Func f, short numIterations, double xstart, double ystart,
          double xend, double yend) {
this.plot(f, xstart, ystart, xend, yend);
}*/
  public void paintComponent(Graphics g) {
g.drawImage(source, 0, 0, getWidth(), getHeight(), null);
  }
  public void plot(Func f, double xstart, double ystart, double xend, double yend) {
    int x, y;
    short iter;
    double a, nextVal;
    double xscale = (double)width / (xend-xstart), yscale = (double)height / (yend-ystart);
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height);
    g.setColor(Color.black);
    g.drawLine(0, 0, 0, height-1);
    g.drawLine(0, height-1, width-1, height-1);
    g.setColor(Color.blue);
    for (x=0; x < width; x++) {
      a = (double)x/xscale + xstart;
      nextVal = f.critPoint();
      for (iter=0; iter < initIter; iter++)
        nextVal = f.compute(a, nextVal);
      for (iter=0; iter < plottedIter; iter++) {
        nextVal = f.compute(a, nextVal);
        y = (height-1) - (int)((nextVal - ystart) * yscale);
        if (y >=0 && y < height)
          g.fillRect(x, y, 1, 1);
      }
    }
    repaint();
  }
}
abstract class Func {
  abstract double compute(double a, double x);
  abstract public double critPoint();
}

class Func1 extends Func {
  public double compute(double a, double x) {
    return a*x*(1-x);
  }

  public double critPoint() {return 0.5;}
}

class Func2 extends Func {
  public double compute(double a, double x) {
    return a*Math.sin(x);
  }

  public double critPoint() {return Math.PI/2;}
}

class Func3 extends Func {
  public double compute(double a, double x) {
    return a*Math.cos(x);
  }
  
  public double critPoint() {return 0;}
}
