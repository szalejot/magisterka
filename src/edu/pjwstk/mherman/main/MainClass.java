package edu.pjwstk.mherman.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.pjwstk.mherman.util.Configuration;
import edu.pjwstk.mherman.util.ConfigurationReader;
import edu.pjwstk.mherman.worker.BenchmarkWorker;

public class MainClass {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Double> resultList = new ArrayList<Double>();
        List<Thread> threads = new ArrayList<Thread>();
        if (args.length != 1) {
            usage();
            return;
        }
        Configuration conf = ConfigurationReader.readConfiguration(args[0]);
        System.out.println("Initializing threads...");
        int numberOfThreads = conf.getNumberOfThreads();
        for (int i = 0; i < numberOfThreads; i++) {
            List< List<Integer> > list = new ArrayList< List<Integer> >();
            for (int j = i; j < conf.getParametersForThreads().size(); j += numberOfThreads) {
                list.add(conf.getParametersForThreads().get(j));
            }
            BenchmarkWorker worker = new BenchmarkWorker(conf, list, resultList);
            worker.init();
            Thread thread = new Thread(worker);
            threads.add(thread);
          }
        System.out.println("Starting benchmark...");
        long startTime = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        System.out.println("Waiting for benchmark to complete...");
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        
        double result = listAvg(resultList);
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Average value returned from worker = " + df.format(result) + "ms");
        System.out.println("Total benchmark time: " + makePrettyTime(elapsedTime));
        System.out.println("Test comment: " + conf.getComment());
    }
    
    private static String makePrettyTime(Long milis) {
        long time = milis / 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        return hours + ":" + minutes + ":" + seconds;
    }
    
    private static void usage() {
        System.out.println("USAGE: app <path_to_param_file>");
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
