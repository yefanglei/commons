package me.own.learn.commons.base.decorate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/24.
 */
public class ListDecorator<T> implements ICollectionDecorator<T> {

    protected IVoDecorator objectDecorator;

    public static ListDecorator Create(IVoDecorator objectDecorator) {
        ListDecorator listDecorator = new ListDecorator();
        listDecorator.setObjectDecorator(objectDecorator);
        return listDecorator;
    }

    @Override
    public List<T> decorate(List<T> sourceItemList) {
        List<T> destinationItemList = new ArrayList<>();
        for (T sourceItem : sourceItemList) {
            T destinationItem = (T) objectDecorator.decorate(sourceItem);
            destinationItemList.add(destinationItem);
        }
        return destinationItemList;
    }

    public IVoDecorator getObjectDecorator() {
        return objectDecorator;
    }

    public void setObjectDecorator(IVoDecorator objectDecorator) {
        this.objectDecorator = objectDecorator;
    }
}
