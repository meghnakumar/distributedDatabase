package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.query.UpdateQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UpdateQueryExecutor implements IQueryExecutor {

    private String tableName;
    private String column;
    private String updatedColumnValue;
    private String whereColumn;
    private String whereValue;
    private String operator;
    private UpdateQuery updateQuery;
    private ArrayList<HashMap<String, String>> tableData;

    public UpdateQueryExecutor(UpdateQuery updateQuery) {
        this.updateQuery = updateQuery;
    }

    @Override
    public boolean execute(Transaction transaction) throws Exception {
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        tableName = updateQuery.getTableName();
        column = updateQuery.getColumnName();
        updatedColumnValue = updateQuery.getUpdatedColumnValue();
        whereColumn = updateQuery.getCriteria().getLeftOperand();
        whereValue = updateQuery.getCriteria().getRightOperand();
        operator = updateQuery.getCriteria().getOperator();
        if (transaction != null || QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName)) {
            if (transaction == null) {
                tableData = TableStructureHelper.getTableStructure(chosenDatabaseName, tableName);
            } else {
                tableData = transaction.getTransactionalTableData().get(updateQuery.getTableName());
            }
            for (HashMap<String, String> eachTableData : tableData) {
                if (operator.equalsIgnoreCase("=")) {
                    if (eachTableData.keySet().contains(whereColumn)) {
                        if (eachTableData.get(whereColumn).equalsIgnoreCase(whereValue)) {
                            eachTableData.put(column, updatedColumnValue);
                        }
                    }
                }
                if (operator.equalsIgnoreCase("<=")) {
                    if (Integer.parseInt(eachTableData.get(whereColumn)) <= Integer.parseInt(whereValue)) {
                        eachTableData.put(column, updatedColumnValue);
                    }
                }
                if (operator.equalsIgnoreCase(">=")) {
                    if (Integer.parseInt(eachTableData.get(whereColumn)) >= Integer.parseInt(whereValue)) {
                        eachTableData.put(column, updatedColumnValue);
                    }
                }
            }
        }
        if (transaction == null) {
            FileUtil.writeTableHashMapToFile(tableData, System.getProperty("user.dir") + "\\" + chosenDatabaseName + "\\" + tableName + ".txt");
        }
        return true;
    }
}

