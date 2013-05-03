package edu.pjwstk.mherman.worker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.PreparedStatementWrapper;

import edu.pjwstk.mherman.util.Configuration;

public class BenchmarkWorker implements Runnable{

    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private List<Double> resultList;
    private Configuration conf;
    List< List<Integer> > params;
    Boolean initialized = false;
 
    public BenchmarkWorker(Configuration configuration, List< List<Integer> > params, List<Double> resultList) {
        this.resultList = resultList;
        this.conf = configuration;
        this.params = params;
    }
    
    public void init() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/"
                    + conf.getDbName() + "?" + "user=" + conf.getUser());
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
            preparedStatement = connect.prepareStatement(conf.getStatementTemplate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int invokedCommands = 0;
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < params.size(); i++) {
            try {
                for (int j = 0; j < params.get(i).size(); j++) {
                    preparedStatement.setInt(j+1, params.get(i).get(j));
                }
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
