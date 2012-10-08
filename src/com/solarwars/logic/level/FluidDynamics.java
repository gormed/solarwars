
package com.solarwars.logic.level;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 *
 * @author Roman
 */
public class FluidDynamics {
    
    //private static final int TEST_SEED=1700;
    
    /* side length of square simulation area */
    public static final int FLUID_RES = 128;
    private static final int FLUID_SIZE = FLUID_RES * FLUID_RES;

    /* number of stars */
    private static final int STAR_COUNT = 1024;
    
    
    /* simulation parameters */
    private static final float DT = 0.01f; /* delta-t */
    private static final float VISC = 0.0001f; /* fluid viscosity */
    private static final float DIFF = 0.0001f; /* diffuse amount */
            
            
    /* current and previous simulation states */
    
    /* flow velocities */
    private float u[],v[],u_prev[],v_prev[];
    
    /* density of blue and red fog */
    private float dens[],dens_prev[];
    private float densb[],densb_prev[];
    
    /* position of stars */
    private float starx[], stary[];
    
    
    /* random number generator */
    
    Random rgen;
    
    /* get simulation array index from x,y coordinates */
    
    private int ix(int x, int y)
    {
        return (x&(FLUID_RES-1))+(y&(FLUID_RES-1))*FLUID_RES;
    }
    
    private void copy(float tgt[],float src[])
    {
        int i;
        
        for (i=0;i<FLUID_SIZE;i++) tgt[i]=src[i];
    }
            
    private void addsource (float tgt[], float src[], float dt)
    {
        int i;
        
        for (i=0;i<FLUID_SIZE;i++) tgt[i]+=dt*src[i];
    }
    
    /* apply a simple blur filter */
    
    private void diffuse(float tgt[], float src[], float diff, float dt)
    {
        int i,j,k;
        
        /* strength of blur dependant on area */
        //float a=dt*diff*FLUID_SIZE;
        
        float a=dt*diff*100;
        
        for (k=0;k<2;k++)
        for (i=0;i<FLUID_RES;i++)
        for (j=0;j<FLUID_RES;j++)
        {
            tgt[ix(i,j)] = src[ix(i,j)] 
                       + a * (
                         tgt[ix(i-1,j)]
                       + tgt[ix(i+1,j)]
                       + tgt[ix(i,j-1)]
                       + tgt[ix(i,j+1)] )
                       / (1+4*a);
        }
    }
    
    /* a reverse propagation step with linear interpolation,
     * global u and v arrays hold velocities used here
     */
    
    private void propagate(float tgt[], float src[], float dt)
    {
        int i,j,i0,j0,i1,j1;
        float x,y,s0,t0,s1,t1,dt0;
        
        dt0 = dt*FLUID_RES;
        
        for(i=0;i<FLUID_RES;i++)
        for(j=0;j<FLUID_RES;j++)
        {
            x = i-dt0*u[ix(i,j)];
            y = j-dt0*v[ix(i,j)];
            
            if (x<0) x--;
            if (y<0) y--;
            
            i0=(int)x; i1=i0+1;
            j0=(int)y; j1=j0+1;
            
            s1 = x-i0;
            s0 = 1-s1;
            t1 = y-j0;
            t0 = 1-t1;
            
            tgt[ix(i,j)] =
                s0*t0*src[ix(i0,j0)]
              + s0*t1*src[ix(i0,j1)]
              + s1*t0*src[ix(i1,j0)]
              + s1*t1*src[ix(i1,j1)];              
        }
    }
    
    /* complete density propagation step */
    
    private void densitystep(float tgt[], float src[], float diff, float dt)
    {   
        // add densities
        addsource(tgt,src,dt);
        
        // temporarily hold result in src array
        diffuse(src,tgt,diff,dt);
        
        // final result is in tgt array
        propagate(tgt,src,dt);
    }
    
    /* project velocities */
    
