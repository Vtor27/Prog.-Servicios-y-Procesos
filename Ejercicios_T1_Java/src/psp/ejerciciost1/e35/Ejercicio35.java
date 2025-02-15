package psp.ejerciciost1.e35;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ejercicio35 {

	public static void main(String[] args) {

		List<Integer> numeros = new ArrayList<>();

		Scanner pedirNumeros = new Scanner(System.in);

		System.out.println("Indica 5 números y el programa te sacará el mayor.");

		for (int i = 0; i < 5; i++) {
			System.out.print("Numero " + (i + 1) + ":");
			int num = pedirNumeros.nextInt();

			numeros.add(num);
		}

		encuentraElMayor(numeros);

		pedirNumeros.close();

	}

	public static void encuentraElMayor(List<Integer> listNumeros) {

		int numComparado = 0;

		for (int i = 0; i < listNumeros.size(); i++) {

			if (listNumeros.get(i) > numComparado) {
				numComparado = listNumeros.get(i);
			}

		}
		System.out.println("El número mas grande de los que has dicho es: " + numComparado);
	}

}