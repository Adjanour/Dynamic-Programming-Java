import java.io.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
//        HashMap<Integer,Integer> memo = new HashMap<>();
//        System.out.println(fib(4,memo));

        for (int i = 0; i < (1<< 5); i++) {
            System.out.println(i);
        }

    }
    public static int fib(int num,HashMap<Integer,Integer> memo){
        if (num == 1 || num == 0){
            return  num;
        }
        if (memo.containsKey(num)){
            return memo.get(num);
        }
       int result = fib(num-1,memo) + fib(num-2,memo);
        memo.put(num,result);
        return result;
    }

}

