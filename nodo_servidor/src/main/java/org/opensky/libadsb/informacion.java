package org.opensky.libadsb;

import java.io.Serializable;

public class informacion implements Serializable{

    public int IDPlaca;
    public String IDAvion;
    public int nMensaje;
    public double TiempoAvion;
    public double TiempoRecepcion;
    public String raw;
	public Double correccion;
}