    private void project(float p[], float div[])
    {
        int i,j,k;
        float h;
        
        h=1.0f/(float)FLUID_RES;
        
        for ( i=0; i<FLUID_RES; i++ )
        for ( j=0; j<FLUID_RES; j++ )
        {
            div[ix(i,j)] = -0.5f*h*(u[ix(i+1,j)]-u[ix(i-1,j)]+
            v[ix(i,j+1)]-v[ix(i,j-1)]);
            p[ix(i,j)] = 0;
        }
        
        for ( k=0; k<20; k++ )
        for ( i=0; i<FLUID_RES; i++ )
        for ( j=0; j<FLUID_RES; j++ )
        {
            p[ix(i,j)] = (div[ix(i,j)]+p[ix(i-1,j)]+p[ix(i+1,j)]+
            p[ix(i,j-1)]+p[ix(i,j+1)])/4;
        }
        
        for ( i=0; i<FLUID_RES; i++ )
        for ( j=0; j<FLUID_RES; j++ ) {
            u[ix(i,j)] -= 0.5*(p[ix(i+1,j)]-p[ix(i-1,j)])/h;
            v[ix(i,j)] -= 0.5*(p[ix(i,j+1)]-p[ix(i,j-1)])/h;
        }
    }
    
    /* complete velocity propagation step */
    
    private void velocitystep(float visc, float dt)
    {
        float temp[];
        
        addsource(u,u_prev,dt);
        addsource(v,v_prev,dt);
        
        copy(u_prev,u);
        copy(v_prev,v);
        
        diffuse(u,u_prev,visc,dt);
        diffuse(v,v_prev,visc,dt);
      
        // reuse u_prev and v_prev arrays for temporay calculations
        project(u_prev,v_prev);
        
        copy(u_prev,u);
        copy(v_prev,v);
        
        propagate(u,u_prev,dt);
        propagate(v,v_prev,dt);
        
        // reuse u_prev and v_prev arrays for temporay calculations
        project(u_prev,v_prev);   
    }
    
    /* crazy math ends here */
    
    /* initialize the simulation arrays */
    
    public void init()
    {
        u = new float[FLUID_SIZE];
        v = new float[FLUID_SIZE];
        u_prev = new float[FLUID_SIZE];
        v_prev = new float[FLUID_SIZE];
        
        dens = new float[FLUID_SIZE];
        densb = new float[FLUID_SIZE];
        dens_prev = new float[FLUID_SIZE];
        densb_prev = new float[FLUID_SIZE];
        
        starx = new float[STAR_COUNT];
        stary = new float[STAR_COUNT];
    }
    
    /* clear arrays for starting a new simulation */
    
    public void start(int seed)
    {
        rgen = new Random(seed);
        
        int i;
        
        for (i=0;i<FLUID_SIZE;i++)
        {
            u_prev[i]=0.0f;
            v_prev[i]=0.0f;
            dens_prev[i]=0.0f;
            densb_prev[i]=0.0f;
            
            u[i]=0.0f;
            v[i]=0.0f;
            dens[i]=0.0f;
            densb[i]=0.0f;
        }
        
        for(i=0;i<STAR_COUNT;i++)
        {
            starx[i]=rgen.nextFloat()*FLUID_RES;
            stary[i]=rgen.nextFloat()*FLUID_RES;
        }
    }

    /* from here, things are optimized to look nice */
    
    /* perform a full simulation step */
    
    public void simulate()
    {
        
        for (int i=0;i<FLUID_SIZE;i++)
        {
            //loose some density for both fog colours
            
            dens[i]*=0.99f;
            dens[i]-=0.01f;
            if(dens[i]<0) dens[i]=0;
            
            densb[i]*=0.99f;
            densb[i]-=0.01f;
            if(densb[i]<0) densb[i]=0;
            
            // the two fog colours cancel out
            // this will create nice fog mountain fronts and twirls
            
            if (densb[i]>dens[i])
            {
                densb[i]-=dens[i];
                dens[i]=0;
            }
            else
            {
                dens[i]-=densb[i];
                densb[i]=0;
            }
            
            // add some randomness to the velocities to shake it up
            u[i]+=rgen.nextFloat()-0.5f;
            v[i]+=rgen.nextFloat()-0.5f;
            
        }
        
        // perform the simulation steps
        
        velocitystep(VISC,DT);
        densitystep(dens,dens_prev,DIFF,DT);
        densitystep(densb,densb_prev,DIFF,DT);
        
        // move the stars along the flow velocities
        
        // NOTE: THIS COULD ALSO BE USED FOR THE GAMES PLANETS!
        
        for (int i=0;i<STAR_COUNT;i++)
        {
            starx[i]+=u[ix((int)starx[i],(int)stary[i])]*DT*FLUID_RES;
            stary[i]+=v[ix((int)starx[i],(int)stary[i])]*DT*FLUID_RES;
            
            if(starx[i]>=FLUID_RES) starx[i]-=FLUID_RES;
            if(stary[i]>=FLUID_RES) stary[i]-=FLUID_RES;
            if(starx[i]<0) starx[i]+=FLUID_RES;
            if(stary[i]<0) stary[i]+=FLUID_RES;
        }
        
    }

