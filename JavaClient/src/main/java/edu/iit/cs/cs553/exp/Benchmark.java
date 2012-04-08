package edu.iit.cs.cs553.exp;

import edu.iit.cs.cs553.javaclient.AppInterface;
import edu.iit.cs.cs553.javaclient.JavaDelegator;
import java.io.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bo Feng
 */
public class Benchmark {

    private static final String baseUrl = "http://localhost:8080";

    private enum Operation {

        PING, INSERT, FIND, REMOVE;
    }

    private final class PingTask implements Callable<Long> {

        @Override
        public Long call() throws Exception {
            AppInterface ai = new JavaDelegator(baseUrl);
            Long start = System.nanoTime();
            ai.connect();
            Long end = System.nanoTime();
            return end - start;
        }
    }

    private final class InsertTask implements Callable<Long> {

        private String filename;

        private InsertTask(String line) {
            this.filename = line;
        }

        @Override
        public Long call() throws Exception {
            AppInterface ai = new JavaDelegator(baseUrl);
            File target = new File(filename);
            FileReader fr = new FileReader(target);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Long start = System.nanoTime();
            boolean result = ai.insert(this.filename, sb.toString());
            Long end = System.nanoTime();
            if(result==false)
                Logger.getLogger(InsertTask.class.getCanonicalName()).log(Level.WARNING, "Insert file failed: {0}", this.filename);
            return end - start;
        }
    }

    private void run(File workload, Operation what, int numOfThreads, boolean memcache) throws InterruptedException, ExecutionException, FileNotFoundException, IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(numOfThreads);
        CompletionService<Long> pool = new ExecutorCompletionService<Long>(threadPool);

        FileReader fr = new FileReader(workload);
        BufferedReader br = new BufferedReader(fr);
        int total = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (what.equals(Operation.INSERT)) {
                pool.submit(new InsertTask(line));
            }
            total++;
        }

        for (int i = 0; i < total; i++) {
            Long result = pool.take().get();
            System.out.println(result);
        }

        threadPool.shutdown();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            File workload = new File("workload.index");
            long start = System.nanoTime();
            new Benchmark().run(workload, Operation.INSERT, 4, true);
            System.out.println("This will take " + (System.nanoTime() - start) + " nano seconds.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
