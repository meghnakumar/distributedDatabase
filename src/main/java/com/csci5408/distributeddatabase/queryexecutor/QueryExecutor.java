package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.Parser;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import static com.csci5408.distributeddatabase.query.QueryType.CREATE_DATABASE;
import static com.csci5408.distributeddatabase.query.QueryType.CREATE_TABLE;

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
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            switch(query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    createDatabaseExecutor.execute();
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
                    insertTableQueryExecutor.execute();
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());
                        createTableExecutor.execute();
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        UpdateQueryExecutor updateQueryExecutor = new UpdateQueryExecutor(updateQuery);
                        updateQueryExecutor.execute();
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        DeleteQueryExecutor deleteQueryExecutor = new DeleteQueryExecutor(deleteQuery);
                        deleteQueryExecutor.execute();
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        SelectQuery selectQuery = (SelectQuery) query;
                        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(selectQuery);
                        selectQueryExecutor.execute();
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
                    useDatabaseQueryExecutor.execute();
                    break;
                default:
                    System.err.println("You have entered an invalid query");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    public String executeTransaction()
    {
        //ToDo handle transaction queries here
        return "";
    }
}
