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

    // getting the POJO for transaction and normal query
    public void execute(String queries) throws Exception {
        queryParser = new QueryParser();
        // splitting the query on the basis of ';'
        List<String> queryList = Arrays.asList(queries.split("(?<=;)"));
        Transaction transaction = new Transaction();
        // iterating over the query list
        for (String query : queryList) {
            // trimming the query
            query = query.trim();
            // if it is not "start" and "commit" transaction query then add it into a queue
            if (!query.equalsIgnoreCase("start transaction;") && !query.equalsIgnoreCase("commit;")) {
                transaction.addQuery(queryParser.parse(query));
            }
        }
        executeTransaction(transaction);
    }

    public String executeQuery() {
        try {
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            switch (query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    CreateDatabaseExecutor createDatabaseExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    createDatabaseExecutor.execute(null);
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    InsertTableQueryExecutor insertTableQueryExecutor = new InsertTableQueryExecutor(insertQuery);
                    insertTableQueryExecutor.execute(null);
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        CreateTableExecutor createTableExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());
                        createTableExecutor.execute(null);
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        UpdateQueryExecutor updateQueryExecutor = new UpdateQueryExecutor(updateQuery);
                        updateQueryExecutor.execute(null);
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        DeleteQueryExecutor deleteQueryExecutor = new DeleteQueryExecutor(deleteQuery);
                        deleteQueryExecutor.execute(null);
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        SelectQuery selectQuery = (SelectQuery) query;
                        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(selectQuery);
                        selectQueryExecutor.execute(null);
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
                    useDatabaseQueryExecutor.execute(null);
                    break;
                default:
                    System.err.println("You have entered an invalid query");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    private void executeTransaction(Transaction transaction) throws Exception {
        //ToDo handle transaction queries here
        Queue<Query> queries = transaction.getQueries();
        IQueryExecutor executor = null;
        transaction.setDatabaseName(QueryExecutorUtil.getChosenDatabase());
        initializeTableInTransaction(transaction);
        for (Query query : queries) {
            queryParser.validateQuery(query, transaction);
        }
        boolean isQuerySuccessfullyExecuted = false;
        for (Query query : queries) {
            executor = getQueryExecutorByQueryType(query);
            isQuerySuccessfullyExecuted = executor.execute(transaction);
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

    private IQueryExecutor getQueryExecutorByQueryType(Query query) throws Exception {
        if (query.getQueryType() == QueryType.CREATE_DATABASE) {
            return new CreateDatabaseExecutor((CreatDatabaseQuery) query);
        } else if (query.getQueryType() == QueryType.CREATE_TABLE) {
            return new CreateTableExecutor((CreateTableQuery) query, QueryExecutorUtil.getChosenDatabase());
        } else if (query.getQueryType() == QueryType.INSERT) {
            return new InsertTableQueryExecutor((InsertQuery) query);
        } else if (query.getQueryType() == QueryType.SELECT) {
            return new SelectQueryExecutor((SelectQuery) query);
        } else if (query.getQueryType() == QueryType.USE) {
            return new UseDatabaseQueryExecutor((UseDatabaseQuery) query);
        } else if (query.getQueryType() == QueryType.UPDATE) {
            return new UpdateQueryExecutor((UpdateQuery) query);
        } else if (query.getQueryType() == QueryType.DELETE) {
            return new DeleteQueryExecutor((DeleteQuery) query);
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
