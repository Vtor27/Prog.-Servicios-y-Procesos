package psp.ejerciciost1.e26;

import java.util.Scanner;

public class Ejercicio26 {

	static int suspendidos = 0; // De esta forma puedo acceder a las variables desde el método y el main
	static int aprobados = 0;
	static int notables = 0;
	static int sobresalientes = 0;
	static int matriculas = 0;

	public static void main(String[] args) {

		Scanner pedirNotas = new Scanner(System.in);

		for (int i = 0; i < 10; i++) {
			System.out.println("Introduce las notas para organizarlas. Las notas estarán comprendidas entre 0-10.");
			double nota = pedirNotas.nextDouble();

			if (nota < 1 || nota > 10) {
				System.out.println("La nota introducida esta fuera del rango de notas, vuelve a introducirla.");
				i--;
			}
			OrganizarNotas(nota);
		}
		System.out.println("Distribución de las notas:");
		System.out.println("Suspendidos:" + suspendidos + '\n' + "Aprobados:" + aprobados + '\n' + "Notables:"
				+ notables + '\n' + "Sobresalientes:" + sobresalientes + '\n' + "Matrículas:" + matriculas);
		pedirNotas.close();

	}

	public static void OrganizarNotas(double nota) {

		if (nota < 5) {
			System.out.println("Suspendido.");
			suspendidos++;

		} else if (nota >= 5 && nota < 7) {
			System.out.println("Aprobado.");
			aprobados++;

		} else if (nota >= 7 && nota < 9) {
			System.out.println("Notable.");
			notables++;

		} else if (nota >= 9 && nota < 10) {
			System.out.println("Sobresaliente.");
			sobresalientes++;

		} else if (nota == 10) {
			System.out.println("Matrícula.");
			matriculas++;
		}
	}
}
