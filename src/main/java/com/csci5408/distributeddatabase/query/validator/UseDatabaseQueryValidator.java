package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.UseDatabaseQuery;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

public class UseDatabaseQueryValidator implements Validator {
    @Override
    public void validate(Query query, Transaction transaction) {
        if (transaction != null) {
            throw new IllegalArgumentException("Cannot user create database inside transaction query");
        }

        UseDatabaseQuery dbQuery = (UseDatabaseQuery) query;
        if (!QueryExecutorUtil.isDatabaseExists(dbQuery.getDatabaseName())) {
            throw new IllegalArgumentException("OOPS!! Database doesn't exist");
        }
    }
}
