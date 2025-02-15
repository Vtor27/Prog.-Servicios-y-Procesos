package es.florida.psp.chatmulticanal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clase Servidor que gestiona la conexión de los clientes y sus canales. Actúa
 * como centro de comunicación, aceptando conexiones, creando hilos para manejar
 * a cada cliente.
 */
public class Servidor {
	public static ArrayList<String> canales = new ArrayList<>();
	public static ArrayList<ArrayList<Hilo>> usuariosPorCanal = new ArrayList<>();

	/**
	 * Carga los nombres de los canales desde un archivo de texto.
	 * 
	 * @param file Archivo desde el que se leen los nombres de los canales.
	 */
	public static void cargarCanales(File file) {
		if (!file.exists()) {
			System.err.println("SERVIDOR >>> El archivo desde donde se leen los canales no se encuentra...");
			return;
		}
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String linea;

			while ((linea = br.readLine()) != null) {
				canales.add(linea);
				usuariosPorCanal.add(new ArrayList<>());
			}
			fr.close();
			br.close();
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error al leer el archivo 'canales.txt'.");
			e.printStackTrace();
		}
	}

	/**
	 * Método principal que se encarga de arranca el servidor, carga los canales y
	 * acepta conexiones de clientes.
	 * 
	 * @param args Argumentos de línea de comandos, en este caso no se utilizan.
	 */
	public static void main(String[] args) {
		System.err.println("SERVIDOR >>> Arranca el servidor, espera de petición... \n");
		ServerSocket socketEscucha = null;

		File archivo = new File("canales.txt");
		cargarCanales(archivo);

		try {
			socketEscucha = new ServerSocket(9090);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			e.printStackTrace();
			return;
		}

		while (true) {
			Socket conexion;
			try {
				conexion = socketEscucha.accept();
				System.err.println("SERVIDOR >>> Conexión recibida --> Lanza un hilo. \n");
				Hilo h = new Hilo(conexion);
				Thread hilo = new Thread(h);
				hilo.start();
			} catch (IOException e) {
				System.err.println("SERVIDOR >>> ERROR");
				e.printStackTrace();
			}
		}
	}
}
