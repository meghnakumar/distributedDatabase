package com.example.demo.query.parsers;

import com.example.demo.query.Query;

import java.util.Arrays;
import java.util.List;

public class QueryParser {

    public Query parse(String sqlQuery) throws Exception {
        sqlQuery = sqlQuery.trim();
        if (!sqlQuery.substring(sqlQuery.length() - 1).equalsIgnoreCase(";")) {
            throw new Exception("Invalid Query Syntax. Semi-Colon is Missing !");
        }

        List<String> sqlQueryParts = Arrays.asList(sqlQuery.split("\\s+"));

        String queryType = sqlQueryParts.get(0);

        Parser parser;

        switch (queryType) {
            case "create":
                parser = new CreateQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "use":
                parser = new UserQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "insert":
                parser = new InsertQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "delete":
                parser = new DeleteQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "update":
                parser = new UpdateQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "select":
                parser = new SelectQueryParser(sqlQuery, sqlQueryParts);
                break;

            default:
                throw new Exception("Oops!! Query Type is Invalid");
        }
        return parser.parse();
    }
}
