package org.opensky.libadsb;

import java.io.Serializable;

public class estructura implements Serializable{

    public double[][] matriz=new double[3][3];
    public double time=0;
    public int contador=0;
    public double oldtime1=0;
    public double oldtime2=0;
    public double oldtime3=0;


}
