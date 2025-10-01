package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class InsertionSortTest {

    @BeforeEach
    void setUp() {
        // Reset metrics before each test
        InsertionSort.sort(new int[0]); // Initialization
    }

    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] array = {};
        InsertionSort.sort(array);
        assertEquals(0, array.length);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle single element array")
    void testSingleElement() {
        int[] array = {5};
        InsertionSort.sort(array);
        assertArrayEquals(new int[]{5}, array);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle already sorted array")
    void testSortedArray() {
        int[] array = {1, 2, 3, 4, 5};
        InsertionSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle reverse sorted array")
    void testReverseSortedArray() {
        int[] array = {5, 4, 3, 2, 1};
        InsertionSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        InsertionSort.sort(array);
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 5, 6, 9}, array);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle nearly sorted array with optimized version")
    void testNearlySortedArray() {
        int[] array = {1, 2, 3, 5, 4, 6, 7};
        InsertionSort.sortOptimized(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7}, array);
        assertTrue(InsertionSort.isSorted(array));
    }

    @Test
    @DisplayName("Should handle null array gracefully")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            InsertionSort.sort(null);
        });
    }

    @Test
    @DisplayName("Should track performance metrics")
    void testPerformanceMetrics() {
        int[] array = {5, 2, 4, 6, 1, 3};
        InsertionSort.sort(array);

        assertTrue(InsertionSort.getComparisons() > 0, "Comparisons should be positive");
        assertTrue(InsertionSort.getSwaps() > 0, "Swaps should be positive");
        assertTrue(InsertionSort.getArrayAccesses() > 0, "Array accesses should be positive");

        System.out.println("Basic Sort - Comparisons: " + InsertionSort.getComparisons() +
                ", Swaps: " + InsertionSort.getSwaps() +
                ", Array Accesses: " + InsertionSort.getArrayAccesses());
    }

    @Test
    @DisplayName("Should compare optimized vs basic versions")
    void testOptimizedVsBasicOnNearlySorted() {
        int[] array1 = generateNearlySortedArray(100);
        int[] array2 = array1.clone();

        InsertionSort.sort(array1);
        long basicComparisons = InsertionSort.getComparisons();
        long basicSwaps = InsertionSort.getSwaps();

        InsertionSort.sortOptimized(array2);
        long optimizedComparisons = InsertionSort.getComparisons();
        long optimizedSwaps = InsertionSort.getSwaps();

        System.out.println("Basic - Comparisons: " + basicComparisons + ", Swaps: " + basicSwaps);
        System.out.println("Optimized - Comparisons: " + optimizedComparisons + ", Swaps: " + optimizedSwaps);

        // Both versions should produce correct results
        assertTrue(InsertionSort.isSorted(array1));
        assertTrue(InsertionSort.isSorted(array2));

        if (optimizedComparisons < basicComparisons) {
            System.out.println("✓ Optimization reduced comparisons by " + (basicComparisons - optimizedComparisons));
        } else {
            System.out.println("ℹ️ Guard element overhead: " + (optimizedComparisons - basicComparisons) + " extra comparisons");
        }
    }

    @Test
    @DisplayName("Should test binary search version")
    void testBinarySearchVersion() {
        int[] array = {5, 2, 4, 6, 1, 3};
        InsertionSort.sortWithBinarySearch(array);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, array);
        assertTrue(InsertionSort.isSorted(array));
        assertTrue(InsertionSort.getComparisons() > 0);
    }

    @Test
    @DisplayName("Should handle large array")
    void testLargeArray() {
        int[] array = generateRandomArray(1000);
        InsertionSort.sort(array);
        assertTrue(InsertionSort.isSorted(array));
    }

    // Helper methods for test data generation
    private int[] generateNearlySortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        // Add some disorder
        if (size > 10) {
            array[5] = 8;
            array[8] = 5;
        }
        return array;
    }

    private int[] generateRandomArray(int size) {
        java.util.Random random = new java.util.Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }
}