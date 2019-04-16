package me.own.learn.commons.base.utils.mapper;

import java.util.List;

/**
 * Created by Christopher.Wang on 2016/4/25.
 */
public abstract class Mapper {
    protected volatile static Mapper mapper = null;

    public static Mapper Default() {
        if (mapper == null) {
            mapper = new DomainBeansMapper();
        }
        return mapper;
    }

    Mapper() {}

    public abstract <T> T map(Object source, Class<T> destinationClass);

    public abstract void map(Object source, Object destination);

    public abstract <T, K> List<T> mapArray(List<K> sourceArray, Class<T> destinationItemClass);

}
