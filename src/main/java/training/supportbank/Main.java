package training.supportbank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
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
            case "json":
                listOfAccounts = AccountCreate.readJSON(path);
                break;
            case "xml":
                listOfAccounts = AccountCreate.readXML(path);
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
        } while (!AccountCreate.searchListOfNames(listOfAccounts, input));

        if (input.equals("all")) {
            for (int i = 0; i < listOfAccounts.size(); i++) {
                String name = listOfAccounts.get(i).getAccountName();
                BigDecimal cash = listOfAccounts.get(i).getBalance();
                System.out.println(name + " £" + cash);
            }
        }
        switch (path.split("\\.")[1]) {
            case "csv":
                Transactions.readTransactions(input, path);
                break;
            case "json":
                Transactions.transactionsJSON(input, path);
                break;
            case "xml":
                Transactions.transactionsXML(input, path);
                break;
            default:
                LOGGER.fatal("Incorrect filetype");
        }
    }
}