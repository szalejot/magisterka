package edu.pjwstk.mherman.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.pjwstk.mherman.worker.BenchmarkWorker;

public class MainClass {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Double> resultList = new ArrayList<Double>();
        List<Thread> threads = new ArrayList<Thread>();
        System.out.println("Initializing threads...");
        int numberofThreads = 4;
        for (int i = 0; i < numberofThreads; i++) {
            BenchmarkWorker worker = new BenchmarkWorker(resultList);
            worker.init();
            Thread thread = new Thread(worker);
            threads.add(thread);
          }
        System.out.println("Starting benchmark...");
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        double result = listAvg(resultList);
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Average value returned from worker = " + df.format(result) + "ms");
    }
    
    private static double listAvg(List<Double> list) {
        if (list.isEmpty()) {
            System.out.println("ERROR: empty list to lisAvg!");
            return 0;
        }
        Double avg = 0d;
        for (Double d : list) {
            if (d == 0) {
                System.out.println("ERROR: list has null element in listAvg!");
            }
            avg += d;
        }
        return avg / list.size();
    }

}
