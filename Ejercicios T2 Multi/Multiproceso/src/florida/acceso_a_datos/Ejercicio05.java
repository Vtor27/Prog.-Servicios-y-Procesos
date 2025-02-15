package florida.acceso_a_datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio05 {
	public static void main(String[] args) {

		int num1 = 1;
		int num2 = 50;
		String nombreFichero1 = "intervalos1.txt";

		int num3 = 51;
		int num4 = 100;
		String nombreFichero2 = "intervalos2.txt";

		// LLAMADA1
		String clase = "florida.acceso_a_datos.Ejercicio03";
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");

		List<String> command1 = new ArrayList<>();
		command1.add(javaBin);
		command1.add("-cp");
		command1.add(classPath);
		command1.add(clase);
		command1.add(String.valueOf(num1));
		command1.add(String.valueOf(num2));
		command1.add(nombreFichero1);

		ProcessBuilder pb1 = new ProcessBuilder(command1);
		try {
			pb1.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// LLAMADA 2
		List<String> command2 = new ArrayList<>();
		command2.add(javaBin);
		command2.add("-cp");
		command2.add(classPath);
		command2.add(clase);
		command2.add(String.valueOf(num3));
		command2.add(String.valueOf(num4));
		command2.add(nombreFichero2);

		ProcessBuilder pb2 = new ProcessBuilder(command2);
		try {
			Process p2 = pb2.start();
			p2.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		boolean ficheroLeido = false;

		while (!ficheroLeido) {
			File fichero1 = new File(nombreFichero1);
			File fichero2 = new File(nombreFichero2);
			FileReader fr1;
			FileReader fr2;
			try {
				fr1 = new FileReader(fichero1);
				BufferedReader br1 = new BufferedReader(fr1);
				String resultado1 = br1.readLine();
				fr1.close();
				br1.close();
				
				fr2 = new FileReader(fichero2);
				BufferedReader br2 = new BufferedReader(fr2);
				String resultado2 = br2.readLine();
				fr2.close();
				br2.close();
				
				System.out.println("Resultado del fichero 1 --> " + resultado1);
				System.out.println("Resultado del fichero 2 --> " + resultado2);
				
				ficheroLeido = true;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

	}

}