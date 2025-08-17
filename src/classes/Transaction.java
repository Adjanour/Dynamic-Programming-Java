package classes;

import java.util.Set;

public class Transaction {
    private final int id;
    private final Set<Integer> items;

    public Transaction(int id, Set<Integer> items) {
        this.id = id;
        this.items = Set.copyOf(items); // defensive copy, immutability
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getItems() {
        return items;
    }
}
