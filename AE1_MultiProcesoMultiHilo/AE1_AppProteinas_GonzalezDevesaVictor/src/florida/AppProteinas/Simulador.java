package florida.AppProteinas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

/**
 * Clase principal que gestiona la simulación de proteínas.
 * Controla la interacción con la interfaz gráfica y se encarga de lanzar la parte de multiproces y multihilo..
 */
public class Simulador {
	private static int contadorSimuMP = 1;
	private Vista vista;

	/**
     * Constructor de la clase Simulador.
     * 
     * @param vista Es la interfaz gáfica que permite interactuar al usuario.
     */
	public Simulador(Vista vista) {
		this.vista = vista;
		controlPressButton();
	}

	
	/**
     * Inicializa la simulación, primero la multiproceso y al terminar la multihilo una vez se presiona el botón.
     */
	public void controlPressButton() {
		vista.getBtnSimular().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Integer> cantidades = controlInputCantidadProteinas();
				if (cantidades != null) {
					controlSimulacionMultiproceso(cantidades);
					try {
						controlSimulacionMultihilo(cantidades);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}

		});
	}

	
	/**
     * Incrementa y devuelve el contador de simulaciones.
     * 
     * @return Devuelve el número de simulación aumentandolo en 1 por cada simulación multiproceso realizada indistintamente del tipo que sea.
     */
	public int contadorSimulacionesMP() {
		return contadorSimuMP++;
	}

	/**
     * Controla la cantidad de proteínas de cada tipo introducida por el usuario en la interfaz.
     * 
     * @return Una lista de enteros que contiene las cantidades de proteínas de cada tipo.
     *         Devuelve null si hay un error o valores negativos.
     */
	public List<Integer> controlInputCantidadProteinas() {
		try {
			int cantidadPrimaria = Integer.parseInt(vista.getInputPrimaria().getText());
			int cantidadSecundaria = Integer.parseInt(vista.getInputSecundaria().getText());
			int cantidadTerciaria = Integer.parseInt(vista.getInputTerciaria().getText());
			int cantidadCuaternaria = Integer.parseInt(vista.getInputCuaternaria().getText());

			if (cantidadPrimaria < 0 || cantidadSecundaria < 0 || cantidadTerciaria < 0 || cantidadCuaternaria < 0) {
				JOptionPane.showMessageDialog(null, "No se pueden introducir números negativos.");
				return null;
			}
			return Arrays.asList(cantidadPrimaria, cantidadSecundaria, cantidadTerciaria, cantidadCuaternaria);

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "No puedes dejar ningún hueco vacio, si no quieres simular algún típo escribe '0'.");
			return null;
		}
	}

	/**
     * Controla la simulación multiproceso. Crea un nuevo proceso para cada
     * simulación, según las cantidades de proteínas indicadas.
     * 
     * @param cantidades Es una lista de números enteros, esta representa la cantidad de proteínas de cada tipo, 
     * esta lista viene de controlInputCantidadProteinas().
     */
	
	public void controlSimulacionMultiproceso(List<Integer> cantidades) {

		vista.getTextArea_MP().setText("");

		String clase = "florida.AppProteinas.SimulacionMP";
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");

		long InicioTotalSim = System.currentTimeMillis();

		int cantidadPrimarias = cantidades.get(0);
		int cantidadSecundarias = cantidades.get(1);
		int cantidadTerciarias = cantidades.get(2);
		int cantidadCuaternarias = cantidades.get(3);

		try {
			for (int tipo = 0; tipo < cantidades.size(); tipo++) {
				int cantidad = cantidades.get(tipo);

				if (cantidad == 0) {
					continue;
				}
				for (int i = 0; i < cantidad; i++) {
					int numeroSimulacion = contadorSimulacionesMP();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SS");
					String tiempoInicioSim = LocalDateTime.now().format(formatter);
					String nombreFichero =  nombreFicheros("MP", (tipo+1), numeroSimulacion, tiempoInicioSim, "");

					List<String> command = new ArrayList<>();

					command.add(javaBin);
					command.add("-cp");
					command.add(classPath);
					command.add(clase);
					command.add(String.valueOf(tipo + 1));
					command.add(nombreFichero);

					ProcessBuilder pb = new ProcessBuilder(command);
					Process process = pb.start();

					int exitCode = process.waitFor();
					if (exitCode != 0) {
						System.err.println("Error: " + exitCode);
					}
					InputStreamReader isr = new InputStreamReader(process.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String linea;
					while ((linea = br.readLine()) != null) {
						vista.getTextArea_MP().append(linea + "\n");
					}
				}
			}
			long tiempoFinTotal = System.currentTimeMillis();
			long duracionTotalMS = tiempoFinTotal - InicioTotalSim;
			String duracionTotalCent = String.valueOf(duracionTotalMS / 10);

			vista.getTextArea_MP().append(mostrarVista(cantidadPrimarias, cantidadSecundarias, cantidadTerciarias,
					cantidadCuaternarias, duracionTotalCent));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al ejecutar la simulación Multiproceso.");
		}
	}
	
	/**
     * Controla la simulación multihilo. Crea y gestiona una pool de hilos que como máximo
     * serán la cantidad de nucleos que tenga el procesador..
     * 
     * @param cantidades Es una lista de números enteros, esta representa la cantidad de proteínas de cada tipo, 
     * esta lista viene de controlInputCantidadProteinas().
     * 
     * @throws InterruptedException Captura el error si ocurre una interrupción durante la espera de los hilos.
     */
	public void controlSimulacionMultihilo(List<Integer> cantidades) throws InterruptedException {
		int maxHilos = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = Executors.newFixedThreadPool(maxHilos);

		long inicioTotalSim = System.currentTimeMillis();

		for (int tipo = 1; tipo <= cantidades.size(); tipo++) {
			int cantidad = cantidades.get(tipo - 1);
			if (cantidad > 0) {
				SimulacionMT simulacion = new SimulacionMT(tipo, cantidad);
				executorService.submit(simulacion);
			}
		}
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		long finTotalSim = System.currentTimeMillis();
		long duracionSimMs = finTotalSim - inicioTotalSim;
		String duracion = String.valueOf(duracionSimMs / 10);

		vista.getTextArea_MT().setText(
				mostrarVista(cantidades.get(0), cantidades.get(1), cantidades.get(2), cantidades.get(3), duracion));
	}

	/**
     * Crea una String que muestra por la vista la cantidad de cada tipo de proteína y el tiempo total que se va a simular.
     * 
     * @param cantidadPrimarias   Cantidad de proteínas primarias simuladas.
     * @param cantidadSecundarias Cantidad de proteínas secundarias simuladas.
     * @param cantidadTerciarias  Cantidad de proteínas terciarias simuladas.
     * @param cantidadCuaternarias Cantidad de proteínas cuaternarias simuladas.
     * @param duracion            Duración total de la simulación en centésimas.
     * @return Una String que servira como plantilla para mostrar por la vista tanto la simulacion MP como MT..
     */
	public String mostrarVista(int cantidadPrimarias, int cantidadSecundarias, int cantidadTerciarias,
			int cantidadCuaternarias, String duracion) {
		return ("Cantidad de proteínas simuladas: \n" + "Primarias: " + cantidadPrimarias + "\n" + "Secundarias: "
				+ cantidadSecundarias + "\n" + "Terciarias: " + cantidadTerciarias + "\n" + "Cuaternarias: "
				+ cantidadCuaternarias + "\n" + "\n" + "Tiempo total de simulación: " + duracion + " centesimas");
	}
	
	/**
	 * Escribe los resultados de una simulación en un archivo.
	 *
	 * @param nombreFichero     Nombre del archivo que se crea y donde se guardarán los resultados.
	 * @param tiempoInicioProte Tiempo de inicio de la simulación en formato "yyyyMMdd_HHmmss".
	 * @param tiempoFinSimProte Tiempo de finalización de la simulación en formato "yyyyMMdd_HHmmss".
	 * @param centesimasIni     Centésimas de segundo correspondientes al inicio de la simulación.
	 * @param centesimasFin     Centésimas de segundo correspondientes al final de la simulación.
	 * @param duracion          Duración de la simulación en el formato "segundos_centésimas".
	 * @param resultado         Resultado calculado de la simulación (double).
	 */
	public static void escribirFichero(String nombreFichero, String tiempoInicioProte, String tiempoFinSimProte,
			String centesimasIni, String centesimasFin, String duracion, double resultado) {
		File file = new File(nombreFichero);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

			bw.write(tiempoInicioProte + "_" + centesimasIni + "\n");
			bw.write(tiempoFinSimProte + "_" + centesimasFin + "\n");
			bw.write(duracion + "\n");
			bw.write(String.valueOf(resultado));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Calcula la duración de una simulación en segundos y centésimas, a partir de los tiempos de inicio y fin.
	 *
	 * @param inicio Marca de tiempo en milisegundos al inicio de la simulación.
	 * @param fin    Marca de tiempo en milisegundos al final de la simulación.
	 * @return La duración de la simulación en el formato "segundos_centésimas".
	 */
	public static String calcularDuracionSimu(long inicio, long fin) {
		long duracionSeg = 0;
		long duracionCent = 0;
		try {
			long duracionMS = fin - inicio;
			duracionSeg = duracionMS / 1000;
			duracionCent = (duracionMS % 1000) / 10;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return String.format("%d_%02d", duracionSeg, duracionCent);
	}
	
	/**
	 * Genera el nombre de un archivo de simulación basado en los parámetros indicados.
	 * El nombre del archivo sigue el formato:
	 * "PROT_{tipoSimulacion}_{tipoProteina}_n{numeroSimulaciones}_{tiempoInicioSim}_{duracionCentesimas}.sim"
	 *
	 * @param tipoSimulacion       Tipo de simulación (por ejemplo, "MT" para multihilo o "MP" para multiproceso).
	 * @param tipoProteina         Tipo de proteína simulada (un valor numérico que identifica el tipo de proteína).
	 * @param numeroSimulaciones   Número de simulación actual dentro del total solicitado (usado para identificar cada simulación).
	 * @param tiempoInicioSim      Tiempo de inicio de la simulación en formato "yyyyMMdd_HHmmss" (para facilitar ordenamiento y registro).
	 * @param duracionCentesimas   Duración de la simulación en centésimas de segundo, calculada al finalizar la simulación.
	 * @return                      El nombre completo del archivo de simulación, incluyendo extensión ".sim".
	 */
	public static String nombreFicheros(String tipoSimulacion, int tipoProteina, int numeroSimulaciones,
			String tiempoInicioSim, String duracionCentesimas) {

		return "PROT_" + tipoSimulacion + "_" + tipoProteina + "_n" + numeroSimulaciones + "_" + tiempoInicioSim + "_"
				+ duracionCentesimas + ".sim";
	}

	/**
     * Es el punto de entrada principal para ejecutar la aplicación Simulador.
     * 
     * @param args Los argumentos no se usan.
     */
	public static void main(String[] args) {
		Vista vista = new Vista();
		Simulador simulador = new Simulador(vista);
	}
}