    // drop a circular supernova explosion thingy into the simulation

    public void push()
    {
        int x=(rgen.nextInt()&(FLUID_RES-1));
        int y=(rgen.nextInt()&(FLUID_RES-1));
        int s=(rgen.nextInt()&(FLUID_RES*4-1))+64;
        
        float den[]=(rgen.nextInt()&1)==1?dens:densb;
        int ra=(rgen.nextInt()&15)-8;
        int rb=(rgen.nextInt()&15)-8;
        for (int i=0;i<FLUID_RES;i++)
        for (int j=0;j<FLUID_RES;j++)
        {
            int id=i-y;
            int jd=j-x;
            if (id>FLUID_RES/2) id=-FLUID_RES+id;
            if (jd>FLUID_RES/2) jd=-FLUID_RES+jd;
            if (id<-FLUID_RES/2) id=FLUID_RES+id;
            if (jd<-FLUID_RES/2) id=FLUID_RES+jd;
            float d=jd*jd+id*id;
            float d2;
            d/=(float)s;
            d2=d*d;
            if (d2>1.0f) d2=0.0f;

            // radial velocities
            
            u[ix(j,i)]+=((float)jd+ra)*d2*0.5f;
            v[ix(j,i)]+=((float)id+rb)*d2*0.5f;
            
            den [ix(j,i)]+=d2;
            //u[IX(j,i)]-=(float)(j-x)*d3*0.1;
            //v[IX(j,i)]-=(float)(i-y)*d3*0.1;
            //v[IX(j,i)]+=(float)(j-x)*d3*0.01;
            //u[IX(j,i)]-=(float)(i-y)*d3*0.01;
        }
    }

    public ByteBuffer createtexture()
    {
        ByteBuffer data = ByteBuffer.allocateDirect(FLUID_RES*FLUID_RES*3);

        float sd,sd2;

        for (int i=0;i<FLUID_RES;i++)
        {
           sd=0;
            for (int j=0;j<FLUID_RES;j++)
            {
                int d=(int)((dens[ix(j,i)]-dens[ix(j-1,i)]*0.6f)*500);
                if (d>255) d=255;
                if (d<0) d=0;

                int d2=(int)((densb[ix(j,i)]-densb[ix(j-1,i)]*0.6f)*500);
                if (d2>255) d2=255;
                if (d2<0) d2=0;

                sd2=dens[ix(j,i)]+densb[ix(j,i)];
                if (sd2>sd)    // simple shadows
                {
                    sd=sd2;
                }
                else
                {
                    d/=2;
                    d2/=2;
                }
                
                data.put((byte)(d+d2*7/8));
                data.put((byte)(d*7/8+d2*7/8));
                data.put((byte)(d2+d*7/8));
                    
                sd-=0.05f;

            }
        }
        
        return data;
   }
    
    /* get density at coordinate x, y in range [0..1] */
    
    public float densityat(float x,float y)
    {
        int xs = (int)(x*FLUID_RES);
        int ys = (int)(y*FLUID_RES);
        
        float d0 = dens[ix(xs,ys)];
        float d1 = densb[ix(xs,ys)];
        
        return (d0>d1)?d0:d1;
    }
    
    /* get star */
    
    public float[] getStarXArray()
    {
        return starx;
    }
    
    public float[] getStarYArray()
    {
        return stary;
    }
}
