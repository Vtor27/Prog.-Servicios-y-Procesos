package psp.ejerciciost1.e32;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ejercicio32B {

	public static void main(String[] args) {

		List<String> nombres = new ArrayList<>();

		System.out.println("Introduce el nombre de 6 compañeros de clase.");
		Scanner pedirNombres = new Scanner(System.in);

		for (int i = 0; i < 6; i++) {
			System.out.print("Introduce el nombre: " + (i + 1) + ".");
			String nombre = pedirNombres.next();

			nombres.add(nombre);
		}

		System.out.print("Los nombres introducidos son: ");

		for (String compañero : nombres) {

			System.out.print(compañero + " ");
		}

		pedirNombres.close();
	}

}
