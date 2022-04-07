package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.queryexecutor.QueryExecutorHelper;
import com.example.demo.query.CreateTableQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

public class CreateTableExecutor {

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
        if(!QueryExecutorHelper.isTableExistsInDatabase(databaseName,tableName)){
            localMetaDataHandler.makeMetadataForTable(databaseName,tableName);
            BufferedWriter writeFile = new BufferedWriter(new FileWriter(file));
            Set<String> columnNames = columns.keySet();
            for(String columnName: columnNames){
                String column= columnName;
                if(columnName.equalsIgnoreCase(primaryKey)){
                    column = column+"(PK)";
                }
                if(columnName.equalsIgnoreCase(foreignKey)){
                    column = column + "(FK)";
                }
                column = column + "("+ columns.get(columnName) + ")";
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
