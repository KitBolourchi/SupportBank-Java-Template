package training.supportbank;

import java.math.BigDecimal;

public class Account {

    private String accountHolder;
    private BigDecimal balance = BigDecimal.valueOf(0);

    public Account(String name) {
        this.accountHolder = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountName() {
        return accountHolder;
    }

    public void addToBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public void subtractFromBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }
}
