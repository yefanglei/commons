package me.own.learn.commons.base.decorate;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public interface IVoDecorator<T>{

    /***
     * Each customized decorator must implements how to decorate an source object
     * @param sourceItem
     * @return
     */
    public abstract T decorate(T sourceItem);
}
