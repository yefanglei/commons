package me.own.learn.commons.base.decorate;

import java.util.List;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public interface ICollectionDecorator<T> {

    /***
     * ICollection Mappper method
     * @param sourceItemList
     * @return
     */
    List<T> decorate(List<T> sourceItemList);
}
