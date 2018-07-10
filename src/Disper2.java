import java.awt.*;
import java.applet.*;
import java.awt.image.*;
//copyright, 1999, M Paez, U of Antioquia

public class Disper2 extends Applet implements Runnable {
  Thread runstring;
  private Image offScreenImage;
  private DispCanvas canv;
  BorderLayout MyLay;
  TextField mass,vel,vely,tbi,tbf,td;//tbi initial impact parameter.
  Label lm,lvx,lvy,lbi,lbf,ld;       //tbf final impact parameter
  int Flag;
  Panel pan,pan1,pan2;
  Button stp,rest;
  double data[]=new double[7];   //array with mass, vel x , vel y ,impact param
  double delta=0.005; //time increment
  private CheckboxGroup poten;
  private Checkbox atrac,repul;
  
  			 
  

   public Disper2() //to initialize Flag
  {
     setFlag(0);
  }

  public void setFlag(int fl)
  {
     Flag=fl;    //Flag 0 do not begin string motion, 1 yes
   }

  public void start()
  {

    if(runstring==null);
    {
      if(Flag==1){ //start string motion
		canv.setgraf(0);
       runstring=new Thread(this);
       runstring.start();
     }
    }
  }

  public void stop()
  {
     if(runstring != null){
       runstring.stop();
	   canv.setgraf(1);
       System.gc();
       runstring=null;
     }
  }

  public void run()
  {	
	double bact;
        bact=data[5]; //actual impact parameter
	canv.nset(0,delta);
	canv.bibf(data[5],data[6]);
    while(bact<=data[6])
	{
		canv.bset(bact);
		canv.nset(1,delta);
		canv.repaint();
		bact+=delta;
      try
	  { 
		  Thread.sleep(200);
      }
      catch(InterruptedException e){ }
    }
	canv.setgraf(1);
  }

    public void update(Graphics g)
  { //To avoid flicker
    Graphics offScreenGraphics=offScreenImage.getGraphics();
    offScreenGraphics.setColor(getBackground());
    offScreenGraphics.fillRect(0,0,size().width,size().height);
    offScreenGraphics.setColor(g.getColor());
    paint(offScreenGraphics);
    g.drawImage(offScreenImage,0,0,this);
  }

    public void init()
     {	int width, hei;

          setLayout(MyLay=new BorderLayout());
          canv=new DispCanvas();
          canv.init();
          canv.yset(-5,-0.26,.1,0,.4,-0.5,0.5);//initial textfield values
          data[5]=-0.5;
          data[6]=0.5;
          canv.resize(600,300); //plot scrollbars panel and canvas
          canv.setBackground(Color.white);
          width=100;
          hei=160;
          pan=new Panel();
          pan1=new Panel();
          pan2=new Panel();
          pan.add(lm=new Label("Masa"));
          pan.add(mass=new TextField("0.2"));
          pan.add(lvx=new Label("Vel. en x"));
          pan.add(vel=new TextField("0.2"));
          pan.add(lvy=new Label("Vel en y"));
          pan.add(vely=new TextField("0.0"));
          pan.add(lbi=new Label("b inicial"));
          pan.add(tbi=new TextField("-0.5"));
          pan.add(lbf=new Label("b final"));
          pan.add(tbf=new TextField("0.5"));
          canv.bibf(-0.5,0.5);  //initial and final impact parameter

          pan2.add(ld=new Label("delta"));
          pan2.add(td=new TextField("0.005"));
          pan1.add(stp=new Button("Stop"));
          pan1.add(rest=new Button("Start"));
          poten=new CheckboxGroup();
          pan2.add(repul=new Checkbox("Repulsivo",poten,true));
          pan2.add(atrac=new Checkbox("Atractivo",poten,false));
          canv.setcon(-1);
          add("West",pan1);
          add("North", pan);
          add("South",pan2);
          add("Center",canv);
          setBackground(Color.black);
          offScreenImage=createImage(size().width,size().height);

    }
    
	
	public boolean action(Event ev, Object arg)
	{	

		if (ev.target  instanceof Button)
		{
			String label=(String)ev.arg;
			if (label.equals("Stop"))
			{
				setFlag(0);
				stop();
			}
   
			if(label.equals("Start"))
                        { //takes the textfield values and write them in
                          //array
                          setFlag(1);
                          data[0]=-5;

                          data[2]=Double.valueOf(vel.getText()).doubleValue();
                          data[3]=Double.valueOf(vely.getText()).doubleValue();
                          data[4]=Double.valueOf(mass.getText()).doubleValue();
                          data[5]=Double.valueOf(tbi.getText()).doubleValue();
                          data[6]=Double.valueOf(tbf.getText()).doubleValue();
                          canv.yset(data[0],data[1],data[2],data[3],data[4],data[5],data[6]);
                          delta=Double.valueOf(td.getText()).doubleValue();
                          start();
			}
			return true;
		}
		else if(ev.target instanceof Checkbox)
		{
			if(atrac.getState()==true)
                        {       canv.setcon(1);//case of attraction
				return true;
			}
			else
			{
                                canv.setcon(-1); //case of repulsion
				return true;
			}
		}
		else
		return false;
	}



