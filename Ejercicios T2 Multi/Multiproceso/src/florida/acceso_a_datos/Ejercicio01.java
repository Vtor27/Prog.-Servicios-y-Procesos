package florida.acceso_a_datos;

public class Ejercicio01 {

	public static int mostrarIntervalo(Integer num1, Integer num2) {

		int resultado = 0;
		for (int i = num1; i <= num2; i++) {
			resultado = +i;
			System.out.print(resultado + " ");
		}
		return resultado;
	}

	public static void main(String[] args) {
		if(args.length == 2) {
			Integer num1 = Integer.parseInt(args[0]);

			Integer num2 = Integer.parseInt(args[1]);
			mostrarIntervalo(num1, num2);
		}else {
			System.out.println("No hay parmetros.");
		}
	}
}
