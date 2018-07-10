   
   public class Hue
   {
      public static final int DecToRGB(double d)
      {
         return setColor((int)(d*1530.0));
      }
      public static final int setColor(int i)//1530 total 0-1529
      {
			if(i>=0)
			{
            if(i<255)
            {
            	return 255<<16 | i <<8;
//               col[0]=255;
//               col[1]=i;
//               col[2]=0;
            }
            else if(i<510)
            {
            	return 510-i<<16 | 255 <<8;
//               col[0]=510-i;
//               col[1]=255;
//               col[2]=0;
            }
            else if(i<765)
            {
            	return 255 <<8 | i-510;
//               col[0]=0;
//               col[1]=255;
//               col[2]=i-510;
            }
            else if(i<1020)
            {
            	return 1020-i <<8 | 255;
//               col[0]=0;
//               col[1]=1020-i;
//               col[2]=255;
            }
            else if(i<1275)
            {
            	return i-1020<<16 | 255;
//               col[0]=i-1020;
//               col[1]=0;
//               col[2]=255;
            }
            else if(i<1530)
            {
            	return 255<<16 | 1530-i;
//               col[0]=255;
//               col[1]=0;
//               col[2]=1530-i;
            }
            else
            {
               return setColor(i%1530);
            }
      }
				else
				{
				return setColor(1530-((-i)%1530));
				}
      }
   }
   
