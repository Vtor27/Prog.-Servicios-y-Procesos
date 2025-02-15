package psp.ejerciciost1.e02;

import java.util.Scanner;

//Programa Java que lea un nombre desde teclado y muestre por pantalla un mensaje tipo
//“Hola xxxxx”

public class Ejercicio2 {

	public static void main(String[] args) {

		Scanner pedirNombre = new Scanner(System.in);

		System.out.print("Introduzca su nombre. --> ");
		String nombre = pedirNombre.nextLine();

		pedirNombre.close();

		System.out.println("Hola " + nombre);
	}

}
