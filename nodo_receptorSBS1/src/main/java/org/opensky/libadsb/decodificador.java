    package org.opensky.libadsb;

import java.io.Serializable;

import java.io.*;
import org.opensky.example.Decoder_def;

import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

import java. lang.InterruptedException;
import java.net.UnknownHostException;
import java.net.SocketException;
import sun.net.*;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;
import java.util.Date;
import java.lang.Integer;
import java.lang.Process;
import java.util.Iterator;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.opensky.libadsb.informacion;


public class decodificador implements Runnable{



//me declaro las variables que necesitaré para crear mi paquete//





    boolean viable=false;





    //*String[] avion = new String[2];
    double milat= 39.391061; //esta configurado para alcazar...//
    double milon= -3.204555;

    //poner aqui la posicion de las otras placas //
    
    double latPlaca2= 39.391061; //esta configurado para alcazar...//
    double lonPlaca2= -3.204555;

    double latPlaca3= 39.391061; //esta configurado para alcazar...//
    double lonPlaca3= -3.204555;


    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    double lat=0;
    double lon=0;    


    int vpropagacion = 299792458;
    double dlin=0;

	double d=0;
    double d2=0;
    double d3=0;

	double Superficie=0;
	double X= 0;
	double lambda=0;

    double h=0;
    double deu=0;
    double momento=0;

    int numerodeplaca=3; //cada placa tendrá un número de placa. 1, 2 y 3//

    int alcance=20040; //configurado para recibir solo en un radio de 200 m

    double retardo=0;

	double timestamp=0;
 
    double tiempoenvio=0;

    //y ya esta//

     Decoder_def decod;
	 Socket socket;
	 int nmensaje=0;
     int mode=0;

     String raw = null;
     


    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) { 
        //double radioTierra = 3958.75;//en millas 
        double radioTierra = 6371;//en kilometros 
        double dLat = Math.toRadians(lat2 - lat1); 
        double dLng = Math.toRadians(lng2 - lng1); 
        double sindLat = Math.sin(dLat / 2); 
        double sindLng = Math.sin(dLng / 2); 
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) 
* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)); 
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1)); 
        double distancia = radioTierra * va2; 
        distancia=distancia*1000;
        return distancia; 
    }

   public decodificador(String values, Socket socket, Integer nmensaje)
   {


        this.raw=values;
		this.socket=socket;
		this.timestamp=timestamp;
		this.nmensaje=nmensaje;

   }

   public void run ()
   {
	
		 

	try{
		timestamp=Double.parseDouble(raw.split(",")[9]);		
		mode=Integer.parseInt(raw.split(",")[1]);
		

        informacion nodo=new informacion( );        

        nodo.TiempoRecepcion = timestamp;


		String icao24 = raw.split(",")[3];
		

        if (mode==5){

		   
                    
                        //este es el momento para calcular la distancia euclidea y luego el retardo usando mi posición actual (poner las coordenadas de cada placa. Actualmente están puestas en Alcazar de San Juan)//

                            nodo.IDAvion=icao24;                
           
                            System.out.println("RELLENO LOS CAMPOS DEL PAQUETE (OBJETO) QUE VOY A MANDAR CON LOS SIGUIENTES DATOS: ");                        
                            System.out.println("_________________________________________");
                            System.out.println("IDAVION= "+nodo.IDAvion);                
                            System.out.println("timestamp recepcion --> "+timestamp);
                        

                            System.out.println(icao24);
				                              


                            lat=Double.parseDouble(raw.split(",")[14]);
                            lon=Double.parseDouble(raw.split(",")[15]);
                           
                            System.out.println("latitud: "+lat);
                            System.out.println("longitud: "+lon);                    

                            d=distanciaCoord(lat, lon, milat, milon);
                            d2=distanciaCoord(lat, lon, latPlaca2, lonPlaca2);
                            d3=distanciaCoord(lat, lon, latPlaca2, lonPlaca3);

							System.out.println(d + " Vs " + d2 + " Vs " + d3 );

                            if ((int)d<=alcance && (int)d2<=alcance && (int)d3 <= alcance){
                                viable=true;
                            }else{
                                viable=false;
                            }

							Superficie=2*Math.PI*6371000;
							X=Superficie/d;
                            h=Double.parseDouble(raw.split(",")[11])*0.3048000; //para pasar de pies a metros//
							lambda=(2*Math.PI)/X;

							dlin=2*(Math.sin(lambda/2))*6371000;
                            deu=Math.sqrt(Math.pow(h, 2) + Math.pow(dlin, 2) - 2*h*dlin*Math.cos((Math.PI-(Math.PI-lambda/2))));
                            
                            retardo=deu/vpropagacion; //vprop en m/segundo//

                            tiempoenvio=(timestamp)-(retardo); //retardo*1000 porque timestamp esta en milisegundos//                
                            
                            nodo.TiempoAvion= tiempoenvio;
            
                            System.out.println("tiempo envio avion -> "+nodo.TiempoAvion);
                        
					
                           


                            nodo.raw = raw;
                            System.out.println("mensaje en crudo (hexadecimal): " + nodo.raw);
							nodo.nMensaje=nmensaje;
							System.out.println("n de mensaje = " + nodo.nMensaje);
                            nodo.IDPlaca = numerodeplaca;
                            System.out.println("Mi ID de PLACA " + nodo.IDPlaca);
							System.out.println("lo envío en el instante ----------- " + System.nanoTime());
                            System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
                        	
                        //mandar al servidor si ha pasado el sanity check y tengo los datos del objeto//
                            if (viable==true){

                             	
							
							synchronized(socket){						
		
                                outputStream = new ObjectOutputStream(socket.getOutputStream());
         
                                System.out.println("Enviando mensaje del avion " + nodo.IDAvion );
                                outputStream.writeObject(nodo);
							}

                          
                            viable = false;
                            //reinicializo el valor de "viable"//

                                
                            }
                        }

                   

        
				
				else {
					
					System.out.println("no nos interesa este mensaje por no contener posición");
                    System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
                
                    
                    
				}
		
		
        } catch (SocketException se) {
               se.printStackTrace();
               // System.exit(0);

        } catch (IOException e) {
               e.printStackTrace();
        }



   }
   
}



























