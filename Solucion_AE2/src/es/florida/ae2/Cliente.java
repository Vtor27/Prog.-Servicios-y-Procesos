package es.florida.ae2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Cliente {

	private static Socket conexion;
	private static InputStream is;
	private static InputStreamReader isr;
	private static BufferedReader br;
	private static OutputStream os;
	private static PrintWriter pw;

	private static void creaConexion(String ip, int puerto) {
		try {
			conexion = new Socket(ip, puerto);
			is = conexion.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			os = conexion.getOutputStream();
			pw = new PrintWriter(os);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void seleccionCanal() {
		try {
			Scanner teclado = new Scanner(System.in);
			String listaCanales = br.readLine();
			System.out.println(listaCanales);
			System.out.print("Selecciona canal: ");
			String canal = teclado.nextLine();
			pw.write(canal + "\n");
			pw.flush();
			System.out.print("Indica nombre de usuario: ");
			String nombreUsuario = teclado.nextLine();
			pw.write(nombreUsuario + "\n");
			pw.flush();
			String respuesta = br.readLine();
			while (!respuesta.equals("OK")) {
				System.out.print("El usuario ya existe, indica otro: ");
				nombreUsuario = teclado.nextLine();
				pw.write(nombreUsuario + "\n");
				pw.flush();
				respuesta = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-HH:mm:ss");
		Scanner teclado = new Scanner(System.in);
		System.out.print("IP: ");
		String ip = teclado.nextLine();
		System.out.print("Puerto: ");
		int puerto = Integer.parseInt(teclado.nextLine());
		creaConexion(ip, puerto);
		seleccionCanal();
		HiloClienteRecepcion recepcionMensajes = new HiloClienteRecepcion(conexion);
		Thread t = new Thread(recepcionMensajes);
		t.start();
		System.out.println("Presiona ENTER para enviar mensajes");
		try {
			String mensaje = "";
			do {
				teclado.nextLine();
				mensaje = JOptionPane.showInputDialog("Introduce 'exit' para cerrar");
				if (mensaje == null) {
					mensaje = "";
				}
				String timestamp = sdf.format(new Date());
				System.out.println(timestamp + ": " + mensaje);
				pw.write(mensaje + "\n");
				pw.flush();
			} while (!mensaje.equals("exit"));
			conexion.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
