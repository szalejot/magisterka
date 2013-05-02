package edu.pjwstk.mherman.worker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BenchmarkWorker implements Runnable{

    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private List<Double> resultList;
    Boolean initialized = false;
 
    public BenchmarkWorker(List<Double> resultList) {
        this.resultList = resultList;
    }
    
    public void init() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost:12345/clusterdb?"
                    + "user=root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialized = true;
    }
    
    @Override
    public void run() {
        if (!initialized) {
            System.out.println("ERROR: worker not initialized.");
            return;
        }
        benchmark();      
    }

    private void benchmark() {
        try {
            preparedStatement = connect.prepareStatement("SELECT * FROM simples WHERE id > ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int invokedCommands = 0;
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 128; i++) {
            try {
                preparedStatement.setInt(1, i);
                resultSet = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            invokedCommands++;
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        
        synchronized(resultList) {
            resultList.add((double) elapsedTime / invokedCommands);
        }
    }


}
