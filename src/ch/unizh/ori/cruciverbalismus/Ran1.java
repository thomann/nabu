package ch.unizh.ori.cruciverbalismus;

//cf. Press, Numerical Recipes in C ch. 7.1 p.280


public class Ran1{
	static int IA = 16807;
	static int IM = 2147483647;
	static double AM = 1.0 / IM;
	static int IQ = 127773;
	static int IR = 2836;
	static int NTAB = 32;
	static int NDIV = 1 + (IM-1)/NTAB;
	static double EPS = 1.2e-7;
	static double RNMX = 1.0 - EPS;
	private int iy;
	private	int [] iv ;
	private int idum;
	
	Ran1(){
		idum=1;
		iy=0;
		iv = new int [NTAB];
	};

	public void seed(int s){
		if(s>0)
			idum=-s;
		else
			idum=s;
		iy=0;
		for(int i=0;i<NTAB;i++)
			iv[i]=0;
	};
	
	public double rand(){
		int j;
		int k;
		double temp;
		
		if(idum<=0 || iy == 0){	//Initialize
			if((-idum)<1)
				idum =1;
			else
				idum = -idum;
			for(j=NTAB+7;j>=0;j--){
				k= idum/IQ;
				idum=IA*(idum-k*IQ)-IR*k;
				if(idum<0)
					idum+=IM;
				if(j<NTAB)
					iv[j]=idum;
			};
			iy=iv[0];
		};
		k=idum/IQ;
		idum=IA*(idum-k*IQ)-IR*k;
		if(idum<0)
			idum +=IM;
		//j=(int) Math.round(iy/NDIV-0.5);
		j = iy/NDIV;
		iy=iv[j];
		iv[j] = idum;
		temp = AM*iy;
		if(temp>RNMX)
			return RNMX;
		else
			return temp;
		
	};
};