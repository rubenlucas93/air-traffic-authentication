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
import org.opensky.libadsb.ListaEnlazada;
import org.opensky.libadsb.Nodo;
import org.opensky.libadsb.Decoder;
import org.opensky.libadsb.Position;
import org.opensky.libadsb.PositionDecoder;
import org.opensky.libadsb.tools;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.exceptions.UnspecifiedFormatError;
import org.opensky.libadsb.msgs.AirbornePositionMsg;
import org.opensky.libadsb.msgs.AirspeedHeadingMsg;
import org.opensky.libadsb.msgs.AllCallReply;
import org.opensky.libadsb.msgs.AltitudeReply;
import org.opensky.libadsb.msgs.CommBAltitudeReply;
import org.opensky.libadsb.msgs.CommBIdentifyReply;
import org.opensky.libadsb.msgs.CommDExtendedLengthMsg;
import org.opensky.libadsb.msgs.EmergencyOrPriorityStatusMsg;
import org.opensky.libadsb.msgs.ExtendedSquitter;
import org.opensky.libadsb.msgs.IdentificationMsg;
import org.opensky.libadsb.msgs.IdentifyReply;
import org.opensky.libadsb.msgs.LongACAS;
import org.opensky.libadsb.msgs.MilitaryExtendedSquitter;
import org.opensky.libadsb.msgs.ModeSReply;
import org.opensky.libadsb.msgs.OperationalStatusMsg;
import org.opensky.libadsb.msgs.ShortACAS;
import org.opensky.libadsb.msgs.SurfacePositionMsg;
import org.opensky.libadsb.msgs.TCASResolutionAdvisoryMsg;
import org.opensky.libadsb.msgs.VelocityOverGroundMsg;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class HiloDeCliente implements Runnable
{
   // En el constructor recibe y guarda los parámetros que sean necesarios.
   // En este caso una lista con toda la conversación y el socket que debe
   // atender.

    private Socket socket; 

    private boolean conectado=true;

    public Servidor_Def servidor;

    private ObjectInputStream entradaDatos;

    private informacion nodo=new informacion( );

    public HiloDeCliente(Socket socket, Servidor_Def servidor)
   {

    

    
            this.socket=socket;
            this.servidor=servidor;




   }

   public void run ()
   {
   while(true){
      
      while (conectado)
      {
         try{         


                System.out.println("justo antes de leer los datos...");

                entradaDatos = new ObjectInputStream(socket.getInputStream());
                                     
                System.out.println("servidor a la escucha!!");
                conectado=true;
     
                nodo = (informacion)entradaDatos.readObject();
        
		System.out.println("ya voy a insertar el lapso con los datos obtenidos del objeto...: ");
		System.out.println(nodo);
                servidor.insertarLapso(nodo);

            } catch (IOException ex) {
                System.out.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                conectado = false; 
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    entradaDatos.close();
                    
                } catch (IOException ex2) {
                    System.out.println("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                }
            } catch (ClassNotFoundException ex) {
                conectado=false;
                System.out.println("Progoramming Error");
            } 
            
      }
   }
  }
} 
