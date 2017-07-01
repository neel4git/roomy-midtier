package com.Roomy.Util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class MailUtil {

	public String generateCustomeWelcomeMail(
			@RequestParam(value = "to") String to,
			@RequestParam(value = "subject") String subject,
			@RequestParam(value = "mailBody") String mailBody) {

		final String username = "raghu.se@gmail.com";
		final String password = "anjisoumya";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"muralidharan.dharan9@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(mailBody);

			Transport.send(message);

			return "Done";

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
