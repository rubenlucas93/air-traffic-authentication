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
 *  adouble with org.opensky.libadsb.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

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

import org.opensky.libadsb.lector;
import org.opensky.libadsb.informacion;
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

import org.opensky.libadsb.decodificador; 


public class Decoder_def {
        public HashMap<String, PositionDecoder> decs;
		public HashMap<Integer, Double> mapa;
		public HashMap<Integer, String> mensajes;



    public Decoder_def() {
		decs = new HashMap<String, PositionDecoder>();
		mapa=new HashMap<Integer, Double>();
		mensajes=	new HashMap<Integer, String>();
	}
	
	public static void main(String[] args) throws Exception {
   
				String direccionServidor = new String("192.168.2.3");     

                System.out.println("receptor ADSB LISTO!");

		        String icao = null;
		       
                if (args.length > 0) {
			        icao = args[1];
			        System.err.println("Set filter to ICAO 24-bit ID '"+icao+"'.");
		        }

      			int nmensaje=1;


		        // iterate over STDIN
		        Scanner sc = new Scanner(System.in, "UTF-8");
		        Decoder_def dec = new Decoder_def();
			    Socket  socket = new Socket(direccionServidor, 8080);
                System.out.println("receptor ADSB CONECTADO!");
				String values=null;


			    Runnable leer = new lector(sc, dec);
     		    Thread hiloLector = new Thread(leer);
      		    hiloLector.start();

				while(true) {

				if (dec.mapa.containsKey(nmensaje)){
                  //conforme voy viendo que se van captando valores, los voy decodificando para enviarselos al servidor//
			      values=dec.mensajes.get(nmensaje);
                  Runnable decodificar = new decodificador(values, icao, dec, socket, nmensaje);
     			  Thread hiloDecodificador = new Thread(decodificar);
      			  hiloDecodificador.start();
				  nmensaje=nmensaje+1;
				}
				

		        }
        

	}
}
