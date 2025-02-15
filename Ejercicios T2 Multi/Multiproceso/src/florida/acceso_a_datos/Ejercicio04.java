package florida.acceso_a_datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio04 {
	public static void lanzadorIntervalo(Integer num1, Integer num2) {

		String clase = "florida.acceso_a_datos.Ejercicio01";
		File nombreFichero = new File("intervalo.txt");

		try {
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classPath = System.getProperty("java.class.path");

			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classPath);
			command.add(clase);
			command.add(num1.toString());
			command.add(num2.toString());

//			System.out.println("Comando que se le pasa al ProcessBuilder: " + command);
//			System.out.println("Comando que se uede ejecutar en el cmd: " + command.toString().replace(",", ""));

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectOutput(nombreFichero);
			Process process = pb.start();

			process.waitFor();
			System.out.println(process.exitValue());

		} catch (Exception e) {
			e.getStackTrace();
		}

		leerFichero(nombreFichero);
	}

	public static void leerFichero(File fichero) {
		if (!fichero.exists() || fichero.length() == 0) {
			System.out.println("El fichero no se ha creado o está vacio.");
		} else {
			try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
				String linea;
				while ((linea = br.readLine()) != null) {
					System.out.println(linea);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		File file = new File("intervalos.txt");
		
		leerFichero(file);
	}

}
