package psp.ejerciciost1.e27;

import java.util.Scanner;

public class Ejercicio27 {

	static char letras[] = { 'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V',
			'H', 'L', 'C', 'K', 'E' };

	public static void main(String[] args) {

		Scanner preguntaDNI = new Scanner(System.in);

		System.out.print("Escribe tu DNI sin la letra. --> ");
		int dni = preguntaDNI.nextInt();

		SaberLetraDNI(dni);
		preguntaDNI.close();
	}

	public static void SaberLetraDNI(int dni) {
		int resto = dni % 23;

		char letraDNI = letras[resto];

		System.out.print("Tu dni completo es este --> " + dni + letraDNI);
	}

}