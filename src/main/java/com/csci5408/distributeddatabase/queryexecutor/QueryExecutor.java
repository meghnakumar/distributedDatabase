package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.parsers.Parser;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;

import static com.csci5408.distributeddatabase.query.QueryType.CREATE_DATABASE;

public class QueryExecutor
{
    private String sqlQuery;

    public QueryExecutor(String sqlQuery)
    {
        this.sqlQuery=sqlQuery;
    }

    public String executeQuery()
    {
        //TODO handle query execution

        try
        {
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);

            if(query.getQueryType().equals(CREATE_DATABASE))
            {
                CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                createDatabaseExecutor.execute();
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
