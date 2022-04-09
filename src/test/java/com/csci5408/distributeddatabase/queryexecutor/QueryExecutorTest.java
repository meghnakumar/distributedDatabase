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
        createDatabaseExecutor.execute(null);
    }

    @Test
    public void useDatabaseQueryExecutorTest() throws Exception
    {
        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
    }

    @Test
    public void createTableQueryExecutorTest() throws Exception
    {
        //create table
        String sql = "create table persons(id int, name varchar(255), primary key (id), foreign key (id) references orders(id);";
        QueryParser parser = new QueryParser();

        CreateTableQuery createTableQuery= (CreateTableQuery) parser.parse(sql);
        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, "demo");
        createTableExecutor.execute(null);
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
        insertTableQueryExecutor.execute(null);
    }

    @Test
    public void selectTableWithCriteriaTest() throws Exception
    {
        useDatabaseQueryExecutorTest();

        String sql = "select id, name from persons where name=janvi;";
        QueryParser parser = new QueryParser();

        SelectQuery selectQuery = (SelectQuery) parser.parse(sql);
        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(selectQuery);
        selectQueryExecutor.execute(null);
    }


    @Test
    public void checkAllQueriesTest() throws Exception
    {
        //create Database
        String sql = "create database demo ;";
        QueryParser parser = new QueryParser();

        CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery)parser.parse(sql);
        CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
        createDatabaseExecutor.execute(null);

        //useDatabase
        sql = "use demo;";
        parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);

        //create table
        sql = "create table persons(id int, name varchar(255), lastname varchar(255), primary key (id), foreign key (id) references orders(id);";
        parser = new QueryParser();

        CreateTableQuery createTableQuery= (CreateTableQuery) parser.parse(sql);
        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, "demo");
        createTableExecutor.execute(null);

        //insert table test
        sql = "insert into persons(id, name, lastname) values (1, janvi, patel);";
        parser = new QueryParser();

        InsertQuery insertQuery = (InsertQuery) parser.parse(sql);
        InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
        insertTableQueryExecutor.execute(null);

        sql = "insert into persons(id, name, lastname) values (2, shathish, annamalai);";
        parser = new QueryParser();

        insertQuery = (InsertQuery) parser.parse(sql);
        insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
        insertTableQueryExecutor.execute(null);

        //select table test
        sql = "select * from persons where name=janvi;";
        parser = new QueryParser();

        SelectQuery selectQuery = (SelectQuery) parser.parse(sql);
        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(selectQuery);
        selectQueryExecutor.execute(null);
    }

    @Test
    public void getTransactionFailureTest() throws Exception {
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("use employee; select * from employee; create table customer(id number, name varchar);");
    }

    @Test
    public void getTransactionSuccessTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("start transaction; select * from employee; create table customer(id number, name varchar); commit;");
    }

    @Test
    public void getCreateTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("start transaction; create table customer(id number, name varchar); create table customer(id number, name varchar); commit;");
    }

    @Test
    public void updateQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("start transaction; update persons set name=kinna where name=nihar; commit;");
    }

    @Test
    public void insertQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("start transaction; insert into persons(id, name, lastname) values (10, ms, dhoni); select * from persons where name=kinna; commit;");

    }

    @Test
    public void deleteQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute(null);
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.execute("start transaction;  insert into persons(id, name, lastname) values (8, vipul, patel); delete from persons where name=hetal; commit;");

    }
}
