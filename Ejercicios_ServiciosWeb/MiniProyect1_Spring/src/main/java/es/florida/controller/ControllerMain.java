package es.florida.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerMain implements Runnable {
	public static int temperaturaActual = 15;
	public static int temperaturaTermostato = 15;
	public static int tempDeseada = 0;

	// Esto equivale a la llamada que se espera -->
	// http://localhost:8080/estufa?variable=temperaturaActual

	public void regularTemperatura() throws InterruptedException {
		while (temperaturaActual != tempDeseada) {
			if (temperaturaActual < tempDeseada) {
				temperaturaActual++;
				System.out.println("La temperatura ha subido 1 grado.");
			} else if (temperaturaActual > tempDeseada) {
				temperaturaActual--;
				System.out.println("La temperatura ha subido 1 grado.");
			}
			Thread.sleep(5000);
			System.out.println("Temperatura actual: " + temperaturaActual);
		}
	}

	@GetMapping("estufa")
	String tempActual(@RequestParam(value = "variable") String strVariable) { // variable equivale a lo que hay delante
																				// del = y strNombre es lo que hay
																				// despues del '='
		String htmlResponse = "";
		if (strVariable.equals("temperaturaActual")) {
			if (tempDeseada != 0) {
				temperaturaTermostato = tempDeseada;
			}
			htmlResponse = "<html><body><h1>Temperatura </br>" + "Temperatura actual--> " + temperaturaActual
					+ "</br>Temperatura del termostato--> " + temperaturaTermostato + "</h1></body></html>";
		} else {
			htmlResponse = "<html><body><h1> Parametro no reconocido </h1></body></html>";
		}
		return htmlResponse;
	}

	@PostMapping("estufa")
	ResponseEntity<Object> postBody(@RequestBody String cuerpoPeticion) { // El objeto responseEntity equivale a los
																			// códigos de 200, 204 no content...
		String[] arrayPeticion = cuerpoPeticion.split("="); // El 'cuerpoPeticion' equivaldria al body por lo que si al
															// dividir el array en dos 'setTemperatura' y 'la temp en
															// grados' tendría las dos partes.

		if (arrayPeticion[0].equals("setTemperatura")) { // si la parte antes del '=' es setTemperatura
			try {
				tempDeseada = Integer.parseInt(arrayPeticion[1]); // Se parsean los grados y se utilizan como número.
				ControllerMain controlador = new ControllerMain();
				Thread hilo = new Thread(controlador);	
				hilo.start();
				return ResponseEntity.noContent().header("Content-Length", "0").build();// Equivale a OK No content
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acceso prohibido al recurso.
			}
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@Override
	public void run() {
		System.out.println("Nueva temperatura deseada: " + tempDeseada);
		try {
			regularTemperatura();
			System.out.println("La temperatura ha llegado a la deseada.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
