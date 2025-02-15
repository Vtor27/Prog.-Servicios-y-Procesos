package es.florida.psp.chatmulticanal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * La clase Cliente implementa la lógica del cliente que se conecta al servidor.
 * Permite al usuario seleccionar un canal, enviar mensajes y recibir respuestas
 * del servidor que luego muestra al cliente..
 */
public class Cliente {

	private static final String host = "localhost";
	private static final int puerto = 9090;

	/**
	 * Método principal del cliente. Este método se encarga de hacer directamente la
	 * conexión con el servidor, permite al usuario seleccionar un canal, introduce
	 * un nombre de usuario válido y gestiona el envío y recepción de mensajes.
	 *
	 * @param args Argumentos de línea de comandos, en este caso no se utilizan.
	 * @throws IOException Si ocurre un error de entrada/salida durante la ejecución
	 *                     queda capturado.
	 */
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		
		try {
			System.out.println("CLIENTE >>> Conectando al servidor... \n");
			Thread.sleep(1500);
			Socket cliente = new Socket(host, puerto);
			System.out.println("CLIENTE >>> Conexión establecida. \n");
			Thread.sleep(1000);
			InputStreamReader isr = new InputStreamReader(cliente.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(cliente.getOutputStream(), true);

			String linea;
			while (!(linea = br.readLine()).isEmpty()) {
				System.out.println(linea);
			}

			int canal = -1;
			boolean canalValido = false;
			while (!canalValido) {
				System.out.print("CLIENTE >>> Elige un canal: ");
				canal = scanner.nextInt();

				if (canal >= 1 && canal <= 4) {
					canalValido = true;
				} else {
					System.out.println("CLIENTE >>> El canal seleccionado no es valido. Elije otro... \n");
				}
			}
			pw.println(canal);
			pw.flush();
			scanner.nextLine();

			String nombreUsuario = "";
			boolean nombreValido = false;
			while (!nombreValido) {
				System.out.print(
						"CLIENTE >>> Introduce el nombre de usuario que quieras utilizar (no se permiten espacios): \n");
				nombreUsuario = scanner.nextLine();

				if (!nombreUsuario.contains(" ") && !nombreUsuario.isEmpty()) {
					pw.println(nombreUsuario);
					pw.flush();

					String respuesta = br.readLine();
					if (respuesta.equals("OK")) {
						nombreValido = true;
					} else {
						System.out.println("CLIENTE >>> " + respuesta);
					}
				} else {
					System.out.println("CLIENTE >>> El nombre de usuario no es válido, intenta nuevamente.");
				}
			}

			Thread hiloRecepcion = new Thread(new RecibirMensajes(br));
			hiloRecepcion.start();

			System.out.println("Pulsa ENTER para enviar un mensaje... \n");
			while (true) {
				scanner.nextLine();

				String mensaje = JOptionPane.showInputDialog(null, "Escribe 'exit' para salir.", "Escribe el mensaje",
						JOptionPane.PLAIN_MESSAGE);

				if (mensaje != null && !mensaje.trim().isEmpty()) {
					pw.println(mensaje);
					pw.flush();

					if (mensaje.equalsIgnoreCase("exit")) {
						System.out.println("CLIENTE >>> Te has desconectado. Cerrando conexión...");
						Thread.sleep(1000);
						break;
					}
				} else {
					System.out.println("CLIENTE >>> No se envió ningún mensaje.");
				}
			}
			scanner.close();
			cliente.close();
		} catch (IOException | InterruptedException e) {
			System.out.println("CLIENTE >>> Error de comunicación con el servidor...");
			e.printStackTrace();
		}
	}

	/**
	 * Clase interna que implementa la recepción de los mensajes. Se crea para poder
	 * lanzar un hilo separado del hilo de conexión del cliente. Se encarga de
	 * escuchar todo el rato al servidor para poder recibir mensajes y mostrarlos al
	 * usuario mientras se envian a la vez.
	 */
	static class RecibirMensajes implements Runnable {
		private BufferedReader br;

		/**
		 * Constructor de la clase RecibirMensajes.
		 * 
		 * @param br BufferedReader Conectado al flujo de entrada de mensajes para poder
		 *           leer y mostrarselos al cliente.
		 */
		public RecibirMensajes(BufferedReader br) {
			this.br = br;
		}

		/**
		 * Método run que define el comportamiento del hilo. Lee mensajes del servidor y
		 * los muestra en la consola.
		 */
		@Override
		public void run() {
			try {
				String mensaje;
				while ((mensaje = br.readLine()) != null) {
					System.out.println(mensaje);
				}
			} catch (IOException e) {
				if (e.getMessage().equals("Socket closed")) {
					System.err.println("Te has desconctado del Servidor.");
				}
			}
		}
	}
}