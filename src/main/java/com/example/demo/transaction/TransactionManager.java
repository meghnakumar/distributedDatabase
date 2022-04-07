package com.example.demo.transaction;

import com.example.demo.query.parsers.QueryParser;

import java.util.Arrays;
import java.util.List;

public class TransactionManager {

    // query parser
    private QueryParser queryParser;

    // constructor
    public TransactionManager() {
        this.queryParser = new QueryParser();
    }

    // getting the Transaction POJO
    public Transaction getTransaction(String queries) throws Exception {
        // if it is not transaction query then throw exception
        if(!isTransactionalQuery(queries)) {
            throw new IllegalArgumentException("Oops! Request query is not transactional type. To parse the normal query please use QueryParser");
        }
        // splitting the query on the basis of ';'
        List<String> queryList = Arrays.asList(queries.split("(?<=;)"));
        Transaction transaction = new Transaction();
        // iterating over the query list
        for (String query : queryList) {
            // trimming the query
            query = query.trim();
            // if it is not "start" and "commit" transaction query then add it into a queue
            if(!query.equalsIgnoreCase("start transaction;") && !query.equalsIgnoreCase("commit;")) {
                transaction.addQuery(queryParser.parse(query));
            }
        }
        // if you want to get Queue<Queries> queries then just call transaction.getQueries() at execution side
        // because this is transaction wrapper which contains Queue<Query> query
        // returning the transaction
        return transaction;
    }

    // invoke this method before requesting the specific POJO object
    // if this method returns true then call this transaction manager to get the Transaction POJO
    // else call the QueryParser class to get the Query POJO
    public boolean isTransactionalQuery(String query) {
        return (query.startsWith("start transaction;") && query.endsWith("commit;"));
    }
}
