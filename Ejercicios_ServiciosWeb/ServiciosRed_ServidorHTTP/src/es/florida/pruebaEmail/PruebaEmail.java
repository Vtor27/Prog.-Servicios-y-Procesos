package es.florida.pruebaEmail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
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

public class PruebaEmail {

	public static void envioMail(String mensaje, String asunto, String email_remitente, String email_remitente_pass,
			String host_email, String port_email, String[] email_destino, String[] anexo)
			throws UnsupportedEncodingException, MessagingException {

		System.out.println("Envio de correo");
		System.out.println("--> Remitente: " + email_remitente);
		for (int i = 0; i < email_destino.length; i++) {
			System.out.println(" -> Destino " + (i + 1) + ": " + email_destino[i]);
		}
		System.out.println(" > Asunto: " + asunto);
		System.out.println(" > Mensaje: " + mensaje);

		// Recogemos las propiedades necvesarias del sistema para hacer la conexión con
		// el servidor.
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host_email);
		props.put("mail.smtp.user", email_remitente);
		props.put("mail.smtp.clave", email_remitente_pass);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");		//Al utilizar tls el puerto es 587
		props.put("mail.smtp.port", port_email);

		Session session = Session.getDefaultInstance(props);

		// Se crea el mensaje, con la sesión, el autor del correo, el tipo de
		// mensaje(envio) y quien lo va a recibir(en este caso un solo destinatario) y
		// el asunto del correo.
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(email_remitente));
		message.addRecipients(Message.RecipientType.TO, email_destino[0]);
		message.setSubject(asunto);
		
		//Ahora se crean las partes del mensaje.
		BodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setText(mensaje);
		
		BodyPart messageBodyPart2 = new MimeBodyPart();
		FileDataSource src = new FileDataSource(anexo[0]);	//????
		messageBodyPart2.setDataHandler(new DataHandler(src));
		messageBodyPart2.setFileName(anexo[0]);
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);
		multipart.addBodyPart(messageBodyPart2);
		
		message.setContent(multipart);
		
		Transport transport = session.getTransport("smtp");
		transport.connect(host_email, email_remitente, email_remitente_pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		//Contraseña de aplicacion dpba ufri lyqy vyye
		System.out.println("Prueba Email con Java");
		
		String strMensaje = "Mensaje";
		String strAsunto = "Asunto";
		String emailRemitente = "vtor2727@gmail.com";
		
		System.out.print("Introduce la contraseña: ");
		String emailRemitentePass = scanner.nextLine();
		String hostEmail = "smtp.gmail.com";
		String portEmail = "587";	
		String[] emailDestino = {"vigode@alumnatflorida.es"};
		String[] anexo = {"C:/Users/vtorg/OneDrive/Escritorio/pruebaTxT.txt"};	//Ruta donde esta el archivo que va a enviar
		
		try {
				envioMail(strMensaje, strAsunto,emailRemitente ,emailRemitentePass , hostEmail, portEmail, emailDestino, anexo);
				System.out.println("Mensaje enviado a " + emailDestino + "....");
			
		}catch(UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		scanner.close();
	}
}
