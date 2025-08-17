import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RealisticTransactionGenerator {

    public static void main(String[] args) throws IOException {
        int numTransactions = 200;
        int numItems = 20;         // item universe: 1..20
        int minItemsPerTx = 3;
        int maxItemsPerTx = 8;
        String fileName = "realistic_data.txt";

        Random rand = new Random(42);

        // Define popular items
        List<Integer> popularItems = Arrays.asList(1, 3, 5, 7, 12);

        try (FileWriter writer = new FileWriter(fileName)) {
            for (int t = 0; t < numTransactions; t++) {
                int txSize = minItemsPerTx + rand.nextInt(maxItemsPerTx - minItemsPerTx + 1);
                Set<Integer> txItems = new LinkedHashSet<>();

                // Add some popular items with higher probability
                for (int item : popularItems) {
                    if (rand.nextDouble() < 0.6) txItems.add(item);
                }

                // Fill the rest with random items
                while (txItems.size() < txSize) {
                    txItems.add(1 + rand.nextInt(numItems));
                }

                // Shuffle and write transaction
                List<Integer> shuffled = new ArrayList<>(txItems);
                Collections.shuffle(shuffled, rand);

                String line = shuffled.stream()
                        .map(String::valueOf)
                        .reduce((a, b) -> a + " " + b)
                        .orElse("");

                writer.write(line + "\n");
            }
        }

        System.out.println("Realistic synthetic data file generated: " + fileName);
    }
}
