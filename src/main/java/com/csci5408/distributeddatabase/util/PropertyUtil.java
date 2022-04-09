package com.csci5408.distributeddatabase.util;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;

import java.io.FileInputStream;
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
            output.close();
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
        System.err.println("Reading property file from location"+propPath);
        Properties prop = new Properties();
        try
        {
            InputStream input = new FileInputStream(propPath);
            prop.load(input);
            input.close();
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
            FileInputStream in = new FileInputStream(propPath);
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(propPath);
            if(!prop.containsKey(propKey))
            {
                prop.setProperty(propKey, propValue);
            }
            else if(prop.containsKey(propKey))
            {
                prop.replace(propKey, propValue);
            }
            prop.store(out, null);
            out.close();
        }
        catch(Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
