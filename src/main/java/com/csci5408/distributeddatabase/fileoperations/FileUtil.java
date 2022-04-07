package com.csci5408.distributeddatabase.fileoperations;

import java.io.File;

public class FileUtil
{
    public static boolean isFileExists(String path)
    {
        System.err.println("checking if file exists "+path);
        return new File(path).exists();
    }

    public static boolean isDirectoryExists(String path)
    {
        System.err.println("checking if directory exists "+path);
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }

    public static boolean makeDirectory(String path)
    {
        System.err.println("Making a new Directory"+path);
        boolean result = true;
        try
        {
            File directory = new File(path);
            if (! directory.exists())
            {
                directory.mkdir();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result=false;
        }
        return result;
    }
}
