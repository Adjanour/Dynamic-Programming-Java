import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FrequentItemSetMiner {
    public static void main(String[] args) throws IOException {
        int minsup = 4;

       MineFrequentItemSets("./data.txt");
    }

    public static void MineFrequentItemSets(String fileName) throws IOException {

        ConcurrentHashMap<Set<Integer>, Set<Integer>> FrequentItemsets = new ConcurrentHashMap<>();

        ArrayList<Integer[]> Data = new ArrayList<>();
        FileReader file = new FileReader(fileName);
        String line;
        int transactionID=0;

        try (BufferedReader reader = new BufferedReader(file)) {

          while (!((line = reader.readLine()) == null)){
              transactionID++;

              String[] transactionSplit = line.split(" ");

              for (String uniqueItem : transactionSplit){
                  int parsedId = Integer.parseInt(uniqueItem);
                  Set<Integer> itemset = new HashSet<>();
                  Set<Integer> itemID = new HashSet<>();
                  itemset.add(Integer.parseInt(uniqueItem));
                  itemID.add(transactionID);

                  if (!FrequentItemsets.containsKey(itemset)){
                      FrequentItemsets.put(itemset,itemID);
                  }else{
                      FrequentItemsets.get(itemset).add(transactionID);
                  }


              }

          }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println(FrequentItemsets);
        printSet(FrequentItemsets);

    }

    public static void printSet( ConcurrentHashMap<Set<Integer>, Set<Integer>> frequentItemsets ){
        // Print results
        for (Map.Entry<Set<Integer>, Set<Integer>> entry : frequentItemsets.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

