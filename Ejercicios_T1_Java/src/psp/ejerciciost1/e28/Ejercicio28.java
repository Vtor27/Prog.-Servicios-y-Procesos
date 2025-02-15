package psp.ejerciciost1.e28;

import java.util.Scanner;

public class Ejercicio28 {

	public static void main(String[] args) {

		Scanner pedirInfo = new Scanner(System.in);

		Vehiculo[] vehiculos = new Vehiculo[5];

		for (int i = 0; i < vehiculos.length; i++) {
			System.out.print("Indica el tipo del vehiculo " + (i + 1) + ".(Coche, Moto...)--> ");
			String tipo = pedirInfo.next();

			System.out.print("Indica la marca.--> ");
			String marca = pedirInfo.next();

			System.out.print("Indica el modelo.--> ");
			String modelo = pedirInfo.next();

			vehiculos[i] = new Vehiculo(tipo, marca, modelo);
		}
		System.out.println("Info de los vehiculos introducidos: ");

		for (Vehiculo vehiculo : vehiculos) {
			vehiculo.mostrarVehiculo();
		}

		pedirInfo.close();
	}
}
