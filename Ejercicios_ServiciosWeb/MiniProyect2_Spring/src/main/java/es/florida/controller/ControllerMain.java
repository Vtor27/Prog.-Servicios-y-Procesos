package es.florida.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerMain implements Runnable {

//	public void nuevaPeticion(HttpServletRequest request) throws IOException {
//		String ipNuevoUser = request.getRemoteAddr();	//Extrae la ip del usuario que se conecta.
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
//		String timeStamp = LocalDateTime.now().format(formatter);
//
//		String nombreFichero = "./src/main/resources/log.txt";
//		File fichero = new File(nombreFichero);
//		if (!fichero.exists()) {
//			fichero.createNewFile();
//			System.out.println("Archivo 'log.txt' creadop con exito.");
//		}
//		FileWriter fw = new FileWriter(fichero, true); 
//		BufferedWriter bw = new BufferedWriter(fw);
//		bw.write("\nNueva conexión: \n");
//		bw.write("IP: " + ipNuevoUser);
//		bw.newLine();
//		bw.write("Conexión: " + timeStamp);
//		bw.newLine();
//		bw.close();
//	}

	public JSONArray leerJson(String rutaJson) throws IOException {
		StringBuilder jsonContent = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(rutaJson))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				jsonContent.append(linea);
			}
		}
		return new JSONArray(jsonContent.toString());
	}

	private String listaAliasDelincuentes() throws IOException {
		System.out.println("Entra en mostrarTodos");
		String ficheroJson = "./src/main/resources/delincuentes.json";
		JSONArray delincuentes = leerJson(ficheroJson);
		StringBuilder htmlResponse = new StringBuilder();

		htmlResponse.append("<html><body><h1>Lista de delincuentes</h1><ul>");
		for (int i = 0; i < delincuentes.length(); i++) {
			JSONObject delincuente = delincuentes.getJSONObject(i);
			htmlResponse.append("<li>").append(delincuente.getString("alias")).append("</li>");
		}
		htmlResponse.append("</ul></body></html>");
		return htmlResponse.toString();
	}

	@GetMapping("/servidor/mostrarTodos")
	public String mostrarTodos() throws IOException {
		return listaAliasDelincuentes();
	}

	@GetMapping("/servidor/mostrarUno")
	public String mostrarPorAlias(@RequestParam(value = "alias") String aliasDelincuente) throws IOException {
		StringBuilder htmlResponse = new StringBuilder();
		String ficheroJson = "./src/main/resources/delincuentes.json";
		JSONArray delincuentes = leerJson(ficheroJson);

		if (aliasDelincuente.isEmpty()) {
			System.out.println("Error: Alias vacío en la URL.");
			htmlResponse.append("<html><head><meta charset=\"UTF-8\"></head><body>");
			htmlResponse.append("<h1>Error: Alias no proporcionado.</h1>");
			htmlResponse.append("</body></html>");
		} else {
			System.out.println("Buscando alias: [" + aliasDelincuente + "]");
		}
		JSONObject delincuenteEncontrado = null;

		for (int i = 0; i < delincuentes.length(); i++) {
			JSONObject delincuente = delincuentes.getJSONObject(i);
			if (delincuente.getString("alias").trim().equalsIgnoreCase(aliasDelincuente)) {
				delincuenteEncontrado = delincuente;
				break;
			}
		}
		if (delincuenteEncontrado != null) {
			htmlResponse.append("<html><head><meta charset=\"UTF-8\"></head><body>"); // Evita los problemas con los
																						// acentos
			htmlResponse.append("<h2>Información del Delincuente</h2>");
			htmlResponse.append("<p><b>Alias:</b> " + delincuenteEncontrado.getString("alias") + "</p>");
			htmlResponse.append("<p><b>Nombre:</b> " + delincuenteEncontrado.getString("nombreCompleto") + "</p>");
			htmlResponse.append(
					"<p><b>Fecha de Nacimiento:</b> " + delincuenteEncontrado.getString("fechaNacimiento") + "</p>");
			htmlResponse.append("<p><b>Nacionalidad:</b> " + delincuenteEncontrado.getString("nacionalidad") + "</p>");
			htmlResponse.append("<img src='" + delincuenteEncontrado.getString("fotografia") + "' alt='Foto'>");
		} else {
			htmlResponse.append("<html><head><meta charset=\"UTF-8\"></head><body>");
			htmlResponse.append("<h1>Delincuente no encontrado</h1>");
			htmlResponse.append("</body></html>");
		}
		return htmlResponse.toString();
	}

	private void guardarJson(String rutaJson, JSONArray jsonArray) throws IOException {
	    try (PrintWriter writer = new PrintWriter(rutaJson, "UTF-8")) {
	        writer.println(jsonArray.toString(4)); // Guarda con indentación de 4 espacios
	    }
	}
	
	@PostMapping("/servidor/nuevo")
	ResponseEntity<Object> postBody(@RequestBody String body) {

		try {
			JSONObject nuevoDelincuente = new JSONObject(body);
			
			String ficheroJson = "./src/main/resources/delincuentes.json";	//Carga el fichero json de los delincuentes.
			JSONArray delincuentesArray = leerJson(ficheroJson);
			delincuentesArray.put(nuevoDelincuente);
			guardarJson(ficheroJson, delincuentesArray);
			
			
			ControllerMain controlador = new ControllerMain();
			Thread hilo = new Thread(controlador);
			hilo.start();
			return ResponseEntity.noContent().header("Content-Length", "0").build();// Equivale a OK No content
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@Override
	public void run() {
		//IMPLEMENTAR CORREO
	}

}
