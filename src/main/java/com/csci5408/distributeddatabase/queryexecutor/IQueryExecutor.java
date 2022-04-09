package com.csci5408.distributeddatabase.queryexecutor;

public interface IQueryExecutor
{
    boolean execute(Transaction transaction) throws Exception;
}
