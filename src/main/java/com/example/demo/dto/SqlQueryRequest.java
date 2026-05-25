package com.example.demo.dto;

/**
 * DTO for SQL query submission
 * Sent to webhook URL with the final SQL query
 */
public class SqlQueryRequest {
    private String finalQuery;

    public SqlQueryRequest() {
    }

    public SqlQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    @Override
    public String toString() {
        return "SqlQueryRequest{" +
                "finalQuery='" + finalQuery + '\'' +
                '}';
    }
}
