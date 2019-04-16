package me.own.learn.commons.base.dao;

import java.util.concurrent.TimeUnit;

/**
 * @author Christopher.Wang 2017/3/7.
 */
public interface BaseRedisValueDao<K> extends BaseRedisDao<K> {
    void set(K key, String value);

    void set(K key, String value, long timeout, TimeUnit unit);

    String get(K key);
}
