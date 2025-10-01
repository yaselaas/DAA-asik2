package metrics;

import java.util.HashMap;
import java.util.Map;


public class PerformanceTracker {
    private static final Map<String, Long> metrics = new HashMap<>();
    private static long startTime;


    public static void startTimer() {
        startTime = System.nanoTime();
    }


    public static void stopTimer(String operationName) {
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        metrics.put(operationName + "_time_ns", duration);
        metrics.put(operationName + "_time_ms", duration / 1_000_000);
    }


    public static void incrementCounter(String counterName) {
        metrics.put(counterName, metrics.getOrDefault(counterName, 0L) + 1);
    }


    public static void setMetric(String metricName, long value) {
        metrics.put(metricName, value);
    }


    public static long getMetric(String metricName) {
        return metrics.getOrDefault(metricName, 0L);
    }


    public static void reset() {
        metrics.clear();
    }


    public static void printMetrics() {
        System.out.println("=== Performance Metrics ===");
        for (Map.Entry<String, Long> entry : metrics.entrySet()) {
            System.out.printf("%-25s: %d%n", entry.getKey(), entry.getValue());
        }
    }


    public static String getMetricsCSV() {
        StringBuilder csv = new StringBuilder();
        for (Map.Entry<String, Long> entry : metrics.entrySet()) {
            csv.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        return csv.toString();
    }

    public static Map<String, Long> getAllMetrics() {
        return new HashMap<>(metrics);
    }
}