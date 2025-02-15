package psp.ejerciciost1.e36;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Ejercicio36 {

	public static void main(String[] args) {

		List<Integer> numeros = new ArrayList<>();

		Scanner pedirNumeros = new Scanner(System.in);
		int suma = 0;

		System.out.println("Introduce 5 números enteros.");
		for (int i = 0; i < 5; i++) {
			System.out.print("Numero " + (i + 1) + ":");
			int num = pedirNumeros.nextInt();

			numeros.add(num);
			suma += num;
		}
		System.out.println("Esta sería la suma de los numeros que has indicado -> " + suma);

		Collections.reverse(numeros); // Invierte la lista.

		System.out.print("Así quedaria la lista invertida -> ");
		for (Integer numero : numeros) {
			System.out.print(numero + " ");
		}

		pedirNumeros.close();
	}

}
