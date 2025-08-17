package interfaces;

import classes.TransactionDatabase;
import classes.FrequentItemSetResult;

public interface Miner {
    FrequentItemSetResult mine(TransactionDatabase db, int minSupport);
}
