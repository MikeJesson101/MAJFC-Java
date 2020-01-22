package com.mikejesson.majfc.helpers.wavelets;

// Continuous Morlet Wavelet Transform  
// Copyright (C) 2006-2007 Richard Buessow  
// richard.buessow@tu-berlin.de  
//import java.lang.Math.*;  

import java.io.Serializable;  
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class BussowCWTBigD implements Serializable{  
	private final BigDecimal log2 = new BigDecimal(Math.log(2d));
    private final BigDecimal pi = new BigDecimal(Math.PI), pt5 = new BigDecimal(0.5), two = new BigDecimal(2);
    /**
	 * 
	 */
	private BigDecimal[][] Re;  
    private BigDecimal[][] Im;  
    public BigDecimal[] f;  
    public BigDecimal[] t;  
    public BigDecimal[] deltaStept;  
    public BussowCWTBigD(double[] y){  
           this(y,y.length,(double) y.length/4,100,"log",8,3);  
        }     
    public BussowCWTBigD(double[] y,int fs){  
           this(y,fs,(double) fs/4,100,"log",8,3);  
        }     
    public BussowCWTBigD(double[] y,int fs,double fmax){  
       this(y,fs,fmax,100,"log",16,3);  
    }  
    public BussowCWTBigD(double[] y,int fs,double fmax, int maxNf){  
           this(y,fs,fmax,maxNf,"log",16,3);  
        }  
    public BussowCWTBigD(double[] y,int fs,double fmax, int maxNf, int stepfac){  
           this(y,fs,fmax,maxNf,"log",stepfac,3);  
        }  
    public BussowCWTBigD(double[] y,int fs,double fmax, int maxNf, int stepfac, int df0){  
           this(y,fs,fmax,maxNf,"log",stepfac,df0);  
        }  
    public BussowCWTBigD(double[] y,int fs,double fmax, int maxNf, String linlog, int stepfac, int df0){  
        // Calculation of the transform  
        // used to calc between a fictiv T=1s and the real  
    	BigDecimal ffac = new BigDecimal(fs).divide(new BigDecimal(y.length));   
        // maximum number of dyades: 2^6<n<2^7 -> maxNd = 6  
        int maxNd = (int) (Math.log((double)y.length)/log2.doubleValue());  
        //   
        //System.out.println("maxNd: " + maxNd);  
        BigDecimal fmor, fd=new BigDecimal(0); 
        BigDecimal f0 = new BigDecimal(pow2(df0));
        int Nd,len,startw,starty,maxNb,index=0,Ninner=0,stepLen=1;  
        boolean lin=false,stepch=true;  
        //  int stopw,stopy;  
        // check maximum freq         
        if (fmax>fs/2) fmax=fs/2;  
        //  
        // how many dyadic steps have actually to be performed to reach fmax  
        for (Nd=0;Nd<maxNd;Nd++)  
            if(f0.multiply(ffac).doubleValue()*pow2(Nd)>=fmax) break;   
        //System.out.println("Nd= " +Nd);  
        //  
        // actually only stepfac that are dyadic are used  
        int dstepfac;// = (int) (Math.log((double)stepfac)/Math.log(2.));  
        int MinStepLen;  
        //  
        // no chang of the step size - not recommended  
        if (stepfac==0){  
            stepch = false;  
            dstepfac=0;  
            MinStepLen=1;  
            // System.out.println("Stepsize always:" + stepLen);  
        }  
        else{  
            dstepfac = (int) (Math.log((double)stepfac)/Math.log(2.));  
            MinStepLen = Math.max(1,pow2(maxNd-Nd-df0-dstepfac+1));  
        }  
        // System.out.println("dstepfac: " + dstepfac);  
        stepfac = pow2(dstepfac);  
        // System.out.println("stepfac: " + stepfac);  
        //  
        // smallest step Length   
        stepLen=MinStepLen;  
        // System.out.println("stepLen: " + stepLen);  
        //   
        // linear frequency axis - not recommended  
        if(linlog.equals("lin"))  
            lin = true;       
        //  
        //   
        Re = new BigDecimal[maxNf][];  
        Im = new BigDecimal[maxNf][];  
        f = new BigDecimal[maxNf];  
        deltaStept = new BigDecimal[maxNf];  
        maxNb = y.length/MinStepLen;  
        Ninner = (int) Math.ceil((double) maxNf/Nd);      
        t = new BigDecimal[maxNb];  
        for (int i=0;i<maxNb;i++) t[i]= new BigDecimal(i*MinStepLen/fs);  
        // calc Transform  
        // outer dyadic loop  
        outer:  
        for (int dy=0;dy<Nd;dy++){ //dy=Nd-1, the inner loop calcs until fmax  
            //System.out.println("dy: " + dy);  
            len= pow2(maxNd - dy);  
            //System.out.println("len: " + len);  
            // calculate actual step length   
            if(stepch)  
                stepLen = Math.max(MinStepLen,len/f0.intValue()/stepfac);  
            // System.out.println("stepLen: " + stepLen);             
            // maximal Number translation parameters b  
            maxNb = y.length/stepLen;  
            //System.out.println("stepLen: " + stepLen + " len: " + len + " maxNb " + maxNb + " stepfac " + stepfac);  
            // calc the wavelet  
            Morlet morlet = new Morlet(len,f0.doubleValue());   
            // for linear freq axis  
            if(lin) Ninner = (int) Math.pow(2,dy+fd.doubleValue());  
            // inner frequency loop  
            for (int inner=0;inner< Ninner;inner++){  
                // frequency in the fictivous 1s window  
                if(lin)  
                    fmor = f0.add(f0.multiply(new BigDecimal(inner/Ninner)));  
                else  
                    fmor = f0.multiply(new BigDecimal(Math.pow(2,( double)inner/Ninner)));  
                // real frequency  
                f[index]=fmor.multiply(new BigDecimal(pow2(dy))).multiply(ffac);  
                deltaStept[index] = new BigDecimal((double) stepLen/fs);  
                //  System.out.println("deltaStept["+index+"]: " + deltaStept[index]);  
                // change freq of morlet  
                morlet.setF0(fmor.doubleValue());  
                Re[index] = new BigDecimal[maxNb];  
                Im[index] = new BigDecimal[maxNb];  
                // translation loop  
                for (int b=0;b<maxNb;b++){  
                    startw=Math.max(0,len/2-b*stepLen);  
                    starty=Math.max(0,b*stepLen-len/2);;  
                    // do not calc at the boarder  
                   // if(b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner))
                    double lenOverF0 = new BigDecimal(len).divide(f0).doubleValue();
                    if(b*stepLen>lenOverF0/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>lenOverF0/Math.pow(2.,(double)inner/Ninner))                     
                    for (int i=0;i<Math.min(len,y.length+len/2-b*stepLen)-startw;i++){  
//                          System.out.println("Re[index][b]: " +Re[index][b]);  
//                          System.out.println("y[starty+i]: " +y[starty+i]);  
//                          System.out.println("morlet.getRe(startw+i): " + morlet.getRe(startw+i));
                    		BigDecimal yBD = new BigDecimal(y[starty+i]); 
                    		Re[index][b] = Re[index][b] == null ? yBD.multiply(morlet.getRe(startw+i)) : Re[index][b].add(yBD.multiply(morlet.getRe(startw+i)));  
                    		Im[index][b] = Im[index][b] == null ? yBD.multiply(morlet.getRe(startw+i)) : Im[index][b].add(yBD.multiply(morlet.getIm(startw+i)));  
                        }  
                    // multiply with 1/sqrt(a) and dt = 1/fs  
                    BigDecimal sqrtFOverFs = new BigDecimal(Math.sqrt(f[index].doubleValue())/fs);
                    Re[index][b] = Re[index][b] == null ? new BigDecimal(0) : Re[index][b].multiply(sqrtFOverFs);  
                    Im[index][b] = Im[index][b] == null ? new BigDecimal(0) : Im[index][b].multiply(sqrtFOverFs);   
                }  
                // :end translation loop  
                index++;  
                //System.out.println(index);  
                if(index==maxNf)  
                    break outer; // breack if number of frequency points is reached  
            } // :end inner frequency loop  
        } // :end outer frequency loop  
        //System.out.println("index= " +index);  
    }  
    // help methods   
    // returning the absolute value  
    public double[][] getAbs(){  
        double out[][] = new double[Re.length][];  
  
        for (int i=0;i<out.length;i++){  
            out[i] = new double[Re[i].length];  
            for (int j=0;j<out[i].length;j++)  
                out[i][j] = Math.pow(Re[i][j].doubleValue(),2.) + Math.pow(Im[i][j].doubleValue(),2.);  
            }  
        return out;  
    }  
    // by cols  
    public double[] getAbs(int i){  
        double out[] = new double[Re[i].length];  
            for (int j=0;j<out.length;j++)  
                out[j] = Math.pow(Re[i][j].doubleValue(),2.) + Math.pow(Im[i][j].doubleValue(),2.);  
        return out;  
    }  
    // :end returning the absolute value  
    // returning delta f for interp   
    public double df(int i){  
        double out;  
        if (i==0)  
            out = pt5.multiply(f[1].subtract(f[0])).doubleValue();  
        else if (i==f.length-1)  
            out = pt5.multiply(f[f.length-1].subtract(f[f.length-2])).doubleValue();  
        else  
            out = pt5.multiply(f[i+1].subtract(f[i-1])).doubleValue();  
        return out;  
    }  
    // : help methods  
    //  
    // output methods  
    //   
    public double[][] pd(){  
        // power density  
        // all values  
        double out[][] = new double[f.length][t.length];  
  
        for (int i=0;i<f.length;i++)  
            out[i] = Numerics.interp(getAbs(i),df(i)/t.length,t.length);  
        return out;  
    }  
    public double[][] pd(int [] cols){  
        // power denity  
        // only cols values    
        double out[][] = new double[f.length][cols.length];  
        for (int i=0;i<f.length;i++)  
            out[i] = Numerics.interp(getAbs(i),df(i)/t.length,t.length,cols);  
        return out;  
    }  
      
    public double[][] ed(){  
        // energy density  
        // all values  
        double out[][] = new double[f.length][t.length];  
        for (int i=0;i<f.length;i++)  
            out[i] = Numerics.interp(getAbs(i),t.length);  
        return out;  
    }  
    public double[][] ed(int [] cols){  
        // energy density  
        // only cols value  
        double out[][] = new double[f.length][cols.length];  
        for (int i=0;i<f.length;i++)  
            out[i] = Numerics.interp(getAbs(i),1,t.length,cols);      
        return out;  
    }  
    public double[] et(){  
        // energy over time  
        double out[] = new double[t.length];  
        double temp[][] = new double[f.length][t.length];  
        double tempv[] = new double[f.length];  
        double tempf[] = new double[f.length];
        for (int i=0;i<f.length-1;i++) {
            temp[i] = Numerics.interp(getAbs(i),t.length);
            tempf[i] = f[i].doubleValue();
        }
        
        tempf[f.length - 1] = f[f.length - 1].doubleValue();
        for (int j=0; j<t.length; j ++){  
            for (int i=0;i<f.length-1;i++)  
                tempv[i] = temp[i][j];  
            out[j] = Numerics.trapez(tempv,tempf);  
        }         
        return out;  
    }  
  
    public double[] ef(){  
        // energy over frequency  
        double out[] = new double[f.length];  
        for (int i=1;i<f.length;i++)  
            out[i] = Numerics.simpson(getAbs(i),deltaStept[i].doubleValue());  
        return out;  
    }  
    public double e(){  
        // total energy  
        double out;  
        double temp[] = new double[f.length];  
        double tempf[] = new double[f.length];
        
        for (int i=1;i<f.length;i++)  {
            temp[i] = Numerics.simpson(getAbs(i),deltaStept[i].doubleValue());
            tempf[i] = f[i].doubleValue();
        }

        out = Numerics.trapez(temp, tempf);  
        return 2*out; // 2*: negative part of the f-axis  
    }     
    public double[] pf(){  
        // energy over frequency  
        double out[] = new double[f.length];  
        double T=t[t.length-1].doubleValue();  
    //  System.out.println("T: " + T);  
        for (int i=1;i<f.length;i++)  
            out[i] = Numerics.simpson(getAbs(i),df(i)/T*deltaStept[i].doubleValue());  
        return out;  
    }     
    public static int pow2(int n){  
        int out=1;  
        for(int i=0;i<n;i++)  
            out *=2;  
        return out;  
    }  
      
    public static class Numerics{  
          
    public static double simpson(double[] y){  
        return simpson(y,1);  
    }  
    public static double simpson(double[] y , double dx){  
        // Simpson's Rule: step size equal   
        double out=0;  
            for (int i=2;i<y.length; i+=2)  
                out += y[i-2]/6. + y[i]/6. + 2./3.*y[i-1];  
        return out*2*dx;  
    }     
    public static double trapez(double [] y, double[] x){  
        // Trapez step size changes  
        double out=0;  
        for (int i=1;i<y.length; i++)  
            out += (y[i-1] + y[i])*(x[i] - x[i-1])/2.;  
    return out;       
    }  
    public static double[] interp(double[] y, double a, int len){  
        // interpol y on len  
        double[] out = new double[len];  
        //int step = len/y.length;  
        if(y.length<len){  
            int step = pow2((int) (Math.log((double)len/y.length)/Math.log(2.)));  
//      System.out.println("step= "+step);  
            for (int j=0;j<y.length-1;j++)  
                for (int i=0;i<step;i++)  
                    out[j*step+i] = a*(y[j] +(double) i/step* (y[j+1] -y[j]));  
            for (int i=0;i<(len-(y.length-1)*step);i++)  
                out[(y.length-1)*step+i] = a*y[y.length-1];       
            return out;  
        }  
        else{  
            for (int i=0;i<y.length;i++)  
                y[i] *= a;  
            return y;  
        }  
    }  
    public static double[] interp(double[] y, int len){  
        return interp(y,1,len);  
    }  
    public static double[] interp(double[] y, double a, int len, int[] index){  
        double[] out = new double[index.length];  
        double[] temp = new double[len];  
        if(y.length<len){  
            int step = pow2((int) (Math.log((double)len/y.length)/Math.log(2.)));         
            for (int j=0;j<y.length-1;j++)  
                for (int i=0;i<step;i++)  
                    temp[j*step+i] = a*(y[j] +(double) i/step* (y[j+1] -y[j]));  
        // alles nach dem letzten wird der letzte Wert zugewiesen  
        for (int i=0;i<step;i++)  
            temp[(y.length-1)*step+i] = a*y[y.length-1];  
        for (int i=0;i<index.length;i++)  
            out[i] = temp[index[i]+1];  
        }  
        else{  
            for (int i=0;i<index.length;i++)  
                out[i] = a*y[index[i]+1];  
        }  
        return out;  
    } //:end method interp  
      
    } //:end Class Numerics  
  
    public class Morlet{  
        private int np;  
        private BigDecimal f0,dt;  
        private double beta=2;
        private BigDecimal[][] vals;  
        // Constructor  
        public Morlet(int np,double f0){  
            this.dt = new BigDecimal(1).divide(new BigDecimal(np));  
            this.np=np;  
            this.f0= new BigDecimal(f0);  
            vals = new BigDecimal[np][2];  
            calcMorlet();  
        }  
        // private Functions  
        private void calcMorlet(){  
        	BigDecimal t;  
                       
            for (int it=0;it<np;it++){  
                t = new BigDecimal(it).multiply(dt).subtract(pt5).multiply(f0);
                BigDecimal tSq = t.multiply(t);
                BigDecimal twoPiT = two.multiply(pi).multiply(t);
                vals[it][0] = new BigDecimal(Math.sqrt(Math.sqrt(beta/Math.PI))*Math.exp(-beta/2*tSq.doubleValue())*Math.cos(2*Math.PI*t.doubleValue()));  
                vals[it][1] = new BigDecimal(Math.sqrt(Math.sqrt(beta/Math.PI))*Math.exp(-beta/2*tSq.doubleValue())*Math.sin(2*Math.PI*t.doubleValue()));  
            //  vals[it][0] = Math.sqrt(beta/Math.PI)*Math.exp(-beta/2*t*t)*Math.cos(2*Math.PI*t);  
            //  vals[it][1] = Math.sqrt(beta/Math.PI)*Math.exp(-beta/2*t*t)*Math.sin(2*Math.PI*t);  
                      
            }         
        }  
        // public Functions  
        public void setBeta(double beta){  
            if (beta!=this.beta){  
                this.beta=beta;  
                calcMorlet();  
            }  
        }  
        public void setF0(double f0){  
            if (f0!=this.f0.doubleValue()){  
                this.f0=new BigDecimal(f0);  
                calcMorlet();  
            }  
        }  
        public BigDecimal getRe(int index){  
            return vals[index][0];  
        }  
        public BigDecimal getIm(int index){  
            return vals[index][1];  
        }  
              
    }  
  
} // :end CWT  

