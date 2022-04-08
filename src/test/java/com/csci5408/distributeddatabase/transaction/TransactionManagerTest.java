package com.csci5408.distributeddatabase.transaction;

import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.transaction.Transaction;
import com.csci5408.distributeddatabase.transaction.TransactionManager;
import org.junit.Test;

import java.util.Queue;

public class TransactionManagerTest {

    @Test(expected = IllegalArgumentException.class)
    public void getTransactionFailureTest() throws Exception {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction = transactionManager.getTransaction("select * from employee; create table customer(id number, name varchar);");
        System.out.println(transaction);
    }

    @Test
    public void getTransactionSuccessTest() throws Exception {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction = transactionManager.getTransaction("start transaction; select * from employee; create table customer(id number, name varchar); commit;");
        Queue<Query> queries = transaction.getQueries();
        System.out.println(transaction);
        System.out.println(queries);
    }
}