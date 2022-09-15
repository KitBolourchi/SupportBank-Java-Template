package training.supportbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String args[]) {
        String CSV = "/Users/kbolourc/Training/bootcampPersonalGitHub/Week1/SupportBank-Java-Template/Transactions2014.csv";
        ArrayList<Account> listOfAccounts = readCSV(CSV);
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
        }

        readTransactions(input, CSV);
    }

    // Function which reads the CSV file and returns an ArrayList of Account type Objects containing the names
    public static ArrayList<Account> readCSV(String path) {
        String line = "";
        String splitBy = ",";

        ArrayList<Account> myAccounts = new ArrayList<>();
        ArrayList<String> listOfNames = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            while ((line = reader.readLine()) != null) {
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

                BigDecimal cash = new BigDecimal(transactions[4]);

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
        return false;
    }

    //Function which reads all the transactions for the specific account specified by the name
    public static void readTransactions(String name, String CSVpath)  {
        String line = "";
        String splitBy = ",";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSVpath));

            while ((line = reader.readLine()) != null) {
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
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}