package com.example.demo.query.parsers;

import com.example.demo.query.Query;

import java.util.Arrays;
import java.util.List;

public abstract class Parser {

    final List<String> operators = Arrays.asList("<=", ">=", "<", ">", "!=", "=");

    String sqlQuery;
    List<String> sqlQueryParts;

    public Parser(String sqlQuery, List<String> sqlQueryParts) {
        this.sqlQuery = sqlQuery;
        this.sqlQueryParts = sqlQueryParts;
    }

    public abstract Query parse() throws Exception;
}
