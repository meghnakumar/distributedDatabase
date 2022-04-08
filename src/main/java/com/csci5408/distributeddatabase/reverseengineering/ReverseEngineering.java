package com.csci5408.distributeddatabase.reverseengineering;

import java.io.*;
import java.util.*;

public class ReverseEngineering {

    public void reverseEngineering(String databaseName) throws Exception {

        String directoryPath = "LOCALMETADATA/" + databaseName;
        File directory = new File(directoryPath);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("ER/Entity_Relationship.txt"));
        if(directory.exists()) {
            for(File file: directory.listFiles()) {
                Properties fileProperties = new Properties(); //PropertyUtil.getPropFromPropFile(file.getPath());
                fileProperties.load(new FileInputStream(file));
                Set tableSet = fileProperties.entrySet();

                String primaryKey = "";
                String foreignKey = "";
                String referenceTable = "";
                String referenceTableField = "";
                String tableColumn = "";

                String singleTableEntityRelation = file.getName().replace(".properties","") + "(";

                Iterator iterator = tableSet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry tableMap = (Map.Entry) iterator.next();
                    String key = (String) tableMap.getKey();
                    String value = (String) tableMap.getValue();

                    if (key.equals("primaryKey")) {
                        primaryKey = value;
                    } else if (key.equals("foreignKey")) {
                        foreignKey = value;
                    } else if (key.equals("referenceTable")) {
                        referenceTable = value;
                    } else if (key.equals("referenceTableField")) {
                        referenceTableField = value;
                    } else {
                        tableColumn += key + "(" + value + "),";
                    }
                }

                singleTableEntityRelation += tableColumn + ")" + getRelationString(primaryKey, foreignKey,
                        referenceTable, referenceTableField);

                bufferedWriter.write(singleTableEntityRelation);
                bufferedWriter.newLine();
            }
        } else {
            throw new Exception("Database doesn't exist");
        }

        bufferedWriter.close();
    }

    private String getRelationString(String primaryKey, String foreignKey, String foreignKeyTableName, String primaryKeyOfForeignKeyTable) {
        String relationString = "";
        String cardinality = " * ----------------> 1 ";

        if(primaryKey != null) {
            relationString += "(Primary_Key: " + primaryKey;
        }

        if(!(foreignKey.equals("") && foreignKeyTableName.equals("") && primaryKeyOfForeignKeyTable.equals(""))) {
            relationString += ", Foreign_Key: " + foreignKey + ")" + cardinality + foreignKeyTableName + "(" + primaryKeyOfForeignKeyTable + ")";
        } else {
            relationString += ")";
        }

        return relationString;
    }
}
