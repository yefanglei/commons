package me.own.learn.commons.base.mapper;

import java.util.List;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public interface ICollectionMapper<S,T> {

    /***
     * ICollection Mappper method
     * @param sourceItemList
     * @param targetItemsClass
     * @return
     */
    List<T> map(List<S> sourceItemList, Class<T> targetItemsClass);
}
