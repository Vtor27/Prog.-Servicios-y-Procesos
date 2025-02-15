package psp.ejerciciost1.e22;

import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio22 {

	public static void main(String[] args) {

		ArrayList<String> listaNombres = new ArrayList<String>();

		Scanner pedirNombres = new Scanner(System.in);
		boolean stop = true;
		String nombre;

		System.out.println("Introduce nombres que quieras agregar a la lista.(Escribe 0 para salir.)");
		while (stop == true) {

			nombre = pedirNombres.next();

			if (nombre.equals("0")) {
				System.out.println("Saliendo...");

				stop = false;

			} else {
				listaNombres.add(nombre);
			}
		}
		System.out.println("Los nombres introducidos son: ");
		for (String personas : listaNombres) {

			System.out.println(personas);
		}
		pedirNombres.close();
	}
}
