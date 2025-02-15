package es.florida.Miniproyecto2_API;

import com.sun.net.httpserver.HttpHandler; //Importar estas 2 a mano, eclipse no las importa automaticamente.
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

//DIFERENCIAS ENTRE .getQuery() Y .getPath().
//PATH -> Para esta petición 'localhost:8888/servidor/mostrarUno?alias=La Cobra' se recoje '/servidor/mostrarUno', no incluye lo que va despues del ?.
//QUERY -> Para esta petición 'localhost:8888/servidor/mostrarUno?alias=La Cobra' se recoje lo que va despues del ? 'alias=La Cobra', los parametros de la consulta.

public class GestorHttp_CNI implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		// Habilitar accesos CORS (intercambio de recursos de origen cruzado) para
		// peticiones POST, PUT y DELETE

		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

		if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) { // Caso PUT y DELETE se pide antes
																			// confirmacion desde cliente
			httpExchange.sendResponseHeaders(204, -1); // Codigo Ok, no devuelve contenido, preparado para POST, PUT o
														// DELETE
			return;
		}
		nuevaPeticion(httpExchange);

		System.out.print("Peticion recibida: Tipo ");
		String requestParamValue = null;
		if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("GET");
			requestParamValue = handleGetRequest(httpExchange);
			try {
				handleGetResponse(httpExchange, requestParamValue);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} else if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("POST");
			requestParamValue = handlePostRequest(httpExchange);
			try {
				handlePostResponse(httpExchange, requestParamValue);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("DESCONOCIDA");
		}
	}

	public void nuevaPeticion(HttpExchange httpExchange) throws IOException {
		// Con esto se extrae la ip del usuario que se conecta en formato
		// InetSocketAddress y se transforma con .getHostAddress() a una String en
		// formato IP legible.
		String ipNuevoUser = httpExchange.getLocalAddress().getAddress().getHostAddress();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
		String timeStamp = LocalDateTime.now().format(formatter);

		String nombreFichero = "./src/main/resources/log.txt";
		File fichero = new File(nombreFichero);
		if (!fichero.exists()) {
			fichero.createNewFile();
			System.out.println("Archivo 'log.txt' creadop con exito.");
		}
		FileWriter fw = new FileWriter(fichero, true); // Con true no se sobreescribe el contenido del log.txt
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("\nNueva conexión: \n");
		bw.write("IP: " + ipNuevoUser);
		bw.newLine();
		bw.write("Conexión: " + timeStamp);
		bw.newLine();
		bw.close();
	}

	private String handleGetRequest(HttpExchange httpExchange) {
		String path = httpExchange.getRequestURI().getPath();
		String query = httpExchange.getRequestURI().getQuery();

		System.out.println("Path recibido: " + path);
		System.out.println("Query recibida: " + query);

		if (query == null) { // No hay parámetros
			if (path.endsWith("mostrarTodos")) {
				return "mostrarTodos";
			}
			return "";
		}

		return query; // Devuelve "alias=La Cobra" si hay un alias en la URL
	}

	private String handlePostRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo POST: " + httpExchange.getRequestBody().toString());
		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

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
	
	private void guardarJson(String rutaJson, JSONArray jsonArray) throws IOException {
	    try (PrintWriter writer = new PrintWriter(rutaJson, "UTF-8")) {
	        writer.println(jsonArray.toString(4)); // Guarda con indentación de 4 espacios
	    }
	}

	private void handleGetResponse(HttpExchange httpExchange, String requestParamValue)
			throws IOException, InterruptedException {
		System.out.println("El servidor pasa a procesar la peticion GET: " + requestParamValue);

		String ficheroJson = "./src/main/resources/delincuentes.json";
		JSONArray delincuentes = leerJson(ficheroJson);
		StringBuilder htmlResponse = new StringBuilder();

		if (requestParamValue.equals("mostrarTodos")) {
			htmlResponse.append("<html><body><h1>Lista de delincuentes</h1><ul>");

			for (int i = 0; i < delincuentes.length(); i++) {
				JSONObject delincuente = delincuentes.getJSONObject(i);
				htmlResponse.append("<li>").append(delincuente.getString("alias")).append("</li>");
			}
			htmlResponse.append("</ul></body></html>");

		} else if (requestParamValue.startsWith("alias=")) {
			System.out.println("Entra aqui");

			String alias = URLDecoder.decode(requestParamValue.substring("alias=".length()), StandardCharsets.UTF_8)
					.trim(); // Con esto me
			// aseguro de
			// que los alias
			// se
			// decodifiquen
			// bien en el
			// buscador y
			// los espacios
			// o tildes no
			// den
			// problemas.
			if (alias.isEmpty()) {
				System.out.println("Error: Alias vacío en la URL.");
				htmlResponse.append("<html><head><meta charset=\"UTF-8\"></head><body>");
				htmlResponse.append("<h1>Error: Alias no proporcionado.</h1>");
				htmlResponse.append("</body></html>");
			} else {
				System.out.println("Buscando alias: [" + alias + "]");
			}
			JSONObject delincuenteEncontrado = null; // Lo inicializo en null para luego darle valor cuando encuentre el
														// delincuente.
			for (int i = 0; i < delincuentes.length(); i++) {
				JSONObject delincuente = delincuentes.getJSONObject(i);
				if (delincuente.getString("alias").trim().equalsIgnoreCase(alias)) {
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
				htmlResponse.append("<p><b>Fecha de Nacimiento:</b> "
						+ delincuenteEncontrado.getString("fechaNacimiento") + "</p>");
				htmlResponse
						.append("<p><b>Nacionalidad:</b> " + delincuenteEncontrado.getString("nacionalidad") + "</p>");
				htmlResponse.append("<img src='" + delincuenteEncontrado.getString("fotografia") + "' alt='Foto'>");
			} else {
				htmlResponse.append("<html><head><meta charset=\"UTF-8\"></head><body>");
				htmlResponse.append("<h1>Delincuente no encontrado</h1>");
				htmlResponse.append("</body></html>");
			}

		} else if (requestParamValue.isEmpty()) {
			htmlResponse.append("<h1>Error: Parámetro no reconocido</h1></body></html>");
		}

		byte[] responseBytes = htmlResponse.toString().getBytes();

		httpExchange.sendResponseHeaders(200, responseBytes.length);

		try (OutputStream outputStream = httpExchange.getResponseBody()) {
			outputStream.write(responseBytes);
			outputStream.flush();
			System.out.println("Devuelve respuesta HTML: " + htmlResponse);
			System.out.println();
		} catch (IOException e) {
			System.err.println("Error al escribir la respuesta: " + e.getMessage());
		}
	}

	private void handlePostResponse(HttpExchange httpExchange, String requestParamValue)
			throws IOException, InterruptedException {

		System.out.println("El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);

		JSONObject nuevoDelincuente = new JSONObject(requestParamValue);
		
		String ficheroJson = "./src/main/resources/delincuentes.json";	//Carga el fichero json de los delincuentes.
        JSONArray delincuentesArray = leerJson(ficheroJson);
        
        delincuentesArray.put(nuevoDelincuente);
        
        guardarJson(ficheroJson, delincuentesArray); //Dirección donde esta el fichero json que se modifica, segundo es el array con los valores recogidos del body.
        
		String responseMessage = "POST recibido correctamente: " + requestParamValue;

		System.out.println("Delincuente añadido correctamente: " + nuevoDelincuente.getString("alias"));

		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		System.out.println("Respuesta enviada al cliente: " + responseMessage);

	}
}
