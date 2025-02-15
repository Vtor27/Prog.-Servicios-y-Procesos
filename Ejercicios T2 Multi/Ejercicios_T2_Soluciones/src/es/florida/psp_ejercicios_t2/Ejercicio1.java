package es.florida.psp_ejercicios_t2;

public class Ejercicio1 {

//	//1.	Realiza un programa en Java que dados dos n�meros enteros, 
//	//		devuelva por pantalla la suma de todos los n�meros que hay entre ellos (incluy�ndolos).
	
	public static void main(String[] args) {
		int numero1 = Integer.parseInt(args[0]);
		int numero2 = Integer.parseInt(args[1]);
		int suma = 0;
		for (int i = numero1; i <= numero2; i++) {
			suma = suma + i;
		}
		System.out.println("Suma numeros entre " + numero1 + " y " + numero2 + " (inclusive): " + suma);
	}

}
