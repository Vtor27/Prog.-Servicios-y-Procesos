package psp.ejerciciost1.e11;

//Implementa un programa que dado un DNI (sin letra) por teclado devuelva el DNI con la
//letra correcta.

import java.util.Scanner;

public class Ejercicio11 {

	public static void main(String[] args) {

		Scanner preguntaDNI = new Scanner(System.in);

		char letras[] = { 'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H',
				'L', 'C', 'K', 'E' };

		System.out.print("Escribe tu DNI sin la letra. --> ");
		int dni = preguntaDNI.nextInt();

		int resto = dni % 23;

		char letraDNI = letras[resto];

		preguntaDNI.close();

		System.out.print("Tu dni completo es este --> " + dni + letraDNI);
	}

}
