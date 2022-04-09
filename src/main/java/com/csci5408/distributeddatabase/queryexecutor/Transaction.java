package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.Query;

import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

public class Transaction {
    // Queue of queries
    private Queue<Query> queries;
    private String databaseName;
    private Map<String, Properties> transactionalTable;

    // constructor
    public Transaction() {
        this.queries = new LinkedList<>();
    }

    // getting the queries
    public Queue<Query> getQueries() {
        return queries;
    }

    // setting the queries
    public void setQueries(Queue<Query> queries) {
        this.queries = queries;
    }

    // adding the queries
    public void addQuery(Query query) {
        this.queries.add(query);
    }

    public Map<String, Properties> getTransactionalTable() {
        return transactionalTable;
    }

    public void setTransactionalTable(Map<String, Properties> transactionalTable) {
        this.transactionalTable = transactionalTable;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
