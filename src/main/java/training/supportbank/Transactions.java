package training.supportbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Transactions {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void readTransactions(String name, String CSVpath)  {
        String line = "";
        String splitBy = ",";
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSVpath));

            while ((line = reader.readLine()) != null) {
                count++;
                String[] transactions = line.split(splitBy);

                if (transactions[1].equals("From")) {
                    continue;
                }

                if(transactions[1].equals(name) || transactions[2].equals(name)) {
                    System.out.println("Date: " + transactions[0]
                            + "    |     Amount: " + transactions[4]
                            + "    |     From: " + transactions[1]
                            + "    |     To: " + transactions[2]
                            + "    |     Narrative: " + transactions[3]);
                }

                if(transactions[1].equals(name) || transactions[2].equals(name)) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    try {
                        LocalDate.parse(transactions[0], dateFormat);
                    } catch (DateTimeParseException e) {
                        String errorMessage = "DATE ERROR: not a valid type for 'Date': ERROR on line " + count + " of CSV file";
                        LOGGER.error(errorMessage);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
