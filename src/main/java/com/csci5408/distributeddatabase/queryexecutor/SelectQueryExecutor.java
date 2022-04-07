package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.Criteria;
import com.csci5408.distributeddatabase.query.SelectQuery;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import org.springframework.util.StringUtils;

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
    public boolean execute() throws Exception
    {
        boolean result=true;
        try
        {
            String databaseName = QueryExecutorUtil.getChosenDatabase();
            String tableName = selectQuery.getTableName();
            List<String> columnsToDisplay = selectQuery.getColumns();

            List<HashMap<String, String>> tableMap = TableStructureHelper.getTableStructure(databaseName, tableName);
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
                        if(columnsToDisplay.contains(column))
                        {
                            line.append(" ").append(column);
                        }
                    }
                    System.out.println(line);
                    isHeaderRowDisplayed=true;
                }

                //display all the rows present in the table
                if(QueryExecutorUtil.checkCriteriaForRow(row, selectQuery.getCriteria()))
                {
                    for(String column: columns)
                    {
                        if(columnsToDisplay.contains(column))
                        {
                            line.append(" ").append(row.get(column));
                        }
                        System.out.println(line);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            result=false;
            ex.printStackTrace();
        }
        return result;
    }

}
