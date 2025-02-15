package es.florida.mp2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GestorHTTP implements HttpHandler {

	private String directorioDatos = "energumenos";
	private String host = "smtp.office365.com";
	private String port = "587";
	private String emailRemitente = "rsanz@florida-uni.es";
	private String emailDestino = "rsanz@florida-uni.es";
	private String ficheroLog = "log.txt";

	/**
	 * Método obligatorio de la interfaz HttpHandler que recibe la petición HTTP y la gestiona según el tipo que sea (GET, POST, PUT o DELETE)
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		// Habilitar accesos CORS (intercambio de recursos de origen cruzado) para
		// peticiones POST, PUT y DELETE
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
		if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) { // Caso PUT y DELETE se pide antes
																			// confirmacion desde cliente
			httpExchange.sendResponseHeaders(204, -1); // Codigo Ok, no devuelve contenido, preparado para POST, PUT o
														// DELETE
			return;
		}

		System.out.print("Peticion recibida: Tipo ");
		ficheroLog(Thread.currentThread().getName() + " : " + Thread.currentThread().getName() + " : " + "Peticion recibida: Tipo ");
		String requestParamValue = null;
		if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("GET");
			ficheroLog(Thread.currentThread().getName() + " : " + "GET");
			requestParamValue = handleGetRequest(httpExchange);
			handleGetResponse(httpExchange, requestParamValue);
		} else if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("POST");
			ficheroLog(Thread.currentThread().getName() + " : " + "POST");
			requestParamValue = handlePostRequest(httpExchange);
			handlePostResponse(httpExchange, requestParamValue);
		} else {
			System.out.println("DESCONOCIDA");
			ficheroLog(Thread.currentThread().getName() + " : " + "DESCONOCIDA");
		}

	}

	// INICIO BLOQUE REQUEST

	/**
	 * Método para gestionar una petición de tipo GET
	 * 
	 * @param httpExchange Petición HTTP
	 * @return Devuelve un String con el text incluído en la petición HTTP.
	 */
	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		ficheroLog(Thread.currentThread().getName() + " : " + "Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		return httpExchange.getRequestURI().toString().split("/servidor/")[1];
	}

	/**
	 * Método para gestionar la petición HTTP de tipo POST
	 * 
	 * @param httpExchange Petición HTTP
	 * @return Devuelve un String con el cuerpo (body) de la petición POST
	 */
	private String handlePostRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo POST: " + httpExchange.getRequestBody().toString());
		ficheroLog(Thread.currentThread().getName() + " : " + "Recibida URI tipo POST: " + httpExchange.getRequestBody().toString());
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
			ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
		}
		return sb.toString();
	}

	// FIN BLOQUE REQUEST

	// INICIO BLOQUE RESPONSE

	/**
	 * Método para gestionar la respuesta a la petición HTTP de tipo GET
	 * 
	 * @param httpExchange Petición HTTP
	 * @param requestParamValue String con el texto de la petición (ruta indicada)
	 */
	private void handleGetResponse(HttpExchange httpExchange, String requestParamValue) {

		System.out.println("El servidor pasa a procesar la peticion GET: " + requestParamValue);
		ficheroLog(Thread.currentThread().getName() + " : " + "El servidor pasa a procesar la peticion GET: " + requestParamValue);

		String strHTML = "";
		if (requestParamValue.equals("mostrarTodos")) {
			System.out.println("Solicitada lista completa.");
			ficheroLog(Thread.currentThread().getName() + " : " + "Solictada lista completa.");
			File directorio = new File(directorioDatos);
			String[] lista = directorio.list(new FiltroExtension(".txt"));
			for (String elemento : lista) {
				strHTML = strHTML + elemento.split(".txt")[0] + "<br>";
			}
		} else if (requestParamValue.contains("mostrarUno?alias=")) {
			System.out.println("Solicitado alias especifico.");
			ficheroLog(Thread.currentThread().getName() + " : " + "Solicitado alias especifico.");
			String alias = requestParamValue.split("=")[1];
			File fichero = new File(directorioDatos + File.separator + alias + ".txt");
			try {
				FileReader fr = new FileReader(fichero);
				BufferedReader br = new BufferedReader(fr);
				alias = br.readLine();
				String nombre = br.readLine();
				String fecha_nac = br.readLine();
				String nacionalidad = br.readLine();
				String imagenBase64 = br.readLine();
				strHTML = strHTML + "<b>ALIAS: </b>" + alias + "<br>";
				strHTML = strHTML + "<b>NOMBRE: </b>" + nombre + "<br>";
				strHTML = strHTML + "<b>FECHA NACIMIENTO: </b>" + fecha_nac + "<br>";
				strHTML = strHTML + "<b>NACIONALIDAD: </b>" + nacionalidad + "<br>";
				strHTML = strHTML + "<b>FOTOGRAF&IacuteA: </b><br>";
				strHTML = strHTML + "<img src=\"" + imagenBase64 + "\"/>";
				br.close();
				fr.close();
			} catch (Exception e) {
				strHTML = "Alias desconocido :(";
				e.printStackTrace();
				ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
			}
		}
		OutputStream outputStream = httpExchange.getResponseBody();
		String htmlResponse = "<html><body>" + strHTML + "</body></html>";
		try {
			httpExchange.sendResponseHeaders(200, htmlResponse.length());
			//MAS CORRECTO SERIA:
			// byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
			// httpExchange.sendResponseHeaders(200, bytes.length);
			outputStream.write(htmlResponse.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
		}
		System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		ficheroLog(Thread.currentThread().getName() + " : " + "Devuelve respuesta HTML: " + htmlResponse);

	}

	/**
	 * Método para gestionar la respuesta que el servidor da a la petición HTTP de tipo POST
	 * 
	 * @param httpExchange Petición HTTP
	 * @param requestParamValue String con el cuerpo (body) de la petición POST
	 */
	private void handlePostResponse(HttpExchange httpExchange, String requestParamValue) {

		System.out.println("El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);
		ficheroLog(Thread.currentThread().getName() + " : " + "El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);

		JSONObject obj = new JSONObject(requestParamValue);
		String alias = (String) obj.get("alias");
		String nombreCompleto = (String) obj.get("nombreCompleto");
		String fechaNacimiento = (String) obj.get("fechaNacimiento");
		String nacionalidad = (String) obj.get("nacionalidad");
		String imagenBase64 = (String) obj.get("imagen");
		try {
			FileWriter fw = new FileWriter(directorioDatos + File.separator + alias + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(alias + "\n");
			bw.write(nombreCompleto + "\n");
			bw.write(fechaNacimiento + "\n");
			bw.write(nacionalidad + "\n");
			bw.write(imagenBase64);
			bw.close();
			httpExchange.sendResponseHeaders(204, -1);
			System.out.println("El servidor devuelve codigo 204");
			ficheroLog(Thread.currentThread().getName() + " : " + "El servidor devuelve codigo 204");
		} catch (IOException e) {
			e.printStackTrace();
			ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
			try {
				httpExchange.sendResponseHeaders(500, -1);
			} catch (IOException e1) {
				e1.printStackTrace();
				ficheroLog(Thread.currentThread().getName() + " : " + e1.toString());
			}
			System.out.println("El servidor devuelve codigo 500 (error)");
			ficheroLog(Thread.currentThread().getName() + " : " + "El servidor devuelve codigo 500 (error)");
		}
		envioMail(alias, nombreCompleto, fechaNacimiento, nacionalidad, imagenBase64);

	}

	// FIN BLOQUE RESPONSE

	/**
	 * Método para enviar un email tomando los datos de los atributos de la clase (host, puerto, remitente y destino)
	 * 
	 * @param alias String con el alias del sujeto
	 * @param nombreCompleto String con el nombre completo del sujeto
	 * @param fechaNacimiento String con la fecha de nacimiento del sujeto
	 * @param nacionalidad String con nacionalidad del sujeto
	 * @param imagenBase64 String con la imagen codificada en base 64
	 */
	private void envioMail(String alias, String nombreCompleto, String fechaNacimiento, String nacionalidad,
			String imagenBase64) {
		System.out.println("Llamada e envioMail");
		ficheroLog(Thread.currentThread().getName() + " : " + "Llamada e envioMail");
		try {
			
			JPasswordField pwd = new JPasswordField(20);
			JOptionPane.showConfirmDialog(null, pwd, "Introducir contraseña: ", JOptionPane.OK_CANCEL_OPTION);
			String password = new String(pwd.getPassword());
			String asunto = "Nuevo energúmeno";
			String mensaje = "<b>ALIAS: </b>" + StringEscapeUtils.escapeHtml4(alias) 
					+ "<br><b>NOMBRE COMPLETO: </b>" + StringEscapeUtils.escapeHtml4(nombreCompleto) 
					+ "<br><b>FECHA NACIMIENTO: </b>"+ StringEscapeUtils.escapeHtml4(fechaNacimiento) 
					+ "<br><b>NACIONALIDAD: </b>" + StringEscapeUtils.escapeHtml4(nacionalidad)
					+ "<br><br><br>";

			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", emailRemitente);
			props.put("mail.smtp.clave", password);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", port);

			Session session = Session.getDefaultInstance(props);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailRemitente));
			message.addRecipients(Message.RecipientType.TO, emailDestino);
			message.setSubject(asunto);

			BodyPart messageBodyPart1 = new MimeBodyPart();
//			messageBodyPart1.setText(mensaje); //Opcion con texto plano
			messageBodyPart1.setContent(mensaje, "text/html");
			
			String tipoFichero = imagenBase64.split("/")[1].split(";")[0];
			byte[] btDataFile =	Base64.decodeBase64(imagenBase64.split(",")[1]);
			OutputStream stream = new FileOutputStream(alias + "." + tipoFichero);
			stream.write(btDataFile);
			stream.close();
			BodyPart messageBodyPart2 = new MimeBodyPart();
		    DataSource src= new FileDataSource(alias + "." + tipoFichero);
		    messageBodyPart2.setDataHandler(new DataHandler(src));
		    messageBodyPart2.setFileName(alias + "." + tipoFichero);
		    System.out.println("Anexando fichero " + alias + "." + tipoFichero);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
			message.setContent(multipart);

			System.out.println("Inicio envio...");
			ficheroLog(Thread.currentThread().getName() + " : " + "Inicio envio...");
			
			Transport transport = session.getTransport("smtp");
			transport.connect(host, emailRemitente, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			System.out.println("Email enviado");
			ficheroLog(Thread.currentThread().getName() + " : " + "Email enviado");
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
			ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
		}

	}
	
	/**
	 * Método para añadir una línea al fichero de log
	 * 
	 * @param linea String con la línea a añadir al fichero de log
	 */
	public void ficheroLog(String linea) {
		try {
			FileWriter fw = new FileWriter(ficheroLog, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss - ").format(new java.util.Date());
			bw.write(timeStamp + linea + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
