package florida.acceso_a_datos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio06 {

	public static void lanzadorEjer01(Integer num1, Integer num2) {
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
			command.add(num1.toString());
			command.add(num2.toString());
			
			System.out.println("Comando que se le pasa al ProcessBuilder: " + command);
			System.out.println("Comando que se uede ejecutar en el cmd: " + command.toString().replace(",", ""));
			
			ProcessBuilder pb = new ProcessBuilder(command);
			Process process = pb.inheritIO().start();
			process.waitFor();
			System.out.println(process.exitValue());
			
		}catch(Exception e) {
			e.getStackTrace();
		}
	}

	public static void main(String[] args) {
		lanzadorEjer01(4, 52);
		System.out.println("Lanzadores Finalizados.");
	}


}
