   
   import java.awt.*;
   public class Blue
   {
      public static int DecToRGB(double d)
      {
         return setColor((int)(d*510.0));
      }
      public static int setColor(int i)//1530 total 0-1529
      {
            if(i!=Integer.MAX_VALUE)
            {
               if(i>=0)
                  if(i<=255)
                  {
                  	return i <<8;
//                	  col[0]=0;
//                	  col[2]=i;
//                	  col[1]=0;
                  }
						else if(i<=510)
                  {
			            	return i-255<<16 | 255 <<8 | i-255;
//							col[0]=i-255;
//							col[2]=255;
//							col[1]=i-255;
                  }
                  else
                  {
							return setColor(510);
                  }
               else
               {
                 return setColor(510-((-i)%510));
               }
            }
            else{
return 0;}
      }
      public void setColor(int[] col, double i)//1530 total 0-1529
      {
         setColor(col, (int)i);
      }
   
   }
   
