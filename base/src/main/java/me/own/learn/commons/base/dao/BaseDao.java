package me.own.learn.commons.base.dao;

import org.hibernate.criterion.Projection;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T extends Serializable> {
    public void create(T entity);
    public void update(T entity);
    public void remove(Serializable id);
    public T get(Serializable id);
    public List<T> getAll(Pagination pagination, QueryOrder oder);
    List<T> filter(QueryCriteriaUtil QueryCriterias, Pagination pagination, QueryOrder... order);

    int getDistinctCount(QueryCriteriaUtil queryCriterias);

    public List<T> getPagedEntity(int pagenumber, int pageSize, QueryCriteriaUtil queryCriterias, QueryOrder order);
    PageQueryResult<T> pageQuery(int pageNumber, int pageSize, QueryCriteriaUtil queryCriteria);
    PageQueryResult<T> pageQuery(int pageNumber, int pageSize, QueryCriteriaUtil queryCriteria, List<QueryOrder> queryOrders);
    List<T> getDistinctPagedEntity(int pagenumber, int pageSize, QueryCriteriaUtil queryCriterias, QueryOrder order);

    Object getAggregationsResult(Projection projection, QueryCriteriaUtil queryCriterias);

    public long getCount(QueryCriteriaUtil queryCriterias);
}
