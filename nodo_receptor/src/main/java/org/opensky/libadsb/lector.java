    package org.opensky.libadsb;

import java.io.Serializable;


import java.io.*;
import org.opensky.example.Decoder_def;

import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

import java. lang.InterruptedException;
import java.net.UnknownHostException;
import org.opensky.libadsb.exceptions.MissingInformationException;
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

public class lector implements Runnable{


	HashMap<Integer, Double > mapa;
		HashMap<Integer, String> mensajes;

	int nmensaje=1;

     String raw = null;
	 Scanner sc;
     double momento=0; 

	

  public lector(Scanner sc, Decoder_def deco)
   {

		         
		this.sc=sc;
        this.mapa=deco.mapa;
		this.mensajes=deco.mensajes;
		this.nmensaje=nmensaje;


   }

   public void run ()
   {
	while(sc.hasNext()){
	raw = sc.nextLine();
	//este comando no debería estar, es resultado de no tener el timestamp fiable calculado en dump  //
	momento=System.nanoTime();
	System.out.println(momento);
	mensajes.put(nmensaje, raw);
	//este comando no debería estar, es resultado de no tener el timestamp fiable calculado en dump  //
	mapa.put(nmensaje, momento);
	nmensaje=nmensaje+1;
	
	}
	sc.close();


	}

}
