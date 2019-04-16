package me.own.learn.commons.base.dao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 封装了redis key相关命令接口，此处与具体的redis数据结构无关
 * @param <K> key类型泛型，理论上可以是任何java类，但实际的redis键体现一种命名空间与key参数的结合
 * @see me.own.learn.commons.base.dao.impl.BaseRedisDaoImpl#convertKey
 * @author Christopher.Wang 2017/3/7.
 */
public interface BaseRedisDao<K> {
    /**
     * 删除一个key，如果被删除的key不存在，则直接忽略
     * @param key application key
     */
    void delete(K key);

    /**
     * 检查key是否存在
     * @param key application key
     * @return key是否存在
     */
    boolean hasKey(K key);

    /**
     * 设置key的过期时间，超过时间后，将会自动删除该key。
     * @param key application key
     * @param timeout 过期时长
     * @param unit 时间单位
     * @return true表示成功，false表示key不存在或者无法设置
     */
    boolean expire(K key, long timeout, TimeUnit unit);

    /**
     * 设置key的过期时间点，超过该时间点后，将会自动删除该key。
     * @param key application key
     * @param date 过期时刻
     * @return true表示成功，false表示key不存在或者无法设置
     */
    boolean expireAt(K key, Date date);

    /**
     * 返回key剩余的过期时间
     * @param key application key
     * @return 过期秒数
     */
    long getExpire(K key);

    /**
     * 返回key剩余的过期时间
     * @param key application key
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    long getExpire(K key, TimeUnit timeUnit);
}
