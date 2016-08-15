package org.opensky.example;
/**
 *  This file is part of org.opensky.libadsb.
 *
 *  org.opensky.libadsb is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  org.opensky.libadsb is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with org.opensky.libadsb.  If not, see <http://www.gnu.org/licenses/>.
 */

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

import org.opensky.libadsb.HiloDeCliente;
import org.opensky.libadsb.recolector;
import org.opensky.libadsb.estructura;
import org.opensky.libadsb.informacion;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.exceptions.UnspecifiedFormatError;

public class Servidor_Def {

public HashMap<String, estructura> mapa;
public HashMap<String, informacion> msgsplaca1 =  new HashMap<String, informacion>();;
public HashMap<String, informacion> msgsplaca2 =  new HashMap<String, informacion>();;
public HashMap<String, informacion> msgsplaca3 =  new HashMap<String, informacion>();;



//Y ahora nuestras variables//
int numerodeplaca=0;




//para evitar que nos pisemos los contadores, creamos uno para cada casuistica//
int count=0;




double lapso=0;

double tiempoavion=0;


double verification=0;





double errorrecepcion= 500*Math.pow(10, -9) ; //500 nanosegundos en segundos//
double errorGPS = 1000*Math.pow(10, -9); //1 microsegundo en segundos//

//el error de drift es innecesario añadirlo (creo)
double threshold= errorrecepcion + errorGPS;
boolean esverdad=false;








public void escribirAvionVerificado(String documento, String avion){

    FileWriter fichero=null;
    PrintWriter pw = null;

    try{

        fichero=new FileWriter(documento);
        pw=new PrintWriter(fichero);

        pw.println(avion);

    }catch (Exception e){

        e.printStackTrace();

    }finally{
        try{        

            if (null!=fichero){

                fichero.close();
            }
        }catch(Exception e2){
            e2.printStackTrace();
        }
    }
}



public void borrarMatriz(String avion){
    synchronized(mapa){

		System.out.println("BORRANDO MATRIZ");
        mapa.remove(avion);
    }
}

public void calcularVerification(String avion, estructura estmat){
        
        
            double[][] matriz= new double[3][3];
            matriz=estmat.matriz;
            
            count=0;
            //algoritmo de comparacion//

            while(count <=2){
                
                verification=verification + ((Math.abs(matriz[0][count]-matriz[1][count])+Math.abs(matriz[0][count]-matriz[2][count])+Math.abs(matriz[1][count]-matriz[2][count]))/3); 

                count=count+1;
            }
                verification=verification/3;
                System.out.println("verification= " + "____"+verification+"____");

                System.out.println("VERIFICATION="+verification+" | THRESHOLD=" +threshold);
                if(verification<=threshold){

                    esverdad=true;
                

	            System.out.println("\033[37m________________________________________________");
                    System.out.println("\033[32mel avion " + avion + " PARECE VALIDO");
		    System.out.println("\033[37m________________________________________________");
                    //en caso de necesitarlos, tenemos los mensajes del avion almacenados en el array estmat.mensajes, pudiendo utilizarlos a nuestro antojo. Ya sea para enviarlos a opensky o para decodificarlos con dump1090 o con la librería utilizada en el decodificador//

                }else{

                    esverdad=false;
		     System.out.println("\033[37m________________________________________________");
                    System.out.println("\033[31mOJO! el avion " + avion + " PARECE FALSO");
		    System.out.println("\033[37m________________________________________________");
                    //puedo apuntarmelo o avisar que se ha producido un presunto intento de engaño//

                }


            //borro los elementos de ese avion de la matriz1 (o al menos el ultimo) y cambio el valor de avionx//
            
            //  borrarMatriz(avion);


            
}





public void insertarLapso(informacion nodo){

      if (mapa.containsKey(nodo.IDAvion)) {
            
			estructura estmat = mapa.get(nodo.IDAvion);
            

            // inserto el dato en el mapa que corresponda //

            System.out.println("mensaje del avion " + nodo.IDAvion + " decodificado!!" + " placa: " + nodo.IDPlaca);
				 if(nodo.IDPlaca==1){
			  	   	synchronized(msgsplaca1){
						msgsplaca1.put(nodo.raw, nodo);
					}
				  }else if(nodo.IDPlaca==2){
					synchronized(msgsplaca2){
						msgsplaca2.put(nodo.raw, nodo);
					}
				  }else if(nodo.IDPlaca==3){
					synchronized(msgsplaca3){
						msgsplaca3.put(nodo.raw, nodo);
					}
				  }
			System.out.println(nodo.raw);

            double timestamp=System.currentTimeMillis();
            
            estmat.time=timestamp;



    
            int placa=nodo.IDPlaca;
            String avion=nodo.IDAvion;

            System.out.println("A analizar!! ");

		if(msgsplaca1.containsKey(nodo.raw) && msgsplaca2.containsKey(nodo.raw) && msgsplaca3.containsKey(nodo.raw)){
		    estmat.msgscomunes=estmat.msgscomunes+1;
			System.out.println("TENGO YA "+ estmat.msgscomunes +" COMUNES PARA EL AVIÓN " + avion);
            if((estmat.oldtime1==0  && nodo.IDPlaca==1) || (nodo.IDPlaca==2 && estmat.oldtime2==0) || (nodo.IDPlaca==3 && estmat.oldtime3==0 ) ){

                    estmat.oldtime1=msgsplaca1.get(nodo.raw).TiempoAvion;

                    estmat.oldtime2=msgsplaca2.get(nodo.raw).TiempoAvion;

                    estmat.oldtime3=msgsplaca3.get(nodo.raw).TiempoAvion;


					estmat.mensajes[estmat.msgscomunes-1]=nodo.raw;
         
            }else{

                     lapso=(msgsplaca1.get(nodo.raw).TiempoAvion)-estmat.oldtime1;
					 estmat.matriz[0][estmat.msgscomunes-2]=lapso; 
					 
                    lapso=(msgsplaca2.get(nodo.raw).TiempoAvion)-estmat.oldtime2; 
					estmat.matriz[1][estmat.msgscomunes-2]=lapso; 

                    lapso=(msgsplaca3.get(nodo.raw).TiempoAvion)-estmat.oldtime3;     
					estmat.matriz[2][estmat.msgscomunes-2]=lapso; 


                	synchronized (estmat){
                   
    //esto es una traza para que se vea como funciona el tema de las matrices//                    
		                for (int x=0; x < estmat.matriz.length; x++) {
		                        System.out.print("|");
		                        for (int y=0; y < estmat.matriz[x].length; y++) {
		                        System.out.print (estmat.matriz[x][y]);
		                        if (y!=estmat.matriz[x].length-1) System.out.print("\t");
		                        }
		                        System.out.println("|");
		                    }


               		 }
		                 
                    estmat.oldtime1=msgsplaca1.get(nodo.raw).TiempoAvion;
                    estmat.oldtime2=msgsplaca2.get(nodo.raw).TiempoAvion;
                    estmat.oldtime3=msgsplaca3.get(nodo.raw).TiempoAvion;
                    
					estmat.mensajes[estmat.msgscomunes-1]=nodo.raw;

					
					if (estmat.msgscomunes==4){

		                calcularVerification(avion, estmat);
						
						borrarMatriz(avion);
						estmat.msgscomunes=0;


		            }

                //para que no se produzcan errores de overflow//
                msgsplaca1.remove(nodo.raw);
                msgsplaca2.remove(nodo.raw);					
                msgsplaca3.remove(nodo.raw);

                }
			}
    }else{

        estructura estmat = new estructura( );
        estmat.time=System.currentTimeMillis();
        

        String avion=nodo.IDAvion;


        if(nodo.IDPlaca==1){
		  synchronized(msgsplaca1){
            msgsplaca1.put(nodo.raw, nodo);
		  }
        }else if(nodo.IDPlaca==2){
		  synchronized(msgsplaca2){
            msgsplaca2.put(nodo.raw, nodo);
		  }
        }else if(nodo.IDPlaca==3){
		  synchronized(msgsplaca3){
            msgsplaca3.put(nodo.raw, nodo);
		  }
        }
        synchronized(mapa){
		    mapa.put(avion, estmat);
        }
      }
}





// Implementa Runnable para poder ser lanzada en un hilo aparte


public Servidor_Def() {

        

        mapa = new HashMap<String, estructura>();
      

    

}


public static void main(String[] args) throws Exception {
   


try{
      int puerto=0;
      int numerodecliente=1;      

      Servidor_Def servidor=new Servidor_Def();

      Runnable barrer = new recolector(servidor);
      Thread hiloRecolector = new Thread(barrer);
      hiloRecolector.start(); 


	  // Se espera y acepta un nuevo cliente
      System.out.println("Esperando clientes...");
      ServerSocket socketServidor = new ServerSocket(8080);

      while (true)
      {
	  


          Socket cliente = socketServidor.accept();

          // Se instancia una clase para atender al cliente y se lanza en
          // un hilo aparte.
	      System.out.println("a punto de ejecutar el hilo del cliente número " + numerodecliente);
          Runnable nuevoCliente = new HiloDeCliente(cliente, servidor);
          Thread hiloSocket = new Thread(nuevoCliente);
          hiloSocket.start();
		  numerodecliente=numerodecliente+1;
      }

            

    }catch(IOException e){
        e.printStackTrace();

    }	



    }

}



