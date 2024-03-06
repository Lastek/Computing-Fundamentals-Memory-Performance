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
    public static final boolean outputCSV = true;

    public static void main(String[] args) {
        if (!checkIfEnoughArgs(args))
            System.exit(-1);

        setArgs(args);
        Experiment_1 e1 = new Experiment_1(size, experiments, seed);

        e1.runTests();

        Experiment_2 e2 = new Experiment_2(size, experiments, seed);
        check();

        e2.runTests();

        Experiment_3 e3 = new Experiment_3(size, experiments, seed);
        e3.runTests();
        if (outputCSV == true)
            printcsv(e1, e2, e3);
        else
            printData(e1, e2, e3);

    }

    private static void printData(Experiment_1 e1, Experiment_2 e2, Experiment_3 e3) {
        out_println(String.format("Command: java cs250.hw2.Memory %s %s %s", size, experiments, seed));

        out_println(String.format("Task 1"));
        out_println(String.format(
                "Regular: %.5f seconds\nVolatile: %.5f seconds\nAvg regular sum: %.2f\nAvg volatile sum: %.2f\n",
                e1.avg_measuredTime_NonVolatile / Math.pow(10l, 9l),
                e1.avg_measuredTime_Volatile / Math.pow(10l, 9l),
                e1.avg_runningSum_NonVolatile.doubleValue(),
                e1.avg_runningSum_Volatile.doubleValue()));

        // out_println("\n");
        out_println(String.format("Task 2"));
        out_println(String.format(
                "Avg time to access known element: %.2f nanoseconds\nAvg time to access random element: %.2f nanoseconds\nSum: %.2f\n",
                e2.avg_measuredTime_first10pct,
                e2.avg_measuredTime_last10pct_rnd,
                e2.avg_sumTotal));
        // out_println("\n");

        out_println(String.format("Task 3"));
        out_println(String.format(
                "Avg time to find in set: %.2f nanoseconds\nAvg time to find in list: %.2f nanoseconds",
                e3.avg_measuredTime_TreeSet,
                e3.avg_measuredTime_LinkedList));
    }

    private static void printcsv(Experiment_1 e1, Experiment_2 e2, Experiment_3 e3) {
        out_println(String.format("%s, %s, %s,", size, experiments, seed));
        out_println(String.format("Task 1"));
        out_println(String.format(
                "Regular:, %.5f, seconds\nVolatile:, %.5f, seconds\nAvg regular sum:, %.2f,\nAvg volatile sum:, %.2f,",
                e1.avg_measuredTime_NonVolatile / Math.pow(10l, 9l),
                e1.avg_measuredTime_Volatile / Math.pow(10l, 9l),
                e1.avg_runningSum_NonVolatile.doubleValue(),
                e1.avg_runningSum_Volatile.doubleValue()));

        out_println(String.format("Task 2"));
        out_println(String.format(
                "Avg time to access known element:, %.2f, nanoseconds\nAvg time to access random element:, %.2f, nanoseconds\nSum:, %.2f,",
                e2.avg_measuredTime_first10pct,
                e2.avg_measuredTime_last10pct_rnd,
                e2.avg_sumTotal));

        out_println(String.format("Task 3"));
        out_println(String.format(
                "Avg time to find in set:, %.2f, nanoseconds\nAvg time to find in list:, %.2f, nanoseconds",
                e3.avg_measuredTime_TreeSet,
                e3.avg_measuredTime_LinkedList));
        out_println("");
    }

    private static void setArgs(String[] args) {
        size = Integer.parseInt(args[0]);
        experiments = Integer.parseInt(args[1]);
        seed = Integer.parseInt(args[2]);
    }

    private static void check() {
        if (DEBUG == true)
            if (size == 25000000) {
                out_println("SIZE IS EQUAL");
            }
    }

    // @formatting:off

    // @formatting:on

    private static boolean checkIfEnoughArgs(String[] args) {
        if (3 != args.length) {
            err_println("args.length arguments passed!");
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
    protected int size;
    protected int experiments;
    protected int seed;

    protected Random rnd;

    Experiment(int size, int experiments, int seed) {
        initArgs(size, experiments, seed);
        initNumberGenerator();
    }

    private void initArgs(int size, int experiments, int seed) {
        this.size = size;
        this.experiments = experiments;
        this.seed = seed;
    }

    private void initNumberGenerator() {
        rnd = new Random((long) this.seed);
    }

    public void runTests() {
        /* Implemented in child */}

    public void calcExperimentAverages() {
        /* Implemented in child */}

    public Long avg(List<Long> data) {
        Long sum = 0l;
        Long numElem = (long) experiments;
        // Long numElem = (long) data.size();
        for (int i = 0; i < numElem; i++) {
            sum += data.get(i);
        }

        return sum / numElem;
    }

    public Double avgdouble(List<Double> data) {
        Double sum = 0d;
        Double numElem = (double) experiments;
        // Long numElem = (long) data.size();
        for (int i = 0; i < numElem; i++) {
            sum += data.get(i);
        }

        return sum / numElem;
    }
    // public Integer avgint(List<Integer> data) {
    // int sum = 0;
    // int numElem = (int) experiments;
    // // Long numElem = (long) data.size();
    // for (int i = 0; i < numElem; i++) {
    // sum += data.get(i);
    // }

    // return sum / numElem;
    // }

}

class Experiment_1 extends Experiment {

    List<Double> measuredTime_Volatile;
    List<Double> measuredTime_NonVolatile;
    List<Long> runningSum_Volatile;
    List<Long> runningSum_NonVolatile;

    Double avg_measuredTime_Volatile;
    Double avg_measuredTime_NonVolatile;
    Long avg_runningSum_Volatile;
    Long avg_runningSum_NonVolatile;

    volatile long iv = 0;
    long i = 0;

    Experiment_1(int size, int experiments, int seed) {
        super(size, experiments, seed);

        this.measuredTime_Volatile = new ArrayList<Double>();
        this.measuredTime_NonVolatile = new ArrayList<Double>();
        this.runningSum_Volatile = new ArrayList<Long>();
        this.runningSum_NonVolatile = new ArrayList<Long>();
    }

    @Override
    public void runTests() {
        int x = this.experiments;
        while (x-- > 0) {
            measureCachePerformance();
            measureMemoryPerformance();
        }
        calcExperimentAverages();
    }

    @Override
    public void calcExperimentAverages() {
        avg_measuredTime_Volatile = avgdouble(measuredTime_Volatile);
        avg_measuredTime_NonVolatile = avgdouble(measuredTime_NonVolatile);
        avg_runningSum_Volatile = avg(runningSum_Volatile);
        avg_runningSum_NonVolatile = avg(runningSum_NonVolatile);
    }

    private void measureMemoryPerformance() {
        long runningTotal = 0;
        double startTime = System.nanoTime();
        for (iv = 0; iv < size; iv++) {
            if (iv % 2 == 1) {
                runningTotal -= iv;
            } else {
                runningTotal += iv;
            }
        }
        double endTime = System.nanoTime();
        measuredTime_Volatile.add(endTime - startTime);
        runningSum_Volatile.add(runningTotal);
    }

    private void measureCachePerformance() {
        long runningTotal = 0;
        double startTime = System.nanoTime();
        for (i = 0; i < size; i++) {
            if (i % 2 == 1) {
                runningTotal -= i;
            } else {
                runningTotal += i;
            }
        }
        double endTime = System.nanoTime();
        measuredTime_NonVolatile.add(endTime - startTime);
        runningSum_NonVolatile.add(runningTotal);
    }
}

class Experiment_2 extends Experiment {
    public int first10pct;
    public int last10pct;
    public int intermediateSum;

    public List<Double> measuredTime_first10pct;
    public List<Double> measuredTime_last10pct_rnd;
    public List<Double> sumTotal;

    Double avg_measuredTime_first10pct;
    Double avg_measuredTime_last10pct_rnd;
    Double avg_sumTotal;

    public Integer[] array;

    Experiment_2(int size, int experiments, int seed) {
        super(size, experiments, seed);

        measuredTime_first10pct = new ArrayList<>();
        measuredTime_last10pct_rnd = new ArrayList<>();

        sumTotal = new ArrayList<Double>();

        initArray();
        fillArray();
        delineateAccess();
    }

    private void initArray() {
        array = new Integer[size];
    }

    private void fillArray() {
        for (int i = 0; i < array.length; i++)
            array[i] = (Integer) rnd.nextInt();
    }

    private void delineateAccess() {
        // Get range for first 10% of array and last 10%
        first10pct = size / 10;
        last10pct = size - first10pct;
    }

    @Override
    public void calcExperimentAverages() {
        avg_measuredTime_first10pct = avgdouble(measuredTime_first10pct);
        avg_measuredTime_last10pct_rnd = avgdouble(measuredTime_last10pct_rnd);
        // avg_sumTotal = avgdouble(sumTotal);
        int p = 0;
        for (int i = 0; i < sumTotal.size(); i++) {
            p += sumTotal.get(i).intValue();
        }
        avg_sumTotal = (double) p / experiments;
    }

    @Override
    public void runTests() {
        int x = experiments;
        while (x-- > 0) {
            intermediateSum = 0;
            measureArrayAccess();
            sumTotal.add((double) intermediateSum);
        }
        calcExperimentAverages();
    }

    private void measureArrayAccess() {
        measureFirst10pct();
        measureLast10pct_singleElem();

    }

    public void measureFirst10pct() {
        int sum = 0;
        double startTime = System.nanoTime();
        for (int i = 0; i < first10pct; i++) {
            sum += array[i];
        }
        double endTime = System.nanoTime();
        double avgTime = (endTime - startTime);
        avgTime /= first10pct;

        // System.out.println("E2: Avg Time: " + avgTime);
        measuredTime_first10pct.add(avgTime);
        intermediateSum += sum;
    }

    public void measureLast10pct_singleElem() {
        int sum = 0;
        int lastIndex = size - rnd.nextInt(size / 10);
        double startTime = System.nanoTime();
        sum = array[lastIndex];
        double endTime = System.nanoTime();
        measuredTime_last10pct_rnd.add(endTime - startTime);
        // System.out.println(measuredTime_last10pct_rnd);
        intermediateSum += sum;
    }
}

class Experiment_3 extends Experiment {
    TreeSet<Integer> treeSet;
    List<Integer> linkedList;

    List<Double> measuredTime_TreeSet;
    List<Double> measuredTime_LinkedList;

    Double avg_measuredTime_TreeSet;
    Double avg_measuredTime_LinkedList;

    Experiment_3(int size, int experiments, int seed) {
        super(size, experiments, seed);

        treeSet = new TreeSet<>();
        linkedList = new LinkedList<>();
        measuredTime_LinkedList = new ArrayList<>();
        measuredTime_TreeSet = new ArrayList<>();

        initCollections();
    }

    private void initCollections() {
        for (int i = 0; i < size; i++) {
            treeSet.add(i);
            linkedList.add(i);
        }
    }

    @Override
    public void runTests() {
        int x = experiments;
        while (x-- > 0) {
            meaasureSearchTime();
        }
        calcExperimentAverages();
    }

    @Override
    public void calcExperimentAverages() {
        avg_measuredTime_TreeSet = avgdouble(measuredTime_TreeSet);
        avg_measuredTime_LinkedList = avgdouble(measuredTime_LinkedList);
    }

    private void meaasureSearchTime() {
        int r = rnd.nextInt(size);
        measureTreeSet(r);
        measureLinkedList(r);
    }

    private void measureTreeSet(int r) {
        double startTime = System.nanoTime();
        treeSet.contains(r);
        double endTime = System.nanoTime();
        measuredTime_TreeSet.add(endTime - startTime);
    }

    private void measureLinkedList(int r) {
        double startTime = System.nanoTime();
        linkedList.contains(r);
        double endTime = System.nanoTime();
        measuredTime_LinkedList.add(endTime - startTime);
    }
}

class GBUtils {

    static final boolean DEBUG = false;

    private static void err_println(String message) {
        System.err.println(message);
    }

    private static void out_println(String message) {
        // Make sure this only works when DEBUG is on!
        if (DEBUG)
            System.out.println(message);
    }
}
