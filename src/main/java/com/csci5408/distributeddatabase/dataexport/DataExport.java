package com.csci5408.distributeddatabase.dataexport;

import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.util.ReadMetaDataUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class DataExport {

    public String exportSQLDump(String database) throws Exception {

        String star = "*****************************";
        boolean directoryExists = FileUtil.createDirectory("sqldump");
        String outputPath = "sqldump/" + database + ".sql";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath));

        String directoryPath = "LOCALMETADATA/" + database;
        File directory = new File(directoryPath);

        if(directory.exists()) {
            bufferedWriter.write("CREATE DATABASE IF NOT EXISTS " + database + ";");
            bufferedWriter.newLine();

            bufferedWriter.write("USE " + database + ";");
            bufferedWriter.newLine();

            bufferedWriter.newLine();
            bufferedWriter.write(star);
            bufferedWriter.newLine();

            String dropTableQuery = "";
            String createTableQuery = "";

            for(File file: directory.listFiles()) {

                String tableName = file.getName().replace(".properties", "");
                dropTableQuery = "DROP TABLE IF EXISTS " + tableName + ";";
                createTableQuery = "CREATE TABLE " + tableName + "(";

                Map<String, String> tableContent = ReadMetaDataUtil.getMetadata(file);

                if(!tableContent.isEmpty()) {
                    String primaryKey = tableContent.get("primaryKey");
                    String foreignKey = tableContent.get("foreignKey");
                    String referenceTable = tableContent.get("referenceTable");
                    String referenceTableField = tableContent.get("referenceTableField");
                    String tableColumn = tableContent.get("tableColumn");

                    if(tableColumn != null) {
                        createTableQuery += tableColumn;
                    }

                    if(primaryKey != null) {
                        createTableQuery += "primary key (" + primaryKey + ") ";
                    }

                    if(foreignKey != null && referenceTable != null && referenceTableField != null) {
                        createTableQuery += ", foreign key (" +  foreignKey + ") references " +
                                referenceTable + "(" + referenceTableField + "));";
                        System.out.println();
                    } else {
                        createTableQuery += ");";
                    }

                    bufferedWriter.write(dropTableQuery);
                    bufferedWriter.newLine();

                    bufferedWriter.write(createTableQuery);
                    bufferedWriter.newLine();
                }

                File tableData = new File(database+"/"+tableName+".txt");
                if(tableData.exists()) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(tableData));
                    String line = "";

                    boolean firstLine = true;
                    String insertQuery = "INSERT INTO " + tableName + "(";
                    String data = "";
                    while((line = bufferedReader.readLine()) != null) {

                        if(!firstLine) {
                            data += "(" + line.replace("*|*|", ", ") + "),";
                        } else {
                            String columns = line.replace("*|*|", ", ");
                            int columnLength = columns.length();
                            insertQuery += (columns.endsWith(", ") ? columns.substring(0, columnLength - 2) : columns) + ") VALUES ";
                            firstLine = false;
                        }
                    }
                    int dataLength = data.length();
                    insertQuery += (data.endsWith(",") ? data.substring(0, dataLength-1) : data) + ";";

                    bufferedWriter.newLine();
                    bufferedWriter.write(star);
                    bufferedWriter.newLine();

                    bufferedWriter.write(insertQuery);
                    bufferedWriter.newLine();

                    bufferedWriter.newLine();
                    bufferedWriter.write(star);
                    bufferedWriter.newLine();
                }
            }
        }

        bufferedWriter.close();
        return FileUtil.readFileData(Path.of(outputPath));
    }
}
