package com.csci5408.distributeddatabase.dataexport;

import com.csci5408.distributeddatabase.util.ReadMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DataExport {

    public void exportSQLDump(List<String> databases) throws Exception {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sqldump/export.sql"));
        for(String database: databases) {
            String directoryPath = "LOCALMETADATA/" + database;
            File directory = new File(directoryPath);

            bufferedWriter.write("CREATE DATABASE IF NOT EXISTS " + database + ";");
            bufferedWriter.newLine();

            bufferedWriter.write("USE " + database + ";");
            bufferedWriter.newLine();

            String dropTableQuery = "DROP TABLE IF EXISTS ";
            String createTableQuery = "";

            if(directory.exists()) {
                for(File file: directory.listFiles()) {

                    String tableName = file.getName().replace(".properties", "");
                    dropTableQuery += tableName + ";";
                    createTableQuery = "CREATE table " + tableName + "(";

                    Map<String, String> tableContent = ReadMetadata.getMetadata(file);

                    if(!tableContent.isEmpty()) {
                        String primaryKey = tableContent.get("primaryKey");
                        String foreignKey = tableContent.get("foreignKey");
                        String referenceTable = tableContent.get("referenceTable");
                        String referenceTableField = tableContent.get("referenceTableField");
                        String tableColumn = tableContent.get("referenceTableField");

                        if(tableColumn != null) {
                            createTableQuery += tableColumn;
                        }

                        if(primaryKey != null) {
                            createTableQuery += " primary key (" + primaryKey + "), ";
                        }

                        if(foreignKey != null && referenceTable != null && referenceTableField != null) {
                            createTableQuery += "foreign key (" +  foreignKey + ") references " +
                                    referenceTable + "(" + referenceTableField + "));";
                        }

                        bufferedWriter.write(dropTableQuery);
                        bufferedWriter.newLine();

                        bufferedWriter.write(createTableQuery);
                        bufferedWriter.newLine();
                    }
                }
            }

            File databasePath = new File(database);

            if(databasePath.exists()) {
                for(File file: databasePath.listFiles()) {

                }
            }

        }

        bufferedWriter.close();
    }
}
