package me.own.learn.commons.base.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public class ListMapper<S, T> implements ICollectionMapper<S, T>{

    protected IPoVoMapper objectMapper;

    public IPoVoMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(IPoVoMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static ListMapper Create(IPoVoMapper objectMapper){
        ListMapper listMapper = new ListMapper();
        listMapper.setObjectMapper(objectMapper);
        return listMapper;
    }

    @Override
    public List<T> map(List<S> sourceItemList, Class<T> targetItemsClass) {
        List<T> destinationItemList = new ArrayList<>();
        for(S sourceItem : sourceItemList){
            T destinationItem = (T)objectMapper.map(sourceItem);
            destinationItemList.add(destinationItem);
        }
        return destinationItemList;
    }
}
