package psp.ejerciciost1.e03;

import java.util.Scanner;

//Realiza un programa que lea dos números desde teclado y muestre la suma por pantalla.

public class Ejercicio3 {

	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);

		System.out.print("Indique el primer número que se va a sumar --> ");
		String pedirNumero1 = pedirNumero.nextLine();
		int numero1 = Integer.parseInt(pedirNumero1); // Transforma el tipo de dato que entra por teclado que por
														// defecto es string a int.

		// System.out.println("numero1 es de tipo " +
		// ((Object)numero1).getClass().getSimpleName());

		System.out.print("Indique el segundo número --> ");
		String pedirNumero2 = pedirNumero.nextLine();
		int numero2 = Integer.parseInt(pedirNumero2);

		// System.out.println("numero2 es de tipo " +
		// ((Object)numero2).getClass().getSimpleName()); Corroboro que ha cambiado el
		// tipo de dato.

		int resultadoSuma = Suma(numero1, numero2);

		pedirNumero.close();

		System.out.print("La suma de los dos números es --> " + resultadoSuma);
	}

	static int Suma(int numero1, int numero2) {

		int resultado = numero1 + numero2;
		return resultado;
	}

}
