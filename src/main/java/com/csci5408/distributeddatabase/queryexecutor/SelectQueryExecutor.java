package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.query.SelectQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SelectQueryExecutor implements IQueryExecutor
{
    private SelectQuery selectQuery;

    public SelectQueryExecutor(SelectQuery selectQuery)
    {
        this.selectQuery=selectQuery;
    }

    @Override
    public String execute() throws Exception
    {
        StringBuilder result = new StringBuilder();
        String databaseName = QueryExecutorUtil.getChosenDatabase();
        String tableName = selectQuery.getTableName();
        DistributedHelper distributedHelper = new DistributedHelper();

        System.err.println("select queru criteria = "+selectQuery.getCriteria());

        if(!distributedHelper.isDatabasePresentInLocalInstance(databaseName))
        {
            result.append(distributedHelper.executeQueryInOtherInstance(this.selectQuery.getSql()));
            return result.toString();
        }

        try
        {
            List<String> columnsToDisplay = selectQuery.getColumns();
            ArrayList<HashMap<String, String>> tableMap = TableStructureHelper.getTableStructure(databaseName, tableName);
            boolean isHeaderRowDisplayed = false;

            for(HashMap<String, String> row : tableMap)
            {
                StringBuilder line = new StringBuilder();
                Set<String> columns = row.keySet();

                //create header row for the table if not done before
                if(!isHeaderRowDisplayed)
                {
                    for(String column: columns)
                    {
                        column = column.trim();
                        if(columnsToDisplay.contains(column))
                        {
                            line.append(" ").append(column);
                        }
                    }
                    System.out.println(line);
                    result.append(line).append("\n");
                    line = new StringBuilder();
                    isHeaderRowDisplayed=true;
                }

                //display all the rows present in the table
                if( selectQuery.getCriteria()==null || (selectQuery.getCriteria()!=null && QueryExecutorUtil.checkCriteriaForRow(row, selectQuery.getCriteria())))
                {
                    for(String column: columns)
                    {
                        String trimmedColumn = column.trim();
                        if(columnsToDisplay.contains(trimmedColumn))
                        {
                            line.append(" ").append(row.get(column));
                            result.append(line).append("\n");
                        }
                    }
                    System.out.println(line);
                }
            }
        }
        catch (Exception ex)
        {
            result = new StringBuilder();
            result.append("Exception occurred in query execution"+ex.getMessage());
            ex.printStackTrace();
        }
        return result.toString();
    }

}
