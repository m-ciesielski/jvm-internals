import com.google.common.primitives.Longs;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AllocationBenchmark {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numberOfMeasures = Integer.parseInt(args[0]);
        int allocationSize = Integer.parseInt(args[1]);
        int threadsCount = Integer.parseInt(args[2]);
        boolean fixedSizeAllocation = Boolean.valueOf(args[3]);

        List<BenchmarkThread> benchmarkThreads = new ArrayList<>();
        for (int i = 0; i < threadsCount; i++) {
            benchmarkThreads.add(new BenchmarkThread(numberOfMeasures, allocationSize, fixedSizeAllocation));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        List<Future<long[]>> benchmarkResults = executorService.invokeAll(benchmarkThreads);

        // Collect overall metrics
        long[] overallMetrics = {0, 0, 0};
        for(Future<long[]> futureResult : benchmarkResults){
            long[] result = futureResult.get();
            for (int i = 0; i < result.length; i++) {
                overallMetrics[i] += result[i];
            }
        }

        System.out.println("Max: " + overallMetrics[0] / benchmarkResults.size());
        System.out.println("Min: " + overallMetrics[1] / benchmarkResults.size());
        System.out.println("Avg: " + overallMetrics[2] / benchmarkResults.size());

        executorService.shutdown();

    }
}
