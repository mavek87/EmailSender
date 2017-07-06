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
    private static final short PORT_GMAIL = 587;
    private static final String SECOND_HOST_GMAIL = "smtp.googlemail.com";
    private static final short SECOND_PORT_GMAIL = 465;
    private static final String HOST_ARUBA = "smtps.aruba.it";
    private static final short PORT_ARUBA = 465;
    private static final String DEFAULT_USERNAME_GMAIL = "infoeinternetstaff@gmail.com";
    private static final String DEFAULT_USERNAME_ARUBA = "matteo.veroni@pec.giuffre.it";
    private static final String DEFAULT_PASSWORD = "password";

    private final String username;
    private final String password;
    private final Session session;

    public enum TransportProtocol {
        SSL, TLS;
    }

    private final TransportProtocol serverTransferProtocol;

    public MailServer() {
        this(null, DEFAULT_USERNAME_GMAIL, DEFAULT_PASSWORD);
    }

    public MailServer(String password) {
        this(null, DEFAULT_USERNAME_GMAIL, password);
    }

    public MailServer(TransportProtocol protocol, String username, String password) {
        this.username = username;
        this.password = password;

        if (protocol != null) {
            this.serverTransferProtocol = protocol;
        } else {
            this.serverTransferProtocol = TransportProtocol.SSL;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");

        switch (this.serverTransferProtocol) {
            case SSL:
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.host", SECOND_HOST_GMAIL);
                props.put("mail.smtp.port", SECOND_PORT_GMAIL);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", HOST_GMAIL);
                props.put("mail.smtp.port", PORT_GMAIL);
                break;
        }

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public boolean testLogin() throws NoSuchProviderException, MessagingException {
        Transport transport = session.getTransport("smtp");
        transport.connect(SECOND_HOST_GMAIL, SECOND_PORT_GMAIL, username, password);
        boolean canLogin = transport.isConnected();
        transport.close();
        return canLogin;
    }

    public void sendEmail(String destinationAddress, String title, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAddress));
        message.setSubject(title);
        message.setContent(body, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("\nThe email is being sent to: " + destinationAddress);
    }
}
