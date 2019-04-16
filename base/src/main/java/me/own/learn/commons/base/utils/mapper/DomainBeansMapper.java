package me.own.learn.commons.base.utils.mapper;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher.Wang on 2016/4/25.
 */
class DomainBeansMapper extends Mapper {

    private static org.dozer.Mapper mapper = new DozerBeanMapper();

    protected DomainBeansMapper(){}

    @Override
    public <T> T map(Object source, Class<T> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    @Override
    public void map(Object source, Object destination) {
        mapper.map(source, destination);
    }

    @Override
    public <T, K> List<T> mapArray(List<K> sourceArray, Class<T> destinationItemClass) {
        List<T> destinationList = new ArrayList<>();
        for (K source : sourceArray){
            destinationList.add(mapper.map(source, destinationItemClass));
        }
        return destinationList;
    }
}
