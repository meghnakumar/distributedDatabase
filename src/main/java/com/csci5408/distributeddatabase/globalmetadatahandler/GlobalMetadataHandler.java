package com.csci5408.distributeddatabase.globalmetadatahandler;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GlobalMetadataHandler
{
    public boolean initGlobalMetaData()
    {
        boolean result = false;

        try
        {
            OutputStream output = new FileOutputStream("src/main/resources/GlobalMetadata.properties");
            Properties prop = new Properties();

            prop.setProperty("currentinstance", "localhost");
            prop.store(output, "Global Metadata File");

            System.out.println(prop);
            result=true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
