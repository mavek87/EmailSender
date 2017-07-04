package com.matteoveroni;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import javax.mail.MessagingException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author: Matteo Veroni
 *
 * http://commons.apache.org/proper/commons-cli/usage.html
 *
 */
public class EmailSender {

    private static MailServer MAIL_SERVER;
    private static final DAO DATA = new DAO();

    private static final String OPTION_ABBR_TITLE = "t";
    private static final String OPTION_ABBR_BODY = "b";
    private static final String OPTION_ABBR_BODY_FROM_FILE = "f";
    private static final String OPTION_ABBR_PASSWORD = "p";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String emailTitle;
        String emailBody;

        Options cmdOptions = buildCommandLineOptions();
        CommandLineParser cmdParser = new DefaultParser();
        try {
            CommandLine cmd = cmdParser.parse(cmdOptions, args);
            emailTitle = cmd.getOptionValue(OPTION_ABBR_TITLE);
            emailBody = cmd.getOptionValue(OPTION_ABBR_BODY);

            try {
                if (new File(emailBody).isFile()) {
                    emailBody = readFile(emailBody, Charset.forName("UTF-8"));
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error reading file " + ex);
            }

            if (cmd.hasOption(OPTION_ABBR_PASSWORD)) {
                String pwd = requestPasswordFromConsole();
                MAIL_SERVER = new MailServer(pwd);
            } else {
                MAIL_SERVER = new MailServer();
            }

            sendMessage(emailTitle, emailBody);

        } catch (RuntimeException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    private static Options buildCommandLineOptions() {
        Options options = new Options();
        Option optionTitle = Option.builder(OPTION_ABBR_TITLE)
                .required()
                .hasArg()
                .desc("set email title")
                .build();
        Option optionBody = Option.builder(OPTION_ABBR_BODY)
                .required()
                .hasArg()
                .desc("set email body")
                .build();
        Option optionPassword = Option.builder(OPTION_ABBR_PASSWORD)
                .desc("set email password")
                .build();
        options.addOption(optionTitle);
        options.addOption(optionBody);
        options.addOption(optionPassword);
        return options;
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

    private static void sendMessage(String title, String message) {
        Iterator<String> destAddressesIterator = DATA.getAddressesIterator();

        while (destAddressesIterator.hasNext()) {
            String destinationAddress = destAddressesIterator.next();
            System.out.println("\ndestination address: " + destinationAddress);
            try {
                MAIL_SERVER.sendEmail(destinationAddress, title, message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendTestMessage() {
        String title = "Title";
        String message = "<html><body><h1>Testo</h1><p>provolona3</p></body></html>";
        sendMessage(title, message);
    }
}
