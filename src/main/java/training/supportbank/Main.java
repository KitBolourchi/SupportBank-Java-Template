package training.supportbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String args[]) {
        String CSV = "/Users/kbolourc/Training/bootcampPersonalGitHub/Week1/SupportBank-Java-Template/Transactions2014.csv";
        ArrayList<Account> listOfAccounts = readCSV(CSV);

        for (int i = 0; i < listOfAccounts.size(); i++) {
            System.out.println(listOfAccounts.get(i).getAccountName());
        }

        System.out.println("Test");
    }

    // Function which reads the CSV file and returns an ArrayList of Account type Objects containing the names
    public static ArrayList<Account> readCSV(String path) {
        String line = "";
        String splitBy = ",";

        ArrayList<Account> myAccounts = new ArrayList<>();
        ArrayList<String> listOfNames = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            int count = 0;
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

                Float cash = Float.parseFloat(transactions[4]);

                System.out.println(cash);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myAccounts;

    }
}