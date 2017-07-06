package com.matteoveroni;

import javax.mail.MessagingException;

/**
 *
 * @author Matteo Veroni
 */
public class SendEmailJob implements Runnable {

    private final MailServer mailServer;
    private final String destinationAddress;
    private final String title;
    private final String message;

    public SendEmailJob(MailServer mailServer, String destinationAddress, String title, String message) {
        this.mailServer = mailServer;
        this.destinationAddress = destinationAddress;
        this.title = title;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            mailServer.sendEmail(destinationAddress, title, message);
        } catch (MessagingException ex) {
            System.out.println("\nError trying to send email to: " + destinationAddress + "\nDetailed error: " + ex);
        }
    }
}
