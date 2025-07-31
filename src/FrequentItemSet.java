import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FrequentItemSet {
    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<Integer> data = getData("/home/katamanso/Documents/java-projects/DynamicProgramming/data.txt");

    }
    public  static ArrayList<Integer> getData(String fileName) throws FileNotFoundException {
        String input = "";
        int tid=1;
        ArrayList<String[]> data = new ArrayList<String[]>();
        ArrayList<Integer> modData = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader(fileName));

        try {
            while ((input = bf.readLine()) != null){
                String[] values = input.split(" ");
               for (String val : values){
                   System.out.println(val+" : "+tid);
               }
               tid++;
                data.add(values);
            }
            for (String[] line : data ){
                for (String value : line){
                    modData.add(Integer.getInteger(value));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return modData;
    }
}
