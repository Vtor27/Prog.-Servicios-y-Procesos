package florida.acceso_a_datos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio02 {

	public static void main(String[] args) {
		int num1 = Integer.parseInt(args[0]);
		int num2 = Integer.parseInt(args[1]);
		
		String clase = "florida.acceso_a_datos.Ejercicio01";	//Se define la clase a la que se va a llamar(ruta competa del paquete con el java que se va a ejecutar.).
		try {
			String javaHome = System.getProperty("java.home"); //Donde está la aplicación de java en el PC.
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java"; //Donde está el ejcutable bin.(File.separatos equivale a / o  \\).
			String classpath = System.getProperty("java.class.path");
//			System.out.println(classpath);
			String className = clase;	//Todo esto esta son Strings porque se va a concatenar en la lista para pasasrselo como comando para ejecutar.
			
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
			Process process = pb.inheritIO().start();
			process.wait();
			System.out.println(process.exitValue());
			
		}catch(Exception e) {
			e.getStackTrace();
		}
	}
}
	
