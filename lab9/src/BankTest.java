import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Account {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;
    private int balance;

    public Account(int initialBalance) {
        this.id = idGenerator.getAndIncrement();
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }
}

class Bank {
    public void transfer(Account from, Account to, int amount) {
        if (from.equals(to) || amount <= 0) return;

        Account lockFirst = from.getId() < to.getId() ? from : to;
        Account lockSecond = from.getId() < to.getId() ? to : from;

        synchronized (lockFirst) {
            synchronized (lockSecond) {
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
    }

    public long getTotalBalance(List<Account> accounts) {
        long total = 0;
        for (Account acc : accounts) {
            total += acc.getBalance();
        }
        return total;
    }
}

public class BankTest {
    static void main() throws InterruptedException {
        Bank bank = new Bank();
        List<Account> accounts = new ArrayList<>();
        int numberOfAccounts = 100;
        int initialBalance = 1000;

        for (int i = 0; i < numberOfAccounts; i++) {
            accounts.add(new Account(initialBalance));
        }

        long totalBefore = bank.getTotalBalance(accounts);
        System.out.println("Total money BEFORE: " + totalBefore);

        int numberOfThreads = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Random random = new Random();

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                Account from = accounts.get(random.nextInt(numberOfAccounts));
                Account to = accounts.get(random.nextInt(numberOfAccounts));
                int amount = random.nextInt(50) + 1; // 1 to 50
                bank.transfer(from, to, amount);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long totalAfter = bank.getTotalBalance(accounts);
        System.out.println("Total money AFTER:  " + totalAfter);

        if (totalBefore == totalAfter) {
            System.out.println("SUCCESS! Money balance matches.");
        } else {
            System.err.println("ERROR! Money leak detected.");
        }
    }
}