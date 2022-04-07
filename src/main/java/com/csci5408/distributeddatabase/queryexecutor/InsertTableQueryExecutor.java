package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.InsertQuery;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.*;

public class InsertTableQueryExecutor implements IQueryExecutor
{
    private InsertQuery insertQuery;

    public InsertTableQueryExecutor(InsertQuery insertQuery)
    {
        this.insertQuery=insertQuery;
    }

    @Override
    public boolean execute() throws Exception
    {
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        String tableName = insertQuery.getTableName();
        try
        {
            //ToDo flush the insert data to the table
            LinkedHashMap columnValueMap = insertQuery.getFieldValueMap();
            Iterator iterator = columnValueMap.entrySet().iterator();

            Set<String> columnNames = columnValueMap.keySet();
            ArrayList<String> columnValues = new ArrayList<String>();
            for(String key : columnNames)
            {
                String columnValue = columnValueMap.get(key).toString();
                columnValues.add(columnValue);
            }

            String newRow = String.join(QueryConstants.SEPARATOR_ROW_COLUMN, columnValues);
            System.err.println("inserting new row into the table "+insertQuery.getTableName());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
