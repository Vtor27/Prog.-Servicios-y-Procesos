package psp.ejerciciost1.e23;

import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio23 {

	public static void main(String[] args) {

		ArrayList<String> listaNombres = new ArrayList<String>();

		Scanner pedirNombres = new Scanner(System.in);
		boolean stop = true;
		String nombre;
		int key = 1;

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

			System.out.println(key + "." + personas);
			key++;
		}
		pedirNombres.close();
	}
}