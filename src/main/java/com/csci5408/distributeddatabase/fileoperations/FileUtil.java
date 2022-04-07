package com.csci5408.distributeddatabase.fileoperations;

import java.io.*;

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

    public static boolean writeToExistingFile(String path, String line) throws Exception
    {
        if(!FileUtil.isFileExists(path))
        {
            System.err.println(path+ "No such file exists");
            throw new FileNotFoundException();
        }

        FileWriter fileWriter = new FileWriter(path, true);

        fileWriter.append(line);
        fileWriter.append('\n');

        fileWriter.flush();
        fileWriter.close();

        return true;
    }
}
