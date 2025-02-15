package psp.ejerciciost1.e32;

import java.util.Scanner;

public class Ejercicio32 {

	public static void main(String[] args) {

		String[] nombres = new String[6];

		System.out.println("Introduce el nombre de 6 compañeros de clase.");
		Scanner pedirNombres = new Scanner(System.in);

		for (int i = 0; i < 6; i++) {
			String nombre = pedirNombres.next();

			nombres[i] = nombre;
		}

		System.out.print("Los nombres introducidos son: ");
		for (int i = 0; i < nombres.length; i++) {
			System.out.print(nombres[i] + " ");
		}

		pedirNombres.close();
	}

}
