package psp.ejerciciost1.e12;

//Realiza un programa en Java que dadas 10 notas introducidas por teclado (valores de 0 a
//10), las agrupe en suspensos, aprobados, notables, sobresalientes y matrícula, y muestre
//por pantalla cuantas notas hay en cada grupo.

import java.util.Scanner;

public class Ejercicio12 {

	public static void main(String[] args) {

		Scanner pedirNotas = new Scanner(System.in);

		int suspendidos = 0;
		int aprobados = 0;
		int notables = 0;
		int sobresalientes = 0;
		int matriculas = 0;

		for (int i = 0; i < 10; i++) {
			System.out.println("Introduce las notas para organizarlas. Las notas estarán comprendidas entre 0-10.");
			double nota = pedirNotas.nextDouble();

			if (nota < 5) {
				System.out.println("Suspendido.");
				suspendidos++;

			} else if (nota >= 5 && nota < 7) {
				System.out.println("Aprobado.");
				aprobados++;

			} else if (nota > 7 && nota < 9) {
				System.out.println("Notable.");
				notables++;

			} else if (nota >= 9 && nota < 10) {
				System.out.println("Sobresaliente.");
				sobresalientes++;

			} else if (nota == 10) {
				System.out.println("Matrícula.");
				matriculas++;
			} else {
				System.out.println("La nota introducida esta fuera del rango de notas, vuelve a introducirla.");
				i--;
			}
		}
		pedirNotas.close();

		System.out.println("Distribución de las notas:");
		System.out.println("Suspendidos:" + suspendidos + '\n' + "Aprobados:" + aprobados + '\n' + "Notables:"
				+ notables + '\n' + "Sobresalientes:" + sobresalientes + '\n' + "Matrículas:" + matriculas);
	}
}
