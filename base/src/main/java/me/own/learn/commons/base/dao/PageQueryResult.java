package me.own.learn.commons.base.dao;

import me.own.learn.commons.base.decorate.ICollectionDecorator;
import me.own.learn.commons.base.mapper.ICollectionMapper;
import me.own.learn.commons.base.utils.mapper.Mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果模型
 * @author Christopher.Wang 2016-12-16
 * @param <T> 查询对象泛型
 */
public class PageQueryResult<T> implements Serializable {
    private List<T> items;

    /**
     * 总条数
     */
    private long totalCount;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 第几页，从1开始
     */
    private int pageNumber;

    public PageQueryResult() {

    }

    public PageQueryResult(int pageNumber, int pageSize) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.items = new ArrayList<>();
        this.totalCount = 0L;
        this.pageCount = 0;
    }

    public PageQueryResult(int pageNumber, int pageSize,List<T> items, long totalCount ) {
        this.items = items;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.pageCount = (int) ((totalCount / pageSize) + (totalCount % pageSize == 0 ? 0 : 1));
    }

    public static PageQueryResult emptyResult(int pageNumber, int pageSize) {
        return new PageQueryResult(pageNumber, pageSize);
    }

    public <K> PageQueryResult<K> mapItems(Class<K> targetItemsClass) {
        PageQueryResult<K> afterMapperPQResult = new PageQueryResult(
                this.pageNumber, this.pageSize, Mapper.Default().mapArray(this.items, targetItemsClass), this.totalCount
        );
        return afterMapperPQResult;
    }

    public <K> PageQueryResult<K> mapItems(Class<K> targetItemsClass, ICollectionMapper mapper) {
        PageQueryResult<K> afterMapperPQResult = new PageQueryResult(
                this.pageNumber, this.pageSize, mapper.map(this.items, targetItemsClass), this.totalCount
        );
        return afterMapperPQResult;
    }

    public <K> PageQueryResult<K> decorateItems(ICollectionDecorator decorator) {
        PageQueryResult<K> afterDecoratePQResult = new PageQueryResult(
                this.pageNumber, this.pageSize, decorator.decorate(this.items), this.totalCount
        );
        return afterDecoratePQResult;
    }

    @Override
    public String toString() {
        return "PageQueryResult{" +
                "items=" + items +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                '}';
    }

    // getter-setter
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
