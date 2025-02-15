package psp.ejerciciost1.e05;

import java.util.Scanner;

//Realiza un programa que lea dos números desde teclado en un bucle repetitivo. El bucle
//deberá finalizar cuando los números leídos sean iguales.

public class Ejercicio5 {

	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);
		int pedirNumero1;
		int pedirNumero2;

		do {

			System.out.print("Indique el primer número --> ");
			pedirNumero1 = pedirNumero.nextInt();

			System.out.print("Indique el segundo número --> ");
			pedirNumero2 = pedirNumero.nextInt();

			if (pedirNumero1 != pedirNumero2) {
				System.out.println("Los números son distintos vuelve a introducirlos.");
			} else {
				System.out.println("Los dos son iguales, sales del bucle.");
			}

		} while (pedirNumero1 != pedirNumero2);

		pedirNumero.close();
	}

}
