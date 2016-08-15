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

import org.opensky.libadsb.decodificador; 

/**
 * ADS-B decoder example: It reads STDIN line-by-line. It should be fed with
 * comma-separated timestamp and message. Example input:
 * 
 * 1,8d4b19f39911088090641010b9b0
 * 2,8d4ca513587153a8184a2fb5adeb
 * 3,8d3413c399014e23c80f947ce87c
 * 4,5d4ca88c079afe
 * 5,a0001838ca3e51f0a8000047a36a
 * 6,8d47a36a58c38668ffb55f000000
 * 7,5d506c28000000
 * 8,a8000102fe81c1000000004401e3
 * 9,a0001839000000000000004401e3
 * 
 * @author Matthias Schäfer (schaefer@opensky-network.org)
 */
public class Decoder_def {
	// we store the position decoder for each aircraft
		public HashMap<Integer, String> mensajes;



    public Decoder_def() {
        //al tener implementada la parte de "decodificar" no necesitamos los otros mapas que creábamos para almacenar los aviones interceptados y poder decodificar los mensajes tal y como nos permitía la librería de opensky//
		mensajes=	new HashMap<Integer, String>();
	}
	
	public static void main(String[] args) throws Exception {
   
				 String direccionServidor = new String("192.168.2.3");     

                System.out.println("receptor ADSB LISTO!");

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

				if (dec.mensajes.containsKey(nmensaje)){
                  //conforme voy viendo que se van captando valores, los voy decodificando para enviarselos al servidor//
					values=dec.mensajes.get(nmensaje);
                    Runnable decodificar = new decodificador(values, socket, nmensaje);
     			    Thread hiloDecodificador = new Thread(decodificar);
      			    hiloDecodificador.start();
					nmensaje=nmensaje+1;
				}
				

		        }
        

	}
}
