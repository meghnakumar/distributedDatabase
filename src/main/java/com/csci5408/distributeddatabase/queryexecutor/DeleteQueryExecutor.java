package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.query.DeleteQuery;
import com.csci5408.distributeddatabase.query.UpdateQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteQueryExecutor implements IQueryExecutor {
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
    }

    @Override
    public boolean execute() throws Exception {
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
//        String chosenDatabaseName = "admin";
        tableName = deleteQuery.getTableName();
        whereColumn = deleteQuery.getCriteria().getLeftOperand();
        whereValue = deleteQuery.getCriteria().getRightOperand();
        operator = deleteQuery.getCriteria().getOperator();
        if(QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName,tableName)) {
            tableData = TableStructureHelper.getTableStructure(chosenDatabaseName,tableName);
            for(HashMap<String,String> eachTableData: tableData){
                if(operator.equalsIgnoreCase("=") || operator.equalsIgnoreCase("!=")){
                    if (eachTableData.get(whereColumn).equalsIgnoreCase(whereValue)){
                        //do nothing
                    }
                    else{
                        updatedHashMap = eachTableData;
                        updatedTableData.add(updatedHashMap);
                    }
                }
                if(operator.equalsIgnoreCase("<=")){
                    if(Integer.parseInt(eachTableData.get(whereColumn))<=Integer.parseInt(whereValue)){
                        //do nothing
                    }
                    else {
                        updatedHashMap = eachTableData;
                        updatedTableData.add(updatedHashMap);
                    }
                }
                if(operator.equalsIgnoreCase(">=")){
                    if(Integer.parseInt(eachTableData.get(whereColumn))<=Integer.parseInt(whereValue)){
                        //do nothing
                    }
                    else {
                        updatedHashMap = eachTableData;
                        updatedTableData.add(updatedHashMap);
                    }
                }

            }
        }
        FileUtil.writeTableHashMapToFile(updatedTableData,System.getProperty("user.dir") + "\\" + chosenDatabaseName + "\\" + tableName + ".txt");
        return true;
    }
}
