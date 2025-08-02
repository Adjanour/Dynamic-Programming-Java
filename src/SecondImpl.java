import java.io.*;
import java.util.*;

public class SecondImpl {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Integer[]> data = getData("data.txt");
        for (Integer[] line : data){
            System.out.println(Arrays.toString(line));
        }
//        ArrayList<HashMap<Set<Integer>,Set<Integer>>> frequentItemSets = getFrequentItemSets(data);
    }
    
    public static ArrayList<Integer[]> getData(String fileName) throws FileNotFoundException {
//        Scanner s = new Scanner(new File("/home/katamanso/Documents/java-projects/DynamicProgramming/data.txt"));
//        while (s.hasNextLine()){
//            System.out.println(s.nextLine());
//            s.nextLine().split(" ");
//        }
        BufferedReader bf = new BufferedReader(new FileReader("/home/katamanso/Documents/java-projects/DynamicProgramming/data.txt"));
        ArrayList<Integer[]> data = new ArrayList<>(){};
        try {
            String input = "";
            while (!((input = bf.readLine()) == null)){
                String[] values = input.split(" ");
                Integer[] preData = new Integer[values.length];
                for (int i = 0; i < values.length; i++) {
                    preData[i] = Integer.parseInt(values[i]);
                }
                data.add(preData);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
