package training.supportbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AccountCreate {

    private static final Logger LOGGER = LogManager.getLogger();

    public static ArrayList<Account> readCSV(String path) {
        String line = "";
        String splitBy = ",";
        int count = 0;

        ArrayList<Account> myAccounts = new ArrayList<>();
        ArrayList<String> listOfNames = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            while ((line = reader.readLine()) != null) {
                count ++;
                String[] transactions = line.split(splitBy);

                if (transactions[1].equals("From")) {
                    continue;
                }

                // Check if the 'From' names already exists in listOfNames, if not add to list of myAccounts & listOfNames
                if (!listOfNames.contains(transactions[1])) {
                    myAccounts.add(new Account(transactions[1]));
                    listOfNames.add(transactions[1]);
                }

                // Check if the 'To' names already exists in listOfNames, if not add to list of myAccounts & listOfNames
                if (!listOfNames.contains(transactions[2])) {
                    myAccounts.add(new Account(transactions[2]));
                    listOfNames.add(transactions[2]);
                }

                BigDecimal cash;
                try {
                    cash = new BigDecimal(transactions[4]);
                } catch (NumberFormatException nfe) {
                    LOGGER.error("AMOUNT ERROR: Not a valid type for 'Amount': ERROR on line " + count + " of CSV file");
                    continue;
                }

                myAccounts.stream()
                        .filter(x -> x.getAccountName().equals(transactions[1]))
                        .forEach(x -> x.addToBalance(cash));

                myAccounts.stream()
                        .filter(x -> x.getAccountName().equals(transactions[2]))
                        .forEach(x -> x.subtractFromBalance(cash));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myAccounts;

    }

    public static boolean searchListOfNames(ArrayList<Account> list, String input) {

        if(input.equals("all")) {
            return true;
        }

        for(Account account : list) {
            if (account.getAccountName().equals(input)) {
                return true;
            }
        }

        LOGGER.error("Wrong input, please either type 'all' for all accounts or the name of the account!");
        return false;
    }
}
