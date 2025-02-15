package psp.ejerciciost1.e08;

import java.util.Scanner;

public class Ejercicio8 {

	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);

		System.out.println("Di un número del 1-12 para saber a que mes equivale.");
		int pedirNum = pedirNumero.nextInt();

		if (pedirNum == 1) {
			System.out.println("Enero");

		} else if (pedirNum == 2) {
			System.out.println("Febrero");

		} else if (pedirNum == 3) {
			System.out.println("Marzo");

		} else if (pedirNum == 4) {
			System.out.println("Abril");

		} else if (pedirNum == 5) {
			System.out.println("Mayo");

		} else if (pedirNum == 6) {
			System.out.println("Junio");

		} else if (pedirNum == 7) {
			System.out.println("Julio");

		} else if (pedirNum == 8) {
			System.out.println("Agosto");

		} else if (pedirNum == 9) {
			System.out.println("Septiembre");

		} else if (pedirNum == 10) {
			System.out.println("Octubre");

		} else if (pedirNum == 11) {
			System.out.println("Noviembre");

		} else if (pedirNum == 12) {
			System.out.println("Diciembre");
		}

		pedirNumero.close();
	}
}
