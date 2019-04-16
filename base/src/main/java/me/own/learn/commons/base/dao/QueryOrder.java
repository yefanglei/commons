package me.own.learn.commons.base.dao;

public class QueryOrder {
    private String columnName;

    private String oder;

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    public QueryOrder() {
    }

    public QueryOrder(String columnName, String oder) {
        this.columnName = columnName;
        this.oder = oder;
    }

    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getOder() {
        return oder;
    }
    public void setOder(String oder) {
        this.oder = oder;
    }
}
