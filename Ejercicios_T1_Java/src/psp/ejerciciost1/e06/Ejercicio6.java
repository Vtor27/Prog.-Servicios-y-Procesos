package psp.ejerciciost1.e06;

import java.util.Scanner;

//Realiza un programa que lea cinco números desde teclado y muestre la suma por pantalla.
//Sugerencia: utilizar una estructura de bucle.

public class Ejercicio6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner pedirNumero = new Scanner(System.in);
		int suma = 0;

		System.out.println("Introduce 5 números que quieras sumar.");

		for (int i = 0; i < 5; i++) {
			System.out.print(i + 1 + ".Introduce un número.");

			int pedirNum = pedirNumero.nextInt();
			suma = suma + pedirNum;
		}

		pedirNumero.close();
		System.out.println("La suma es: " + suma);
	}

}
