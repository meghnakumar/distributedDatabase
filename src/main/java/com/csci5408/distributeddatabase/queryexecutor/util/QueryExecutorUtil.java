package com.csci5408.distributeddatabase.queryexecutor.util;

import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;

import java.io.File;
import java.util.Properties;

public class QueryExecutorUtil
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

    public static boolean isDatabaseChosen()
    {
        Properties prop = System.getProperties();
        if(prop.getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE)==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static String getChosenDatabase() throws Exception
    {
        String currentDBName = new String();
        if(!isDatabaseChosen())
        {
            System.err.println(QueryConstants.NO_DATABASE_CHOSEN_ERROR);
            throw new Exception(QueryConstants.NO_DATABASE_CHOSEN_ERROR);
        }
        else
        {
            currentDBName=System.getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE);
        }
        return currentDBName;
    }
}
