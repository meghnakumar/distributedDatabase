package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.query.DeleteQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DeleteQueryExecutor implements IQueryExecutor, ITransactionExecutor {
    private String tableName;
    private String whereColumn;
    private String whereValue;
    private String operator;
    private DeleteQuery deleteQuery;
    private ArrayList<HashMap<String, String>> tableData;
    private ArrayList<HashMap<String, String>> updatedTableData = new ArrayList<>();
    private HashMap<String, String> updatedHashMap = new HashMap<>();


    public DeleteQueryExecutor(DeleteQuery deleteQuery) {
        this.deleteQuery = deleteQuery;
        whereColumn = deleteQuery.getCriteria().getLeftOperand();
        whereValue = deleteQuery.getCriteria().getRightOperand();
        operator = deleteQuery.getCriteria().getOperator();
        tableName = deleteQuery.getTableName();
    }

    @Override
    public String execute() throws Exception {
        StringBuilder result = new StringBuilder();
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();

        DistributedHelper distributedHelper = new DistributedHelper();
        if(!distributedHelper.isDatabasePresentInLocalInstance(chosenDatabaseName))
        {
            result.append(distributedHelper.executeQueryInOtherInstance(this.deleteQuery.getSql()));
            return result.toString();
        }
        if (QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName))
        {
            tableData = TableStructureHelper.getTableStructure(chosenDatabaseName, tableName);
            deleteQueryOnDataStructure();
            FileUtil.writeTableHashMapToFile(updatedTableData, System.getProperty("user.dir") + "\\" + chosenDatabaseName + "\\" + tableName + ".txt");

            String path= System.getProperty("user.dir")+ File.separator+chosenDatabaseName+File.separator+tableName+".txt";
            FileUtil.writeTableHashMapToFile(updatedTableData, path);
            result.append("changes updated in table and flushed to file successfully");

        }
        return result.toString();

    }

    private void deleteQueryOnDataStructure()
    {
        for (HashMap<String, String> eachTableData : tableData) {
            if (operator.equalsIgnoreCase("=") || operator.equalsIgnoreCase("!=")) {
                if (eachTableData.get(whereColumn).equalsIgnoreCase(whereValue)) {
                    //do nothing
                } else {
                    updatedHashMap = eachTableData;
                    updatedTableData.add(updatedHashMap);
                }
            }
            if (operator.equalsIgnoreCase("<=")) {
                if (Integer.parseInt(eachTableData.get(whereColumn)) <= Integer.parseInt(whereValue)) {
                    //do nothing
                } else {
                    updatedHashMap = eachTableData;
                    updatedTableData.add(updatedHashMap);
                }
            }
            if (operator.equalsIgnoreCase(">=")) {
                if (Integer.parseInt(eachTableData.get(whereColumn)) <= Integer.parseInt(whereValue)) {
                    //do nothing
                } else {
                    updatedHashMap = eachTableData;
                    updatedTableData.add(updatedHashMap);
                }
            }
        }
    }

    @Override
    public boolean executeTransaction(Transaction transaction) {
       try
       {
           String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
           if (QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName))
           {
               tableData = transaction.getTransactionalTableData().get(deleteQuery.getTableName());
               deleteQueryOnDataStructure();
               transaction.getTransactionalTableData().put(deleteQuery.getTableName(), updatedTableData);
               return true;
           }
           else
           {
               return false;
           }
       }
       catch (Exception ex)
       {
            ex.printStackTrace();
       }
       return false;
    }
}
