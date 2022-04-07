package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import org.junit.Test;

public class QueryExecutorTest
{
    @Test
    public void createDatabaseQueryExecutorTest() throws Exception
    {
        //create Database
        String sql = "create database demo;";
        QueryParser parser = new QueryParser();

        CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery)parser.parse(sql);
        CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
        createDatabaseExecutor.execute();
    }

    @Test
    public void useDatabaseQueryExecutorTest() throws Exception
    {
        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
    }

    @Test
    public void createTableQueryExecutorTest() throws Exception
    {
        //create table
        String sql = "create table persons(id int, name varchar(255), primary key (id), foreign key (id) references orders(id);";
        QueryParser parser = new QueryParser();

        CreateTableQuery createTableQuery= (CreateTableQuery) parser.parse(sql);
        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, "demo");
        createTableExecutor.execute();
    }

    @Test
    public void insertTableQueryExecutorTest() throws Exception
    {
        useDatabaseQueryExecutorTest();

        //insert table test
        String sql = "insert into persons(name, lastname) values (janvi, patel);";
        QueryParser parser = new QueryParser();

        InsertQuery insertQuery = (InsertQuery) parser.parse(sql);
        InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
        insertTableQueryExecutor.execute();
    }

    @Test
    public void checkAllQueriesTest() throws Exception
    {
        //create Database
        String sql = "create database demo ;";
        QueryParser parser = new QueryParser();

        CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery)parser.parse(sql);
        CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
        createDatabaseExecutor.execute();

        //useDatabase
        sql = "use demo;";
        parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();

        //create table
        sql = "create table persons(id int, name varchar(255), primary key (id), foreign key (id) references orders(id);";
        parser = new QueryParser();

        CreateTableQuery createTableQuery= (CreateTableQuery) parser.parse(sql);
        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, "demo");
        createTableExecutor.execute();

        //insert table test
        sql = "insert into persons(name, lastname) values (janvi, patel);";
        parser = new QueryParser();

        InsertQuery insertQuery = (InsertQuery) parser.parse(sql);
        InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
        insertTableQueryExecutor.execute();

        sql = "insert into persons(name, lastname) values (shathish, annamalai);";
        parser = new QueryParser();

        insertQuery = (InsertQuery) parser.parse(sql);
        insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
        insertTableQueryExecutor.execute();
    }

}
