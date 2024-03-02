package cs250.hw2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// import java.io.*;
import java.lang.Math;
import java.util.Random;
import java.util.TreeSet;

public class Memory {
    static int size;
    static int experiments;
    static int seed;

    public static final boolean DEBUG = false;

    public static void main(String[] args) {
        if (!checkIfEnoughArgs(args))
            System.exit(-1);

        setArgs(args);

        Experiment_2 e2 = new Experiment_2(size, experiments, seed);

        e2.runTests();

        Experiment_3 e3 = new Experiment_3(size, experiments, seed);
        e3.runTests();

        out_println("Done");
    }

    private static void setArgs(String[] args) {
        size = Integer.parseInt(args[0]);
        experiments = Integer.parseInt(args[1]);
        seed = Integer.parseInt(args[2]);
    }

    // @formatting:off

    // @formatting:on

    private static boolean checkIfEnoughArgs(String[] args) {
        if (3 != args.length) {
            err_println("Insufficient Arguments Passed: " + "\n\t" +
                    args.length + " arguments passed!");
            err_println("Usage: java cs250.hw2.Memory \"<size>\" \"<experiments\" \"<seed>\"\"");
            return false;
        }
        return true;
    }

    private static void err_println(String message) {
        System.err.println(message);
    }

    private static void out_println(String message) {
        // Make sure this only works when DEBUG is on!
        // if (DEBUG)
        System.out.println(message);
    }
}

class Experiment {
    int size;
    int experiments;
    int seed;

    Random rnd;

    Experiment(int size, int experiments, int seed) {
        initArgs(size, experiments, seed);
        initNumberGenerator();
    }

    // private void initTestRunner(int size, int experiments, int seed){
    // this.size = size;
    // this.experiments = experiments;
    // this.seed = seed;
    // }

    private void initArgs(int size, int experiments, int seed) {
        this.size = size;
        this.experiments = experiments;
        this.seed = seed;
    }

    private void initNumberGenerator() {
        rnd = new Random(this.seed);
    }

    public void runTests() {
        /* Implemented in child */}
    
    public void calcExperimentAverages() {
        /* Implemented in child */}

    private Long avg(List<Long> data) {
        Long sum = 0l;
        Long numElem = (long) data.size();
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
        }

        return sum / numElem;
    }

}

class Experiment_1 extends Experiment {

    List<Long> measuredTime_Volatile;
    List<Long> measuredTime_NonVolatile;
    List<Long> runningTotal_Volatile;
    List<Long> runningTotal_NonVolatile;

    volatile long iv = 0;
    long i = 0;

    Experiment_1(int size, int experiments, int seed) {
        super(size, experiments, seed);

        this.measuredTime_Volatile = new ArrayList<Long>();
        this.measuredTime_NonVolatile = new ArrayList<Long>();
        this.runningTotal_Volatile = new ArrayList<Long>();
        this.runningTotal_NonVolatile = new ArrayList<Long>();
    }

    @Override
    public void runTests() {
        int x = this.experiments;
        while (x-- > 0) {
            measureCachePerformance();
            measureMemoryPerformance();
        }
    }
//TODO:Calc the averages. Add appropriate vars in class to hold these values and then return them via method call
    @Override
    public void calcExperimentAverages(){
        
    }

    private void measureMemoryPerformance() {
        long runningTotal = 0;
        long startTime = System.nanoTime();
        for (i = 0; i < size; i++) {
            if (i % 2 == 1) {
                runningTotal -= 1;
            } else {
                runningTotal += 1;
            }
        }
        long endTime = System.nanoTime();
        measuredTime_Volatile.add(endTime - startTime);
        runningTotal_Volatile.add(runningTotal);
    }

    private void measureCachePerformance() {
        long runningTotal = 0;
        long startTime = System.nanoTime();
        for (i = 0; i < size; i++) {
            if (i % 2 == 1) {
                runningTotal -= 1;
            } else {
                runningTotal += 1;
            }
        }
        long endTime = System.nanoTime();
        measuredTime_NonVolatile.add(endTime - startTime);
        runningTotal_NonVolatile.add(runningTotal);
    }
}

