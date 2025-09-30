package metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Performance metrics tracker for algorithm analysis
 * Tracks: execution time, comparisons, swaps, array accesses
 */
public class PerformanceTracker {
    private static final Map<String, Long> metrics = new HashMap<>();
    private static long startTime;

    /**
     * Starts timer for execution time measurement
     */
    public static void startTimer() {
        startTime = System.nanoTime();
    }

    /**
     * Stops timer and saves execution time
     * @param operationName operation identifier
     */
    public static void stopTimer(String operationName) {
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        metrics.put(operationName + "_time_ns", duration);
        metrics.put(operationName + "_time_ms", duration / 1_000_000);
    }

    /**
     * Increments counter by 1
     * @param counterName counter identifier
     */
    public static void incrementCounter(String counterName) {
        metrics.put(counterName, metrics.getOrDefault(counterName, 0L) + 1);
    }

    /**
     * Sets specific metric value
     * @param metricName metric identifier
     * @param value metric value
     */
    public static void setMetric(String metricName, long value) {
        metrics.put(metricName, value);
    }

    /**
     * Gets metric value
     * @param metricName metric identifier
     * @return metric value or 0 if not exists
     */
    public static long getMetric(String metricName) {
        return metrics.getOrDefault(metricName, 0L);
    }

    /**
     * Resets all metrics
     */
    public static void reset() {
        metrics.clear();
    }

    /**
     * Prints all metrics to console
     */
    public static void printMetrics() {
        System.out.println("=== Performance Metrics ===");
        for (Map.Entry<String, Long> entry : metrics.entrySet()) {
            System.out.printf("%-25s: %d%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns metrics in CSV format
     * @return CSV string with metrics
     */
    public static String getMetricsCSV() {
        StringBuilder csv = new StringBuilder();
        for (Map.Entry<String, Long> entry : metrics.entrySet()) {
            csv.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        return csv.toString();
    }

    /**
     * Returns all metrics as Map
     * @return Map with all metrics
     */
    public static Map<String, Long> getAllMetrics() {
        return new HashMap<>(metrics);
    }
}