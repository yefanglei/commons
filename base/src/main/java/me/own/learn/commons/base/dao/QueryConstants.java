package me.own.learn.commons.base.dao;

public class QueryConstants {

    public enum SimpleQueryMode
    {
        Equal,
        Like,
        GreaterThan,
        LessThan,
        NotEqual,
        LessEqual,
        GreaterEqual,
        IsNull,
        IsNotNull,
    }

    public enum ComplexQueryMode
    {
        In,
        Between
    }

    public enum QueryType
    {
        Conjunction,
        Disjunction
    }
}
