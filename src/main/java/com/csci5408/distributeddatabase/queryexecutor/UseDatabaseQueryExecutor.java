package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.UseDatabaseQuery;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;

public class UseDatabaseQueryExecutor implements IQueryExecutor
{
    private UseDatabaseQuery useDatabaseQuery;

    public UseDatabaseQueryExecutor(UseDatabaseQuery useDatabaseQuery)
    {
        this.useDatabaseQuery=useDatabaseQuery;
    }

    @Override
    public String execute() throws Exception
    {
        System.setProperty(QueryConstants.PROPERTY_CURRENT_DATABASE, useDatabaseQuery.getDatabaseName());
        System.out.println(System.getProperties());
        return System.getProperties().getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE);
    }
}
