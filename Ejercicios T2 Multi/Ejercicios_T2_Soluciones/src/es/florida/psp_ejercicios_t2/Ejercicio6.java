package es.florida.psp_ejercicios_t2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio6 {
	
	//6.	Crea una ampliación del programa 2 para que redirija la salida de la ejecución 
	//		del programa 1 a su flujo de ejecución y lo muestre por consola (pista: utilizar inheritIO).

	public static void main(String[] args) {
		
		int numero1 = 1;
		int numero2 = 10;
		
		String clase = "es.florida.psp_ejercicios_t2.Ejercicio1";
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = clase;
		ArrayList<String> command = new ArrayList<>();
		command.add(javaBin);
		command.add("-cp");
		command.add(classpath);
		command.add(className);
		command.add(String.valueOf(numero1));
		command.add(String.valueOf(numero2));
		System.out.println("Comando que se pasa a ProcessBuilder: " + command);
		System.out.println("Comando a ejecutar en cmd.exe: " + command.toString().replace(",",""));
		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			Process process = builder.inheritIO().start();
			process.waitFor();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
}
