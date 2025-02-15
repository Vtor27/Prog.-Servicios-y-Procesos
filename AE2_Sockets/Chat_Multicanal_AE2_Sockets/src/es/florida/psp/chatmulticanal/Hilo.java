package es.florida.psp.chatmulticanal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * La clase Hilo implementa la lógica de manejo del cliente en el servidor. Cada
 * instancia de esta clase gestiona un cliente conectado a través de un socket.
 */
public class Hilo implements Runnable {

	private Socket cliente;
	private int canal;
	private String nombreUsuario;
	private InputStreamReader isr;
	private BufferedReader br;
	private PrintWriter pw;
	private String horaConexion;
	private Scanner scanner;

	/**
	 * Constructor de la clase Hilo.
	 * 
	 * @param cliente Que representa al socket que permite hacer la conexión del
	 *                cliente con el servidor.
	 */
	public Hilo(Socket cliente) {
		this.cliente = cliente;
	}

	/**
	 * Devuelve el nombre del usuario conectado.
	 * 
	 * @return Nombre del usuario.
	 */
	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	/**
	 * Devuelve el socket del cliente conectado.
	 * 
	 * @return Socket del cliente.
	 */
	public Socket getCliente() {
		return cliente;
	}

	/**
	 * Método principal que se ejecuta cuando se inicia el hilo del cliente. Envía
	 * la lista de canales disponibles, solicita el canal y el nombre de usuario, y
	 * lanza el hilo que se utilizará para el envio en paralelo de los mensajes.
	 */
	@Override
	public void run() {
		try {
			isr = new InputStreamReader(cliente.getInputStream());
			br = new BufferedReader(isr);
			pw = new PrintWriter(cliente.getOutputStream());

			enviarCanales();

			canal = Integer.parseInt(br.readLine()) - 1;

			while (true) {
				nombreUsuario = br.readLine();

				boolean nombreExistente = false;
				synchronized (Servidor.usuariosPorCanal) {
					for (Hilo cliente : Servidor.usuariosPorCanal.get(canal)) {
						if (cliente.getNombreUsuario().equals(nombreUsuario)) {
							nombreExistente = true;
							break;
						}
					}

					if (!nombreExistente && !nombreUsuario.contains(" ") && !nombreUsuario.isEmpty()) {
						Servidor.usuariosPorCanal.get(canal).add(this);
						pw.println("OK");
						pw.flush();
						break;
					} else {
						pw.println("El nombre de usuario ya está en uso. Inténtalo de nuevo.");
						pw.flush();
					}
				}
			}

			horaConexion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			System.err.println(
					"SERVIDOR >>> [" + horaConexion + "] Usuario " + nombreUsuario + " conectado al canal " + canal);

			Thread hiloEnvio = new Thread(new EnviarMensajes());
			hiloEnvio.start();

		} catch (IOException e) {
			System.err.println("HILO >>> Error en la comunicación con el cliente.");
			e.printStackTrace();
		}
	}

	/**
	 * Reenvía un mensaje que podrán leer todos los clientes que estén conectados a
	 * ese canal.
	 * 
	 * @param canal         Canal al que está conectado el cliente.
	 * @param nombreUsuario Nombre del usuario que envía el mensaje.
	 * @param mensaje       Contenido del mensaje.
	 */
	public void reenviarMensajesAlCanal(int canal, String nombreUsuario, String mensaje) {
		ArrayList<Hilo> clientesDelCanal = Servidor.usuariosPorCanal.get(this.canal);

		String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		String mensajeFormateado = "[" + hora + "] <<< " + nombreUsuario + ": " + mensaje;

		synchronized (clientesDelCanal) {
			for (Hilo cliente : clientesDelCanal) {
				if (cliente != this) {
					cliente.pw.println(mensajeFormateado);
					cliente.pw.flush();
				}
			}
		}
	}

	/**
	 * Obtiene el índice del canal al que está conectado el cliente.
	 * 
	 * @return Índice del canal.
	 */
	private int obtenerCanalDelUsuario() {
		for (int i = 0; i < Servidor.usuariosPorCanal.size(); i++) {
			if (Servidor.usuariosPorCanal.get(i).contains(Hilo.this)) {
				System.out.println("SERVIDOR >>> Se encontró al cliente en el canal " + (i + 1));
				return i;
			}
		}
		return -1; // Se pone -1 porque asi me aseguro de que si hay algún error devuelve un valor
				   // no valido.
	}

	/**
	 * Envía al cliente la lista de canales disponibles del servidor.
	 */
	public void enviarCanales() {
		List<String> canales = Servidor.canales;
		int index = 1;
		pw.write("Canales disponibles: \n");

		for (String canal : canales) {
			pw.write(index + ". " + canal + "\n");
			index++;
		}
		pw.write("\n");
		pw.flush();
	}

	/**
	 * Clase interna que gestiona el envío de mensajes por parte del cliente. Se
	 * crea para lanzar un hilo separado y que esté funcionando en paralelo con el
	 * que se utiliza para la recepción de los mensajes del servidor y que así no
	 * haya interferecias en los envios ni recepciones.
	 */
	class EnviarMensajes implements Runnable {
		public static final String RESET = "\u001B[0m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";

