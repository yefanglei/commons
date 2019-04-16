package me.own.learn.commons.base.dao.impl;

import me.own.learn.commons.base.dao.BaseRedisHashDao;
import me.own.learn.commons.base.dao.ClassConverter;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 关于redis Hash的基础实现
 * @param <K> 应用key类型泛型
 * @param <E> hash对应的java实体泛型，在java应用中，通常一个特定的DAO实现是针对一个特定的java bean而不是随意的map类型
 * @author Christopher.Wang 2017/3/7.
 */
public abstract class BaseRedisHashDaoImpl<K, E> extends BaseRedisDaoImpl<K, String> implements BaseRedisHashDao<K, E> {
    /**
     * 指定hash元素的实体类型
     */
    protected abstract Class<E> getEntityClass();

    protected HashOperations<String, String, String> getOps() {
        return redisTemplate.opsForHash();
    }

    @Override
    public void setValue(K key, String field, Object value) {
        getOps().put(convertKey(key), field, value.toString());
    }

    @Override
    public void setEntity(K key, E entity) {
        BeanUtilsHashMapper<E> mapper = new BeanUtilsHashMapper<>((Class<E>) entity.getClass());
        Map<String, String> hash = mapper.toHash(entity);
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            // exclude java class name from redis hash key-value pair
            if (!"class".equals(entry.getKey())) {
                setValue(key, entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public long deleteValue(K key, String... fields) {
        return getOps().delete(convertKey(key), fields);
    }

    @Override
    public boolean hasField(K key, String field) {
        return getOps().hasKey(convertKey(key), field);
    }

    @Override
    public <V> V getValue(K key, String field, Class<V> valueClazz) {
        String value = getOps().get(convertKey(key), field);
        return (V) ClassConverter.convertValue(value, valueClazz);
    }

    @Override
    public E getEntity(K key) {
        if (!this.hasKey(key)) {
            return null;
        }
        Map<String, String> entries = getOps().entries(convertKey(key));
        E result;
        try {
            result = getEntityClass().newInstance();
            for (Method method : getEntityClass().getMethods()) {
                String methodName = method.getName();
                if (methodName.startsWith("set") && method.getParameterTypes().length == 1 && method.getReturnType().equals(void.class)) {
                    // get the property name by lower case first char of setter
                    String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    Class<?> setterParameterType = method.getParameterTypes()[0];
                    String v = entries.get(fieldName);
                    if (v != null) {
                        Object value = ClassConverter.convertValue(v, setterParameterType);
                        method.invoke(result, value);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
