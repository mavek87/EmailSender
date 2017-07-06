package com.matteoveroni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Matteo Veroni
 */
public class DAO {

    private static final Set<String> MAIL_ADDRESSES = new HashSet<>();

    private final String csvFilePath;

    public DAO() {
        this("Data" + File.separator + "emails.csv");
    }

    public DAO(String csvFilePath) {
        try {
            Paths.get(csvFilePath);
        } catch (InvalidPathException | NullPointerException ex) {
            throw new RuntimeException(ex);
        }
        this.csvFilePath = csvFilePath;
        populateAddressesFromCSVFile();
    }

    private void populateAddressesFromCSVFile() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFilePath));
            while ((line = br.readLine()) != null) {
                String[] emailAddresses = line.split(cvsSplitBy);
                MAIL_ADDRESSES.add(emailAddresses[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Iterator getAddressesIterator() {
        return MAIL_ADDRESSES.iterator();
    }
}
