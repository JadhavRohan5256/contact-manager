package contact.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
	public boolean sendMessage(String messageSubject, String messageTo, String message) {
		String subject = "Conatct Manager - " + messageSubject;
		String from = "birthdate2001@gmail.com";
		final String username = "birthdate2001@gmail.com";
		final String password = "osjetihyhczrxwgz";
		final String host = "smtp.gmail.com";
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
			
		});
		
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(messageTo));
			mimeMessage.setSubject(subject);
			mimeMessage.setContent(message, "text/html");
			
			Transport.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