   public void paint(Graphics g){
     canv.paint(g);    //called many times
   }


}//class							

//---------------------------------------------------------

class DispCanvas extends Canvas implements ImageObserver{

	int graf,first;
	double mxx,bxx,myy,byy;


  final double MAXSTP =10000;      //from Numerical Recipes
  final double TINY =1.0e-30;      //in C , by Vetterling et al
  int kmax=0,kount=0;              //variables needed
  double dxsav=0,x,hnext,hdid;     //routines modified to eliminate
  double xp[],yp[][];              //pointers
  double ystart[]=new double[5];
  int nok,nbad;
  final double PGROW =-0.20;
  final double PSHRNK =-0.25;
  final double FCOR =0.06666666;
  final double SAFETY =0.9;
  final double ERRCON =6.0e-4;

  double m;   // mass particle
  public double posi[][] = new double[2][610];
  int pas,n=0;
  public double ydatos[]=new double[5];
  public double b1,b2;
  int cons;
  double bc;
  double bf,delt;
  

//  private Image potencial;
									
												  
  public void nrerror(String error_text)
	{
//		showStatus("error de ejecucion : " + error_text);
//		exit(1);		   <- añadir una terminacion
	}


  public void bibf(double bc1,double bf1)
  {   //initial and final impact parameters
	  bc=bc1;
	  bf=bf1;
  }

  public void setcon(int con)
  { //con=1 for attraction , -1 repulsion
	  cons=con;
  }

  public void derivs(double xi,double yi[],double dydx[])
  {  // called by rk4 , derivatives of potential
	double p,q;

	dydx[1]=yi[3];
	dydx[2]=yi[4];
	p=yi[1];
	q=yi[2];
	dydx[3]=(cons/m)*(2*p*Math.pow(q,2)*Math.exp(-(Math.pow(p,2)+Math.pow(q,2)))*(1-Math.pow(p,2)));
	dydx[4]=(cons/m)*(2*q*Math.pow(p,2)*Math.exp(-(Math.pow(p,2)+Math.pow(q,2)))*(1-Math.pow(q,2)));
  }


  void rk4(double y[],double dydx[],int n,double x1,double h,double yout[])
{//from Vetterling et al . Numerical recipes in C
//modified to eliminate pointers
	int i;
	double xh,hh,h6;
	double dym[]=new double[5];
	double dyt[]=new double[5];
	double yt[]=new double[5];
	
	
	hh=h*0.5;
	h6=h/6.0;
	xh=x1+hh;
	for(i=1;i<=n;i++) yt[i]=y[i]+hh*dydx[i];
	derivs(xh,yt,dyt);
	for(i=1;i<=n;i++) yt[i]=y[i]+hh*dyt[i];
	derivs(xh,yt,dym);
	for(i=1;i<=n;i++)
	{
		yt[i]=y[i]+h*dym[i];
		dym[i]+=dyt[i];
	}
	derivs(x1+h,yt,dyt);
	for(i=1;i<=n;i++)
		yout[i]=y[i]+h6*(dydx[i]+dyt[i]+2.0*dym[i]);
	
}


void rkqc(double y[],double dydx[],int n,double htry,double eps,double yscal[])
{//Fron Numerical Recipes in C, Vetterling et all. No pointers
	int i;
	double xsav,hh,h,temp,errmax;
	double dysav[]=new double[5];
	double ysav[]=new double[5];
	double ytemp[]=new double[5];
	
	

	xsav=(x);
	for(i=1;i<=n;i++)
	{
		ysav[i]=y[i];
		dysav[i]=dydx[i];
	}
	h=htry;
	for(;;)
	{
		hh=0.5*h;
		rk4(ysav,dysav,n,xsav,hh,ytemp);	
		x=xsav+hh;
		derivs(x,ytemp,dydx);
		rk4(ytemp,dydx,n,x,hh,y);		 
		x=xsav+h;
		if(x==xsav) nrerror("salto muy pequeno");
		rk4(ysav,dysav,n,xsav,h,ytemp);	
		errmax=0.0;
		for(i=1;i<=n;i++)
		{
			ytemp[i]=y[i]-ytemp[i];
			temp=Math.abs(ytemp[i]/yscal[i]);
			if (errmax<temp) errmax=temp;
		}
		errmax /=eps;
		if(errmax<=1.0)
		{
			hdid=h;
			hnext=(errmax > ERRCON ? SAFETY*h*Math.exp(PGROW*Math.log(errmax)) : 4.0*h);
			break;
		}
		h=SAFETY*h*Math.exp(PSHRNK*Math.log(errmax));
	}
	for(i=1;i<=n;i++) y[i]+=ytemp[i]*FCOR;
	
}


