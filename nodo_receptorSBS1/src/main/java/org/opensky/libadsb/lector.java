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

public class lector implements Runnable{


	 HashMap<Integer, String> mensajes;

	 int nmensaje=1;

     String raw = null;
	 Scanner sc;
     double momento=0; 

	

  public lector(Scanner sc, Decoder_def deco)
   {

		         
		this.sc=sc;
		this.mensajes=deco.mensajes;
		this.nmensaje=nmensaje;


   }

   public void run ()
   {
	while(sc.hasNext()){
//aquí solo leo y lo meto en el mapa para que vayan analizándose los mensajes en otro hilo de ejecución. De esta manera el retardo entre la recepción y el resultado es mínimo//
	raw = sc.nextLine();
	mensajes.put(nmensaje, raw);
	nmensaje=nmensaje+1;
	
	}
	sc.close();


	}

}
