package me.own.learn.commons.base.dao.impl;

import me.own.learn.commons.base.dao.BaseRedisListDao;
import org.springframework.data.redis.core.ListOperations;

import java.util.Collection;
import java.util.List;

/**
 * 关于redis list的基础实现
 * @param <K> 应用key类型泛型
 * @param <V> List元素类型泛型
 * @author Christopher.Wang 2017/3/7.
 */
public abstract class BaseRedisListDaoImpl<K, V> extends BaseRedisDaoImpl<K, V> implements BaseRedisListDao<K, V> {
    protected ListOperations<String, V> getOps() {
        return redisTemplate.opsForList();
    }

    @Override
    public List<V> range(K key, long start, long end) {
        return getOps().range(convertKey(key), start, end);
    }

    @Override
    public List<V> getAll(K key) {
        return range(key, 0, -1);
    }

    @Override
    public long size(K key) {
        return getOps().size(convertKey(key));
    }

    @Override
    public long leftPush(K key, V value) {
        return getOps().leftPush(convertKey(key), value);
    }

    @Override
    public long leftPushAll(K key, Collection<V> values) {
        return getOps().leftPushAll(convertKey(key), values);
    }

    @Override
    public long rightPush(K key, V value) {
        return getOps().rightPush(convertKey(key), value);
    }

    @Override
    public long rightPushAll(K key, Collection<V> values) {
        return getOps().rightPushAll(convertKey(key), values);
    }
}
