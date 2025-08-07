import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FrequentItemSetMiner {
    public static void main(String[] args) throws IOException {
        int minsup = 6;

        Map<Set<Integer>, Set<Integer>> frequentItemsets = MineFrequentItemSets("./data.txt", minsup);

        System.out.println("\n--- Frequent Itemsets ---");
        for (Map.Entry<Set<Integer>, Set<Integer>> entry : frequentItemsets.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " (supp=" + entry.getValue().size() + ")");
        }
    }

    public static Map<Set<Integer>, Set<Integer>> MineFrequentItemSets(String fileName, int minsup) throws IOException {

        ConcurrentHashMap<Set<Integer>, Set<Integer>> FrequentItemsets = new ConcurrentHashMap<>();

        String line;
        int transactionID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                transactionID++;
                String[] transactionSplit = line.split(" ");

                for (String uniqueItem : transactionSplit) {
                    Set<Integer> itemset = new HashSet<>();
                    itemset.add(Integer.parseInt(uniqueItem));

                    FrequentItemsets.computeIfAbsent(itemset, k -> new HashSet<>()).add(transactionID);
                }
            }
        }

        // Remove infrequent singletons
        FrequentItemsets.entrySet().removeIf(entry -> entry.getValue().size() < minsup);

        List<Set<Integer>> LoopingList = new ArrayList<>(FrequentItemsets.keySet());
        LoopingList.sort(Comparator.comparingInt(o -> o.iterator().next()));

        ConcurrentHashMap<Set<Integer>, Set<Integer>> nextLevelItemsets = new ConcurrentHashMap<>();

        while (LoopingList.size() > 1) {
            for (int i = 0; i < LoopingList.size(); i++) {
                for (int j = i + 1; j < LoopingList.size(); j++) {
                    Set<Integer> set1 = LoopingList.get(i);
                    Set<Integer> set2 = LoopingList.get(j);

                    // Apriori candidate generation check
                    List<Set<Integer>> prefixSuffix1 = getPrefixAndSuffix(set1);
                    List<Set<Integer>> prefixSuffix2 = getPrefixAndSuffix(set2);

                    if (prefixSuffix1.get(0).equals(prefixSuffix2.get(0))
                            && !prefixSuffix1.get(1).equals(prefixSuffix2.get(1))) {

                        Set<Integer> unionSet = new HashSet<>(set1);
                        unionSet.addAll(set2);

                        Set<Integer> intersectionSet = new HashSet<>(FrequentItemsets.get(set1));
                        intersectionSet.retainAll(FrequentItemsets.get(set2));

                        if (intersectionSet.size() >= minsup) {
                            FrequentItemsets.put(unionSet, intersectionSet);
                            nextLevelItemsets.put(unionSet, intersectionSet);
                        }
                    }
                }
            }
            LoopingList = new ArrayList<>(nextLevelItemsets.keySet());
            nextLevelItemsets.clear();
        }

        return FrequentItemsets;
    }

    public static List<Set<Integer>> getPrefixAndSuffix(Set<Integer> itemset) {
        List<Integer> sortedList = new ArrayList<>(itemset);
        Collections.sort(sortedList);

        if (sortedList.isEmpty()) {
            return Arrays.asList(new HashSet<>(), new HashSet<>());
        }

        int lastItem = sortedList.get(sortedList.size() - 1);

        Set<Integer> suffix = new HashSet<>();
        suffix.add(lastItem);

        Set<Integer> prefix = new HashSet<>(sortedList);
        prefix.remove(lastItem);

        return Arrays.asList(prefix, suffix);
    }


    public static void printSet( ConcurrentHashMap<Set<Integer>, Set<Integer>> frequentItemsets ){
        // Print results
        for (Map.Entry<Set<Integer>, Set<Integer>> entry : frequentItemsets.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }


}

