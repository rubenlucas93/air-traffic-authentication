package org.opensky.libadsb;

import java.io.Serializable;

public class informacion implements Serializable{

    public int IDPlaca=0;
    public String IDAvion=new String("default");
    public int nMensaje=0;
    public double TiempoAvion=0;
    public double TiempoRecepcion=0;
    public String raw=new String("default");
	public Double correccion;

}
