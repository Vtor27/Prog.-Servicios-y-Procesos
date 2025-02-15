package es.florida.ae2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloClienteRecepcion implements Runnable {

	private Socket conexion;
	private BufferedReader br;

	public HiloClienteRecepcion(Socket conexion) {
		try {
			this.conexion = conexion;
			InputStream is = conexion.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			this.br = new BufferedReader(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try {
			while (true) {
				String mensaje = br.readLine();
				System.err.println(mensaje);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

}
