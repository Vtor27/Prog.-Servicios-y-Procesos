package es.florida.mp2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

public class Servidor {

	private static String ficheroLog = "log.txt"; //Se puede pasar como parametro la ruta del fichero de log
	private static String rutaFicheroConfiguracion = "config.txt";  //Se puede pasar como parametro la ruta del fichero de configuracion
	
    /**
     * Método main de la clase Servidor. Ejecuta el servidor multihilo con la configuración indicada en el fichero config.txt y queda a la espera de peticiones de clientes.
     * 
     * @param args No se utilizan
     */
    public static void main(String[] args) {

    	System.out.println("Arranca el servidor");
    	ficheroLog("Arranca el servidor");

    	File ficheroConfiguracion = new File(rutaFicheroConfiguracion);
		try {
			FileReader fr = new FileReader(ficheroConfiguracion);
	    	BufferedReader br = new BufferedReader(fr);
	    	String host = br.readLine().split("=")[1];
	    	System.out.println("IP: " + host);
	    	ficheroLog("IP: " + host);
	    	int puerto = Integer.parseInt(br.readLine().split("=")[1]);
	    	System.out.println("Puerto: " + puerto);
	    	ficheroLog("Puerto: " + puerto);
	    	String rutaRespuesta = br.readLine().split("=")[1];   
	    	System.out.println("Ruta contexto: " + rutaRespuesta);
	    	ficheroLog("Ruta contexto: " + rutaRespuesta);
	    	
	    	InetSocketAddress direccionTCPIP = new InetSocketAddress(host,puerto);
	    	int backlog = Integer.parseInt(br.readLine().split("=")[1]); //Numero de conexiones pendientes que el servidor puede mantener en cola
	    	HttpServer servidor = HttpServer.create(direccionTCPIP, backlog);
	    	
	    	GestorHTTP gestorHTTP = new GestorHTTP();   //Clase que gestionara los GETs, POSTs, etc.
	    	servidor.createContext(rutaRespuesta, gestorHTTP);   //Crea un contexto, asocia la ruta al gestor HTTP
	    	
	    	int numThreads = Integer.parseInt(br.readLine().split("=")[1]);
	    	ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThreads);
	    	servidor.setExecutor(threadPoolExecutor); 
	    	
	    	servidor.start();
	    	System.out.println("Servidor HTTP arranca en el puerto " + puerto);
	    	ficheroLog("Servidor HTTP arranca en el puerto " + puerto);
	    	br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Método para añadir una línea al fichero de log
	 * 
	 * @param linea String con la línea a añadir al fichero de log
	 */
    public static void ficheroLog(String linea) {
		try {
			FileWriter fw = new FileWriter(ficheroLog, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss - ").format(new java.util.Date());
			bw.write(timeStamp + linea + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}