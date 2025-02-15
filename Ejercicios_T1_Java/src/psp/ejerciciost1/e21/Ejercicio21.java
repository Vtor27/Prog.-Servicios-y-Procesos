package psp.ejerciciost1.e21;

import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio21 {

	public static void main(String[] args) {
		// ¿Puede ser que quisieras que fuese un array con 5 sitios en vez de un
		// ArrayList?
		ArrayList<String> listaNombres = new ArrayList<String>();

		Scanner pedirNombres = new Scanner(System.in);

		System.out.println("Introduce el nombre de 5 personas.");
		while (listaNombres.size() < 5) {
			String nombres = pedirNombres.next();

			listaNombres.add(nombres);
		}

		pedirNombres.close();

		System.out.println("Los nombres introducidos son: ");
		for (String personas : listaNombres) {

			System.out.println(personas);
		}

	}

}