		@Override
		public void run() {
			try {
				scanner = new Scanner(cliente.getInputStream());
				String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				while (true) {
					String mensaje = scanner.nextLine();

					if (mensaje.equalsIgnoreCase("whois")) {
						mensajeWhois();
					} else if (mensaje.equalsIgnoreCase("exit")) {
						mensajeExit();
						break;
					} else if (mensaje.startsWith("@canal")) {
						mensajeAlCanal(mensaje);
					} else if (mensaje.equalsIgnoreCase("channels")) {
						mostrarCanalesDisponibles();
					} else if (mensaje.equals("?")) {
						pw.println(GREEN + "Palabras clave: \n"
								+ "-'whois': Muestra los usuarios conectados al canal. \n"
								+ "-'exit': Sales del canal y te desconectas del servidor. \n"
								+ "-'@canalX': Sustituye la 'X' por el número del canal al que quieres enviar el mensaje. \n"
								+ "-'channels': Muestra los canales disponibles a los que te puedes conectar." + RESET);
						pw.flush();
					} else {
						pw.println("[" + hora + "] >>> " + mensaje);
						pw.flush();

						canal = obtenerCanalDelUsuario();
						reenviarMensajesAlCanal(canal, nombreUsuario, mensaje);
					}
				}
			} catch (IOException e) {
				try {
					mensajeExit();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		/**
		 * Muestra la lista de canales disponibles al cliente. Envía un mensaje al
		 * cliente con los nombres y números de los canales disponibles.
		 */
		private void mostrarCanalesDisponibles() {
			StringBuilder sb = new StringBuilder();
			sb.append(RED).append("Canales disponibles: \n");
			for (int i = 0; i < Servidor.canales.size(); i++) {
				sb.append(i + 1).append(". ").append(Servidor.canales.get(i)).append("\n");
			}
			sb.append(RESET);
			pw.print(sb);
			pw.flush();
		}

		/**
		 * Envía un mensaje a un canal específico que indique el cliente.
		 * 
		 * @param mensaje El mensaje que envie el cliente debe tener el siguiente
		 *                formato(@canalX mensaje). Donde 'X' es el número del canal al
		 *                que se quiere enviar el mensaje.
		 */
		private void mensajeAlCanal(String mensaje) {
			try {
				String[] mensajePorPartes = mensaje.split(" ", 2);
				String numeroCanal = mensajePorPartes[0].substring(6); // Primera parte, comprende desde @ hasta el
																		// número del canal que elija.
				int canal = Integer.parseInt(numeroCanal) - 1; // Se resta 1 para quie coincida con el indice de la
																// lista de canales.

				if (canal < 0 || canal >= Servidor.canales.size()) {
					pw.println("El canal al que intentas enviar el mensaje no existe.");
					pw.flush();
					return;
				}

				String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				String mensajeFormateado = "[" + hora + "] " + nombreUsuario + " (CANAL " + (Hilo.this.canal + 1)
						+ "): " + mensajePorPartes[1];
				String mensajeUser = "[" + hora + "] >>> " + mensajePorPartes[1];
				synchronized (Servidor.usuariosPorCanal) {
					ArrayList<Hilo> clientesDelCanal = Servidor.usuariosPorCanal.get(canal);
					for (Hilo cliente : clientesDelCanal) {
						cliente.pw.println(RED + mensajeFormateado + RESET);
						cliente.pw.flush();
					}
				}
				pw.println(mensajeUser);
				pw.flush();
			} catch (Exception e) {
				pw.println("Error al enviar el mensaje. Asegúrate de usar el formato correcto: @canalX mensaje");
				pw.flush();
			}
		}

		/**
		 * Este método maneja la desconexión del cliente. Se elimina al cliente de la
		 * lista de usuarios del canal en el que estaba y se cierra la conexión con el
		 * servidor.
		 * 
		 * @throws IOException Mostrará el error si hay algún problema al desconectarse.
		 */
		private void mensajeExit() throws IOException {
			synchronized (Servidor.usuariosPorCanal) {

				ArrayList<Hilo> clientesDelCanal = Servidor.usuariosPorCanal.get(Hilo.this.canal);
				Hilo eliminarCliente = null;

				for (Hilo hilo : clientesDelCanal) {
					if (hilo.getCliente().equals(cliente)) {
						eliminarCliente = hilo;
						break;
					}
				}
				if (eliminarCliente != null) {
					clientesDelCanal.remove(eliminarCliente);
				} else {
					System.out.println("SERVIDOR >>> No se encontró al cliente en la lista.");
				}

			}
			System.err.println("SERVIDOR >>> El cliente " + nombreUsuario + " se ha desconectado.");
			pw.println(RED + "Te has desconectado del canal." + RESET);
			pw.flush();
			pw.close();
			br.close();
			cliente.close();
		}

		/**
		 * Muestra la lista de los usuarios que se encuentran conectados al canal
		 * actual. Envía al cliente una lista con los nombres de los usuarios conectados
		 * al canal.
		 */
		private void mensajeWhois() {
			synchronized (Servidor.usuariosPorCanal) {
				ArrayList<Hilo> clientesPorCanal = Servidor.usuariosPorCanal.get(Hilo.this.canal);
				StringBuilder sb = new StringBuilder();
				sb.append(RED).append(">>> Usuarios conectados al canal(" + (Hilo.this.canal + 1) + "): ");

				for (Hilo cliente : clientesPorCanal) {
					sb.append(cliente.getNombreUsuario()).append(", ");
				}
				if (clientesPorCanal.size() > 0) {
					sb.setLength(sb.length() - 2);
				}
				sb.append(RESET);
				pw.println(sb);
				pw.flush();
			}

		}
	}
}
