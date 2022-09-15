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
import java.util.Scanner;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void main(String args[]) {
        String path = args[0];
        ArrayList<Account> listOfAccounts;

        switch (path.split("\\.")[1]) {
            case "csv":
                listOfAccounts = readCSV(path);
                break;
            default:
                LOGGER.fatal("Incorrect filetype");
                return;
        }


        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("Choose between listing all or specific account: 'all' || 'Account name'");
            input =  scanner.nextLine();
        } while (!searchListOfNames(listOfAccounts, input));

        if (input.equals("all")) {
            for (int i = 0; i < listOfAccounts.size(); i++) {
                String name = listOfAccounts.get(i).getAccountName();
                BigDecimal cash = listOfAccounts.get(i).getBalance();
                System.out.println(name + " £" + cash);
            }
        } else {
            readTransactions(input, path);
        }
    }

    // Function which reads the CSV file and returns an ArrayList of Account type Objects containing the names
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

    //Function which searches the list of names to use in the Do While loop
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

    //Function which reads all the transactions for the specific account specified by the name
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
                    isValidDate(transactions[0], count);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void isValidDate(String currDate, int count) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate.parse(currDate, dateFormat);
        } catch (DateTimeParseException e) {
            String errorMessage = "DATE ERROR: not a valid type for 'Date': ERROR on line " + count + " of CSV file";
            LOGGER.error(errorMessage);
        }
    }
}