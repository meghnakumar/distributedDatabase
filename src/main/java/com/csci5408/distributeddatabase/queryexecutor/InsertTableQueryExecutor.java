package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.fileoperations.FileUtil;
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
    public String execute() throws Exception
    {
        StringBuilder result = new StringBuilder();
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        String tableName = insertQuery.getTableName();
        DistributedHelper distributedHelper = new DistributedHelper();

        if(!distributedHelper.isDatabasePresentInLocalInstance(chosenDatabaseName))
        {
            result.append(distributedHelper.executeQueryInOtherInstance(this.insertQuery.getSql()));
            return result.toString();
        }

        //Step 1 check if the table exists to insert the data
        if(!QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName))
        {
            System.err.println("Insert table not possible here as database or the table text file does not exists");
        }

        //step flush the data to the file
        try
        {
            String tableFileName = QueryExecutorUtil.getTableFileName(chosenDatabaseName, tableName);

            //ToDo flush the insert data to the table
            LinkedHashMap columnValueMap = insertQuery.getFieldValueMap();
            Set<String> columnNames = columnValueMap.keySet();
            ArrayList<String> columnValues = new ArrayList<String>();

            for(String key : columnNames)
            {
                String columnValue = columnValueMap.get(key).toString();
                columnValues.add(columnValue);
            }

            String newRow = String.join(QueryConstants.SEPARATOR_ROW_COLUMN, columnValues);
            System.err.println("inserting new row into the table "+insertQuery.getTableName());
            FileUtil.writeToExistingFile(tableFileName, newRow);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }
}
