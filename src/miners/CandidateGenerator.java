package miners;

import classes.ItemSet;

import java.util.*;

public class CandidateGenerator {

    /**
     * Try to join two ItemSets using the Apriori property.
     * <p>
     * Condition: first k-1 items are the same, last item differs.
     * If joinable, return a new ItemSet. Otherwise return null.
     */
    public static ItemSet join(ItemSet a, ItemSet b) {
        List<Integer> itemsA = a.getItems(); // already sorted
        List<Integer> itemsB = b.getItems(); // already sorted

        int k = itemsA.size();
        int lastIndex = k - 1;

    // compare prefixes in one line
        if (!itemsA.subList(0, lastIndex).equals(itemsB.subList(0, lastIndex))) {
            return null; // early exit if first k-1 items differ
        }

    // check last element
        if (itemsA.get(lastIndex).equals(itemsB.get(lastIndex))) {
            return null;
        }

    // joinable, do intersection
        Set<Integer> newItems = new HashSet<>(itemsA);
        newItems.addAll(itemsB);
        Set<Integer> newTxIds = new HashSet<>(a.getTransactionIds());
        newTxIds.retainAll(b.getTransactionIds());

        return new ItemSet(newItems, newTxIds);

    }
}
