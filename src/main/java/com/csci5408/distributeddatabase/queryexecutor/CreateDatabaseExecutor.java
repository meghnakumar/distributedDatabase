package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataConstants;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

public class CreateDatabaseExecutor implements IQueryExecutor
{
    private String databaseName;

    private GlobalMetadataHandler globalMetadataHandler;

    private LocalMetaDataHandler localMetaDataHandler;

    private CreatDatabaseQuery creatDatabaseQuery;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public CreatDatabaseQuery getCreatDatabaseQuery() {
        return creatDatabaseQuery;
    }

    public void setCreatDatabaseQuery(CreatDatabaseQuery creatDatabaseQuery) {
        this.creatDatabaseQuery = creatDatabaseQuery;
    }

    public LocalMetaDataHandler getLocalMetaDataHandler() {
        return localMetaDataHandler;
    }

    public void setLocalMetaDataHandler(LocalMetaDataHandler localMetaDataHandler) {
        this.localMetaDataHandler = localMetaDataHandler;
    }

    public GlobalMetadataHandler getGlobalMetadataHandler() {
        return globalMetadataHandler;
    }

    public void setGlobalMetadataHandler(GlobalMetadataHandler globalMetadataHandler) {
        this.globalMetadataHandler = globalMetadataHandler;
    }

    public CreateDatabaseExecutor(String databaseName)
    {
        this.databaseName=databaseName;
        localMetaDataHandler = new LocalMetaDataHandler();
        globalMetadataHandler = new GlobalMetadataHandler();
    }

    public CreateDatabaseExecutor(CreatDatabaseQuery creatDatabaseQuery)
    {
        this.creatDatabaseQuery=creatDatabaseQuery;
        this.databaseName=creatDatabaseQuery.getDatabaseName();
        localMetaDataHandler = new LocalMetaDataHandler();
        globalMetadataHandler = new GlobalMetadataHandler();
    }

    @Override
    public boolean execute()
    {
        boolean result=true;
        try
        {
            if(QueryExecutorUtil.isDatabaseExists(databaseName))
            {
                result=false;
                System.err.println("Database already exists");
            }
            else
            {
                //make directory for new database
                FileUtil.makeDirectory(databaseName);

                //update local global data prop
                System.err.println("writing new database to local global metadata");
                globalMetadataHandler.writeToMetaData(databaseName, GlobalMetadataConstants.INSTANCE_CURRENT);

                //update other instance global metadata property
                System.err.println("writing new database to other instance global metadata");
                DistributedHelper distributedHelper = new DistributedHelper();
                distributedHelper.updateGlobalMetadataPropInOtherInstance(databaseName, GlobalMetadataConstants.INSTANCE_OTHER);

                //create local metadata for the database
                localMetaDataHandler.makeMetadataForDatabase(databaseName);
            }
        }
        catch(Exception ex)
        {
            result=false;
            ex.printStackTrace();
        }
        return result;
    }
}
