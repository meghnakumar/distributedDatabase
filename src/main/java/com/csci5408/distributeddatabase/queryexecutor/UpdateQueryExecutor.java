package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.query.UpdateQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateQueryExecutor implements IQueryExecutor{

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
    public String execute() throws Exception {

        StringBuilder result = new StringBuilder();
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        tableName = updateQuery.getTableName();

        DistributedHelper distributedHelper = new DistributedHelper();
        if(!distributedHelper.isDatabasePresentInLocalInstance(chosenDatabaseName))
        {
            result.append(distributedHelper.executeQueryInOtherInstance(this.updateQuery.getSql()));
            return result.toString();
        }
        column = updateQuery.getColumnName();
        updatedColumnValue = updateQuery.getUpdatedColumnValue();
        whereColumn = updateQuery.getCriteria().getLeftOperand();
        whereValue = updateQuery.getCriteria().getRightOperand();
        operator = updateQuery.getCriteria().getOperator();
        if(QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName,tableName)){
            tableData = TableStructureHelper.getTableStructure(chosenDatabaseName,tableName);
            for(HashMap<String,String> eachTableData: tableData){
                if( updateQuery.getCriteria()==null || (updateQuery.getCriteria()!=null && QueryExecutorUtil.checkCriteriaForRow(eachTableData, updateQuery.getCriteria()))){
                    eachTableData.put(column, updatedColumnValue);
                }
            }
            String path= System.getProperty("user.dir")+ File.separator+chosenDatabaseName+File.separator+tableName+".txt";
            FileUtil.writeTableHashMapToFile(tableData, path);
            result.append("update changes happened successfully in table");
        }
        else
        {
            result.append("Table does not exists in the instance");
        }
            return result.toString();
        }
     }

