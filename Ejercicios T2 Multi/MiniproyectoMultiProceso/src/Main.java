import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

//	public static ArrayList<String> leerFicheroNeos(String fichero) {
//		File file = new File(fichero);
//		ArrayList<String> nombresNeo = new ArrayList<>();
//		ArrayList<Double> distanciasNeo = new ArrayList<>();
//		ArrayList<Double> velocidadesNeo = new ArrayList<>();
//
//		try {
//			FileReader fr = new FileReader(file);
//			BufferedReader br = new BufferedReader(fr);
//
//			String linea;
//			while ((linea = br.readLine()) != null) {
//				String[] datosNeo = linea.split(",");
//
//				nombresNeo.add(datosNeo[0]);
//				distanciasNeo.add(Double.parseDouble(datosNeo[1]));
//				velocidadesNeo.add(Double.parseDouble(datosNeo[2]));
//			}
//			fr.close();
//			br.close();
//			int contador = 1;
//			for (int i = 0; i < nombresNeo.size(); i++) {
//				System.out.println(
//						"[" + contador + "]" + "Nombre: " + nombresNeo.get(i) + " -Distancia relativa a la Tierra: "
//								+ distanciasNeo.get(i) + " -Velocidad: " + velocidadesNeo.get(i));
//				contador++;
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return nombresNeo;
//	}

	public static void lanzarProbabilidad(String nombreNEO, double posicion, double velocidad) throws IOException {

		String clase = "probabilidadDeColision";
		File ficheroResultado = new File(nombreNEO + ".txt");
		try {
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classPath = System.getProperty("java.class.path");

			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classPath);
			command.add(clase);
			command.add(String.valueOf(posicion));
			command.add(String.valueOf(velocidad));

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectOutput(ficheroResultado);
			builder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getResultadoFichero(String nombreFichero) {		//BUSCAR EXPLICACIÓN
		double probabilidad = 0;

		try {
			FileInputStream fichero = new FileInputStream(nombreFichero);
			InputStreamReader isr = new InputStreamReader(fichero);
			BufferedReader br = new BufferedReader(isr);
			String linea = br.readLine();
			probabilidad = Double.parseDouble(linea);
			br.close();
			return probabilidad;

		} catch (FileNotFoundException e) {
			System.out.println("No se pudo abrir " + nombreFichero);
		} catch (IOException e) {
			System.out.println("No hay nada en " + nombreFichero);
		}
		return probabilidad;
	}

	public static double[] getBloqueAnalisis(String[] arrayNombres) {
		double[] arrayProbabilidades = new double[arrayNombres.length];
		for (int i = 0; i < arrayNombres.length; i++) {
			arrayProbabilidades[i] = getResultadoFichero(arrayNombres[i] + ".txt");
		}
		return arrayProbabilidades;
	}

	public static int contarLineasFichero(File file) {
		int lineas = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				lineas++;
			}
			fr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineas;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		long tiempoInicio = System.nanoTime();

//		int cores = Runtime.getRuntime().availableProcessors();
		File ficherNEOs = new File("Neos.txt"); // Tengo 16 nucleos asi que tengo que hacer que el máximo de lineas a
												// leer sean el número de lineas si superan los nucleos que tengo.
		int numeroLineasFichero = contarLineasFichero(ficherNEOs);
		int cores = Math.min(Runtime.getRuntime().availableProcessors(), numeroLineasFichero);

		File archivoDatos = new File("NEOs.txt");
		FileReader fr = new FileReader(archivoDatos);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();

		String[] arrayNombres = new String[cores];
		double[] arrayPosiciones = new double[cores];
		double[] arrayVelocidades = new double[cores];
		double[] arrayProbabilidades = new double[cores];

		for (int i = 0; i < cores; i++) {
			String[] elementosLinea = linea.split(",");
			arrayNombres[i] = elementosLinea[0];
			arrayPosiciones[i] = Double.parseDouble(elementosLinea[1]);
			arrayVelocidades[i] = Double.parseDouble(elementosLinea[2]);
			lanzarProbabilidad(arrayNombres[i], arrayPosiciones[i], arrayVelocidades[i]);
			System.out.println("Analisis " + (i + 1) + "lanzado --> (NEO: " + arrayNombres[i] + ")");
			linea = br.readLine();
		}
		br.close();
		fr.close();

		boolean comprobarFin = false;
		while (!comprobarFin) {
			try {
				arrayProbabilidades = getBloqueAnalisis(arrayNombres);
				comprobarFin = true;
			} catch (Exception e) {
				// No hará nada.
			}
		}

		for (int i = 0; i < cores; i++) {
			if (arrayProbabilidades[i] > 10) {
				System.err.println("ALERTA!! El NEO " + arrayNombres[i] + " tiene una probabilidad de colision del "
						+ String.format("%.2f", arrayProbabilidades[i]) + "%");
			} else {
				System.out.println("El NEO " + arrayNombres[i] + " solo tiene una probabilidad de colisión del "
						+ String.format("%.2f", arrayProbabilidades[i]) + "%");
			}
		}
		long tiempoFin = System.nanoTime();
		long duracion = (tiempoFin - tiempoInicio) / 1000000; // Pasa de nanosegundos a milisegundos.
		System.out.println("FIN - Tiempo de ejecución total: " + duracion + "ms.");
		System.out.println("    - Tiempo de ejecución medio por NEO: " + duracion / cores + "ms.");
	}

}
