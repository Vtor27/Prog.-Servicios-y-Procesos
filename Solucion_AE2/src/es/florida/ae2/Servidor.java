package es.florida.ae2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

	private static ArrayList<HiloServidor> listaHilos1;
	private static ArrayList<HiloServidor> listaHilos2;
	private static ArrayList<HiloServidor> listaHilos3;
	private static ArrayList<HiloServidor> listaHilos4;
	
	/**
	 * Método para gestionar conexiones de clientes y generar hilos de servidor que gestionen cada partida independientemente.
	 * 
	 * @param args No se utilizan.
	 */
	public static void main(String[] args) {
		listaHilos1 = new ArrayList<HiloServidor>();
		listaHilos2 = new ArrayList<HiloServidor>();
		listaHilos3 = new ArrayList<HiloServidor>();
		listaHilos4 = new ArrayList<HiloServidor>();
		System.err.println("SERVIDOR >>> Arranca el servidor, espera peticion...");
		ServerSocket socketEscucha = null;
		try {
			socketEscucha = new ServerSocket(5000);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			e.printStackTrace();
			return;
		}
		while (true) {		
			Socket conexion;
			try {
				conexion = socketEscucha.accept();
				System.err.println("SERVIDOR >>> Conexion recibida --> Lanza nuevo hilo");
				HiloServidor h = new HiloServidor(conexion, listaHilos1, listaHilos2, listaHilos3, listaHilos4);
				Thread t = new Thread(h);
				t.start();
			} catch (IOException e) {
				System.err.println("SERVIDOR >>> Error");
				e.printStackTrace();
			}
		}

	}

}
