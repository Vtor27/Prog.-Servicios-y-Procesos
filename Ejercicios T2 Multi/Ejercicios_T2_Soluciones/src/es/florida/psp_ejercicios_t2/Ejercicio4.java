package es.florida.psp_ejercicios_t2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio4 {
	
	//4.	Crear una modificación del programa número 2 para que lea el resultado 
	//  	que ha escrito el programa 3 y lo muestre por pantalla. Deberá implementar 
	//		algún procedimiento para controlar que el fichero esté efectivamente escrito y su contenido disponible.

	public static void main(String[] args) {
		
		int numero1 = 1;
		int numero2 = 10;
		String nombreFichero = "resultado.txt";
		
		String clase = "es.florida.psp_ejercicios_t2.Ejercicio3";
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
		command.add(nombreFichero);
		System.out.println("Comando que se pasa a ProcessBuilder: " + command);
		System.out.println("Comando a ejecutar en cmd.exe: " + command.toString().replace(",",""));
		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			Process p = builder.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ficheroLeido = false;
//		while(!ficheroLeido) {
			File fichero = new File(nombreFichero);
			FileReader fr;
			try {
				fr = new FileReader(fichero);
				BufferedReader br = new BufferedReader(fr);
				String resultado = br.readLine();
				System.out.println("Resultado (leído de fichero): " + resultado);
				ficheroLeido = true;
				br.close();
				fr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//System.out.println("Buscando fichero resultado...");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
		
//		}
		
	}
	
}
