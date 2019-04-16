package me.own.learn.commons.base.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/4/5.
 */
public abstract class BaseRedisJsonHashDaoImpl<K, E> extends BaseRedisHashDaoImpl<K, E>{

    private static final Logger logger = LoggerFactory.getLogger(BaseRedisJsonHashDaoImpl.class);;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setEntity(K key, E entity) {
        Class<E> clazz = (Class<E>) entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            try{
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                Method method = clazz.getMethod(methodName);
                String value = objectMapper.writeValueAsString(method.invoke(entity));
                if(StringUtils.isNotEmpty(value)){
                    setValue(key, fieldName, value);
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error("set entity error, class : {}, field : {} exception : {}", clazz.toString(), field.getName(), e.getMessage());
            }
        }
    }

    @Override
    public E getEntity(K key) {
        if (!this.hasKey(key)) {
            return null;
        }
        Map<String, String> entries = getOps().entries(convertKey(key));
        E result = null;
        try {
            result = getEntityClass().newInstance();
            for (Method method : getEntityClass().getMethods()) {
                String methodName = method.getName();
                if (methodName.startsWith("set") && method.getParameterTypes().length == 1 && method.getReturnType().equals(void.class)) {
                    // get the property name by lower case first char of setter
                    String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    String v = entries.get(fieldName);
                    if (v != null) {
                        Class<?> fieldClass = getEntityClass().getDeclaredField(fieldName).getType();
                        Object value = objectMapper.readValue(v, fieldClass);
                        method.invoke(result, value);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            logger.error("get entity error, class {} {}", getEntityClass().toString(), e.getMessage());
        }

        return result;
    }
}
