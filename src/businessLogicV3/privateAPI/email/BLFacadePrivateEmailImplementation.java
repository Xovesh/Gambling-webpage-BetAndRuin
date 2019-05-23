package businessLogicV3.privateAPI.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.User;

public class BLFacadePrivateEmailImplementation implements BLFacadePrivateEmail{
	private DataAccess dataManager;
	private BLFacadePrivate privateAPI;
	
	public BLFacadePrivateEmailImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
		this.privateAPI = privateAPI;
	}

	/**
	 * This function sends an email to a user to recover the password
	 * 
	 * @param username
	 *            of the user
	 */
	@Override
	public boolean recoverPassword(String username, String language) {
		User user = privateAPI.getUsers().retrieveUser(username);
		if (user == null) {
			return false;
		}
		if (!language.equals("ES") && !language.equals("EUS") && !language.equals("EN")) {
			return false;
		}
		String token = privateAPI.getOthers().createToken(5);
		String FROM = "support@betandruin.eus";
		String FROMNAME = "Bet And Ruin Support Team";
		String TO = user.getEmail();
		String SMTP_USERNAME = "support@betandruin.eus";
		String SMTP_PASSWORD = "6M0AtSkO";
		String HOST = "mail.betandruin.eus";
		int PORT = 587;
		String recoveryLink = privateAPI.getOthers().createToken(50);
		String link = "www.betandruin.eus/" + language + "/passwordrecovery/" + recoveryLink;
		String SUBJECT = "Bet and Ruin Password Recovery";
		if (language.equals("ES")) {

			SUBJECT = "Bet and Ruin Recuperar Contraseña";

		} else if (language.equals("EUS")) {
			SUBJECT = "Bet and Ruin Pasahitza Eguneratu";
		}
		String BODY = String.join(System.getProperty("line.separator"), "<h1>Bet And Ruin Support team</h1>",
				"<p>Hello " + username + ",</p>", "<p>We have received a request for changing your password.</p>",
				"<br>", "<p>This is your verification code: " + token + "</p>",
				"<p>You can use this <a href=\"" + link + "\">link</a> to modify the password<p>", link);
		if (language.equals("ES")) {
			BODY = String.join(System.getProperty("line.separator"), "<h1>Bet And Ruin Equipo de Soporte</h1>",

					"<p>Hola " + username + ",</p>", "<p>Hemos recibido una petición para cambiar la contraseña.</p>",
					"<br>", "<p>Este es tu código de verificación: " + token + "</p>",
					"<p>Puedes usar este <a href=\"" + link + "\">link</a> para modificar la contraseña<p>", link);

		} else if (language.equals("EUS")) {
			BODY = String.join(System.getProperty("line.separator"), "<h1>Bet And Ruin taldea </h1>",
					"<p>Kaixo " + username + ",</p>", "<p>Eskaera bat izan dugu pasahitza eguneratzeko</p>",
					"<br>", "<p>Hau zure berifikazio kodea da: " + token + "</p>", "<p><a href=\""
							+ link + "\">Link</a> hau erabili ahal dezakezu pasahitza eguneratzeko<p>",
					link);
		}

		boolean success = dataManager.getDataAccessEmail().passwordReset(username, user.getEmail(), token, recoveryLink);

		if (!success) {
			return false;
		}
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", PORT);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(props);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM, FROMNAME));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			msg.setSubject(SUBJECT);
			msg.setContent(BODY, "text/html");

			Transport transport = session.getTransport();

			try {
				System.out.println("Sending...");
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
				transport.sendMessage(msg, msg.getAllRecipients());
				System.out.println("Email sent!");
			} catch (Exception ex) {
				System.out.println("The email was not sent.");
				System.out.println("Error message: " + ex.getMessage());
				return false;
			} finally {
				transport.close();
			}
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
}
