package es.florida.mp2.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;

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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private String directorioDatos = "energumenos";
	private String host = "smtp.office365.com";
	private String port = "587";
	private String emailRemitente = "rsanz@florida-uni.es";
	private String emailDestino = "rsanz@florida-uni.es";
	private String ficheroLog = "log.txt";

	@GetMapping("servidor/mostrarTodos")
	String mostrarTodos() {
		ficheroLog(
				Thread.currentThread().getName() + " : " + "El servidor pasa a procesar la peticion GET: mostrarTodos");
		ficheroLog(Thread.currentThread().getName() + " : " + "Solicitada lista completa.");
		File directorio = new File(directorioDatos);
		String[] lista = directorio.list(new FiltroExtension(".txt"));
		String strHTML = "";
		for (String elemento : lista) {
			strHTML = strHTML + elemento.split(".txt")[0] + "<br>";
		}
		String htmlResponse = "<html><body>" + strHTML + "</body></html>";
		return htmlResponse;
	}

	@GetMapping("servidor/mostrarUno")
	String mostrarUno(@RequestParam(value = "alias") String strAlias) {
		System.out.println("Solicitado alias especifico.");
		ficheroLog(Thread.currentThread().getName() + " : " + "Solicitado alias especifico.");
		File fichero = new File(directorioDatos + File.separator + strAlias + ".txt");
		String strHTML = "";
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			strAlias = br.readLine();
			String nombre = br.readLine();
			String fecha_nac = br.readLine();
			String nacionalidad = br.readLine();
			String imagenBase64 = br.readLine();
			strHTML = strHTML + "<b>ALIAS: </b>" + strAlias + "<br>";
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
		String htmlResponse = "<html><body>" + strHTML + "</body></html>";
		return htmlResponse;
	}

	@PostMapping("servidor/nuevo")
	ResponseEntity<Object> postBody(@RequestBody String cuerpoPeticion) {
		JSONObject obj = new JSONObject(cuerpoPeticion);
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
			ficheroLog(Thread.currentThread().getName() + " : " + "El servidor escribe el nuevo fichero.");
			//envioMail(alias, nombreCompleto, fechaNacimiento, nacionalidad, imagenBase64);
		} catch (IOException e) {
			e.printStackTrace();
			ficheroLog(Thread.currentThread().getName() + " : " + "Error");
			ficheroLog(Thread.currentThread().getName() + " : " + e.toString());
		}
		return ResponseEntity.noContent().header("Content-Length", "0").build();
	}

	
	
	
	
	/**
	 * Método para enviar un email tomando los datos de los atributos de la clase (host, puerto, remitente y destino)
	 * 
	 * @param alias String con el alias del sujeto
	 * @param nombreCompleto String con el nombre completo del sujeto
	 * @param fechaNacimiento String con la fecha de nacimiento del sujeto
	 * @param nacionalidad String con nacionalidad del sujeto
	 * @param imagenBase64 String con la imagen codificada en base 64
	 */
	private void envioMail(String alias, String nombreCompleto, String fechaNacimiento, String nacionalidad, String imagenBase64) {
		System.out.println("Llamada e envioMail");
		ficheroLog(Thread.currentThread().getName() + " : " + "Llamada e envioMail");
		try {
			
//			Scanner teclado = new Scanner(System.in);
//			String password = teclado.nextLine();
			Console console = System.console();
			String password = String.valueOf(console.readPassword());
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
