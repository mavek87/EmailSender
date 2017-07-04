package com.matteoveroni;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.mail.MessagingException;

/**
 * @author: Matteo Veroni
 */
public class EmailSender {

    private static MailServer MAIL_SERVER;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String pwd = requestPasswordFromConsole();

        if (pwd.trim().isEmpty()) {
            MAIL_SERVER = new MailServer();
        } else {
            MAIL_SERVER = new MailServer(pwd);
        }

        if (args.length == 1) {

            String path = args[0];
            File file = new File(path);
            try {
                if (file.isFile()) {
                    String emailBodyHtml = readFile(path, Charset.forName("UTF-8"));
                    sendMessage("matver87@gmail.com", "Title123", emailBodyHtml);
                } else {
                    throw new RuntimeException("File " + file + " doesn\'t exist");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error reading file " + ex);
            }

        } else {
            sendTestMessage();
        }
    }

    private static String requestPasswordFromConsole() {
        String pwd = "";
        try {
            Console console = System.console();
            if (console != null) {
                char[] emailPasswordChars = console.readPassword("Email password: ");
                pwd = new String(emailPasswordChars);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pwd;
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static void sendMessage(String destinationAddress, String title, String message) {
        try {
            MAIL_SERVER.sendEmail(destinationAddress, title, message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void sendTestMessage() {
        String destinationAddress = "matver87@gmail.com";
        String title = "Title";
        String message = "<html><body><h1>Testo</h1><p>provolona3</p></body></html>";
        sendMessage(destinationAddress, title, message);
    }
}
