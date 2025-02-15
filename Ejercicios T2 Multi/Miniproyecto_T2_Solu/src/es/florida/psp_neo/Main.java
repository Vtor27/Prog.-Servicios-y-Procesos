package es.florida.psp_neo;

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
	
	public static void lanzarProbabilidad(String nombreNEO, double posicion, double velocidad) throws IOException {
			
		String clase = "es.florida.psp_neo.Probabilidad";
		File fichResultado = new File(nombreNEO);
		try {
			
			String javaHome = System.getProperty("java.home");
		    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		    String classpath = System.getProperty("java.class.path");
		    String className = clase;

		    List<String> command = new ArrayList<>();
		    command.add(javaBin);
		    command.add("-cp");
		    command.add(classpath);
		    command.add(className);
		    command.add(String.valueOf(posicion));
		    command.add(String.valueOf(velocidad));
		    
		    ProcessBuilder builder = new ProcessBuilder(command);
		    builder.redirectOutput(fichResultado);
			builder.start();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public static double getResultadoFichero(String nombreFichero) {
		
		double probabilidad = 0;
		try {
			FileInputStream fichero = new FileInputStream(nombreFichero);
			InputStreamReader fir = new InputStreamReader(fichero);
			BufferedReader br = new BufferedReader(fir);
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
			arrayProbabilidades[i] = getResultadoFichero(arrayNombres[i]);
		}
		return arrayProbabilidades;

	}

	public static void main(String[] args) throws IOException,InterruptedException {
		
		long tiempoInicio = System.nanoTime();

		int cores = Runtime.getRuntime().availableProcessors();
		
		File archivoDatos = new File("NEOs.txt");
		FileReader fr = new FileReader(archivoDatos);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();
		
		//Controlar cuantas lineas hay y como quedarian los bloques de analisis de NEOs
		int numeroTotalNEOs = 0;
		while (linea != null) {
			numeroTotalNEOs++;
			linea = br.readLine();
		}
		br.close();
		int bloquesCompletos = numeroTotalNEOs / cores;
		int tamanyoUltimoBloque = numeroTotalNEOs % cores;
		int[] arrayTamanyoBloques = new int[bloquesCompletos + 1];
		for (int i = 0; i < bloquesCompletos; i++) {
			arrayTamanyoBloques[i] = cores;
		}
		arrayTamanyoBloques[arrayTamanyoBloques.length - 1] = tamanyoUltimoBloque;
		
		System.out.println("Cores de procesador disponibles: " + cores);
		System.out.println("No. de NEOs a calcular: " + numeroTotalNEOs);
		System.out.println(" -> El proceso se realizara en " + arrayTamanyoBloques.length + " bloques");
		for (int i = 0; i < arrayTamanyoBloques.length; i++) {
			System.out.println(" -> Bloque " + (i + 1) + ": se analizaran " + arrayTamanyoBloques[i] + " NEOs.");
		}
		
		fr = new FileReader(archivoDatos);
		br = new BufferedReader(fr);  //Reiniciar buffer de lectura
		linea = br.readLine();
		
		for (int j = 0; j < arrayTamanyoBloques.length; j++) {
			int tamanyoBloqueActual = arrayTamanyoBloques[j];
			String[] arrayNombres = new String[tamanyoBloqueActual];
			double[] arrayPosiciones = new double[tamanyoBloqueActual];
			double[] arrayVelocidades = new double[tamanyoBloqueActual];
			double[] arrayProbabilidades = new double[tamanyoBloqueActual];
			
			System.out.println("\nAnalisis del bloque " + (j + 1) + " de " + arrayTamanyoBloques.length);
			for (int i = 0; i < tamanyoBloqueActual; i++) {
				String[] elementosLinea = linea.split(",");
				arrayNombres[i] = elementosLinea[0];
				arrayPosiciones[i] = Double.parseDouble(elementosLinea[1]);
				arrayVelocidades[i] = Double.parseDouble(elementosLinea[2]);
				lanzarProbabilidad(arrayNombres[i], arrayPosiciones[i], arrayVelocidades[i]);
				System.out.println("Analisis " + (i + 1) + " lanzado... (NEO: " + arrayNombres[i] + ")");
				linea = br.readLine();
			}
			
			boolean comprobarFin = false;
			while (!comprobarFin) {
				try {
					arrayProbabilidades = getBloqueAnalisis(arrayNombres);
					comprobarFin = true;
				} catch (Exception e) {
					//Nada
				}
			}
			
			for (int i = 0; i < tamanyoBloqueActual; i++) {
				if (arrayProbabilidades[i] > 10) {
					System.err.println("ALERTA!!! El NEO " + arrayNombres[i] + " tiene una probilidad de colision del " + String.format("%.2f", arrayProbabilidades[i]) + " %");
				} else {
					System.out.println("El NEO "+ arrayNombres[i] + " solo tiene una probilidad de colision del " + String.format("%.2f", arrayProbabilidades[i]) + " %");
				}
			}
			
			Thread.sleep(100);
			
		}
		
		br.close();
		fr.close();
		
		
		long tiempoFin = System.nanoTime();
		long duracion = (tiempoFin - tiempoInicio)/1000000;  //milisegundos
		Thread.sleep(100); //Pausa para mostrar mensaje final
		System.out.println("\nFIN - Tiempo ejecucion total: " + duracion + " ms");
		System.out.println("    - Tiempo medio de ejecucion por NEO: " + duracion/numeroTotalNEOs + " ms");
		
	}


}

