package me.own.learn.commons.base.utils.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CN-LEBOOK-YANGLI on 1/4/2017.
 */
public class ResponseUtils {

    /***
     * Initialize a map response entity with success as false
     * @return
     */
    public static Map<String,Object> initAjaxResponseMap(){
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("success", false);
        return response;
    }

    /***
     * Add a new key-value result to the map response entity
     * @param paramkey
     * @param paramValue
     * @param response
     */
    public static void setAjaxResponseMapParam(String paramkey,Object paramValue,Map<String,Object> response){
        response.put(paramkey, paramValue);
    }

    /***
     * Set success as true
     * @param response
     */
    public static void setAjaxResponseSuccess(Map<String,Object> response){
        response.put("success", true);
    }
}
