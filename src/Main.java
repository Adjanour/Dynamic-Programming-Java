import classes.FrequentItemSetResult;
import classes.ItemSet;
import classes.Transaction;
import classes.TransactionDatabase;
import miners.AprioriMiner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        Runtime runtime = Runtime.getRuntime();

        // Warm up JVM GC before measurement
        System.gc();

        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();



        TransactionDatabase db = TransactionDatabase.fromFile("tria-2.txt");
        int minSupport = 100;

        AprioriMiner miner = new AprioriMiner();
        FrequentItemSetResult result = miner.mine(db, minSupport);


        long endTime = System.currentTimeMillis();
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        long duration = endTime - startTime;
        long memoryUsed = (afterUsedMem - beforeUsedMem);

        System.out.println("=== Benchmark: " + miner.getClass().getSimpleName() + " ===");
        System.out.println("Frequent itemsets: " + result.getItemsets().size());
        System.out.println("Execution time: " + duration + " ms");
        System.out.println("Memory used: " + (memoryUsed / (1024 * 1024)) + " MB");
        System.out.println();

        System.out.println("--- Frequent Itemsets ---");
        result.print();
    }

}

