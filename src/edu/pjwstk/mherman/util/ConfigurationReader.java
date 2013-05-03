package edu.pjwstk.mherman.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationReader {

    public static Configuration readConfiguration(String fileName) {
        Configuration conf = new Configuration();
        Map<String, String> paramMap = generateParamMap(fileName);
        
        conf.setHost(paramMap.get("host"));
        conf.setPort(paramMap.get("port"));
        conf.setDbName(paramMap.get("dbName"));
        conf.setUser(paramMap.get("user"));
        conf.setNumberOfThreads(Integer.parseInt(paramMap.get("numberOfThreads")));
        
        String statementsFileName = paramMap.get("statementsParamFileName");
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(statementsFileName));
            sCurrentLine = br.readLine();
            conf.setStatementTemplate(sCurrentLine);
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.trim().isEmpty()) {
                    continue;
                }
                String[] strArr = sCurrentLine.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (String s : strArr) {
                    list.add(Integer.parseInt(s.trim()));
                }
                conf.getParametersForThreads().add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return conf;
    }
    
    private static Map<String, String> generateParamMap(String fileName) {
        Map<String, String> paramMap = new HashMap<String, String>();

        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileName));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.trim().isEmpty()) {
                    continue;
                }
                String[] strArr = sCurrentLine.split("=");
                if (strArr.length != 2) {
                    System.out.println("ERROR: cannot parse configuration line: " + sCurrentLine);
                }
                paramMap.put(strArr[0].trim(), strArr[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return paramMap;
    }
    
}
