package edu.pjwstk.mherman.util;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

	private String comment;
    private String host;
    private String port;
    private String dbName;
    private String user;
    private int numberOfThreads;
    private String statementTemplate;
    private List<List<Integer>> parametersForThreads = new ArrayList<List<Integer>>();

    public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public String getStatementTemplate() {
        return statementTemplate;
    }

    public void setStatementTemplate(String preparedStatement) {
        this.statementTemplate = preparedStatement;
    }

    public List<List<Integer>> getParametersForThreads() {
        return parametersForThreads;
    }
}
