package es.florida.ae2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class HiloServidor implements Runnable {

	Socket conexion;
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	private OutputStream os;
	private PrintWriter pw;
	private String usuarioActual;
	private String canal;
	private ArrayList<HiloServidor> listaHilos1;
	private ArrayList<HiloServidor> listaHilos2;
	private ArrayList<HiloServidor> listaHilos3;
	private ArrayList<HiloServidor> listaHilos4;
//	private String idUnico;

	HiloServidor(Socket conexion, ArrayList<HiloServidor> listaHilos1, ArrayList<HiloServidor> listaHilos2,
			ArrayList<HiloServidor> listaHilos3, ArrayList<HiloServidor> listaHilos4) {
		try {
			this.conexion = conexion;
			this.is = conexion.getInputStream();
			this.isr = new InputStreamReader(is);
			this.br = new BufferedReader(isr);
			this.os = conexion.getOutputStream();
			this.pw = new PrintWriter(os, true);
			this.listaHilos1 = listaHilos1;
			this.listaHilos2 = listaHilos2;
			this.listaHilos3 = listaHilos3;
			this.listaHilos4 = listaHilos4;
//			idUnico = UUID.randomUUID().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-HH:mm:ss");
		seleccionCanal();
		try {
			String mensaje = br.readLine();
			while (!mensaje.equals("exit")) {
				System.err.println("SERVIDOR >>> " + usuarioActual + " (canal " + canal + ") >>> " + mensaje);
				if (!mensaje.equals("")) {
					String timestamp = sdf.format(new Date());
					if (mensaje.equals("whois")) {
						String listaUsuarios = "";
						for (HiloServidor h : canalSeleccionado()) {
							listaUsuarios += h.usuarioActual + " | ";
						}
						pw.println(timestamp + ": Usuarios activos canal " + canal + ": " + listaUsuarios);
					} else if (String.valueOf(mensaje.charAt(0)).equals("@")) {
						String canalDestino = String.valueOf(mensaje.charAt(6));
						String cuerpoMensaje = mensaje.substring(mensaje.indexOf(" ") + 1);
						for (HiloServidor h : seleccionOtroCanal(canalDestino)) {
							h.pw.println(timestamp + ": (canal" + canal + ", " + usuarioActual + ") >>> " + cuerpoMensaje);
						}
					} else if (mensaje.equals("channels")) {
						enviarCanales();
					} else {
						for (HiloServidor h : canalSeleccionado()) {
							if (h.usuarioActual.equals(usuarioActual)) {
								continue;
							}
							h.pw.println(timestamp + ": " + usuarioActual + " >>> " + mensaje);
						}
					}
				}
				mensaje = br.readLine();
			}
			canalSeleccionado().remove(this);
			conexion.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void seleccionCanal() {
		try {
			System.err.println("SERVIDOR >>> Esperando seleccion de canal");
			enviarCanales();
			canal = br.readLine();
			canalSeleccionado().add(this);
			String nombreUsuario = br.readLine();
			while (compruebaUsuario(nombreUsuario)) {
				pw.println("ERROR");
				nombreUsuario = br.readLine();
			}
			pw.println("OK");
			usuarioActual = nombreUsuario;
			System.err.println("SERVIDOR >>> Usuario " + usuarioActual + " ha seleccionado canal: " + canal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void enviarCanales() {
		try {
			FileReader fr = new FileReader("channels.txt");
			BufferedReader br = new BufferedReader(fr);
			String linea = "Canales disponibles: ";
			ArrayList<String> canales = new ArrayList<String>();
			while ((linea = br.readLine()) != null) {
				canales.add(linea);
			}
			br.close();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-HH:mm:ss");
			String timestamp = sdf.format(new Date());
			pw.println(timestamp + ": Canales disponibles: " + canales.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean compruebaUsuario(String nombreUsuario) {
		boolean existe = false;
		for (HiloServidor hs : canalSeleccionado()) {
			if (hs.usuarioActual != null)
				if (hs.usuarioActual.equals(nombreUsuario))
					existe = true;
		}
		return existe;
	}

	private ArrayList<HiloServidor> canalSeleccionado() {
		switch (canal) {
		case "1":
			return listaHilos1;
		case "2":
			return listaHilos2;
		case "3":
			return listaHilos3;
		case "4":
			return listaHilos4;
		default:
			return null;
		}
	}

	private ArrayList<HiloServidor> seleccionOtroCanal(String otroCanal) {
		switch (otroCanal) {
		case "1":
			return listaHilos1;
		case "2":
			return listaHilos2;
		case "3":
			return listaHilos3;
		case "4":
			return listaHilos4;
		default:
			return null;
		}
	}

}
