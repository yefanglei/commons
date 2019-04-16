package me.own.learn.commons.base.utils.enums;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CN-LEBOOK-YANGLI on 1/23/2017.
 */
public class EnumUtil {

    private static void ensureEnumExists(Class<?> clazz) throws Exception{
        if(hasClazz(clazz)){
            return;
        }
        synchronized (clazz){
            putEnumObject(clazz);
        }
    }

    /***
     * 注入枚举对象
     * @param clazz
     * @return
     */
    private static void putEnumObject(Class<?> clazz) throws Exception{

        // return if already exists
        if(hasClazz(clazz)){
            return;
        }

        // the clazz must be an enum class
        if(!clazz.isEnum()){
            return;
        }

        // check if clazz inherits from EnumName
        boolean isEnumName = false;
        Class<?>[] interfaces = clazz.getInterfaces();
        for(Class<?> classInterface : interfaces){
            if(classInterface.getName().equals(EnumConstant.ENUMNAME_CLASS)){
                isEnumName = true;
                break;
            }
        }

        if(!isEnumName){
            return;
        }

        Map<Integer, EnumName> single_enum_map = new HashMap<>();
        Method method = clazz.getMethod("values");
        EnumName inter[] = (EnumName[]) method.invoke(null, null);
        for (EnumName enumName : inter) {
            single_enum_map.put(enumName.getCode(), enumName);
        }

        EnumConstant.ENUM_MAP.put(clazz, single_enum_map);
    }

    /***
     * Check if the class already registered into the ENUM_MAP
     * @param clazz
     * @return
     */
    private static boolean hasClazz(Class<?> clazz){
        return EnumConstant.ENUM_MAP.containsKey(clazz);
    }

    /**
     * 获取value返回枚举对象
     * @param code
     * @param clazz
     * */
    public static <T extends  EnumName> T getEnumObject(int code,Class<T> clazz){
        try{
            ensureEnumExists(clazz);
            Map<Integer, T> clazzMap = ( Map<Integer, T>)EnumConstant.ENUM_MAP.get(clazz);
            return clazzMap != null ? clazzMap.get(code) : null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取value返回枚举对象
     * @param code
     * @param clazz
     * */
    public static <T extends  EnumName>  String getEnumObjectName(int code, Class<T> clazz){
        T enumNameObject = getEnumObject(code, clazz);
        return enumNameObject != null ? enumNameObject.getName() : "";
    }

    /***
     * 获取EnumName定义的所有枚举值列表
     * @param clazz 类别
     * @return 枚举类型列表
     * 例如
     * [{"code": 0,"name": "name1"},{"code": 1,"name": "name2"}]
     */
    public static List<Map<String, Object>> getEnumNameValueList(Class<?> clazz){
        List<Map<String, Object>> enumValueList = new ArrayList();
        try{
            ensureEnumExists(clazz);
            Map<Integer, EnumName> clazzMap = (Map<Integer, EnumName>)EnumConstant.ENUM_MAP.get(clazz);
            if(clazzMap !=  null){
                for(Integer key : clazzMap.keySet()){
                    Map<String, Object> enumValue = new HashMap<>();
                    enumValue.put("code", key);
                    enumValue.put("name", clazzMap.get(key).getName());
                    enumValueList.add(enumValue);
                }
            }
        }catch (Exception e){

        }
        return enumValueList;
    }
}
