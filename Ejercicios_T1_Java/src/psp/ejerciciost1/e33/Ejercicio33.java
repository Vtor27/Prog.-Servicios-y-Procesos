package psp.ejerciciost1.e33;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ejercicio33 {

	public static void main(String[] args) {

		Scanner pedirNumero = new Scanner(System.in);

		System.out.println("Introduce un número el cual será el límite para calcular la suma.");
		int parametro = pedirNumero.nextInt();

		sumaNumeros(parametro);

		System.out.println("La suma de todos los números pares hasta el número indicado es: " + sumaNumeros(parametro));
		System.out.println("El número indicado como límites es: " + parametro);
		pedirNumero.close();

	}

	public static int sumaNumeros(int parametro) {

		List<Integer> numeros = new ArrayList<>();
		int suma = 0;

		for (int i = 0; i <= parametro; i++) {

			if (i % 2 == 0) {

				numeros.add(i);
				suma += i;
			}
		}
		return suma;
	}

}
