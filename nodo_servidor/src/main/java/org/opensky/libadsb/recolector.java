package org.opensky.libadsb;

import java.io.Serializable;


import java.io.*;
import sun.net.*;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;
import java.util.Date;
import java.lang.Integer;
import java.lang.Process;


import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.opensky.example.Servidor_Def;
import org.opensky.libadsb.HiloDeCliente;
import org.opensky.libadsb.recolector;
import org.opensky.libadsb.estructura;
import org.opensky.libadsb.informacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class recolector implements Runnable{

double actualtime=0;

public Servidor_Def servidor;



   public recolector(Servidor_Def servidor)
   {

        this.servidor=servidor;

   }

   public void run ()
   {
       while(true){
// cleanup decoders every 100.000 messages to avoid excessive memory usage
			// therefore, remove decoders which have not been used for more than one hour.
			List<String> to_remove = new ArrayList<String>();

            actualtime=System.currentTimeMillis();

synchronized(servidor.mapa){

			for (String key : servidor.mapa.keySet())
				if (servidor.mapa.get(key).time<actualtime-900000){ //elimino si no me envian en 15 minutos//
					to_remove.add(key);
					System.out.println("procedo a borrar el avion "  + key);
				}
			for (String key : to_remove)
				servidor.mapa.remove(key);
       }
	}
   }
   
}
