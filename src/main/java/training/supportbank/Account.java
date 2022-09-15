package training.supportbank;

public class Account {

    private String accountHolder;
    private int balance = 0;

    public Account(String name) {
        this.accountHolder = name;
    }

    public int getBalance() {
        return balance;
    }

    public String getAccountName() {
        return accountHolder;
    }

    public void addToBalance(float amount) {
        this.balance += amount;
    }

    public void subtractFromBalance(float amount) {
        this.balance -= amount;
    }
}
