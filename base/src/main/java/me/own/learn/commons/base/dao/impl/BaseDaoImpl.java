package me.own.learn.commons.base.dao.impl;

import me.own.learn.commons.base.dao.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {

    private final Logger log =LoggerFactory.getLogger(this.getEntityClass());

    @Autowired(required=true)
    protected SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(T entity) {
        try {
            getCurrentSession().save(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            getCurrentSession().update(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void remove(Serializable id) {
        try {
            T entity = get(id);
            if (entity!=null){
                getCurrentSession().delete(entity);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public T get(Serializable id) {
        T object = (T)getCurrentSession().get(getEntityClass(), id);
        return object;
    }

    @Override
    public Object getAggregationsResult(Projection projection, QueryCriteriaUtil queryCriterias){
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        if(queryCriterias!=null){
            Map<String, String> Alias = new HashMap<String, String>();
            if(queryCriterias!=null){
                Alias = queryCriterias.GetAlias();
            }
            if (Alias.size() > 0){
                for (String alia : Alias.keySet()){
                    rowcountcriteria.createAlias(alia, Alias.get(alia));
                }
            }
            List<Criterion> conConditions = queryCriterias.getConjunctionConditions();
            Conjunction conexpress = Restrictions.conjunction();
            if(conConditions.size()>0){
                for (Criterion ac : conConditions){
                    conexpress.add(ac);
                }
            }
            List<Criterion> disConditions = queryCriterias.getDisjunctionConditions();
            Disjunction disexpress = Restrictions.disjunction();
            if(disConditions.size()>0){
                for (Criterion ac : disConditions){
                    disexpress.add(ac);
                }
                conexpress.add(disexpress);
            }
            if (conConditions.size() > 0 || disConditions.size() > 0){
                rowcountcriteria.add(conexpress);
            }
//            rowcountcriteria.setProjection(Projections.rowCount());
        }
        rowcountcriteria.setProjection(projection);
        return rowcountcriteria.uniqueResult();
    }

    @Override
    public long getCount(QueryCriteriaUtil queryCriterias){
        long count = 0;
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        if(queryCriterias!=null){
            Map<String, String> Alias = new HashMap<String, String>();
            if(queryCriterias!=null){
                Alias = queryCriterias.GetAlias();
            }
            if (Alias.size() > 0){
                for (String alia : Alias.keySet()){
                    rowcountcriteria.createAlias(alia, Alias.get(alia));
                }
            }
            List<Criterion> conConditions = queryCriterias.getConjunctionConditions();
            Conjunction conexpress = Restrictions.conjunction();
            if(conConditions.size()>0){
                for (Criterion ac : conConditions){
                    conexpress.add(ac);
                }
            }
            List<Criterion> disConditions = queryCriterias.getDisjunctionConditions();
            Disjunction disexpress = Restrictions.disjunction();
            if(disConditions.size()>0){
                for (Criterion ac : disConditions){
                    disexpress.add(ac);
                }
                conexpress.add(disexpress);
            }
            if (conConditions.size() > 0 || disConditions.size() > 0){
                rowcountcriteria.add(conexpress);
            }
            rowcountcriteria.setProjection(Projections.rowCount());
        }else{
            rowcountcriteria.setProjection(Projections.rowCount()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
        count = (Long)rowcountcriteria.uniqueResult();
        return count;
    }

    @Override
    public int getDistinctCount(QueryCriteriaUtil queryCriterias){
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        if(queryCriterias!=null){
            Map<String, String> Alias = new HashMap<String, String>();
            if(queryCriterias!=null){
                Alias = queryCriterias.GetAlias();
            }
            if (Alias.size() > 0){
                for (String alia : Alias.keySet()){
                    rowcountcriteria.createAlias(alia, Alias.get(alia));
                }
            }
            List<Criterion> conConditions = queryCriterias.getConjunctionConditions();
            Conjunction conexpress = Restrictions.conjunction();
            if(conConditions.size()>0){
                for (Criterion ac : conConditions){
                    conexpress.add(ac);
                }
            }
            List<Criterion> disConditions = queryCriterias.getDisjunctionConditions();
            Disjunction disexpress = Restrictions.disjunction();
            if(disConditions.size()>0){
                for (Criterion ac : disConditions){
                    disexpress.add(ac);
                }
                conexpress.add(disexpress);
            }
            if (conConditions.size() > 0 || disConditions.size() > 0){
                rowcountcriteria.add(conexpress);
            }
        }
        rowcountcriteria.setProjection(Projections.distinct(Projections.id()));
        List<Serializable> idList = rowcountcriteria.list();
        return idList.size();
    }

    @Override
    public List<T> getPagedEntity(int pagenumber, int pageSize, QueryCriteriaUtil queryCriterias, QueryOrder order) {
        Criteria criteria = getCurrentSession().createCriteria(getEntityClass());
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        Map<String, String> Alias = queryCriterias.GetAlias();
        if (Alias.size() > 0){
            for (String alia : Alias.keySet()){
                criteria.createAlias(alia, Alias.get(alia));
                rowcountcriteria.createAlias(alia, Alias.get(alia));
            }
        }
        List<Criterion> conConditions = queryCriterias.getConjunctionConditions();
        Conjunction conexpress = Restrictions.conjunction();
        for (Criterion ac : conConditions){
            conexpress.add(ac);
        }
        List<Criterion> disConditions = queryCriterias.getDisjunctionConditions();
        Disjunction disexpress = Restrictions.disjunction();
        for (Criterion ac : disConditions){
            disexpress.add(ac);
        }
        if (disConditions.size() > 0){
            conexpress.add(disexpress);
        }
        if (conConditions.size() > 0 || disConditions.size() > 0){
            criteria.add(conexpress);
            rowcountcriteria.add(conexpress);
        }
        rowcountcriteria.setProjection(Projections.rowCount());
        criteria.setFirstResult((pagenumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        if (order!=null&&!StringUtils.isEmpty(order.getColumnName())&&!StringUtils.isEmpty(order.getOder())){
            if (QueryOrder.ASC.equals(order.getOder())){
                criteria.addOrder(Order.asc(order.getColumnName()));
            } else {
                criteria.addOrder(Order.desc(order.getColumnName()));
            }
        }
        return (List<T>)criteria.list();
    }

    @Override
    public PageQueryResult<T> pageQuery(int pageNumber, int pageSize, QueryCriteriaUtil queryCriteria){
        List<QueryOrder> queryOrders = new ArrayList<>();
        QueryOrder order = new QueryOrder();
        order.setColumnName("id");
        order.setOder(QueryOrder.DESC);
        queryOrders.add(order);

        return pageQuery(pageNumber, pageSize,queryCriteria, queryOrders);
    }

    @Override
    public PageQueryResult<T> pageQuery(int pageNumber, int pageSize, QueryCriteriaUtil queryCriteria, List<QueryOrder> queryOrders) {
        Criteria criteria = getCurrentSession().createCriteria(getEntityClass());
        Criteria rowCountCriteria = getCurrentSession().createCriteria(getEntityClass());
        Map<String, String> Alias = queryCriteria.GetAlias();
        if (Alias.size() > 0){
            for (String alia : Alias.keySet()){
                criteria.createAlias(alia, Alias.get(alia));
                rowCountCriteria.createAlias(alia, Alias.get(alia));
            }
        }
        List<Criterion> conConditions = queryCriteria.getConjunctionConditions();
        Conjunction conexpress = Restrictions.conjunction();
        for (Criterion ac : conConditions){
            conexpress.add(ac);
        }
        List<Criterion> disConditions = queryCriteria.getDisjunctionConditions();
        Disjunction disexpress = Restrictions.disjunction();
        for (Criterion ac : disConditions){
            disexpress.add(ac);
        }
        if (disConditions.size() > 0){
            conexpress.add(disexpress);
        }
        if (conConditions.size() > 0 || disConditions.size() > 0){
            criteria.add(conexpress);
            rowCountCriteria.add(conexpress);
        }
        rowCountCriteria.setProjection(Projections.rowCount());
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        if (CollectionUtils.isNotEmpty(queryOrders)) {
            for (QueryOrder order : queryOrders) {
                if (QueryOrder.ASC.equals(order.getOder())){
                    criteria.addOrder(Order.asc(order.getColumnName()));
                } else {
                    criteria.addOrder(Order.desc(order.getColumnName()));
                }
            }
        }
        long totalCount = (Long)rowCountCriteria.uniqueResult();
        if (totalCount == 0) {
            return PageQueryResult.emptyResult(pageNumber, pageSize);
        } else {
            List<T> items = (List < T >)criteria.list();
            return new PageQueryResult<T>(pageNumber, pageSize, items, totalCount);
        }
    }

    @Override
    public List<T> getDistinctPagedEntity(int pagenumber, int pageSize, QueryCriteriaUtil queryCriterias, QueryOrder order) {
        Criteria criteria = getCurrentSession().createCriteria(getEntityClass());
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        Map<String, String> Alias = queryCriterias.GetAlias();
        if (Alias.size() > 0){
            for (String alia : Alias.keySet()){
                criteria.createAlias(alia, Alias.get(alia));
                rowcountcriteria.createAlias(alia, Alias.get(alia));
            }
        }
        List<Criterion> conConditions = queryCriterias.getConjunctionConditions();
        Conjunction conexpress = Restrictions.conjunction();
        for (Criterion ac : conConditions){
            conexpress.add(ac);
        }
        List<Criterion> disConditions = queryCriterias.getDisjunctionConditions();
        Disjunction disexpress = Restrictions.disjunction();
        for (Criterion ac : disConditions){
            disexpress.add(ac);
        }
        if (disConditions.size() > 0){
            conexpress.add(disexpress);
        }
        if (conConditions.size() > 0 || disConditions.size() > 0){
            criteria.add(conexpress);
            rowcountcriteria.add(conexpress);
        }
        rowcountcriteria.setProjection(Projections.rowCount());
        criteria.setFirstResult((pagenumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        if (order!=null&&!StringUtils.isEmpty(order.getColumnName())&&!StringUtils.isEmpty(order.getOder())){
            if (QueryOrder.ASC.equals(order.getOder())){
                criteria.addOrder(Order.asc(order.getColumnName()));
            } else {
                criteria.addOrder(Order.desc(order.getColumnName()));
            }
        }
        criteria.setProjection(Projections.distinct(Projections.id()));
        List<Serializable> idList = criteria.list();
        // FIXME 1+n Algorithm
        List<T> pagedEntity = new ArrayList<>();
        for (Serializable id : idList){
            pagedEntity.add(this.get(id));
        }
        return pagedEntity;
    }

    @Override
    public List<T> getAll(Pagination pagination, QueryOrder oder) {
        try {
            Criteria c = getCurrentSession().createCriteria(getEntityClass());
            if (pagination!=null){
                c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                c.setProjection(Projections.rowCount());
                pagination.setiTotalCount((Long)c.uniqueResult());
                c.setProjection(null);
                c.setFirstResult(pagination.getiDisplayStart());
                c.setMaxResults(pagination.getiDisplayLength());
            }
            if (oder!=null&&!StringUtils.isEmpty(oder.getColumnName())&&!StringUtils.isEmpty(oder.getOder())){
                if (QueryOrder.ASC.equals(oder.getOder())){
                    c.addOrder(Order.asc(oder.getColumnName()));
                } else {
                    c.addOrder(Order.desc(oder.getColumnName()));
                }
            }
            return (List<T>)c.list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<T> filter(QueryCriteriaUtil QueryCriterias, Pagination pagination, QueryOrder... orders) {
        Criteria criteria = getCurrentSession().createCriteria(getEntityClass());
        Criteria rowcountcriteria = getCurrentSession().createCriteria(getEntityClass());
        Map<String, String> Alias = QueryCriterias.GetAlias();
        if (Alias.size() > 0){
            for (String alia : Alias.keySet()){
                criteria.createAlias(alia, Alias.get(alia));
                rowcountcriteria.createAlias(alia, Alias.get(alia));
            }
        }
        List<Criterion> conConditions = QueryCriterias.getConjunctionConditions();
        Conjunction conexpress = Restrictions.conjunction();
        for (Criterion ac : conConditions){
            conexpress.add(ac);
        }
        List<Criterion> disConditions = QueryCriterias.getDisjunctionConditions();
        Disjunction disexpress = Restrictions.disjunction();
        for (Criterion ac : disConditions){
            disexpress.add(ac);
        }
        if (disConditions.size() > 0){
            conexpress.add(disexpress);
        }
        if (conConditions.size() > 0 || disConditions.size() > 0){
            criteria.add(conexpress);
            rowcountcriteria.add(conexpress);
        }
        if (pagination!=null){
            rowcountcriteria.setProjection(Projections.rowCount());
            pagination.setiTotalCount((Long)rowcountcriteria.uniqueResult());
            criteria.setFirstResult(pagination.getiDisplayStart());
            criteria.setMaxResults(pagination.getiDisplayLength());
        }
        if (orders != null && orders.length > 0) {
            for (QueryOrder order : orders) {
                if (order!=null&&!StringUtils.isEmpty(order.getColumnName())&&!StringUtils.isEmpty(order.getOder())){
                    if (QueryOrder.ASC.equals(order.getOder())){
                        criteria.addOrder(Order.asc(order.getColumnName()));
                    } else {
                        criteria.addOrder(Order.desc(order.getColumnName()));
                    }
                }
            }
        }
        return (List<T>) criteria.list();
    }

    protected abstract Class<T> getEntityClass();
}
