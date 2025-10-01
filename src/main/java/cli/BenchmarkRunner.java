package cli;

import algorithms.InsertionSort;
import metrics.PerformanceTracker;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class BenchmarkRunner {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--help")) {
            printHelp();
            return;
        }

        System.out.println("Insertion Sort Benchmark");
        System.out.println("============================");

        // CSV header
        System.out.println("Type,Size,Time(ns),Comparisons,Swaps,ArrayAccesses,Iterations");

        int[] sizes = {100, 500, 1000, 5000, 10000};

        try (FileWriter csvWriter = new FileWriter("benchmark_results.csv")) {
            csvWriter.write("Type,Size,Time(ns),Comparisons,Swaps,ArrayAccesses,Iterations\n");

            for (int size : sizes) {
                System.out.printf("\nTesting size: %d%n", size);

                // Test on random data
                int[] randomArray = generateRandomArray(size);
                runBenchmark("Random", randomArray, csvWriter);

                // Test on sorted data (best case)
                int[] sortedArray = generateSortedArray(size);
                runBenchmark("Sorted", sortedArray, csvWriter);

                // Test on reverse sorted data (worst case)
                int[] reverseSortedArray = generateReverseSortedArray(size);
                runBenchmark("ReverseSorted", reverseSortedArray, csvWriter);

                // Test on nearly sorted data
                int[] nearlySortedArray = generateNearlySortedArray(size);
                runBenchmark("NearlySorted", nearlySortedArray, csvWriter);

                // Test optimized version on nearly sorted data
                runOptimizedBenchmark(nearlySortedArray.clone(), csvWriter);

                // Test binary search version
                runBinarySearchBenchmark(randomArray.clone(), csvWriter);
            }

            System.out.println("\n✅ Benchmark completed! Results saved to benchmark_results.csv");

        } catch (IOException e) {
            System.err.println("❌ Error writing CSV file: " + e.getMessage());
        }
    }

    private static void runBenchmark(String type, int[] array, FileWriter csvWriter)
            throws IOException {
        long startTime = System.nanoTime();
        InsertionSort.sort(array);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;

        String result = String.format("%s,%d,%d,%d,%d,%d,%d",
                type,
                array.length,
                duration,
                InsertionSort.getComparisons(),
                InsertionSort.getSwaps(),
                InsertionSort.getArrayAccesses(),
                InsertionSort.getIterations()
        );

        System.out.println(result);
        csvWriter.write(result + "\n");
    }

    private static void runOptimizedBenchmark(int[] array, FileWriter csvWriter)
            throws IOException {
        long startTime = System.nanoTime();
        InsertionSort.sortOptimized(array);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;

        String result = String.format("Optimized,%d,%d,%d,%d,%d,%d",
                array.length,
                duration,
                InsertionSort.getComparisons(),
                InsertionSort.getSwaps(),
                InsertionSort.getArrayAccesses(),
                InsertionSort.getIterations()
        );

        System.out.println(result);
        csvWriter.write(result + "\n");
    }

    private static void runBinarySearchBenchmark(int[] array, FileWriter csvWriter)
            throws IOException {
        long startTime = System.nanoTime();
        InsertionSort.sortWithBinarySearch(array);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;

        String result = String.format("BinarySearch,%d,%d,%d,%d,%d,%d",
                array.length,
                duration,
                InsertionSort.getComparisons(),
                InsertionSort.getSwaps(),
                InsertionSort.getArrayAccesses(),
                InsertionSort.getIterations()
        );

        System.out.println(result);
        csvWriter.write(result + "\n");
    }

    // Array generation methods
    private static int[] generateRandomArray(int size) {
        Random random = new Random(42); // Fixed seed for reproducibility
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }

    private static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    private static int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i - 1;
        }
        return array;
    }

    private static int[] generateNearlySortedArray(int size) {
        int[] array = generateSortedArray(size);
        Random random = new Random(42);

        // Shuffle 5% of elements
        int swaps = size / 20;
        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
    }

    private static void printHelp() {
        System.out.println("Insertion Sort Benchmark Runner");
        System.out.println("Usage: java cli.BenchmarkRunner");
        System.out.println("\nThis will run benchmarks on various input sizes and distributions.");
        System.out.println("Results are printed to console and saved to benchmark_results.csv");
        System.out.println("\nTested distributions:");
        System.out.println("- Random: Randomly generated arrays");
        System.out.println("- Sorted: Already sorted arrays (best case)");
        System.out.println("- ReverseSorted: Reverse sorted arrays (worst case)");
        System.out.println("- NearlySorted: Mostly sorted arrays with some disorder");
    }
}