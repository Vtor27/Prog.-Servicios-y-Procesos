package florida.AppProteinas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * La clase SimulacionMT realiza la simulación multihilo de las proteínas que se especifíquen, implementando una interfaz Runnable.
 * Cada simulación se ejecuta como un hilo independiente, se guarda en un archivo que contiene su duración y resultado. 
 */
public class SimulacionMT implements Runnable {
	private int tipoProteina;
	private int numeroSimulaciones;
	private static int contadorSimuMT = 1;

	
	/**
     * Constructor de SimulacionMT.
     *
     * @param tipoProteina       Tipo de proteína que se va a simular.
     * @param numeroSimulaciones El número de simulaciones que se realizarán para cada tipo de proteína.
     */
	public SimulacionMT(int tipoProteina, int numeroSimulaciones) {
		this.tipoProteina = tipoProteina;
		this.numeroSimulaciones = numeroSimulaciones;
	}

	
	/**
     * Método que define el comportamiento del hilo cuando se inicia. 
     * Ejecuta una serie de simulaciones del tipo de proteína especificado, 
     * registra los tiempos de inicio y fin, y escribe los resultados en un archivo.
     */
	public void run() {
		for (int i = 0; i < numeroSimulaciones; i++) {
			DateTimeFormatter dateFormatterDate = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

			LocalDateTime inicioSimProte = LocalDateTime.now();
			String tiempoInicioProte = inicioSimProte.format(dateFormatterDate);

			long timeInicio = System.currentTimeMillis();

			double resultado = simulation(tipoProteina);

			long timeFin = System.currentTimeMillis();

			LocalDateTime tiempoFinSimProte = LocalDateTime.now();
			String tiempoFinProte = tiempoFinSimProte.format(dateFormatterDate);

			
			String duracion = Simulador.calcularDuracionSimu(timeInicio, timeFin);

			String centesimasIni = String.format("%02d", (timeInicio % 1000) / 10);
			String centesimasFin = String.format("%02d", (timeFin % 1000) / 10);

			String nombreFichero = Simulador.nombreFicheros("MT", tipoProteina, sumarContadorMT(), tiempoInicioProte,
					centesimasIni);

			Simulador.escribirFichero(nombreFichero, tiempoInicioProte, tiempoFinProte, centesimasIni, centesimasFin, duracion,
					resultado);
		}
	}
	
	/**
     * Incrementa y devuelve el contador de simulaciones.
     * Se utiliza para hacer que el método sea seguro para los subprocesos multihilo.
     * 
     * @return Devuelve el número de simulación aumentandolo en 1 por cada simulación multihilo realizada indistintamente del tipo que sea.
     */
	public synchronized int sumarContadorMT() {
		return contadorSimuMT++;
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
}
