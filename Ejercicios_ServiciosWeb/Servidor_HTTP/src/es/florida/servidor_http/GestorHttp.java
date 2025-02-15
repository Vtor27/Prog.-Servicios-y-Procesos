package es.florida.servidor_http;

import com.sun.net.httpserver.HttpHandler; //Importar estas 2 a mano, eclipse no las importa automaticamente.
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class GestorHttp implements HttpHandler {

	//http://localhost:5000/test?nombre=XXX
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		// Habilitar accesos CORS (intercambio de recursos de origen cruzado) para
		// peticiones POST, PUT y DELETE
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

		if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) { // Caso PUT y DELETE se pide antes
																			// confirmacion desde cliente
			httpExchange.sendResponseHeaders(204, -1); // Codigo Ok, no devuelve contenido, preparado para POST, PUT o DELETE
			return;
		}

		System.out.print("Peticion recibida: Tipo ");
		String requestParamValue = null;
		if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("GET");
			requestParamValue = handleGetRequest(httpExchange);
			handleGetResponse(httpExchange, requestParamValue);
		} else if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("POST");
			requestParamValue = handlePostRequest(httpExchange);
			handlePostResponse(httpExchange, requestParamValue);
		} else if ("PUT".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("PUT");
			requestParamValue = handlePutRequest(httpExchange);
			handlePutResponse(httpExchange, requestParamValue);
		} else if ("DELETE".equals(httpExchange.getRequestMethod())) {
			System.out.println("DELETE");
			requestParamValue = handleDeleteRequest(httpExchange);
			handleDeleteResponse(httpExchange, requestParamValue);
		} else {
			System.out.println("DESCONOCIDA");
		}

	}

	// INICIO BLOQUE REQUEST

	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
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

	private String handlePutRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo PUT: " + httpExchange.getRequestBody().toString());
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

	private String handleDeleteRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo DELETE: " + httpExchange.getRequestBody().toString());
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

	// FIN BLOQUE REQUEST

	// INICIO BLOQUE RESPONSE

	private void handleGetResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

		System.out.println("El servidor pasa a procesar la peticion GET: " + requestParamValue);

		// Ejemplo de respuesta: el servidor devuelve al cliente un HTML simple:
		OutputStream outputStream = httpExchange.getResponseBody();
		String htmlResponse = "<html><body><h1>Hola " + requestParamValue + "</h1></body></html>";
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		// MAS CORRECTO SERIA:
		// byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
		// httpExchange.sendResponseHeaders(200, bytes.length);
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
		System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// TODO: en vez del string htmlResponse anterior con la pagina web simple, se
		// podria incluir
		// en dicho string cualquier otro string (tipo JSON, por ejemplo) que el cliente
		// haya solicitado
		// a traves de la peticion Axios de Javascript. Por tanto, podria ser necesario
		// llamar desde este
		// este metodo a lo/s metodo/s necesario/s para acceder a una base de datos como
		// las que hemos
		// trabajado en el modulo de Acceso a Datos.

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handlePostResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

		System.out.println("El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);

		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
//		 OutputStream outputStream = httpExchange.getResponseBody();
//		 System.out.println("despues de ouput");
//		 String htmlResponse = "Parametro/s POST: " + requestParamValue + " -> Se procesara por parte del servidor";
//		 outputStream.write(htmlResponse.getBytes());
//		 System.out.println("despues del write");
//		 outputStream.flush();
//		 outputStream.close();
//		 System.out.println("despues de escribirlo");
//		 System.out.println("Devuelve respuesta HTML: " + htmlResponse);
//		 httpExchange.sendResponseHeaders(200, htmlResponse.length());
//		 System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
//		httpExchange.sendResponseHeaders(204, -1);
//		System.out.println("El servidor devuelve codigo 204");

	    
	    //Opción 3: Responde a un post con texto plano.
	    String responseMessage = "POST recibido correctamente: " + requestParamValue;
	    byte[] responseBytes = responseMessage.getBytes("UTF-8");
	    
		httpExchange.sendResponseHeaders(200, responseBytes.length);
		System.out.println("El servidor devuelve codigo 200");
		
		// Enviar respuesta
	    OutputStream outputStream = httpExchange.getResponseBody();
	    outputStream.write(responseBytes);
	    outputStream.flush();
	    outputStream.close();
	    
	    System.out.println("Respuesta enviada al cliente: " + responseMessage);

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion POST (ejemplo: insertar en una base de datos lo que nos envia el
		// cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handlePutResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

		System.out.println("El servidor pasa a procesar el body de la peticion PUT: " + requestParamValue);

		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
		// OutputStream outputStream = httpExchange.getResponseBody();
		// String htmlResponse = "Parametro/s PUT: " + requestParamValue + " -> Se
		// procesara por parte del servidor";
		// outputStream.write(htmlResponse.getBytes());
		// outputStream.flush();
		// outputStream.close();
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		// httpExchange.sendResponseHeaders(200, htmlResponse.length());
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion PUT (ejemplo: actualizar en una base de datos lo que nos envia
		// el cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handleDeleteResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

		System.out.println("El servidor pasa a procesar el body de la peticion DELETE: " + requestParamValue);

		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
		// OutputStream outputStream = httpExchange.getResponseBody();
		// String htmlResponse = "Parametro/s DELETE: " + requestParamValue + " -> Se
		// procesara por parte del servidor";
		// outputStream.write(htmlResponse.getBytes());
		// outputStream.flush();
		// outputStream.close();
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		// httpExchange.sendResponseHeaders(200, htmlResponse.length());
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion DELETE (ejemplo: borrar de una base de datos lo que nos indica
		// el cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	// FIN BLOQUE RESPONSE

}

//		CODIGO DEL VIDEO DE TEORIA
//String requestParamValue = null;
//if("GET".equals(httpExchange.getRequestMethod())) {
//	requestParamValue = handleGetRequest(httpExchange);
//	handleGETResponse(httpExchange, requestParamValue);
//} else if("POST".equals(httpExchange.getRequestMethod())) {
//	requestParamValue = handlePostRequest(httpExchange);
//	handlePOSTResponse(httpExchange, requestParamValue);
//}
//}
//private String handleGetRequest(HttpExchange httpExchange) {
//System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
//return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
//}
//
//private String handlePostRequest(HttpExchange httpExchange) {
//System.out.println("Recibida URI tipo POST: " + httpExchange.getRequestBody().toString());
//InputStream is = httpExchange.getRequestBody();
//InputStreamReader isr = new InputStreamReader(is);
//BufferedReader br = new BufferedReader(isr);
//StringBuilder sb = new StringBuilder();
//String linea;
//try {
//	while((linea = br.readLine()) != null) {
//		sb.append(linea);
//	}
//	br.close();
//}catch(IOException e) {
//	e.printStackTrace();
//}
////return httpExchange.getRequestBody().toString();
//System.out.println(sb.toString());
//return sb.toString();
//}
//
//private void handleGETResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
//OutputStream outputStream = httpExchange.getResponseBody();
//String htmlResponse = "<html><body><h1>Hola " + requestParamValue + "</h1></body></html>";
//httpExchange.sendResponseHeaders(200, htmlResponse.length());
//outputStream.write(htmlResponse.getBytes());
//outputStream.flush();
//outputStream.close();
//System.out.println("Devuelve respuesta HTML: " + htmlResponse);
//}
//
//private void handlePOSTResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
//String htmlResponse = "Parametro/s POST: " + requestParamValue + " --> Se procesará por parte del servidor.";
//byte[] responseBytes = htmlResponse.getBytes("UTF-8");
//
//httpExchange.sendResponseHeaders(200, htmlResponse.length());
//OutputStream outputStream = httpExchange.getResponseBody();
//outputStream.write(responseBytes);
//outputStream.flush();
//outputStream.close();
//System.out.println("Devuelve respuesta HTML: " + htmlResponse);
//}
