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

    private static final String baseUrl = "http://save-files.appspot.com";

    private enum Operation {

        PING, INSERT, FIND, REMOVE;
    }

    private final class PingTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            AppInterface ai = new JavaDelegator(baseUrl);
            Long start = System.nanoTime();
            ai.connect();
            Long end = System.nanoTime();
            return String.valueOf(end - start);
        }
    }

    private final class InsertTask implements Callable<String> {

        private String filename;

        private InsertTask(String line) {
            this.filename = line;
        }

        @Override
        public String call() throws Exception {
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
            if (result == false) {
                Logger.getLogger(InsertTask.class.getCanonicalName()).log(Level.WARNING, "Insert file failed: {0}", this.filename);
            }
            return String.format("%d,%d", target.length(), end - start);
        }
    }

    private final class FindTask implements Callable<String> {

        private String filename;

        private FindTask(String line) {
            this.filename = line;
        }

        @Override
        public String call() throws Exception {
            File target = new File(filename);
            AppInterface ai = new JavaDelegator(baseUrl);
            Long start = System.nanoTime();
            ai.find(this.filename);
            Long end = System.nanoTime();
            return String.format("%d,%d", target.length(), end - start);
        }
    }

    private final class RemoveTask implements Callable<String> {

        private String filename;

        private RemoveTask(String line) {
            this.filename = line;
        }

        @Override
        public String call() throws Exception {
            File target = new File(filename);
            AppInterface ai = new JavaDelegator(baseUrl);
            Long start = System.nanoTime();
            ai.remove(this.filename);
            Long end = System.nanoTime();
            return String.format("%d,%d", target.length(), end - start);
        }
    }

    private void run(File workload, Operation what, int numOfThreads, boolean memcache) throws InterruptedException, ExecutionException, FileNotFoundException, IOException {
        if (workload == null || what == null || numOfThreads < 1) {
            throw new IllegalArgumentException("Please setup workload file, test action, number of threads.");
        }

        AppInterface ai = new JavaDelegator(baseUrl);
        if (!ai.memcache(memcache)) {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, "Memcache setup error.");
            return;
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(numOfThreads);
        CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

        FileReader fr = new FileReader(workload);
        BufferedReader br = new BufferedReader(fr);
        int total = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (what.equals(Operation.PING)) {
                pool.submit(new PingTask());
            } else if (what.equals(Operation.INSERT)) {
                pool.submit(new InsertTask(line));
            } else if (what.equals(Operation.FIND)) {
                pool.submit(new FindTask(line));
            } else if (what.equals(Operation.REMOVE)) {
                pool.submit(null);
            }
            total++;
        }

        for (int i = 0; i < total; i++) {
            String result = pool.take().get();
            System.out.println(result);
        }

        threadPool.shutdown();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            if (args.length < 4) {
                return;
            }

            File workload = new File(args[0]);
            Operation op = Operation.valueOf(args[1].toUpperCase());
            int numOfThreads = Integer.valueOf(args[2]);
            boolean turnMem = Boolean.valueOf(args[3]);

            long start = System.nanoTime();
            new Benchmark().run(workload, op, numOfThreads, turnMem);
            System.out.println("This will take " + (System.nanoTime() - start) + " nano seconds.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
