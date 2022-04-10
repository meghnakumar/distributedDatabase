package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.CreateTableQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

public class CreateTableExecutor implements IQueryExecutor{

    private static final String SEPERATOR = "*|*|";
    private String databaseName;
    private CreateTableQuery createTableQuery;
    private LocalMetaDataHandler localMetaDataHandler;

    public CreateTableExecutor(CreateTableQuery createTableQuery, String databaseName)
    {
        this.databaseName=databaseName;
        this.createTableQuery = createTableQuery;
        localMetaDataHandler = new LocalMetaDataHandler();
    }

    @Override
    public boolean execute() throws IOException {
        String tableName = createTableQuery.getTableName();
        String primaryKey = createTableQuery.getPrimaryKey();
        String referenceTable="";
        String referenceTableField = "";
        String foreignKey="";
        if(null != createTableQuery.getForeignKey()){
             foreignKey = createTableQuery.getForeignKey();
        }
        if(null!=createTableQuery.getReferenceTable() && null!=createTableQuery.getReferenceTableField()){
             referenceTable = createTableQuery.getReferenceTable();
             referenceTableField = createTableQuery.getReferenceTableField();
        }
        LinkedHashMap<String,String> columns = createTableQuery.getFieldMap();
        String path= System.getProperty("user.dir")+"\\";
        File file = new File(path+"\\"+databaseName+"\\"+tableName+".txt");
        if(!QueryExecutorUtil.isTableExistsInDatabase(databaseName,tableName)){
            localMetaDataHandler.makeMetadataForTable(databaseName,tableName);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"primaryKey",primaryKey);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"foreignKey",foreignKey);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"referenceTable",referenceTable);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"referenceTableField",referenceTableField);
            BufferedWriter writeFile = new BufferedWriter(new FileWriter(file));
            Set<String> columnNames = columns.keySet();
            for(String columnName: columnNames){
                String column= columnName;
                localMetaDataHandler.addMetadataForTable(databaseName,tableName,column,columns.get(column));
                if(columnName.equalsIgnoreCase(primaryKey)){
                    //column = column+"(PK)";
                }
                if(columnName.equalsIgnoreCase(foreignKey)){
                    //column = column + "(FK)";
                }
                //column = column + "("+ columns.get(columnName) + ")";
                writeFile.write(column+" "+SEPERATOR);
            }
            writeFile.newLine();
            writeFile.close();

            return true;

        }
        else{
            System.out.println("File already exists");
        }
        return false;
   }
}
