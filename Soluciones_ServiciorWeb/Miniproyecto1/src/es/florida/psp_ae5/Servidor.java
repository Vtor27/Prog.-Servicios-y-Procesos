package es.florida.psp_ae5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

public class Servidor {

	public static void main(String[] args) {

		try {
			FileReader fr = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(fr);
			String host = br.readLine();
			int puerto = Integer.parseInt(br.readLine());
			br.close();
			InetSocketAddress direccionTCPIP = new InetSocketAddress(host, puerto);
			int backlog = 0; // Numero de conexiones pendientes que el servidor puede mantener en cola
			HttpServer servidor = HttpServer.create(direccionTCPIP, backlog);

			GestorHTTP gestorHTTP = new GestorHTTP(); // Clase que gestionara los GETs, POSTs, etc.
			String rutaRespuesta = "/estufa"; // Ruta (a partir de localhost en este ejemplo) en la que el servidor dara
												// respuesta
			servidor.createContext(rutaRespuesta, gestorHTTP); // Crea un contexto, asocia la ruta al gestor HTTP

			ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
			servidor.setExecutor(threadPoolExecutor);

			servidor.start();
			System.out.println("Servidor HTTP arranca en " + host + ":" + puerto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}