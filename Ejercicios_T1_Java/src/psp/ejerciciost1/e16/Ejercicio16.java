package psp.ejerciciost1.e16;

import java.util.Scanner;

public class Ejercicio16 {

	public static void main(String[] args) {

		int dia = 0;
		int mes = 0;
		int año = 0;
		int numSuerte = 0;

		System.out.println("Si quieres saber cual es tu número de la suerte indica tu fecha de nacimiento. -->");
		Scanner pedirFecha = new Scanner(System.in);

		System.out.print("Indica el día -->");
		dia = pedirFecha.nextInt();
		System.out.print("Indica el mes -->");
		mes = pedirFecha.nextInt();
		System.out.print("Indica el año -->");
		año = pedirFecha.nextInt();

		int sumaFecha = (dia + mes + año);

		while (sumaFecha > 0) {

			numSuerte += sumaFecha % 10; // Extrae y suma el último dígito de sumaFecha.
			sumaFecha /= 10; // Esto va eliminando en cada iteración el número que se ha utilizado para
								// numSuerte.
		}

		pedirFecha.close();

		System.out.println("Según esta fecha -> " + dia + "/" + mes + "/" + año + '\n' + "Tu número de la suerte es ->"
				+ numSuerte);
	}

}
