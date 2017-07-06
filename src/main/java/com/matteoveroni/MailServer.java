package com.matteoveroni;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
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

    private static final String HOST_GMAIL = "smtp.gmail.com";
    private static final String PORT_GMAIL = "587";
    private static final String HOST_ARUBA = "smtps.aruba.it";
    private static final String PORT_ARUBA = "465";
    private static final String DEFAULT_USERNAME_GMAIL = "infoeinternetstaff@gmail.com";
    private static final String DEFAULT_USERNAME_ARUBA = "matteo.veroni@pec.giuffre.it";
    private static final String DEFAULT_PASSWORD = "password";
    private final String username;
    private final String password;
    private final Session session;

    public MailServer() {
        this(DEFAULT_USERNAME_GMAIL, DEFAULT_PASSWORD);
    }

    public MailServer(String password) {
        this(DEFAULT_USERNAME_GMAIL, password);
    }

    public MailServer(String username, String password) {
        this.username = username;
        this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_GMAIL);
        props.put("mail.smtp.port", PORT_GMAIL);

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public boolean isConnected() {
        try {
            return session.getTransport("smtp").isConnected();
        } catch (NoSuchProviderException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendEmail(String destinationAddress, String title, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAddress));
        message.setSubject(title);
        message.setContent(body, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("The email is being sent to " + destinationAddress);
    }
}
