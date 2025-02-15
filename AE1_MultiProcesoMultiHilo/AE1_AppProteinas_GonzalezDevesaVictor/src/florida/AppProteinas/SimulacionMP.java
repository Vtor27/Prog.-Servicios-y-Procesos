package florida.AppProteinas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * La clase SimulacionMP realiza la simulación multiproceso de las proteínas que se especifíquen.
 * El resultado de cada simulación se guarda en un archivo que contiene su duracion y resultado. 
 */
public class SimulacionMP {

	/**
	 * Realiza una simulación de una proteína de un tipo específico y guarda los resultados en un archivo.
	 * 
	 * Simula el procesamiento de una proteína, midiendo su duración total y registrando el tiempo de inicio y final
	 * así como el resultado que luego se escribe en el archivo creado.
	 *
	 * @param tipo          Tipo de proteína a simular.
	 * @param nombreFichero Nombre del archivo donde se guardarán los resultados de la simulación.
	 * @throws IOException  Captura el error si ocurre una interrupción.
	 */
	public static void realizarSimulacion(int tipo, String nombreFichero) throws IOException {
		DateTimeFormatter dateFormatterDate = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

		LocalDateTime inicioSimProte = LocalDateTime.now();
		String tiempoInicioProte = inicioSimProte.format(dateFormatterDate);

		long timeInicio = System.currentTimeMillis();

		double resultado = simulation(tipo);

		long timeFin = System.currentTimeMillis();

		LocalDateTime tiempoFinSimProte = LocalDateTime.now();
		String tiempoFinProte = tiempoFinSimProte.format(dateFormatterDate);

		
		String centesimasTimeIni = String.format("%02d", (timeInicio % 1000) / 10);
		String centesimasTimeFin = String.format("%02d", (timeFin % 1000) / 10);
		
		String duracion = Simulador.calcularDuracionSimu(timeInicio, timeFin);
		Simulador.escribirFichero(nombreFichero, tiempoInicioProte, tiempoFinProte, centesimasTimeIni, centesimasTimeFin, duracion, resultado);
	}

	/**
	 * Simula un cálculo de la proteína mediante unos cálculos matemáticos basado en el tipo de proteína.
	 * Este método se utiliza para aumentar el tiempo de cálculo de cada proceso.
	 *
	 * @param  type El tipo de proteína a simular, que afecta la duración de la simulación.
	 * @return calc Devuelve un número de tipo double como resultado de los cálculos.
	 */
	public static double simulation(int type) {
		double calc = 0.0;
		double simulationTime = Math.pow(5, type);
		double startTime = System.currentTimeMillis();
		double endTime = startTime + simulationTime;
		while (System.currentTimeMillis() < endTime) {
			calc = Math.sin(Math.pow(Math.random(), 2));
		}
		return calc;
	}
	
	/**
	 * Punto de entrada principal para ejecutar una simulación de proteína.
	 * Este método se inicia desde la clase 'Simulador' y espera recibir como argumentos los valores que se indiquen,
	 * tipo y el nombre del fichero.
	 * 
	 * @param args Argumentos que recibe.
	 *             - 'args[0]': Tipo de proteína a simular, debe ser un número entero que se convierte a int.
	 *             - 'args[1]': Nombre del archivo donde se guardarán los resultados de la simulación.
	 * @throws IOException Si ocurre un error durante la ejecución de 'realizarSimulacion'.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Debe haber como mínimo un tipo de proteina que simular.");
			return;
		}
		int tipoProteina = Integer.parseInt(args[0]);
		String nombreFichero = args[1];

		realizarSimulacion(tipoProteina, nombreFichero);

	}
}