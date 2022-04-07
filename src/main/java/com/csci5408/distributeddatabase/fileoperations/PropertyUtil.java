package com.csci5408.distributeddatabase.fileoperations;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtil
{
    public static boolean createPropFile(String path)
    {
        System.err.println("creating a new property file "+path);
        boolean result = true;
        try
        {
            Properties prop = new Properties();
            OutputStream output = new FileOutputStream(path);
            prop.store(output, "Metadata File");
        }
        catch(Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static Properties getPropFromPropFile(String propPath)
    {
        System.err.println("creating a new property file "+propPath);
        Properties prop = new Properties();
        try
        {
            InputStream input = GlobalMetadataHandler.class.getClassLoader().getResourceAsStream(propPath);
            prop.load(input);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return prop;
    }

    public static boolean writeToPropFile(String propPath, String propKey, String propValue)
    {
        System.err.println("writing to an existing prop file "+propPath);
        boolean result = true;
        try
        {
            OutputStream output = new FileOutputStream(propPath, true);
            Properties  prop = getPropFromPropFile(propPath);

            if(prop.getProperty(propKey)==null)
            {
                prop.setProperty(propKey, propValue);
            }
            else
            {
                prop.replace(propKey, propValue);
            }
            prop.store(output, null);

        }
        catch(Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
