package me.own.learn.commons.base.utils.request;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/17.
 */
public class RequestUtils {

    /***
     * Copy http request parameters to filter object
     *
     * Only support basic java types such as Integer, Double, Float, Long, String
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Object> T buildQueryFilter(HttpServletRequest request, Class<T> clazz){
        try{
            T instance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                String fieldName = field.getName();
                String requestParameter = request.getParameter(fieldName);
                if(StringUtils.isNotEmpty(requestParameter)){
                    String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                    try{
                        Method method = clazz.getDeclaredMethod(methodName, field.getType());
                        Object methodParameter = convertParameterType(requestParameter, field.getType());
                        method.invoke(instance, methodParameter);
                    }catch(NoSuchMethodException | InvocationTargetException e){
                        continue;
                    }catch (Exception e){
                        continue;
                    }
                }
            }
            return instance;
        }catch (InstantiationException | IllegalAccessException e){
            return null;
        }
    }

    private static Object convertParameterType(String requestParameter, Class<?> clazz) throws Exception{
        String clazzName = clazz.getSimpleName();
        switch (clazzName){
            case "String":
                return requestParameter;
            case "Integer":
                return Integer.parseInt(requestParameter);
            case "Double":
                return Double.parseDouble(requestParameter);
            case "Float":
                return Float.parseFloat(requestParameter);
            case "Long":
                return Long.parseLong(requestParameter);
            case "Date":
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sf.parse(requestParameter);
            default:
                throw new Exception("unsupported field type " + clazzName);
        }
    }

    /***
     * Get remote client visit real ip
     * @see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_5
     * nginx有代理的情况:
     * 在nginx中配置中加入
     * proxy_set_header Host $host;
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header X-Real-Port $remote_port;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     * @param request
     * @return
     */
    public static String getRemoteAddressIp(HttpServletRequest request){
        String clientIp = "";
        String ipList = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ipList)){
            ipList = request.getHeader("X-Real-IP");
        }
        if(StringUtils.isNotEmpty(ipList)){
            clientIp = ipList.split(",")[0];
        }
        if(StringUtils.isEmpty(clientIp)){
            ipList =  request.getHeader("PROXY_FORWARDED_FOR");
            if(StringUtils.isEmpty(ipList)){
                clientIp = request.getRemoteAddr();
            }else{
                clientIp = ipList.split(",")[0];
            }
        }

        // check if ip valid
        if(!isIP(clientIp)){
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    private static boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        return mat.find();
    }
}
