package edu.iit.cs.cs553.exp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Bo Feng
 */
public class WorkloadGenerator {

    private final static String folder = "files/";

    private static String randomAlphaNumericalString(int length) {
        Random r = new Random(System.nanoTime());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 0 - 9, A - Z
            int n = r.nextInt(36);
            char c;

            if (n <= 9) {
                c = (char) ('0' + n);
            } else {
                c = (char) ('A' + (n - 10));
            }

            sb.append(c);
        }
        return sb.toString();
    }

    private static String generateFile(int bytes) throws IOException {
        String filename = randomAlphaNumericalString(10);
        File file = new File(folder + filename);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        int lines = (bytes % 100 > 0) ? (bytes / 100 + 1) : bytes / 100;
        for (int i = 0; i < lines; i++) {
            String cbuf = randomAlphaNumericalString(100);
            bw.write(cbuf);
            bw.newLine();
        }
        bw.close();
        fw.close();
        return filename;
    }

    public static void main(String[] args) throws IOException {
        if(args.length<6)
            throw new IllegalArgumentException();

        int _1K_counter = Integer.valueOf(args[0]);
        int _10K_counter = Integer.valueOf(args[1]);
        int _100K_counter = Integer.valueOf(args[2]);
        int _1M_counter = Integer.valueOf(args[3]);
        int _10M_counter = Integer.valueOf(args[4]);
        int _100M_counter = Integer.valueOf(args[5]);
        File file = new File(args[6]);
        
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        while (_1K_counter + _10K_counter + _100K_counter + _1M_counter + _10M_counter + _100M_counter > 0) {
            int size;
            Random r = new Random(System.nanoTime());
            int next = r.nextInt(_1K_counter + _10K_counter + _100K_counter + _1M_counter + _10M_counter + _100M_counter);
            if (next < _1K_counter) {
                size = 1000;
                _1K_counter--;
            } else if (next < _1K_counter + _10K_counter) {
                size = 10 * 1000;
                _10K_counter--;
            } else if (next < _1K_counter + _10K_counter + _100K_counter) {
                size = 100 * 1000;
                _100K_counter--;
            } else if (next < _1K_counter + _10K_counter + _100K_counter + _1M_counter) {
                size = 1000 * 1000;
                _1M_counter--;
            } else if (next < _1K_counter + _10K_counter + _100K_counter + _1M_counter + _10M_counter) {
                size = 10 * 1000 * 1000;
                _10M_counter--;
            } else {
                size = 100 * 1000 * 1000;
                _100M_counter--;
            }
            bw.write(folder + generateFile(size));
            bw.newLine();
        }
        bw.close();
        fw.close();

        System.out.println("done");
    }
}
