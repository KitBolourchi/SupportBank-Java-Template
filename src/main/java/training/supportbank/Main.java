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
                listOfAccounts = AccountCreate.readCSV(path);
                break;
//            case "json":
//                break;
//            case "xml":
//                break;
            default:
                LOGGER.fatal("Incorrect filetype");
                return;
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("Choose between listing all or specific account: 'all' || 'Account name'");
            input =  scanner.nextLine();
        } while (!AccountCreate.searchListOfNames(listOfAccounts, input));

        if (input.equals("all")) {
            for (int i = 0; i < listOfAccounts.size(); i++) {
                String name = listOfAccounts.get(i).getAccountName();
                BigDecimal cash = listOfAccounts.get(i).getBalance();
                System.out.println(name + " £" + cash);
            }
        } else {
            Transactions.readTransactions(input, path);
        }
    }
}