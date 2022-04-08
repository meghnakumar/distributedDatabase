package com.csci5408.distributeddatabase.globalmetadatahandler;

import com.csci5408.distributeddatabase.fileoperations.PropertyUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
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
            Properties prop = getGlobalMetadataProperties();

            if(prop.getProperty("currentinstance")==null)
            {
                prop.setProperty("currentinstance", GlobalMetadataConstants.INSTANCE_CURRENT);
                prop.store(output, "Global Metadata File");
            }

            result=true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean writeToMetaData(String propName, String propValue)
    {
        boolean result = false;
        try
        {
            OutputStream output = new FileOutputStream("src/main/resources/GlobalMetadata.properties", true);
            Properties  prop = getGlobalMetadataProperties();

            if(prop.getProperty(propName)==null)
            {
                prop.setProperty(propName, propValue);
            }
            else
            {
                prop.replace(propName, propValue);
            }

            prop.store(output, null);
            result=true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public Properties getGlobalMetadataProperties()
    {
        return PropertyUtil.getPropFromPropFile(GlobalMetadataConstants.GLOBAL_METADATA_FILE);
    }
}
