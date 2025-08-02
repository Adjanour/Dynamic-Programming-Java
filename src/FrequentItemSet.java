import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FrequentItemSet {
    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<Integer[]> data = getData("/home/katamanso/Documents/java-projects/DynamicProgramming/data.txt");
        frequentItemSet(data,6);

    }
    public  static ArrayList<Integer[]> getData(String fileName) throws FileNotFoundException {
        String input = "";
        ArrayList<Integer[]> Data = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader(fileName));

        try {
            while ((input = bf.readLine()) != null){
                String[] values = input.split(" ");
                ArrayList<Integer> preData = new ArrayList<>();
                for(String val : values){
                    int data = Integer.parseInt(val);
                    preData.add(data);
                }
                Data.add(preData.toArray(new Integer[0]));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Data;
    }

    public static void frequentItemSet(ArrayList<Integer[]> data,int minSupport) {
        List<Map<Set<Integer>, Set<Integer>>> allFrequentItemsets = new ArrayList<>();
        ConcurrentHashMap<Set<Integer>, Set<Integer>> frequentItemsets = new ConcurrentHashMap<>();
        
        for (int i = 1; i < data.size(); i++) {
            Integer[] transaction = data.get(i);
            for (Integer item : transaction) {
                Set<Integer> key = new HashSet<>();
                key.add(item);
//                if(!itemSet.containsKey(key)){
//                    itemSet.put(key,new HashSet<>());
//                }
                frequentItemsets.computeIfAbsent(key, k -> new HashSet<>()).add(i);
            }
        }
        printSet(frequentItemsets);

        frequentItemsets.entrySet().removeIf(entry -> entry.getValue().size() < minSupport);


        while (!frequentItemsets.isEmpty()) {
            ConcurrentHashMap<Set<Integer>, Set<Integer>> nextLevelItemsets = new ConcurrentHashMap<>();

            List<Set<Integer>> keys = new ArrayList<>(frequentItemsets.keySet());
            for (int i = 0; i < keys.size(); i++) {
                for (int j = i+1; j < keys.size() ; j++) {
                    Set<Integer> set1 = keys.get(i);
                    Set<Integer> set2 = keys.get(j);

                    Set<Integer> union = new HashSet<>(set1);
                    union.addAll(set2);

                    if (union.size() == set1.size() + 1){
                        Set<Integer> tids1 = frequentItemsets.get(set1);
                        Set<Integer> tids2 = frequentItemsets.get(set2);

                        Set<Integer> intersection = new HashSet<>(tids1);
                        intersection.retainAll(tids2);

                        // If it meets minSupport, keep it
                        if (intersection.size() >= minSupport) {
                            nextLevelItemsets.put(union, intersection);
                        }
                    }

                }
            }
            System.out.println("Next Level:");
            printSet(nextLevelItemsets);
            allFrequentItemsets.add(frequentItemsets);
            frequentItemsets = nextLevelItemsets;
        }
        printAllFrequentItemsets(allFrequentItemsets);
    }
    public static void printSet( ConcurrentHashMap<Set<Integer>, Set<Integer>> frequentItemsets ){
        // Print results
        for (Map.Entry<Set<Integer>, Set<Integer>> entry : frequentItemsets.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
    public static void printAllFrequentItemsets(List<Map<Set<Integer>, Set<Integer>>> levels) {
        StringBuilder sb = new StringBuilder();

        for (int level = 0; level < levels.size(); level++) {
            Map<Set<Integer>, Set<Integer>> itemsets = levels.get(level);
            sb.append("Level ").append(level + 1).append(" itemsets:\n");
            for (Map.Entry<Set<Integer>, Set<Integer>> entry : itemsets.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(" → ").append(entry.getValue()).append("\n");
            }
            sb.append("\n");
        }

        try {
            writeToFile("frequent_itemsets.txt", sb.toString());
        } catch (IOException e) {
            System.out.println("Failed to write itemsets to file.");
        }

        // Still print to console
        System.out.println(sb);
    }


    public static void writeToFile(String fileName,String Data) throws IOException {
        Path p = Path.of(fileName);
        FileWriter fw = new FileWriter(p.toFile());
        fw.append(Data);
        fw.close();

    }


}
