import com.google.common.primitives.Longs;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * Created by Mateusz on 29.03.2017.
 */
public class BenchmarkThread implements Callable<long[]> {

    int numberOfMeasures;
    int allocationSize;
    boolean fixedSizeAllocation = true;

    BenchmarkThread(int numberOfMeasures, int allocationSize, boolean fixedSizeAllocation){
        this.numberOfMeasures = numberOfMeasures;
        this.allocationSize = allocationSize;
        this.fixedSizeAllocation = fixedSizeAllocation;
    }

    Vector fixedSizeAllocation(int size){
        Vector<byte[]> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            byte b[] = new byte[512];
            v.add(b);
        }
        return v;
    }

    Vector randomSizeAllocation(int size){
        Random rnd = new Random(7);
        Vector<byte[]> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            byte b[] = new byte[Math.abs(rnd.nextInt() % 512)];
            v.add(b);
        }
        return v;
    }

    @Override
    public long[] call() throws Exception {
        long[] measuresResults = new long[numberOfMeasures];
        for (int i = 0; i < numberOfMeasures; i++) {
            long startTime = System.nanoTime();
            Vector v = (fixedSizeAllocation) ? fixedSizeAllocation(allocationSize) : randomSizeAllocation(allocationSize);
            v.size();
            long duration = System.nanoTime() - startTime;
            measuresResults[i] = duration;
        }
        long max = Longs.max(measuresResults);
        long min = Longs.min(measuresResults);
        long avg = (long) Arrays.stream(measuresResults).average().getAsDouble();
        long[] stats = {max, min, avg};
        return stats;
    }
}
