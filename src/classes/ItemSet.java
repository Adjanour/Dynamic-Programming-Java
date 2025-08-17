package classes;

import java.util.*;

public class ItemSet {
    private final List<Integer> items;          // Sorted list of items
    private final Set<Integer> transactionIds;

    public ItemSet(Set<Integer> items, Set<Integer> transactionIds) {
        this.items = new ArrayList<>(items);
        Collections.sort(this.items); // Always sorted
        this.transactionIds = new HashSet<>(transactionIds);
    }

    public List<Integer> getItems() {
        return items;
    }

    public Set<Integer> getTransactionIds() {
        return transactionIds;
    }

    public int getSupport() {
        return transactionIds.size();
    }

    @Override
    public String toString() {
        return items + " (supp=" + getSupport() + ")";
    }
}
