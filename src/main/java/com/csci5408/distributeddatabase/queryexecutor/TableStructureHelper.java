package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.io.*;
import java.util.*;

public class TableStructureHelper {

    public static ArrayList getTableStructure(String databaseName, String tableName) throws IOException {
        String line = "";
        ArrayList<HashMap> tableData = new ArrayList<>();
        int counter = 1;
        File file = new File(System.getProperty("user.dir") + "\\" + databaseName + "\\" + tableName + ".txt");
        List<String> columnsNames = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line = bufferedReader.readLine()) != null) {
            String[] rowContent = line.split("(\\*\\|){2}");
            HashMap<String, String> tableRows = new HashMap<>();
            if (counter == 1) {
                int columns = rowContent.length;
                int i = 0;
                while (i < columns) {
                    columnsNames.add(rowContent[i].trim());
                    i++;
                }
            } else {
                int i = 0;
                while (i < columnsNames.size()) {
                    tableRows.put(columnsNames.get(i), rowContent[i]);
                    i++;
                }
                tableData.add(tableRows);
            }
            counter++;
        }

        return tableData;
    }
}
