package me.own.learn.commons.base.mapper;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public interface IPoVoMapper<S,T> {

    /***
     * Each customized mapper must implements how to map an source object
     * @param sourceItem
     * @return
     */
    public abstract T map(S sourceItem);
}
