package florida.acceso_a_datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio04OtraForma {
    public static void main(String[] args) {

        int num1 = 1;
        int num2 = 50;
        String nombreFichero = "intervalos.txt";
        
        String clase = "florida.acceso_a_datos.Ejercicio03";
        String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");

		List<String> command = new ArrayList<>();
		command.add(javaBin);
		command.add("-cp");
		command.add(classPath);
		command.add(clase);
		command.add(String.valueOf(num1));
		command.add(String.valueOf(num2));
		command.add(nombreFichero);

		ProcessBuilder pb = new ProcessBuilder(command);
		try {
			Process p = pb.start();
			p.waitFor();
		}catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
		

		boolean ficheroLeido = false;
		while (!ficheroLeido) {
		    File fichero = new File(nombreFichero);
		    FileReader fr;
		    try{
		    	fr = new FileReader(fichero);
		    	BufferedReader br = new BufferedReader(fr);
		        String resultado;
		        int contador = 0;
		        while ((resultado = br.readLine()) != null) {
		        contador++;
		        System.out.println("[" + contador + "]Lectura del fichero: " + resultado);
		        }
		        ficheroLeido = true;
		        fr.close();
		        br.close();
		    } catch(FileNotFoundException e) {
		    	e.printStackTrace();
		    	System.out.println("No se encuentra el fichero.");
		    	return;
		    }	catch (IOException e) {
		        e.printStackTrace();
		    }
		}
    }
}
