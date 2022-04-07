package reverseenginerring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ReverseEngineering {

    public void reverseEngineering(String database_name) throws Exception {

        List<String> tablesList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input.csv"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Entity_Relationship.txt"));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(line.startsWith(database_name)) {
                tablesList.add(line);
            }
        }

        if(!tablesList.isEmpty()) {
            for(String tables: tablesList) {
                String[] tableContents = tables.split(",");

                int tableContentsLength = tableContents.length;
                int primaryKeyLocation = tableContentsLength-4;

                String singleTable = tableContents[1] + " (";
                for(int i=2; i < primaryKeyLocation; i+=2) {
                    singleTable += tableContents[i] + "(" + tableContents[i+1] + "))";
                    if((i+1) != primaryKeyLocation) {
                        singleTable += ",";
                    } else {
                        singleTable += ")";
                    }
                }

                singleTable += getRelationString(tableContents[primaryKeyLocation], tableContents[tableContentsLength-3],
                        tableContents[tableContentsLength-1], tableContents[tableContentsLength-2]);

                bufferedWriter.write(singleTable);
                bufferedWriter.newLine();
            }
        } else {
            throw new Exception("Database doesn't exist");
        }
        bufferedReader.close();
        bufferedWriter.close();
    }

    private String getRelationString(String primaryKey, String foreignKey, String foreignKeyTableName, String primaryKeyOfForeignKeyTable) {
        String relationString = "";
        String cardinality = " * ----------------> 1 ";
        String nullString = "null";

        if(primaryKey != null) {
            relationString += "(Primary_Key: " + primaryKey;
        }

        if(!(foreignKey.equals(nullString) && foreignKeyTableName.equals(nullString) && primaryKeyOfForeignKeyTable.equals(nullString))) {
            relationString += ", Foreign_Key: " + foreignKey + ")" + cardinality + foreignKeyTableName + "(" + primaryKeyOfForeignKeyTable + ")";
        } else {
            relationString += ")";
        }

        return relationString;
    }
}
