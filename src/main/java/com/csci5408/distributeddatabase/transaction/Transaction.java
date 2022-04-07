package com.csci5408.distributeddatabase.transaction;

import com.csci5408.distributeddatabase.query.Query;

import java.util.LinkedList;
import java.util.Queue;

public class Transaction {
    // Queue of queries
    private Queue<Query> queries;

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
}
