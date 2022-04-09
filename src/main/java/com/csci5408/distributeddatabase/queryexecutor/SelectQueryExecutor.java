package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.SelectQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SelectQueryExecutor implements IQueryExecutor {
    private SelectQuery selectQuery;

    public SelectQueryExecutor(SelectQuery selectQuery) {
        this.selectQuery = selectQuery;
    }

    @Override
    public boolean execute(Transaction transaction) throws Exception {
        boolean result = true;
        try {
            String databaseName = QueryExecutorUtil.getChosenDatabase();
            String tableName = selectQuery.getTableName();
            List<String> columnsToDisplay = selectQuery.getColumns();
            boolean displayAllColumns = "*".equals(columnsToDisplay.get(0).trim());

            ArrayList<HashMap<String, String>> tableMap = TableStructureHelper.getTableStructure(databaseName, tableName);
            boolean isHeaderRowDisplayed = false;

            for (HashMap<String, String> row : tableMap) {
                StringBuilder line = new StringBuilder();
                Set<String> columns = row.keySet();

                //create header row for the table if not done before
                if (!isHeaderRowDisplayed) {
                    for (String column : columns) {
                        column = column.trim();
                        if (displayAllColumns || columnsToDisplay.contains(column)) {
                            line.append(" ").append(column);
                        }
                    }
                    System.out.println(line);
                    line = new StringBuilder();
                    isHeaderRowDisplayed = true;
                }

                //display all the rows present in the table
                if (QueryExecutorUtil.checkCriteriaForRow(row, selectQuery.getCriteria())) {
                    for (String column : columns) {
                        String trimmedColumn = column.trim();
                        if (displayAllColumns || columnsToDisplay.contains(trimmedColumn)) {
                            line.append(" ").append(row.get(column));
                        }
                    }
                    System.out.println(line);
                }
            }
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

}
