package psp.ejerciciost1.e13;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Ejercicio13 {

	public static void main(String[] args) {

		Scanner pedirGrados = new Scanner(System.in);
		DecimalFormat df = new DecimalFormat("#.0"); // La cantidad de "0" despues de #. serán los decimales a los que
														// se formatea el número/variable que indiques.

		System.out.println(
				"Introduce la temperatura en grados Celsius para saber a cuanto equivale en la escala Farenheit. -->");

		double gradosC = pedirGrados.nextDouble();

		double gradosF = (gradosC * 9 / 5) + 32;

		pedirGrados.close();

		System.out.println(gradosC + "ºC equivalen a " + df.format(gradosF) + "ºF");
	}

}