class Experiment_2 extends Experiment {
    // Random rand = new Random();
    public int first10pct;
    public int last10pct;
    public long intermediateSum;

    public List<Long> measuredTime_first10pct;
    public List<Long> measuredTime_last10pct_rnd;
    public List<Long> sumTotal;

    public Integer[] array;

    Experiment_2(int size, int experiments, int seed) {
        super(size, experiments, seed);

        measuredTime_first10pct = new ArrayList<>();
        measuredTime_last10pct_rnd = new ArrayList<>();

        sumTotal = new ArrayList<Long>();

        initArray();
        fillArray();
        delineateAccess();
    }

    @Override
    public void runTests() {
        int x = experiments;
        while (x-- > 0) {
            intermediateSum = 0;
            measureArrayAccess();
            sumTotal.add(intermediateSum);
        }
    }

    @Override
    public void calcExperimentAverages(){
        
    }

    private void initArray() {
        array = new Integer[size];
    }

    private void fillArray() {
        for (int i = 0; i < array.length; i++)
            array[i] = rnd.nextInt();
    }

    private void delineateAccess() {
        // Get range for first 10% of array and last 10%
        first10pct = size / 10;
        last10pct = size - first10pct;
    }

    private void measureArrayAccess() {
        measureFirst10pct();
        measureLast10pct_singleElem();
    }

    private void measureFirst10pct() {
        long sum = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < first10pct; i++) {
            sum += array[i];
        }
        long endTime = System.nanoTime();
        long avgTime = (endTime - startTime) / first10pct;
        measuredTime_first10pct.add(avgTime);
        intermediateSum += sum;
    }

    private void measureLast10pct_singleElem() {
        int sum = 0;
        long startTime = System.nanoTime();
        sum += rnd.nextInt(first10pct);
        sum += array[last10pct + sum];
        long endTime = System.nanoTime();
        measuredTime_last10pct_rnd.add(endTime - startTime);
        intermediateSum += sum;
    }
}

class Experiment_3 extends Experiment {
    TreeSet<Integer> treeSet;
    List<Integer> linkedList;

    List<Long> measuredTime_TreeSet;
    List<Long> measuredTime_LinkedList;

    Long avg_measuredTime_TreeSet;
    Long avg_measuredTime_LinkedList;

    Experiment_3(int size, int experiments, int seed) {
        super(size, experiments, seed);

        treeSet = new TreeSet<>();
        linkedList = new LinkedList<>();
        measuredTime_LinkedList = new ArrayList<>();
        measuredTime_TreeSet = new ArrayList<>();

        initCollections();
    }

    @Override
    public void runTests() {
        int x = experiments;
        while (x-- > 0) {
            meaasureSearchTime();
        }
    }
    
    @Override
    public void calcExperimentAverages(){
        
    }

    private void initCollections() {
        for (int i = 0; i < size; i++) {
            treeSet.add(i);
            linkedList.add(i);
        }
    }

    private void meaasureSearchTime() {
        int r = rnd.nextInt(size);
        measureTreeSet(r);
        measureLinkedList(r);

    }

    private void measureTreeSet(int r) {
        long startTime = System.nanoTime();
        treeSet.contains(r);
        long endTime = System.nanoTime();

        measuredTime_TreeSet.add(endTime - startTime);
    }

    private void measureLinkedList(int r) {
        long startTime = System.nanoTime();
        linkedList.contains(r);
        long endTime = System.nanoTime();

        measuredTime_LinkedList.add(endTime - startTime);
    }



}

class GBUtils {

    static final boolean DEBUG = true;

    private static void err_println(String message) {
        System.err.println(message);
    }

    private static void out_println(String message) {
        // Make sure this only works when DEBUG is on!
        if (DEBUG)
            System.out.println(message);
    }
}
