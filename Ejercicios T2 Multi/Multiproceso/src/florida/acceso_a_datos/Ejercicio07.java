package florida.acceso_a_datos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio07 {

	public static void main(String[] args) {

		int num1 = 1;
		int num2 = 50;
		String nombreFichero = "intervalos_Ejer07.txt";
		File fichero = new File(nombreFichero);

		String clase = "florida.acceso_a_datos.Ejercicio01";
		try {
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classpath = System.getProperty("java.class.path");

			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classpath);
			command.add(clase);
			command.add(String.valueOf(num1));
			command.add(String.valueOf(num2));

			System.out.println("Comando que se le pasa al ProcessBuilder: " + command);
			System.out.println("Comando que se uede ejecutar en el cmd: " + command.toString().replace(",", ""));

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectOutput(fichero);

			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
