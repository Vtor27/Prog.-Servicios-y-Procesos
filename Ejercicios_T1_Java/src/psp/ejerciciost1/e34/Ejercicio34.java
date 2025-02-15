package psp.ejerciciost1.e34;

public class Ejercicio34 {

	public static void main(String[] args) {

		long suma = 1; // int se queda pequeño para el tipo de numero que da.

		for (int i = 1; i < 16; i++) {

			suma *= i;
		}
		System.out.println("El resultado de 15! es: " + suma);

	}

}
