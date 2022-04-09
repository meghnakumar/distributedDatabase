package com.csci5408.distributeddatabase.dataexport;

import org.junit.Test;


public class DataExportTest {

    @Test()
    public void generateSQLDumpTest() throws Exception {
        DataExport dataExport = new DataExport();
        dataExport.exportSQLDump("demo");
    }
}
