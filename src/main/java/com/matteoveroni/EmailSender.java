package com.matteoveroni;

import java.io.File;
import java.io.InputStreamReader;
import javax.mail.MessagingException;

/**
 * @author: Matteo Veroni
 */
public class EmailSender {

    private static final MailServer MAIL_SERVER = new MailServer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String path;

        if (args.length == 1) {
            path = args[0];
            
            File emailBody = new File(path);
            if(emailBody.isFile()){
                
                
                Reader ir = new InputStreamReader();
                
                sendMessage("a@b.it", "Title123", emailBody);
            }else{
                throw new RuntimeException("Invalid html passed as email body");
            }
            
            sendMessage();
        } else {
            sendTestMessage();
        }

    }

    private static void sendTestMessage() {
        String destinationAddress = "matver87@gmail.com";
        String title = "Title";
        String message = "<html><body><h1>Testo</h1><p>provolona3</p></body></html>";
        sendMessage(destinationAddress, title, message);
    }

    private static void sendMessage(String destinationAddress, String title, String message) {
        try {
            MAIL_SERVER.sendEmail(destinationAddress, title, message);
            System.out.println("Email sent");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
