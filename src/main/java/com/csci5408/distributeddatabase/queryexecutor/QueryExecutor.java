package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.fileoperations.FileUtil;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

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

    public String executeQuery() {
        try {
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            switch (query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    createDatabaseExecutor.execute();
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
                    insertTableQueryExecutor.execute();
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());
                        createTableExecutor.execute();
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        UpdateQueryExecutor updateQueryExecutor = new UpdateQueryExecutor(updateQuery);
                        updateQueryExecutor.execute();
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        DeleteQueryExecutor deleteQueryExecutor = new DeleteQueryExecutor(deleteQuery);
                        deleteQueryExecutor.execute();
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        SelectQuery selectQuery = (SelectQuery) query;
                        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(selectQuery);
                        selectQueryExecutor.execute();
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
                    useDatabaseQueryExecutor.execute();
                    break;
                default:
                    System.err.println("You have entered an invalid query");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void executeTransaction(String transactionQuery) throws Exception
    {
        queryParser = new QueryParser();
        // splitting the query on the basis of ';'
        List<String> queryList = Arrays.asList(transactionQuery.split("(?<=;)"));
        Transaction transaction = new Transaction();
        if(queryList.get(0).equalsIgnoreCase("start transaction") && queryList.get(queryList.size()-1).equalsIgnoreCase("commit")) {
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
             return new CreateDatabaseExecutor((CreatDatabaseQuery) query).execute();
        } else if (query.getQueryType() == QueryType.CREATE_TABLE) {
            return new CreateTableExecutor((CreateTableQuery) query, QueryExecutorUtil.getChosenDatabase()).execute();
        } else if (query.getQueryType() == QueryType.INSERT) {
            ITransactionExecutor transactionExecutor = new InsertTableQueryExecutor((InsertQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.SELECT) {
            return new SelectQueryExecutor((SelectQuery) query).execute();
        } else if (query.getQueryType() == QueryType.USE) {
            return new UseDatabaseQueryExecutor((UseDatabaseQuery) query).execute();
        } else if (query.getQueryType() == QueryType.UPDATE) {
            ITransactionExecutor transactionExecutor = new UpdateQueryExecutor((UpdateQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.DELETE) {
            ITransactionExecutor transactionExecutor = new DeleteQueryExecutor((DeleteQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        }
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