	public void setgraf(int a)
	{
		graf=a;
	}


	public double ang(double cx,double cy)
  {  //scattering angle after scattering
	  if((Math.abs(cx)==0)&&(cy>0)) 
		  return (Math.PI/2);
	  else if((Math.abs(cx)==0)&&(cy<0)) 
		  return (3*Math.PI/2);

	  else if((Math.abs(cx)==cx)&&(Math.abs(cy)==cy)) 	//primero
		  return (Math.PI/2)-Math.atan2(cx,cy);

	  else if((Math.abs(cx)==-cx)&&(Math.abs(cy)==cy)) 	//segundo
		  return (Math.PI/2)-Math.atan2(cx,cy);

	  else if((Math.abs(cx)==-cx)&&(Math.abs(cy)==-cy)) //tercero
		  return (-Math.PI/2)-(Math.PI+Math.atan2(cx,cy));

	  else if((Math.abs(cx)==cx)&&(Math.abs(cy)==-cy)) 	 //cuarto
		  return -Math.atan2(cx,cy)+Math.PI/2;

	  else return 0;

  }



	public void bset(double b)
        {  //select initial y impact parameter
		ydatos[2]=b;
	}

	public void nset(int b,double del)
        {  //to change delta t (time)
		delt=del;
		if(b==0) n=0;
		else n++;
	}



  public void yset(double q1,double q2,double q3,double q4,double mass,double bi,double bf)
  {//assign values to arrays
        ydatos[1]=q1;  //x particle position
        ydatos[2]=q2;  //y particle position
        ydatos[3]=q3;  //x velocity
        ydatos[4]=q4;  //y velocity
	m=mass;
        b1=bi;         //initial impact parameter y
        b2=bf;         //final impact parameter in y
  }



public void odeint(int nvar,double x1,double x2,double eps,double h1,double hmin)
{//numerical recipes in C Vetterling et al. No pointers
	int nstp,i;
	double xsav=0,h;
	double yscal[]=new double[5];
	double dydx[]=new double[5];
	double y[]=new double[5];

	
	x=x1;
	h=(x2>x1) ? Math.abs(h1) : -Math.abs(h1);
        nok=0;
        nbad=0;
        kount=0;
	for(i=1;i<=nvar;i++) y[i]=ystart[i];
	if(kmax>0) xsav=x-dxsav*2.0;
	for(nstp=1;nstp<=MAXSTP;nstp++)
	{
		derivs(x,y,dydx);
		for(i=1;i<=nvar;i++)
			yscal[i]=Math.abs(y[i])+Math.abs(dydx[i]*h)+TINY;
		if(kmax>0)
		{
			if(Math.abs(x-xsav)>Math.abs(dxsav))
			{
				if(kount<kmax-1)
				{
					xp[++kount]=x;
					for(i=1;i<=nvar;i++) yp[i][kount]=y[i];
					xsav=x;
				}
			}
		}
		if((x+h-x2)*(x+h-x1)>0.0) h=x2-x;
		rkqc(y,dydx,nvar,h,eps,yscal);	   
		if(hdid==h) ++(nok); else ++(nbad);
		if((x-x2)*(x2-x1)>=0.0)
		{
			for(i=1;i<=nvar;i++) ystart[i]=y[i];
			if(kmax==1)		   // afirmativo es 0 o 1 if(kmax)
			{
				xp[++kount]=x;
				for(i=1;i<=nvar;i++) yp[i][kount]=y[i];
			}
			return;
		}
		if(Math.abs(hnext)<=hmin) nrerror("paso muy pequeno");
		h=hnext;
	}
	nrerror("demasiados pasos");
}
								

