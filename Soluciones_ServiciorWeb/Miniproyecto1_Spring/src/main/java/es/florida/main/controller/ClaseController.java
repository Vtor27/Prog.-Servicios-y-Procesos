package es.florida.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaseController implements Runnable {

	static int temperaturaActual = 15;
	static int temperaturaTermostato = 15;
	
	//Llamada esperada: http://localhost:8080/estufa?variable=temperaturaActual
	@GetMapping("estufa")
	String mostrarTemperatura(@RequestParam(value = "variable") String strVariable) {
		String htmlResponse = "";
		if (strVariable.equals("temperaturaActual")) {
			htmlResponse = "<html><body><h1>Temperatura actual: " + temperaturaActual + " - Temperatura termostato: " + temperaturaTermostato + "</h1></body></html>";
		} else {
			htmlResponse = "<html><body><h1>Parametro no reconocido</h1></body></html>";
		}
		return htmlResponse;
	}
	
	@PostMapping("estufa")
	ResponseEntity<Object> postBody(@RequestBody String cuerpoPeticion) {
		String[] arrayPeticion = cuerpoPeticion.split("=");
		if (arrayPeticion[0].equals("setTemperatura")) {
			try {
				temperaturaTermostato = Integer.parseInt(arrayPeticion[1]);
				ClaseController controlador = new ClaseController();
				Thread hilo = new Thread(controlador);
				hilo.start();
				return ResponseEntity.noContent().header("Content-Length", "0").build();
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	
	
	public void run() {
		System.out.println("Nueva temperatura: " + temperaturaTermostato);
		while (temperaturaActual < temperaturaTermostato) {
			temperaturaActual++;
			System.out.println("Actualiza temperatura: " + temperaturaActual);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (temperaturaActual > temperaturaTermostato) {
			temperaturaActual--;
			System.out.println("Actualiza temperatura: " + temperaturaActual);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
