import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelMonteCarloPi {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = 1;
        if (args.length > 0) {
            try {
                numThreads = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                return;
            }
        } else {
            System.out.println("No arguments provided. Using 1 thread by default.");
        }
        long totalIterations = 1_000_000_000L;
        long iterationsPerThread = totalIterations / numThreads;

        System.out.println("Starting calculation with " + numThreads + " threads...");
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Long>> results = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            Callable<Long> task = new MonteCarloTask(iterationsPerThread);
            results.add(executor.submit(task));
        }
        long totalHits = 0;
        for (Future<Long> result : results) {
            totalHits += result.get();
        }

        executor.shutdown();

        long endTime = System.currentTimeMillis();
        double pi = 4.0 * totalHits / totalIterations;
        System.out.printf("PI is %.5f%n", pi);
        System.out.println("THREADS " + numThreads);
        System.out.printf("ITERATIONS %,d%n", totalIterations);
        System.out.println("TIME " + (endTime - startTime) + "ms");
    }
    static class MonteCarloTask implements Callable<Long> {
        private final long iterations;

        public MonteCarloTask(long iterations) {
            this.iterations = iterations;
        }

        @Override
        public Long call() {
            long hits = 0;
            ThreadLocalRandom random = ThreadLocalRandom.current();

            for (long i = 0; i < iterations; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();
                if (x * x + y * y <= 1.0) {
                    hits++;
                }
            }
            return hits;
        }
    }
}