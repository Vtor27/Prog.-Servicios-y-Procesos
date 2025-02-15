package psp.ejerciciost1.e15;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Ejercicio15 {

	public static void main(String[] args) {

		Scanner pedirRadio = new Scanner(System.in);
		DecimalFormat df = new DecimalFormat("#.000"); // Tres decimales.

		System.out.println("Indica la longitud del radio(en cm) para calcular su diametro y área.");
		double radio = pedirRadio.nextDouble();

		double d = 2 * radio;
		double area = d * Math.PI;
		double volumen = (4.0 / 3.0 * Math.PI) * Math.pow(radio, 3); // (4/3) se tienen que poner tambien como doubles,
																		// si no no calcula bien.

		pedirRadio.close();

		System.out.println("El diámetreo de la circunferencia que has indicado será de ->" + df.format(d) + "cm" + '\n'
				+ "El área será ->" + df.format(area) + "cm²" + '\n' + "EL volumen será -> " + df.format(volumen)
				+ "cm³");

	}

}
