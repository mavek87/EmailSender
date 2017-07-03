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
 * @author: Matteo Veroni
 */
public class EmailSender {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String HOST = "smtp.gmail.com";
        String PORT = "587";
        String USER = "infoeinternetstaff@gmail.com";
        String PASS = "password";

        String SOURCE_MAIL_ADDRESS = USER;
        String DESTINATION_MAIL_ADDRESS = "matver87@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mamam"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(DESTINATION_MAIL_ADDRESS));
            message.setSubject("Titolo");
            message.setContent("<html><body><h1>Testo</h1><p>provolona3</p></body></html>", "text/html; charset=utf-8");
            Transport.send(message);

            System.out.println("The email is being sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
