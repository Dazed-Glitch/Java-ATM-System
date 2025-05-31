import java.util.ArrayList;
import java.util.List;

public class ATMUser {
    private String username;
    private String pin;
    private double balance;
    private List<String> transactionHistory;

    public ATMUser(String username, String pin, double balance) {
        this.username = username;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited $" + String.format("%.2f", amount));
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdrew $" + String.format("%.2f", amount));
            return true;
        }
        return false;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String entry) {
        transactionHistory.add(entry);
    }
} 