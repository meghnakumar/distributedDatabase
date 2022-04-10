package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import com.csci5408.distributeddatabase.util.FileUtil;
import user.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class QueryExecutor {
    // query parser
    private QueryParser queryParser;

    private String sqlQuery;

    public QueryExecutor() {
    }

    // constructor
    public QueryExecutor(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String executeQuery()
    {
        try
        {
            IQueryExecutor queryExecutor = null;
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            parser.validateQuery(query,null);
            switch (query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    queryExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    queryExecutor = new InsertTableQueryExecutor(insertQuery);
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        queryExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        queryExecutor = new UpdateQueryExecutor(updateQuery);
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        queryExecutor = new DeleteQueryExecutor(deleteQuery);
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        SelectQuery selectQuery = (SelectQuery) query;
                        queryExecutor = new SelectQueryExecutor(selectQuery);
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    queryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
                    break;
                default:
                    Logger.eventLogger(query+" "+"QUERY FAILED");
                    System.err.println("You have entered an invalid query");
            }
            return queryExecutor.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "returned after switch";
    }

    public void executeTransaction(String transactionQuery) throws Exception
    {
        queryParser = new QueryParser();
        // splitting the query on the basis of ';'
        List<String> queryList = Arrays.asList(transactionQuery.split("(?<=;)"));
        Transaction transaction = new Transaction();
        if(queryList.get(0).equalsIgnoreCase("start transaction;") && queryList.get(queryList.size()-1).trim().equalsIgnoreCase("commit;")) {
            // iterating over the query list
            for (String query : queryList) {
                // trimming the query
                query = query.trim();
                // if it is not "start" and "commit" transaction query then add it into a queue
                if (!query.equalsIgnoreCase("start transaction;") && !query.equalsIgnoreCase("commit;")) {
                    transaction.addQuery(queryParser.parse(query));
                }
            }
        } else {
            Logger.eventLogger("::::::::::::::::TRANSACTION FAILED::::::::::::::::::::");
            throw new IllegalAccessException("Entered query is not a transaction");
        }

        //ToDo handle transaction queries here
        Queue<Query> queries = transaction.getQueries();
        IQueryExecutor executor = null;
        transaction.setDatabaseName(QueryExecutorUtil.getChosenDatabase());
        initializeTableInTransaction(transaction);
        for (Query query : queries) {
            queryParser.validateQuery(query, transaction);
        }

        boolean isQuerySuccessfullyExecuted=false;
        for (Query query : queries)
        {
            isQuerySuccessfullyExecuted = executeQueryForTransactions(query, transaction);
            if(!isQuerySuccessfullyExecuted) {
                break;
            }
        }
        if(isQuerySuccessfullyExecuted) {
            for (Map.Entry<String, ArrayList> tableData : transaction.getTransactionalTableData().entrySet()) {
                FileUtil.writeTableHashMapToFile(tableData.getValue(), System.getProperty("user.dir") + "\\" + transaction.getDatabaseName() + "\\" + tableData.getKey() + ".txt");
            }
        }
    }

    private boolean executeQueryForTransactions(Query query, Transaction transaction) throws Exception {
        if (query.getQueryType() == QueryType.CREATE_DATABASE) {
              new CreateDatabaseExecutor((CreatDatabaseQuery) query).execute();
              return true;
        } else if (query.getQueryType() == QueryType.CREATE_TABLE) {
            new CreateTableExecutor((CreateTableQuery) query, QueryExecutorUtil.getChosenDatabase()).execute();
            return true;
        } else if (query.getQueryType() == QueryType.INSERT) {
            ITransactionExecutor transactionExecutor = new InsertTableQueryExecutor((InsertQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.SELECT) {
            new SelectQueryExecutor((SelectQuery) query).execute();
            return true;
        } else if (query.getQueryType() == QueryType.USE) {
            new UseDatabaseQueryExecutor((UseDatabaseQuery) query).execute();
            return true;
        } else if (query.getQueryType() == QueryType.UPDATE) {
            ITransactionExecutor transactionExecutor = new UpdateQueryExecutor((UpdateQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.DELETE) {
            ITransactionExecutor transactionExecutor = new DeleteQueryExecutor((DeleteQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        }
        Logger.eventLogger("::::::::::::::::TRANSACTION FAILED::::::::::::::::::::");
        throw new IllegalArgumentException("Oops query executor not found!!");
    }

    private void initializeTableInTransaction(Transaction transaction) throws IOException {
        Map<String, Properties> prop = new HashMap<>();
        Map<String, ArrayList> tableData = new HashMap<>();
        File file = new File(LocalMetaDataHandler.getDatabaseMetadataFolderPath(transaction.getDatabaseName()));
        File[] fileList = file.listFiles();
        String tableName;

        for (File list : fileList) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(list));
            tableName = list.getName().replace("properties", "").replace(".", "");
            prop.put(tableName, properties);
            ArrayList tableStructure = TableStructureHelper.getTableStructure(transaction.getDatabaseName(), tableName);
            tableData.put(tableName, tableStructure);
        }
        transaction.setTransactionalTableProp(prop);
        transaction.setTransactionalTableData(tableData);
    }
}
