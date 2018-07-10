   
   import java.awt.*;
   public class Red
   {
      private static int r = 0;
      private static int g = 0;
      private static int b = 255;
      //private enum side {PBLUEG, ;
      public Red()
      {
      }
      public Red(int re, int gr, int bl)
      {
         r = re;
         b = gr;
         g = bl;
      }
      public Color getColor()
      {
         return new Color(r, b, g);
      }
      public static int getRGB()
      {
         return new Color(r, b, g).getRGB();
      }
      private void consider(int b, int g)
      {
         if(b==255)
         {
            if(g == 255)
               b--;
            else
               g++;
         }
      }
      public void previous(int b, int g)
      {
         if(b==255)
         {
            if(g==0)
            {}}
         
         
      }
      public void next()
      {
         consider(b, g);
         consider(g, r);
         consider(r, g);
      }
      public void back()
      {
         previous(b, g);
         previous(g, r);
         previous(r, g);
      }
      public static int DecToRGB(double d)
      {
		int q = 0;
         double frac = d*(510.0+q)+q;
         return RGB((int)frac);
      }
      public static int DecToRGB(double d, int q)
      {
         double frac = d*(510.0-q)+q;
         return RGB((int)frac);
      }
      public static int RGB(int i)
      {
         setColor(i);
         try{
            return getRGB();
         }
            catch(IllegalArgumentException e){System.out.println(""+r+" "+b+" "+g+" ");}
         return 0;
      }
      public static void setColor(int i)//1530 total 0-1529
      {
         //i=(int)(i/255.0);
         try{
            if(i!=Integer.MAX_VALUE)
            {
               if(i>=0)
                  if(i<=255)
                  {
                     g=0;
                     r=i;
                     b=0;
                  }
						else if(i<=510)
                  {
                     g=i-255;
                     r=255;
                     b=i-255;
                  }
                  else
                  {
                     //setColor(i%255);
							setColor(510);
                  }
               else
               {
                  setColor(510-((-i)%510));
               }
            }
            else{r = 0; g = 0; b = 0;}
         }
            catch(IllegalArgumentException e){System.out.println(""+r+" "+b+" "+g+" "+e);}
      }
      public void setColor(double i)//1530 total 0-1529
      {
         setColor((int)i);
      }
   
   }
   
