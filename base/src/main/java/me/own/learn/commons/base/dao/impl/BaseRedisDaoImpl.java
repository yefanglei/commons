package me.own.learn.commons.base.dao.impl;

import me.own.learn.commons.base.dao.BaseRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis关于key操作的最基础实现
 * @param <K> 应用键类型泛型
 * @param <V> 值类型泛型
 * @author Christopher.Wang 2017/3/7.
 */
public abstract class BaseRedisDaoImpl<K, V> implements BaseRedisDao<K> {
    @Autowired
    protected RedisTemplate<String, V> redisTemplate;

    /**
     * 应用键向redis键转换的函数
     *      在应用中，为了业务逻辑清晰，可以选择需要的参数作为键，可以选用任意的java bean
     *      而在redis中，官方文档建议使用易读的带有命名空间的层级key设置
     *      如：
     *          application key: {work: 20170307000023, article: 9527}
     *          key: 'lishu:page:work:20170307000023:article:9527'
 *          命名空间说明：
     *          lishu -- 应用名，类似于mysql的数据库名
     *          page -- 类型名，类似于mysql的表名
     *          work:20170307000023:article:9527 -- 类似于mysql联合主键
     * @param key application key
     * @return redis key
     */
    protected abstract String convertKey(K key);

    @Override
    public void delete(K key) {
        redisTemplate.delete(convertKey(key));
    }

    @Override
    public boolean hasKey(K key) {
        return redisTemplate.hasKey(convertKey(key));
    }

    @Override
    public boolean expire(K key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(convertKey(key), timeout, unit);
    }

    @Override
    public boolean expireAt(K key, Date date) {
        return redisTemplate.expireAt(convertKey(key), date);
    }

    @Override
    public long getExpire(K key) {
        return redisTemplate.getExpire(convertKey(key));
    }

    @Override
    public long getExpire(K key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(convertKey(key), timeUnit);
    }
}
