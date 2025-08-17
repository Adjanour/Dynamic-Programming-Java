package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TransactionDatabase {
    private final List<Transaction> transactions;

    public TransactionDatabase(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public static TransactionDatabase fromFile(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        int transactionID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactionID++;
                String[] transactionSplit = line.split(" ");

                Set<Integer> items = new HashSet<>();
                for (String uniqueItem : transactionSplit) {
                    items.add(Integer.parseInt(uniqueItem));
                }

                transactions.add(new Transaction(transactionID, items));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }

        return new TransactionDatabase(transactions);
    }
}