   public void init()
   {
    first=1;// plot  with scroll only once
    int flag=0; //do not draw string
   
   }
   public DispCanvas() //to initialize class and variables
   {
                coormundo(-5,5,5,-5); //initial world coordinates
   }
  

   void coormundo(double xiz,double ysu,double xde,double yinf)
  {//to transform world coordinates to screen coordinates
    double maxx,maxy,xxfin,xxcom,yyin,yysu;
    maxx=320;
    maxy=300;
    xxcom=0.1*maxx;
    xxfin=0.9*maxx;
    yyin=0.8*maxy;
    yysu=0.1*maxy;
    mxx=(xxfin-xxcom)/(xde-xiz);
    bxx=0.5*(xxcom+xxfin-mxx*(xiz+xde));
    myy=(yyin-yysu)/(yinf-ysu);
    byy=0.5*(yysu+yyin-myy*(yinf+ysu));
  }

  

  public void paint(Graphics g)
  {
	double b,i;
	double r=0.7,mygraf,mxgraf,Em,E;
	int ad,yn,xn;


		//ydatos[2]=b;
		
		mygraf=(double)-150/(2*Math.PI);
		ystart[1]=ydatos[1];
		ystart[2]=ydatos[2];
		ystart[3]=ydatos[3];
		ystart[4]=ydatos[4];
		i=0;
		pas=0;
		Em=1/Math.exp(2);
		E=0.5*m*(Math.sqrt(ystart[3]*ystart[3]+ ydatos[4]*ydatos[4]));
		g.setColor(new Color(0,0,0));
		g.drawOval((int)((-1-r)*mxx+bxx),(int)((-1+r)*myy+byy),30,30);
		g.drawOval((int)((1-r)*mxx+bxx),(int)((-1+r)*myy+byy),30,30);
		g.drawOval((int)((-1-r)*mxx+bxx),(int)((1+r)*myy+byy),30,30);
		g.drawOval((int)((1-r)*mxx+bxx),(int)((1+r)*myy+byy),30,30);
                //potential circles
		g.drawLine(300,200,500,200);
		g.drawLine(300,200,300,50);
		g.drawString("b",400,230);
		g.drawString("Angulo",250,30);
                 //axes
		g.drawString(" 180",260,50);
		g.drawString(" 90",260,87);
		g.drawString(" 0 ",260,125);
		g.drawString("-90",260,162);
		g.drawString("-180 ",260,200);
                g.drawString(String.valueOf(bc),290,215);//for axes
                //initial impact parameter
                g.drawString(String.valueOf((bf+bc)/2),390,215);//axis
                //middle value impact parameter
                g.drawString(String.valueOf(bf),490,215);//final x axis
		g.drawString("E/Em ="+String.valueOf(E/Em),420,15);


		g.setColor(new Color(0,0,(int)(ystart[2]*100+125)));

		while((Math.abs(ystart[2])<=5) &&(Math.abs(ystart[1])<=5) && (pas<15000))
                { //outside this region it leaves scatterred

			pas++;
			odeint(4,i,i+0.05,0.1,0.1,0.0);
			g.drawLine((int)(ystart[1]*mxx+bxx),(int)(ystart[2]*myy+byy),(int)(ystart[1]*mxx+bxx),(int)(ystart[2]*myy+byy));
			i+=.1;
				
		}				   
		posi[0][n]=ystart[3];					   
		posi[1][n]=ystart[4];				
                if(true)  //could be also bf        (graf<2)
		{
			g.setColor(new Color(0,0,0));
			mxgraf=200/((bf-bc)/delt);
			
			for(ad=0;ad<n;ad++)
			{
				xn=(int)(300+ad*mxgraf);
				yn=(int)(125+ang(posi[0][ad],posi[1][ad])*mygraf);
				g.drawLine(xn,yn,xn,yn);
			}
			setgraf(0);
		}
	}

 }//canvas class