package org.opensky.libadsb;

import java.io.Serializable;

public class estructura implements Serializable{

    public double[][] matriz=new double[3][3];
    public double time=0;
    public double oldtime1=0;
    public double oldtime2=0;
    public double oldtime3=0;
	public int msgscomunes=0;
    public String[] mensajes=new String[4];
	public double threshold=0;


}
