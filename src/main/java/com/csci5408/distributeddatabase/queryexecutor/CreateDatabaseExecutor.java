package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataConstants;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;

public class CreateDatabaseExecutor
{
    private GlobalMetadataHandler globalMetadataHandler;

    private LocalMetaDataHandler localMetaDataHandler;

    public CreateDatabaseExecutor()
    {
        localMetaDataHandler = new LocalMetaDataHandler();
        globalMetadataHandler = new GlobalMetadataHandler();
    }

    public String execute()
    {
        //ToDo get the database name from the query.
        String responseData = "";
        String databaseName = "admin";

        try
        {
            if(QueryExecutorHelper.isDatabaseExists(databaseName))
            {
                responseData = "Database already exists";
                System.err.println(responseData);
            }
            else
            {
                FileUtil.makeDirectory(databaseName);
                globalMetadataHandler.initGlobalMetaData();
                globalMetadataHandler.writeToMetaData(databaseName, GlobalMetadataConstants.INSTANCE_CURRENT);
                localMetaDataHandler.makeMetadataForDatabase(databaseName);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return responseData;
    }
}
