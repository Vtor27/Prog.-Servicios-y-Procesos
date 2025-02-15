package psp.ejerciciost1.e37;

import java.util.Scanner;

public class Ejercicio37 {

	public static void main(String[] args) {

		Scanner pedirInfo = new Scanner(System.in);

		System.out.print("Indica el nombre del trabajador. -> ");
		String nombre = pedirInfo.next();

		System.out.print("Indica los años de experiencia como desarrollador de software. -> ");
		int experiencia = pedirInfo.nextInt();

		if (experiencia <= 1) {
			System.out.println("Nombre del trabajador: " + nombre + '\n' + "Desarrollador Junior L1" + '\n'
					+ "Salario: 15000-18000");

		} else if (experiencia > 1 && experiencia <= 2) {
			System.out.println("Nombre del trabajador: " + nombre + '\n' + "Desarrollador Junior L2" + '\n'
					+ "Salario: 18000-22000");

		} else if (experiencia >= 3 && experiencia <= 5) {
			System.out.println("Nombre del trabajador: " + nombre + '\n' + "Desarrollador Senior L1" + '\n'
					+ "Salario: 22000-28000");

		} else if (experiencia > 5 && experiencia < 8) {
			System.out.println("Nombre del trabajador: " + nombre + '\n' + "Desarrollador Senior L2" + '\n'
					+ "Salario: 28000-36000");

		} else if (experiencia > 8) {
			System.out.println("Nombre del trabajador: " + nombre + '\n' + "Analista/Arquitecto" + '\n'
					+ "Salario: A convenio con el rol desempeñado");
		}
		pedirInfo.close();
	}

}
