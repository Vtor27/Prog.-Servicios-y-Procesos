package psp.ejerciciost1.e24;

import java.util.Random;
import java.util.Scanner;

public class Ejercicio24 {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("No puedes introducir mas de un número.");
			return;
		}

		Random numRandom = new Random();
		int numIntroducido = Integer.parseInt(args[0]);
		Scanner pedirPremio = new Scanner(System.in);

		int num = numRandom.nextInt(11);

		if (num == numIntroducido) {
			System.out.println("El número introducido " + numIntroducido + "es igual al aleatorio " + num);

			System.out.println("¡Enhorabuena! Has acertado, por favor elije un premio.");
			int premio = pedirPremio.nextInt();

			if (premio < 1 || premio > 3) {
				System.out.println("No hay un premio disponible para esa selección.");
			} else {
				if (premio == 1) {
					System.out.println("Has elegido un balón de futbol.");
				} else if (premio == 2) {
					System.out.println("Has elegido una raqueta de tenis.");
				} else if (premio == 3) {
					System.out.println("Has elegido unas zapatillas.");
				}
			}
			pedirPremio.close();
		} else {
			System.out.println("Lo siento no has acertado, mas suerte la proxima vez.");
		}

	}

}
