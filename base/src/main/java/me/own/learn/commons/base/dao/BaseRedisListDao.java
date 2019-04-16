package me.own.learn.commons.base.dao;

import java.util.Collection;
import java.util.List;

/**
 * 封装了List相关的redis命令接口
 * @param <K> 应用key类型泛型
 * @param <V> List元素类型泛型
 * @author Christopher.Wang 2017-3-8
 */
public interface BaseRedisListDao<K, V> extends BaseRedisDao<K> {
    /**
     * 返回存储在 key 的列表里指定范围内的元素，下标从0开始，并且支持负数，即从尾部倒数
     * 注意：
     *      此命令对应redis中的lrange，是从队头取数据
     *      如果数据也从队头存入list，那么这将是一个LIFO后进先出的栈，返回结果中的顺序会与 leftPushAll() 中相反
     *      如果希望range/push数据顺序相同，那么请用rightPushAll()
     * @see #leftPushAll
     * @see #rightPushAll
     * @param key application key
     * @param start 开始下标
     * @param end 结束下标
     * @return 元素列表
     */
    List<V> range(K key, long start, long end);

    /**
     * 返回全部元素
     * @param key application key
     * @return 元素列表
     */
    List<V> getAll(K key);

    /**
     * 返回元素个数
     */
    long size(K key);

    /**
     * 将所有指定的值插入到存于 key 的列表的头部。
     * 如果 key 不存在，那么在进行 push 操作前会创建一个空列表。
     * 如果 key 对应的值不是一个 list 的话，那么会返回一个错误。
     * @param key application key
     * @param value 元素
     * @return push完成后list长度
     */
    long leftPush(K key, V value);

    long leftPushAll(K key, Collection<V> values);

    /**
     * 向存于 key 的列表的尾部插入所有指定的值。
     * 如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。
     * 当 key 保存的不是一个列表，那么会返回一个错误。
     * @param key application key
     * @param value 元素
     * @return push完成后list长度
     */
    long rightPush(K key, V value);

    long rightPushAll(K key, Collection<V> values);
}
