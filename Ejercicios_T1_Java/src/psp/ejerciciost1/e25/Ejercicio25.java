package psp.ejerciciost1.e25;

import java.util.Random;
import java.util.Scanner;

public class Ejercicio25 {

	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Tienes que introducir 3 números.");
			return;
		}

		Random numRandom = new Random();
		int numIntroducido1 = Integer.parseInt(args[0]);
		int numIntroducido2 = Integer.parseInt(args[1]);
		int numIntroducido3 = Integer.parseInt(args[2]);
		Scanner pedirPremio = new Scanner(System.in);

		int num = numRandom.nextInt(11);

		if (num == numIntroducido1 || num == numIntroducido2 || num == numIntroducido3) {
			System.out.println("Has acertado una de tus 3 elecciones, el numero aleatorio es --> " + num);
			// Este apartado se podría hacer más específico filtrando 3 veces por separado
			// para indicar también qué número elegido es el que es igual.

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
