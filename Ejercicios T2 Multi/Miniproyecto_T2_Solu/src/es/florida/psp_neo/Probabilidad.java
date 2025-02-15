package es.florida.psp_neo;

public class Probabilidad {
	
	public double calculaProbabilidad(double posicionNEO, double velocidadNEO) {
		
		double resultado = 0;
		double posicionTierra = 1;
		double velocidadTierra = 100;
		for (int i = 0; i < (50 * 365 * 24 * 60 * 60); i++) {
			posicionNEO = posicionNEO + velocidadNEO * i;
			posicionTierra = posicionTierra + velocidadTierra * i;
		}
		resultado = 100 * Math.random() * Math.pow( ( (posicionNEO - posicionTierra) / (posicionNEO + posicionTierra) ) , 2);
		return resultado;
	}

	public static void main(String[] args){
		Probabilidad p = new Probabilidad();
		double posicion = Double.parseDouble(args[0]);
		double velocidad= Double.parseDouble(args[1]);
		double resultado = p.calculaProbabilidad(posicion, velocidad);
		System.out.println(resultado);
	}
}