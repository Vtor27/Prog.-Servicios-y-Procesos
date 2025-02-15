package psp.ejerciciost1.e07;

import java.util.Scanner;

//Resolver el ejercicio anterior empleando otra estructura de bucle.

public class Ejercicio7 {

	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);
		int suma = 0;

		System.out.println("Introduce 5 números que quieras sumar.");
		int i = 0;

		while (i != 5) {
			System.out.print(i + 1 + ".Introduce un número.");

			int pedirNum = pedirNumero.nextInt();
			suma = suma + pedirNum;
			i++;
		}

		pedirNumero.close();

		System.out.println("La suma es: " + suma);
	}
}
