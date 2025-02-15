package psp.ejerciciost1.e28;

import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio29 {

	public static void main(String[] args) {

		Scanner pedirInfo = new Scanner(System.in);

		ArrayList<Vehiculo> vehiculos = new ArrayList<Vehiculo>();
		String continuar;
		boolean stop = true;
		int key = 1;

		do {
			System.out.print("Indica el tipo del vehiculo " + key + ".(Coche, Moto...)--> ");
			String tipo = pedirInfo.next();

			System.out.print("Indica la marca.--> ");
			String marca = pedirInfo.next();

			System.out.print("Indica el modelo.--> ");
			String modelo = pedirInfo.next();

			Vehiculo vehiculo = new Vehiculo(tipo, marca, modelo);

			vehiculos.add(vehiculo);
			key++;

			System.out.println("Quieres introducir otro vehiculo? (S - SALIR | C - CONTINUAR)");
			continuar = pedirInfo.next();

			if (continuar.equals("S") || continuar.equals("s")) { // Para mayuscula o minúscula.
				System.out.println("Saliendo...");
				stop = false;
			}

		} while (stop == true);

		System.out.println("Info de los vehiculos introducidos: ");

		for (Vehiculo vehiculo : vehiculos) {
			vehiculo.mostrarVehiculo();
		}

		pedirInfo.close();
	}

}
