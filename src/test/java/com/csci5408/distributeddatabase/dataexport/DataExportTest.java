package com.csci5408.distributeddatabase.dataexport;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DataExportTest {

    @Test()
    public void generateSQLDumpTest() throws Exception {
        List<String> databaseList = new ArrayList<>();
        databaseList.add("demo");
        DataExport dataExport = new DataExport();
        dataExport.exportSQLDump(databaseList);
    }
}
