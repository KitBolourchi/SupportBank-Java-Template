package training.supportbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String args[]) {
        String CSV = "/Users/kbolourc/Training/bootcampPersonalGitHub/Week1/SupportBank-Java-Template/Transactions2014.csv";
        ArrayList<Account> listOfAccounts = new ArrayList<>();

        listOfAccounts = readCSV(CSV);

        for (int i = 0; i < listOfAccounts.size(); i++) {
            System.out.println(listOfAccounts.get(i).getAccountName());
        }
    }

    private static ArrayList<Account> readCSV(String path) {
        String line = "";
        String splitBy = ",";

        ArrayList<Account> myAccounts = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            while ((line = reader.readLine()) != null) {
                String[] transactions = line.split(splitBy);
                if (!myAccounts.contains(transactions[1])) {
                    myAccounts.add(new Account(transactions[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myAccounts;
    }
}
