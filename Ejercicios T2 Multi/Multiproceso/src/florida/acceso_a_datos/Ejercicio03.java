package florida.acceso_a_datos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio03 {
	public static void main(String[] args) {
		int num1 = Integer.parseInt(args[0]);
		int num2 = Integer.parseInt(args[1]);
		String nombreFichero = args[2];
		
		String clase = "florida.acceso_a_datos.Ejercicio01"; 
		
		try {
			String javaHome = System.getProperty("java.home"); 
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java"; 
			String classpath = "C:\\Users\\vtorg\\OneDrive\\Escritorio\\FLORIDA\\Acceso a Datos\\Ejercicios T2 Multi\\Multiproceso\\bin";
			System.out.println(classpath);
			String className = clase; 

			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classpath);
			command.add(className);
			command.add(String.valueOf(num1));
			command.add(String.valueOf(num2));

			System.out.println("Comando que se le pasa al ProcessBuilder: " + command);
			System.out.println("Comando que se uede ejecutar en el cmd: " + command.toString().replace(",", ""));

			ProcessBuilder pb = new ProcessBuilder(command);
			Process process = pb.redirectOutput(new File(nombreFichero + ".txt")).start();
			
			process.waitFor();
			System.out.println(process.exitValue());

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
