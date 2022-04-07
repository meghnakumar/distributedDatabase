package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.fileoperations.FileUtil;

import java.io.File;

public class QueryExecutorHelper
{
    public static boolean isDatabaseExists(String databaseName)
    {
        return FileUtil.isDirectoryExists(databaseName);
    }

    public static boolean isTableExistsInDatabase(String databaseName, String tableName)
    {
        String filePath = databaseName+ File.pathSeparator+tableName+".txt";
        return FileUtil.isFileExists(filePath);
    }
}
