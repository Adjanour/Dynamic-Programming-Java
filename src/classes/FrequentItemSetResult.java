package classes;

import java.util.List;

public class FrequentItemSetResult {
    private final List<ItemSet> itemsets;

    public FrequentItemSetResult(List<ItemSet> itemsets) {
        this.itemsets = itemsets;
    }

    public List<ItemSet> getItemsets() {
        return itemsets;
    }

    public void print() {
        for (ItemSet is : itemsets) {
            System.out.println(is.getItems() + " (supp=" + is.getSupport() + ")");
        }
    }
}
