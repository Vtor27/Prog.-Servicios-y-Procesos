package psp.ejerciciost1.e04;

import java.util.Scanner;

//Programa que lea dos números desde teclado y muestre el menor y el mayor en pantalla. Si
//son iguales deberá mostrar un mensaje indicándolo.

public class Ejercicio4 {

	public static void main(String[] args) {
		Scanner pedirNumero = new Scanner(System.in);

		System.out.print("Introduce el primer número a comparar. --> ");
		int numero1 = pedirNumero.nextInt();

		System.out.print("Introduce el segundo. --> ");
		int numero2 = pedirNumero.nextInt();

		if (numero1 == numero2) {
			System.out.println("Los dos números son iguales.");
		} else if (numero1 < numero2) {
			System.out.println("El segundo número introducido es mayor.");
		} else {
			System.out.println("El primer introducido es mayor.");
		}
		pedirNumero.close();
	}
}
