package me.own.learn.commons.base.dao.impl;

import me.own.learn.commons.base.dao.BaseRedisValueDao;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author Christopher.Wang 2017/3/7.
 */
public abstract class BaseRedisValueDaoImpl<K> extends BaseRedisDaoImpl<K, String> implements BaseRedisValueDao<K> {
    protected ValueOperations<String, String> getOps() {
        return redisTemplate.opsForValue();
    }

    @Override
    public void set(K key, String value) {
        getOps().set(convertKey(key), value);
    }

    @Override
    public void set(K key, String value, long timeout, TimeUnit unit) {
        getOps().set(convertKey(key), value, timeout, unit);
    }

    @Override
    public String get(K key) {
        return getOps().get(convertKey(key));
    }
}
