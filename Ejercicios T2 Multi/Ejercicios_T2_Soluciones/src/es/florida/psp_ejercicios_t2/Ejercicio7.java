package es.florida.psp_ejercicios_t2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio7 {
	
	//7.	Crear otra ampliaci�n del programa 2 para que redirija la salida de la ejecuci�n 
	//		del programa 1 a su flujo de ejecuci�n y lo escriba en un fichero (pista: utilizar redirectOutput).

	public static void main(String[] args) {
		
		int numero1 = 1;
		int numero2 = 10;
		String nombreFichero = "resultado_ej7.txt";
		File fichero = new File(nombreFichero);
		
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
		builder.redirectOutput(fichero);
		try {
			builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
