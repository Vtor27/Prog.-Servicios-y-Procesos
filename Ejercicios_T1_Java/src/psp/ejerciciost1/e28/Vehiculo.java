package psp.ejerciciost1.e28;

public class Vehiculo {

	String tipo, marca, modelo;

	public Vehiculo(String tipo, String marca, String modelo) {
		super();
		this.tipo = tipo;
		this.marca = marca;
		this.modelo = modelo;
	}

	public String getTipo() {
		return tipo;
	}

	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void mostrarVehiculo() {

		System.out.println("El vehiculo indicado es: Tipo: " + tipo + " Marca: " + marca + " Modelo: " + modelo);
	}
}
