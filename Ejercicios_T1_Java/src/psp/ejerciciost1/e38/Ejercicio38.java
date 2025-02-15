package psp.ejerciciost1.e38;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ejercicio38 {
	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);

		List<Integer> listaNumeros = new ArrayList<>();

		System.out.print("Indica un número que será el principio del intervalo. --> ");
		int primerNum = pedirNumero.nextInt();

		System.out.print("Indica el segundo número que será el final. --> ");
		int segundoNum = pedirNumero.nextInt();

		long startTime = System.nanoTime(); // Inicia la medición del tiempo.

		for (int i = primerNum; i <= segundoNum; i++) { // Inserta los números en la lista empezando por el primer
														// número que diga y acaba en el segundo que he introducido.
			listaNumeros.add(i);
		}

		for (Integer integer : listaNumeros) {
			if (integer <= 1) {

				System.out.println(integer + " No");
			} else {
				boolean esPrimo = true;

				for (int i = 2; i <= Math.sqrt(integer); i++) { // Si no se encuentra ningún divisor hasta la raíz
																// cuadrada, no se encontrará en los números mayores,
																// cambiará "esPrimo" a false y pasa al siguiente.
					if (integer % i == 0) {
						esPrimo = false; // Sacado de StackOverflow.
						break;
					}
				}
				if (esPrimo) {
					System.out.println(integer + " Si");
				} else {
					System.out.println(integer + " No");
				}
			}
		}
		long endTime = System.nanoTime() - startTime; // Muestra el tiempo total en nanosegundos(Mas preciso que los
														// segundos.).

		System.out.println("El tiempo que tarda en crear el intervalo es: " + endTime + "ns");
		pedirNumero.close();
	}
}
