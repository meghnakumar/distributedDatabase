package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;


public class QueryExecutor
{
    private String sqlQuery;

    public QueryExecutor(String sqlQuery)
    {
        this.sqlQuery=sqlQuery;
    }

    public String executeQuery()
    {
        try
        {
            IQueryExecutor queryExecutor = null;
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            switch(query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    queryExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    queryExecutor = new InsertTableQueryExecutor(insertQuery);
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        queryExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        queryExecutor = new UpdateQueryExecutor(updateQuery);
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        queryExecutor = new DeleteQueryExecutor(deleteQuery);
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        SelectQuery selectQuery = (SelectQuery) query;
                        queryExecutor = new SelectQueryExecutor(selectQuery);
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    queryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
                    break;
                default:
                    System.err.println("You have entered an invalid query");
            }
            return queryExecutor.execute();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "returned after switch";
    }

    public String executeTransaction()
    {
        //ToDo handle transaction queries here
        return "";
    }
}
