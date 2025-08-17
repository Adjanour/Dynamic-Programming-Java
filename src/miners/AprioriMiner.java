package miners;

import interfaces.Miner;
import classes.*;

import java.util.*;

public class AprioriMiner implements Miner {
    @Override
    public FrequentItemSetResult mine(TransactionDatabase db, int minSupport) {
        List<ItemSet> frequentItemsets = new ArrayList<>();

        // Step 1: Generate frequent singletons
        Map<Integer, Set<Integer>> itemToTransactions = new HashMap<>();
        for (Transaction tx : db.getTransactions()) {
            for (Integer item : tx.getItems()) {
                itemToTransactions.computeIfAbsent(item, k -> new HashSet<>()).add(tx.getId());
            }
        }

        List<ItemSet> currentLevel = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : itemToTransactions.entrySet()) {
            ItemSet is = new ItemSet(Set.of(entry.getKey()), entry.getValue());
            if (is.getSupport() >= minSupport) {
                currentLevel.add(is);
                frequentItemsets.add(is);
            }
        }

        // Step 2: Iteratively generate candidates
        while (!currentLevel.isEmpty()) {
            List<ItemSet> nextLevel = new ArrayList<>();

            // Ensure lexicographic order
            currentLevel.sort(new Comparator<ItemSet>() {
                @Override
                public int compare(ItemSet o1, ItemSet o2) {
                    return Integer.compare(o1.getItems().getFirst(), o2.getItems().getFirst());
                }
            });


            for (int i = 0; i < currentLevel.size(); i++) {
                for (int j = i + 1; j < currentLevel.size(); j++) {
                    ItemSet candidate = CandidateGenerator.join(currentLevel.get(i), currentLevel.get(j));

                    if (candidate == null) {
                        // Apriori property failed: since sorted, no later j will match → break early
                        break;
                    }

                    if (candidate.getSupport() >= minSupport &&
                            !containsItemSet(frequentItemsets, candidate)) {
                        nextLevel.add(candidate);
                        frequentItemsets.add(candidate);
                    }
                }
            }
            currentLevel = nextLevel;
        }

        return new FrequentItemSetResult(frequentItemsets);
    }

    private boolean containsItemSet(List<ItemSet> itemsets, ItemSet candidate) {
        for (ItemSet is : itemsets) {
            if (is.getItems().equals(candidate.getItems())) {
                return true;
            }
        }
        return false;
    }
}
