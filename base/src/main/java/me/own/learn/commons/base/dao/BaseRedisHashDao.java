package me.own.learn.commons.base.dao;

/**
 * 封装了Hash相关的redis命令接口
 * @param <K> 应用key类型泛型
 * @param <E> hash对应的java实体泛型，在java应用中，通常一个特定的DAO实现是针对一个特定的java bean而不是随意的map类型
 * @author Christopher.Wang 2017-3-7
 */
public interface BaseRedisHashDao<K, E> extends BaseRedisDao<K> {
    /**
     * 设置属性值
     *      在已知更新的字段是哪个的时候，尽量使用setValue而不是setEntity，这也是使用hash而不是使用json序列化java bean的性能优势
     * @param key application key
     * @param field 属性名
     * @param value 属性值
     */
    void setValue(K key, String field, Object value);

    /**
     * 设置实体，即设置整个hash的属性值
     * @param key application key
     * @param entity java bean实体
     */
    void setEntity(K key, E entity);

    /**
     * 删除特定属性的值
     * @param key application key
     * @param fields 属性名
     * @return 返回从哈希集中成功移除的域的数量，不包括指出但不存在的那些域
     */
    long deleteValue(K key, String... fields);

    boolean hasField(K key, String field);

    /**
     * 获取属性值
     *      在已知获取的字段是哪个的时候，尽量使用getValue而不是getEntity获取整个实体，这也是使用hash而不是使用json序列化java bean的性能优势
     * @param key application key
     * @param field 属性名
     * @param valueClazz 目标类型，只支持java 8大基本类型（包括包装类）及String,java.util.Date
     * @param <V> 属性值类型泛型
     * @return 属性值
     */
    <V> V getValue(K key, String field, Class<V> valueClazz);

    /**
     * 获取整个实体
     * @param key application key
     */
    E getEntity(K key);
}
