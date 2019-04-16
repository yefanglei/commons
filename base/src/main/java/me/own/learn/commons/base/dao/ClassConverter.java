package me.own.learn.commons.base.dao;

import java.lang.reflect.Method;
import java.sql.Date;


public class ClassConverter {

    public static Object convertValue(String value, Class<?> targetType){
        if (targetType.isEnum()){
            try {
                Method method = targetType.getMethod("valueOf", String.class);
                return method.invoke(null, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (targetType.getName().equalsIgnoreCase("byte") || Byte.class.equals(targetType)){
            return Byte.parseByte(value);
        }
        if (targetType.getName().equalsIgnoreCase("short")|| Short.class.equals(targetType)){
            return Short.parseShort(value);
        }
        if (targetType.getName().equalsIgnoreCase("int") || Integer.class.equals(targetType)){
            return Integer.parseInt(value);
        }
        if (targetType.getName().equalsIgnoreCase("long") || Long.class.equals(targetType)){
            return Long.parseLong(value);
        }
        if (targetType.getName().equalsIgnoreCase("boolean") || Boolean.class.equals(targetType)){
            return Boolean.parseBoolean(value);
        }
        if (targetType.getName().equalsIgnoreCase("float") || Float.class.equals(targetType)){
            return Float.parseFloat(value);
        }
        if (targetType.getName().equalsIgnoreCase("double") || Double.class.equals(targetType)){
            return Double.parseDouble(value);
        }
        if (targetType.getName().equalsIgnoreCase("java.lang.String")){
            return value;
        }
        if(targetType.getName().equalsIgnoreCase("java.util.Date")){
            try{
                return Date.valueOf(value);
            }catch (IllegalArgumentException iae){
                return new Date(Long.valueOf(value));
            }
        }
        else {
            return null;
        }
    }
}
