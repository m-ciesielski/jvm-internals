import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentDateFormat {

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private ThreadLocal<SimpleDateFormat> localSdf = new ThreadLocal<>().withInitial(
            () -> new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH));

    // Use Simple date format in concurrent scenario
    // in order to prove that it is not thread safe.
    private void runSimpleDateFormatConcurrently(int threadsCount, boolean threadLocal) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        List<Callable<Date>> tasks = new ArrayList<>();

        Date expectedParsedDate = new Date();
        String testDate = "June 6, 2017";
        try {
            expectedParsedDate = threadLocal ? localSdf.get().parse(testDate) : sdf.parse(testDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < threadsCount; i++) {
            tasks.add(() -> threadLocal ? localSdf.get().parse(testDate) : sdf.parse(testDate));
        }
        try {
            List<Future<Date>> results = executorService.invokeAll(tasks);
            for (Future<Date> result: results){
                Date parsedDate = result.get();
                if (!expectedParsedDate.equals(parsedDate)){
                    throw new IllegalStateException(
                            String.format("Expected date: %s, got date: %s", expectedParsedDate,
                            parsedDate));
                }
            }
        } catch (InterruptedException|ExecutionException|IllegalStateException e) {
            e.printStackTrace();
        }
        finally {
            executorService.shutdown();
        }


    }


    public static void main(String[] args){
        ConcurrentDateFormat cdf = new ConcurrentDateFormat();
        int threads = Runtime.getRuntime().availableProcessors();
        System.out.println(String.format("Running SimpleDateFormat using %d threads ...", threads));
        System.out.println("This should raise an exception after running a few times.");
        cdf.runSimpleDateFormatConcurrently(threads, false);
        System.out.println(String.format("\nRunning SimpleDateFormat wrapped as ThreadLocal" +
                " using %d threads ...", threads));
        System.out.println("This should never raise an exception.");
        cdf.runSimpleDateFormatConcurrently(threads, true);
    }
}
