package com.matteoveroni;

import java.util.Properties;
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

    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String DEFAULT_USERNAME = "infoeinternetstaff@gmail.com";
    private static final String DEFAULT_PASSWORD = "password";
    private final String username;
    private final String password;
    private final Session session;

    public MailServer() {
        this(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public MailServer(String password) {
        this(DEFAULT_USERNAME, password);
    }

    public MailServer(String username, String password) {
        this.username = username;
        this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public boolean isConnected() throws NoSuchProviderException {
        return session.getTransport("smtp").isConnected();
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
