package algorithms;

import metrics.PerformanceTracker;

/**
 * Insertion Sort implementation with advanced optimizations
 * for nearly-sorted data and detailed metrics collection
 *
 * Features:
 * - Basic Insertion Sort version
 * - Optimized version with guard element
 * - Binary search version for reduced comparisons
 * - Complete performance metrics tracking
 *
 * Complexity:
 * - Best case: O(n) - already sorted array
 * - Worst case: O(n²) - reverse sorted array
 * - Average case: O(n²)
 * - Space: O(1) - in-place sorting
 */
public class InsertionSort {
    // Static counters for detailed operation tracking
    private static long comparisons;
    private static long swaps;
    private static long arrayAccesses;
    private static long iterations;

    /**
     * Basic Insertion Sort version
     * @param array array to sort
     * @throws IllegalArgumentException if array is null
     */
    public static void sort(int[] array) {
        validateArray(array);
        resetMetrics();
        PerformanceTracker.startTimer();

        basicInsertionSort(array);

        PerformanceTracker.stopTimer("basic_sort");
        saveMetrics("basic");
    }

    /**
     * Optimized version for nearly-sorted data
     * Uses guard element to reduce comparison count
     * @param array array to sort
     * @throws IllegalArgumentException if array is null
     */
    public static void sortOptimized(int[] array) {
        validateArray(array);
        resetMetrics();
        PerformanceTracker.startTimer();

        optimizedInsertionSort(array);

        PerformanceTracker.stopTimer("optimized_sort");
        saveMetrics("optimized");
    }

    /**
     * Binary search version for insertion position finding
     * Reduces comparisons at the cost of increased array accesses
     * @param array array to sort
     * @throws IllegalArgumentException if array is null
     */
    public static void sortWithBinarySearch(int[] array) {
        validateArray(array);
        resetMetrics();
        PerformanceTracker.startTimer();

        binarySearchInsertionSort(array);

        PerformanceTracker.stopTimer("binary_search_sort");
        saveMetrics("binary_search");
    }

    // ===== ALGORITHM IMPLEMENTATIONS =====

    /**
     * Basic Insertion Sort implementation
     */
    private static void basicInsertionSort(int[] array) {
        if (array.length <= 1) return;

        for (int i = 1; i < array.length; i++) {
            iterations++;
            int key = array[i];
            arrayAccesses++;
            int j = i - 1;

            // Shift elements greater than key
            while (j >= 0) {
                comparisons++;
                arrayAccesses++;
                if (array[j] > key) {
                    array[j + 1] = array[j];
                    swaps++;
                    arrayAccesses++;
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = key;
            arrayAccesses++;
        }
    }

    /**
     * Optimized version with guard element
     */
    private static void optimizedInsertionSort(int[] array) {
        if (array.length <= 1) return;

        // Optimization: find minimum element and place at start
        // Creates guard element and reduces j >= 0 checks
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            comparisons++;
            if (array[i] < array[minIndex]) {
                minIndex = i;
            }
        }

        // Place minimum element at start if not already there
        if (minIndex != 0) {
            swap(array, 0, minIndex);
        }

        // Main sorting with guard element at array[0]
        for (int i = 2; i < array.length; i++) {
            iterations++;
            int key = array[i];
            arrayAccesses++;
            int j = i - 1;

            // Thanks to guard element, no need to check j >= 0
            while (array[j] > key) {
                comparisons++;
                array[j + 1] = array[j];
                swaps++;
                arrayAccesses += 2;
                j--;
            }
            comparisons++; // Account for last comparison
            array[j + 1] = key;
            arrayAccesses++;
        }
    }

    /**
     * Binary search version for insertion position
     */
    private static void binarySearchInsertionSort(int[] array) {
        if (array.length <= 1) return;

        for (int i = 1; i < array.length; i++) {
            iterations++;
            int key = array[i];
            arrayAccesses++;

            // Use binary search to find insertion position
            int pos = binarySearchPosition(array, 0, i - 1, key);

            // Shift elements
            for (int j = i - 1; j >= pos; j--) {
                array[j + 1] = array[j];
                swaps++;
                arrayAccesses += 2;
            }
            array[pos] = key;
            arrayAccesses++;
        }
    }

    /**
     * Binary search for insertion position
     */
    private static int binarySearchPosition(int[] array, int left, int right, int key) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            arrayAccesses++;

            if (array[mid] == key) {
                return mid + 1; // Insert after equal element
            } else if (array[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    // ===== HELPER METHODS =====

    /**
     * Swap two elements in array
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        swaps += 3; // 3 write operations
        arrayAccesses += 4;
    }

    /**
     * Validate input array
     */
    private static void validateArray(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
    }

    /**
     * Reset all metrics
     */
    private static void resetMetrics() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        iterations = 0;
        PerformanceTracker.reset();
    }

    /**
     * Save metrics to PerformanceTracker
     */
    private static void saveMetrics(String prefix) {
        PerformanceTracker.setMetric(prefix + "_comparisons", comparisons);
        PerformanceTracker.setMetric(prefix + "_swaps", swaps);
        PerformanceTracker.setMetric(prefix + "_array_accesses", arrayAccesses);
        PerformanceTracker.setMetric(prefix + "_iterations", iterations);
    }

    // ===== PUBLIC METRIC ACCESS METHODS =====

    public static long getComparisons() { return comparisons; }
    public static long getSwaps() { return swaps; }
    public static long getArrayAccesses() { return arrayAccesses; }
    public static long getIterations() { return iterations; }

    /**
     * Check if array is sorted
     */
    public static boolean isSorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility method to print array
     */
    public static void printArray(int[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}