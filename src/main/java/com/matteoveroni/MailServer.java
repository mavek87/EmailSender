package com.matteoveroni;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Matteo Veroni
 */
public class MailServer {

    private final static String HOST = "smtp.gmail.com";
    private final static String PORT = "587";

    private final static String USER = "infoeinternetstaff@gmail.com";
    private final static String PASS = "password";

    private final Session session;

    public MailServer() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });
    }

    public void sendEmail(String destinationAddress, String title, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAddress));
        message.setSubject(title);
        message.setContent(body, "text/html; charset=utf-8");
        Transport.send(message);

        System.out.println("The email is being sent!");
    }

}
