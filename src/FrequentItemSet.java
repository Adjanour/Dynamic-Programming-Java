import java.io.*;
import java.util.*;

public class FrequentItemSet {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java FrequentItemSet <filePath> <minSupport>");
            return;
        }

        String filePath = args[0];
        int minSupport = Integer.parseInt(args[1]);

        try {
            // Read data from the file provided as an argument
            ArrayList<Integer[]> data = getData(filePath);
            // Run the algorithm with the given minSupport
            frequentItemSet(data, minSupport);

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found at " + filePath);
        } catch (NumberFormatException e) {
            System.err.println("Error: minSupport must be an integer.");
        }
    }

    /**
     * Reads transaction data from a file.
     * Each line in the file is a transaction, with items separated by spaces.
     * @param fileName The path to the data file.
     * @return An ArrayList where each element is an Integer array representing a transaction.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public static ArrayList<Integer[]> getData(String fileName) throws FileNotFoundException {
        ArrayList<Integer[]> data = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bf.readLine()) != null) {
                String[] values = line.trim().split("\\s+"); // Use regex for any whitespace
                ArrayList<Integer> transaction = new ArrayList<>();
                for (String val : values) {
                    if (!val.isEmpty()) {
                        transaction.add(Integer.parseInt(val));
                    }
                }
                data.add(transaction.toArray(new Integer[0]));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
        return data;
    }

    /**
     * Implements the Apriori algorithm to find all frequent itemsets.
     * @param data The transaction database.
     * @param minSupport The minimum support count for an itemset to be considered frequent.
     */
    public static void frequentItemSet(ArrayList<Integer[]> data, int minSupport) {
        // This list will hold the frequent itemsets found at each level (L1, L2, etc.)
        List<Map<Set<Integer>, Set<Integer>>> allFrequentItemsets = new ArrayList<>();

        // This map (Lk) holds the frequent itemsets for the current level k.
        // Key: The itemset (e.g., {1, 2})
        // Value: The set of transaction IDs (TIDs) where it appears (e.g., {0, 3, 5})
        Map<Set<Integer>, Set<Integer>> frequentItemsets = new HashMap<>();

        // --- PHASE 1: Generate frequent 1-itemsets (L1) ---
        for (int i = 0; i < data.size(); i++) {
            Integer[] transaction = data.get(i);
            for (Integer item : transaction) {
                Set<Integer> itemset = new HashSet<>();
                itemset.add(item);
                // Add the current transaction ID (i) to this item's tidset
                frequentItemsets.computeIfAbsent(itemset, _ -> new HashSet<>()).add(i);
            }
        }

        // Prune the 1-itemsets that do not meet minSupport
        frequentItemsets.entrySet().removeIf(entry -> entry.getValue().size() < minSupport);

        // Main loop to generate Lk from L(k-1)
        while (!frequentItemsets.isEmpty()) {
            allFrequentItemsets.add(frequentItemsets);

            // --- PHASE 2: Generate candidate (k+1)-itemsets from frequent k-itemsets ---
            Map<Set<Integer>, Set<Integer>> nextLevelItemsets = new HashMap<>();
            List<Set<Integer>> keys = new ArrayList<>(frequentItemsets.keySet());

            // Join Lk with Lk to generate C(k+1)
            for (int i = 0; i < keys.size(); i++) {
                for (int j = i + 1; j < keys.size(); j++) {
                    Set<Integer> set1 = keys.get(i);
                    Set<Integer> set2 = keys.get(j);

                    // Create a union to form the candidate
                    Set<Integer> union = new HashSet<>(set1);
                    union.addAll(set2);

                    // Join only if the itemsets have k-1 common items, resulting in a (k+1)-itemset
                    if (union.size() == set1.size() + 1) {
                        // This is the candidate C(k+1)
                        // To get its support, intersect the tidsets of its parent k-itemsets
                        Set<Integer> tids1 = frequentItemsets.get(set1);
                        Set<Integer> tids2 = frequentItemsets.get(set2);

                        Set<Integer> intersection = new HashSet<>(tids1);
                        intersection.retainAll(tids2); // The new tidset for the union

                        // --- PHASE 3: Prune candidates ---
                        // If the candidate meets minSupport, add it to the next level's frequent itemsets
                        if (intersection.size() >= minSupport) {
                            nextLevelItemsets.put(union, intersection);
                        }
                    }
                }
            }
            // The candidates for the next level become the frequent itemsets for the next loop iteration
            frequentItemsets = nextLevelItemsets;
        }

        // --- Final step: Print and write results to a file ---
        printAndWriteFrequentItemsets(allFrequentItemsets);
    }

    /**
     * Generates all the strong association rules that meet the confidence threshold
     * @param allFrequentItemsets The mined frequent item sets
     * @param minConfidence The minimum confidence for a rule to considered true
     */
    public static void generateAssociationRules(
            List<Map<Set<Integer>, Set<Integer>>> allFrequentItemsets,
            double minConfidence
    ){

    }

    /**
     * Formats the final list of frequent itemsets, prints them to the console,
     * and writes them to a file named "frequent_itemsets.txt".
     * @param levels A list containing the frequent itemset maps for each level.
     */
    public static void printAndWriteFrequentItemsets(List<Map<Set<Integer>, Set<Integer>>> levels) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Frequent Itemsets ---\n\n");

        for (int i = 0; i < levels.size(); i++) {
            Map<Set<Integer>, Set<Integer>> itemsets = levels.get(i);
            sb.append("Level ").append(i + 1).append(" (L").append(i + 1).append(") Frequent Itemsets:\n");
            if (itemsets.isEmpty()) {
                sb.append("  None\n");
            } else {
                for (Map.Entry<Set<Integer>, Set<Integer>> entry : itemsets.entrySet()) {
                    // Itemset -> Support Count
                    sb.append("  ")
                            .append(entry.getKey())
                            .append("  (Support: ")
                            .append(entry.getValue().size())
                            .append(")\n");
                }
            }
            sb.append("\n");
        }

        // Print to console
        System.out.println(sb);

        // Write to file
        try {
            writeToFile("frequent_itemsets.txt", sb.toString());
            System.out.println("Results successfully written to frequent_itemsets.txt");
        } catch (IOException e) {
            System.err.println("Failed to write itemsets to file: " + e.getMessage());
        }
    }

    /**
     * A simple utility to write string data to a file.
     * @param fileName The name of the file to write to.
     * @param data The string content to write.
     * @throws IOException If a file writing error occurs.
     * */
    public static void writeToFile(String fileName, String data) throws IOException {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(data);
        }
    }
}