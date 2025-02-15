package es.florida.Miniprojecto1_API;

import com.sun.net.httpserver.HttpHandler; //Importar estas 2 a mano, eclipse no las importa automaticamente.
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class GestorHttp_Winterfell implements HttpHandler {

	public static int temperaturaActual = 15;
	public static int temperaturaTermostato = 15;
	public static int tempDeseada = 0;

	// http://localhost:7777/estufa

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

		System.out.print("Peticion recibida: Tipo ");
		String requestParamValue = null;
		if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("GET");
			requestParamValue = handleGetRequest(httpExchange);
			try {
				handleGetResponse(httpExchange, requestParamValue);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("POST");
			requestParamValue = handlePostRequest(httpExchange);
			try {
				handlePostResponse(httpExchange, requestParamValue);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("DESCONOCIDA");
		}
	}

	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		System.out.println(httpExchange.getRequestURI().toString().split("\\?")[1]);
		return httpExchange.getRequestURI().toString().split("\\?")[1];
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

	private void handleGetResponse(HttpExchange httpExchange, String requestParamValue)
			throws IOException, InterruptedException {

		System.out.println("El servidor pasa a procesar la peticion GET: " + requestParamValue);
		String htmlResponse = "";
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		if (requestParamValue.equals("temperaturaActual")) {
			OutputStream outputStream = httpExchange.getResponseBody();
			System.out.println("Entra en el if");
			if (tempDeseada != 0) {
				temperaturaTermostato = tempDeseada;
			}

			htmlResponse = "<html><body><h1>Temperatura </br>" + "Temperatura actual--> " + temperaturaActual
					+ "</br>Temperatura del termostato--> " + temperaturaTermostato + "</h1></body></html>";

			// MAS CORRECTO SERIA:
			// byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
			// httpExchange.sendResponseHeaders(200, bytes.length);
			outputStream.write(htmlResponse.getBytes());
			outputStream.flush();
			System.out.println("Devuelve respuesta HTML: " + htmlResponse);
			System.out.println();

			outputStream.close();

		} else {
			OutputStream outputStream = httpExchange.getResponseBody();
			htmlResponse = "<html><body><h1>No hay temperaturas</h1></body></html>";
			httpExchange.sendResponseHeaders(200, htmlResponse.length());

			outputStream.write(htmlResponse.getBytes());
			outputStream.flush();
			outputStream.close();
			System.out.println("Devuelve respuesta HTML: " + htmlResponse);
			System.out.println();
		}
	}

	private void handlePostResponse(HttpExchange httpExchange, String requestParamValue)
			throws IOException, InterruptedException {

		System.out.println("El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);

		String responseMessage = "POST recibido correctamente: " + requestParamValue;

		String responseMessageCortado = responseMessage.split(":")[1].split("=")[1];
		int tempCambiada = Integer.parseInt(responseMessageCortado);
		tempDeseada = tempCambiada;

		while (temperaturaActual < tempDeseada) {
			regularTemperatura(temperaturaTermostato);
		}

		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		System.out.println("Respuesta enviada al cliente: " + responseMessage);
	}

	public void regularTemperatura(int tempTermostato) throws InterruptedException {
		temperaturaActual++;
		Thread.sleep(3000);
		System.out.println("La temperatura ha subido 1 grado.");
		if (temperaturaActual == tempDeseada) {
			System.out.println("La temperatura ambiente ha llegado a la deseada.");
		}
	}

}
