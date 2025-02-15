package psp.ejerciciost1.e14;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Ejercicio14 {

	public static void main(String[] args) {

		// Area = diametro * pi

		Scanner pedirRadio = new Scanner(System.in);
		DecimalFormat df = new DecimalFormat("#.000"); // Tres decimales.

		System.out.println("Indica la longitud del radio(en cm) para calcular su diametro y área.");
		double radio = pedirRadio.nextDouble();

		double d = 2 * radio;
		double area = d * Math.PI;

		pedirRadio.close();

		System.out.println("El diámetreo de la circunferencia que has indicado será de ->" + df.format(d) + '\n'
				+ "Y el área será ->" + df.format(area));
	}

}
