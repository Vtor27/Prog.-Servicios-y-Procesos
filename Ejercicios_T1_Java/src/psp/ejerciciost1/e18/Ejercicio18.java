package psp.ejerciciost1.e18;

import java.util.Scanner;

public class Ejercicio18 {

	public static void main(String[] args) {
		Scanner pedirContraseña = new Scanner(System.in);

		boolean valido = true;

		System.out.println("Introduce una contraseña(mínimo 1 número, 1 mayúscula y tener al menos 5 caracteres)");
		String contraseña = pedirContraseña.nextLine();

		if (contraseña.length() < 5) {
			System.out.println("La contraseña tiene que tener como mínimo 5 caracteres.");
			valido = false;

		}
		if (!contraseña.matches(".*[A-Z].*")) {
			System.out.println("La contraseña debe tener al menos 1 letra mayúscula.");
			valido = false;

		}
		if (!contraseña.matches(".*[0-9].*")) {
			System.out.println("La contraseña debe tener al menos 1 número.");
			valido = false;

		}
		if (valido) {
			System.out.println("La contraseña es válida.");
		}

		System.out.println("Vuelve a introducir la contraseña. -> ");// Para esta parte tambien se deberia aplicar las
																		// restricciones de arriba.
		String repetirContraseña = pedirContraseña.next();

		if (!contraseña.equals(repetirContraseña)) {
			System.out.println("Las contraseñas NO coinciden.");
		} else {
			System.out.println("Las dos contraseñas SI coinciden.");
		}

		pedirContraseña.close();
	}

}
