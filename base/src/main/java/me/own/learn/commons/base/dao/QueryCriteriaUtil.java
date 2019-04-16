package me.own.learn.commons.base.dao;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryCriteriaUtil {

    private final static Logger log =LoggerFactory.getLogger(QueryCriteriaUtil.class);

    private Map<String, Criterion> ConjunctionConditions = new HashMap<String, Criterion>();

    private Map<String, Criterion> DisjunctionConditions = new HashMap<String, Criterion>();

    private Map<String, Class> properties = new HashMap<String, Class>();

    private Map<String, String> alias = new HashMap<String, String>();

    private QueryConstants.SimpleQueryMode DefaultQueryMode = QueryConstants.SimpleQueryMode.Equal;

    private String refKeyName = "";

    private String EscapeChar = "\\";

    private Class localClass;

    public QueryCriteriaUtil(Class c)
    {
        this.localClass = c;
        DefaultQueryMode = QueryConstants.SimpleQueryMode.Equal;
        BuildOwnPropertiesMapping();
    }

    public void SetDefaultQueryMode(QueryConstants.SimpleQueryMode QueryMode)
    {
        DefaultQueryMode = QueryMode;
    }

    public void ClearConditions()
    {
        ConjunctionConditions.clear();
        DisjunctionConditions.clear();
        alias.clear();
        refKeyName = "";
        DefaultQueryMode = QueryConstants.SimpleQueryMode.Equal;
    }
    /**
     Just keep the parameter name align with the entity property name. Take SalesOrder for example, filter by SalesOrderItem,
     the parameter name should be SalesOrderItem.SalesOrderItemID; filter by the salesman should be ContractedBy.Name
     **/
//    public void BuildQueryModelFromRequest(HttpServletRequest request)
//    {
//        if (request != null)
//        {
//            Enumeration<String>	paramKeys = request.getParameterNames();
//
//            while(paramKeys.hasMoreElements()){
//                try
//                {
//                    String key = paramKeys.nextElement();
//                    setSimpleCondition(key, request.getParameter(key), DefaultQueryMode);
//                } catch (Exception ex) {
//                    log.error(ex.getMessage(), ex);
//                    continue;
//                }
//            }
//        }
//    }

    private Class GetLastPropertyType(String[] PropertyKey, int i, Class c)
    {
        Class t1 = BuildReferencePropertiesMapping(c).get(PropertyKey[i]);
        if (i < PropertyKey.length - 1)
        {
            String aliasName = "";
            for (int j = 0; j <= i;j++ )
            {
                if (j == i)
                {
                    aliasName += PropertyKey[j];
                }
                else
                {
                    aliasName += PropertyKey[j]+".";
                }

            }
            if (t1.getGenericInterfaces().length > 0 && t1.getGenericInterfaces()[0] instanceof ParameterizedType){
                ParameterizedType t = (ParameterizedType)t1.getGenericInterfaces()[0];
                t1 = (Class)t.getActualTypeArguments()[0];
            }
            if (!alias.containsKey(aliasName))
            {
                alias.put(aliasName, PropertyKey[i]);
            }

            refKeyName = PropertyKey[PropertyKey.length - 2];

            i++;
            return GetLastPropertyType(PropertyKey, i, t1);
        }
        else
        {
            return t1;
        }
    }

    public void setDeletedFalseCondition(){
        this.setSimpleCondition("deleted", "false", QueryConstants.SimpleQueryMode.Equal);
    }

    /**
     Just keep the parameter name align with the entity property name. Take SalesOrder for example, filter by SalesOrderItem,
     the parameter name should be SalesOrderItem.SalesOrderItemID; filter by the salesman should be ContractedBy.Name
     This is a conjunction query by default, if you wanna customize the clause, call
     setSimpleCondition(string ColumnName, string ColumnValue, SimpleQueryMode QueryMode, QueryType QueryType)
     **/
    public void setSimpleCondition(String ColumnName, String ColumnValue, QueryConstants.SimpleQueryMode QueryMode)
    {
        setSimpleCondition(ColumnName, ColumnValue, QueryMode, QueryConstants.QueryType.Conjunction);
    }

    /**
     Just keep the parameter name align with the entity property name. Take SalesOrder for example, filter by SalesOrderItem,
     the parameter name should be SalesOrderItem.SalesOrderItemID; filter by the salesman should be ContractedBy.Name.
     This is a conjunction query by default, if you wanna customize the clause, call
     setComplexCondition(string ColumnName, string ColumnValue, SimpleQueryMode QueryMode, QueryType QueryType)
     **/
    public void setComplexCondition(String ColumnName, List<String> ColumnValue, QueryConstants.ComplexQueryMode QueryMode)
    {
        setComplexCondition(ColumnName, ColumnValue, QueryMode, QueryConstants.QueryType.Conjunction);
    }

    /**
     Just keep the parameter name align with the entity property name.
     Check property is null or not
     **/
    public void setSimpleNullCondition(String ColumnName, boolean isNull){
        setSimpleNullCondition(ColumnName, isNull, QueryConstants.QueryType.Conjunction);
    }

    /**
     Just keep the parameter name align with the entity property name.
     Check property is null or not
     **/
    public void setSimpleNullCondition(String ColumnName, boolean isNull, QueryConstants.QueryType QueryType)
    {
        if (ColumnName == null || ColumnName.length() == 0)
        {
            return;
        }

        if (ColumnName.contains("."))
        {
            String[] Objreferences = ColumnName.split("\\.");
            String objPropKey = Objreferences[0];
            refKeyName = Objreferences[Objreferences.length - 2];
            if (properties.containsKey(objPropKey))
            {
                Class tpassin = properties.get(objPropKey);

                if (tpassin.getGenericInterfaces().length > 0 && tpassin.getGenericInterfaces()[0] instanceof ParameterizedType){
                    ParameterizedType t = (ParameterizedType)tpassin.getGenericInterfaces()[0];
                    tpassin = (Class)t.getActualTypeArguments()[0];
                    alias.put(objPropKey, objPropKey);
                    refKeyName = tpassin.getName();
                }
                else
                {
                    if (!alias.containsKey(objPropKey))
                    {
                        //alias.Add(objPropKey, properties[objPropKey].Name);
                        alias.put(objPropKey, objPropKey);
                    }
                }

                Class t = GetLastPropertyType(Objreferences, 1, tpassin);
                Criterion se = BuildFilter(refKeyName + "." + Objreferences[Objreferences.length - 1], "",
                        isNull ? QueryConstants.SimpleQueryMode.IsNull : QueryConstants.SimpleQueryMode.IsNotNull);
                SetConditions(ColumnName, se, QueryType);
            }
        }
        else
        {
            if (properties.containsKey(ColumnName))
            {
                Object result;
                Criterion se = BuildFilter(ColumnName, "",
                        isNull ? QueryConstants.SimpleQueryMode.IsNull : QueryConstants.SimpleQueryMode.IsNotNull);
                SetConditions(ColumnName, se, QueryType);
            }
        }
    }

    /**
     Just keep the parameter name align with the entity property name. Take SalesOrder for example, filter by SalesOrderItem,
     the parameter name should be SalesOrderItem.SalesOrderItemID; filter by the salesman should be ContractedBy.Name
     **/
    public void setSimpleCondition(String ColumnName, String ColumnValue, QueryConstants.SimpleQueryMode QueryMode, QueryConstants.QueryType QueryType)
    {
        if (ColumnName == null || ColumnName.length() == 0 || ColumnValue == null || ColumnValue.length() == 0)
        {
            return;
        }

        if (ColumnName.contains("."))
        {
            String[] Objreferences = ColumnName.split("\\.");
            String objPropKey = Objreferences[0];
            refKeyName = Objreferences[Objreferences.length - 2];
            if (properties.containsKey(objPropKey))
            {
                Class tpassin = properties.get(objPropKey);

                if (tpassin.getGenericInterfaces().length > 0 && tpassin.getGenericInterfaces()[0] instanceof ParameterizedType){
                    ParameterizedType t = (ParameterizedType)tpassin.getGenericInterfaces()[0];
                    tpassin = (Class)t.getActualTypeArguments()[0];
                    alias.put(objPropKey, objPropKey);
                    refKeyName = tpassin.getName();
                }
                else
                {
                    if (!alias.containsKey(objPropKey))
                    {
                        //alias.Add(objPropKey, properties[objPropKey].Name);
                        alias.put(objPropKey, objPropKey);
                    }
                }

                Class t = GetLastPropertyType(Objreferences, 1, tpassin);

                Object result;

                result = ClassConverter.convertValue(ColumnValue, t);

                Criterion se = BuildFilter(refKeyName + "." + Objreferences[Objreferences.length - 1], result, QueryMode);
                SetConditions(ColumnName, se, QueryType);
            }
        }
        else
        {
            if (properties.containsKey(ColumnName))
            {
                Object result;
                result = ClassConverter.convertValue(ColumnValue, properties.get(ColumnName));
                Criterion se = BuildFilter(ColumnName, result, QueryMode);
                SetConditions(ColumnName, se, QueryType);
            }
        }
    }

    /**
     Just keep the parameter name align with the entity property name. Take SalesOrder for example, filter by SalesOrderItem,
     the parameter name should be SalesOrderItem.SalesOrderItemID; filter by the salesman should be ContractedBy.Name
     **/
    public void setComplexCondition(String ColumnName, List<String> ColumnValue, QueryConstants.ComplexQueryMode QueryMode, QueryConstants.QueryType QueryType)
    {
        if (ColumnName == null || ColumnName.length() == 0 || ColumnValue == null || ColumnValue.size() == 0)
        {
            return;
        }
        if (ColumnName.contains("."))
        {
            String[] Objreferences = ColumnName.split("\\.");
            String objPropKey = Objreferences[0];
            refKeyName = Objreferences[Objreferences.length - 2];

            if (properties.containsKey(objPropKey))
            {
                Class tpassin = properties.get(objPropKey);
                if (tpassin.getGenericInterfaces()[0] instanceof ParameterizedType){
                    ParameterizedType t = (ParameterizedType)tpassin.getGenericInterfaces()[0];
                    tpassin = (Class)t.getActualTypeArguments()[0];
                    alias.put(objPropKey, objPropKey);
                    refKeyName = tpassin.getName();
                } else {
                    if (!alias.containsKey(objPropKey))
                    {
                        //alias.Add(objPropKey, properties[objPropKey].Name);
                        alias.put(objPropKey, objPropKey);
                    }
                }
                Class t = GetLastPropertyType(Objreferences, 1, tpassin);
                List<Object> objlist = new ArrayList<Object>();
                for (String value : ColumnValue)
                {
                    Object result = ClassConverter.convertValue(value, t);
                    objlist.add(result);
                }
                Criterion ac = BuildFilter(refKeyName + "." + Objreferences[Objreferences.length - 1], objlist, QueryMode, t);
                SetConditions(ColumnName, ac, QueryType);
            }
        }
        else
        {
            if (properties.containsKey(ColumnName))
            {
                List<Object> objlist = new ArrayList<Object>();
                for (String value : ColumnValue)
                {
                    Object result = ClassConverter.convertValue(value, properties.get(ColumnName));
                    objlist.add(result);

                }
                Criterion ac = BuildFilter(ColumnName, objlist, QueryMode, properties.get(ColumnName));
                SetConditions(ColumnName, ac, QueryType);
            }
        }
    }


    private Criterion BuildFilter(String ColumnName, List<Object> ColumnValue, QueryConstants.ComplexQueryMode QueryMode, Class c)
    {
        switch (QueryMode)
        {
            case In:
                return Restrictions.in(ColumnName, ColumnValue.toArray());
            case Between:
                //if (ColumnValue.size() == 2&&
                //Comparable.class.isAssignableFrom(c))
                //{
                Comparable rawValue1 = (Comparable)ColumnValue.get(0);
                Comparable rawValue2 = (Comparable)ColumnValue.get(1);
                Comparable ComparableValue1 = rawValue1;
                Comparable ComparableValue2 = rawValue2;
                if (ComparableValue1.compareTo(ComparableValue2) == -1)
                {
                    return Restrictions.between(ColumnName, rawValue1, rawValue2);
                }
                else
                {
                    return Restrictions.between(ColumnName, rawValue2, rawValue1);
                }
                //}
                //else
                //{
                //return Restrictions.in(ColumnName, ColumnValue.toArray());
                //}
            default:
                return Restrictions.in(ColumnName, ColumnValue.toArray());
        }
    }

    private Criterion BuildFilter(String ColumnName, Object ColumnValue, QueryConstants.SimpleQueryMode QueryMode)
    {
        switch (QueryMode)
        {
            case Equal:
                return Restrictions.eq(ColumnName, ColumnValue);
            case NotEqual:
                return Restrictions.ne(ColumnName, ColumnValue);
            case Like:
                if (ColumnValue instanceof String)
                {
                    // XXX: Potential issue when column value contains '%' or '_'.
                    return EscapedLikeRestriction(ColumnName, (String)ColumnValue, MatchMode.ANYWHERE);
                } else {
                    return Restrictions.like(ColumnName, ColumnValue);
                }
            case LessThan:
                return Restrictions.lt(ColumnName, ColumnValue);
            case GreaterThan:
                return Restrictions.gt(ColumnName, ColumnValue);
            case LessEqual:
                return Restrictions.le(ColumnName, ColumnValue);
            case GreaterEqual:
                return Restrictions.ge(ColumnName, ColumnValue);
            case IsNull:
                return Restrictions.isNull(ColumnName);
            case IsNotNull:
                return Restrictions.isNotNull(ColumnName);
            default:
                return Restrictions.eq(ColumnName, ColumnValue);
        }
    }

    /**
     Return a copy of the conjunction conditions list
     **/
    public List<Criterion> getConjunctionConditions()
    {
        List<Criterion> retConditions = new ArrayList<Criterion>();
        for (Criterion se : ConjunctionConditions.values())
        {
            retConditions.add(se);
        }
        return retConditions;
    }

    /**
     Return a copy of the disjunction conditions list
     **/
    public List<Criterion> getDisjunctionConditions()
    {
        List<Criterion> retConditions = new ArrayList<Criterion>();
        for (Criterion se : DisjunctionConditions.values())
        {
            retConditions.add(se);
        }
        return retConditions;
    }

    /**
     Return a copy of the alias list
     **/
    public Map<String, String> GetAlias()
    {
        Map<String, String> retAlias = new HashMap<String, String>();
        for(String alia : alias.keySet()) {
            retAlias.put(alia, alias.get(alia));
        }
        return retAlias;
    }

    private Map<String, Class> BuildReferencePropertiesMapping(Class c)
    {
        Map<String, Class> properties = new HashMap<String, Class>();

        Field[] pinfos = c.getDeclaredFields();
        if(c.getSuperclass().getName() !="java.lang.Object"){
            Field[] superpinfos = c.getSuperclass().getDeclaredFields();
            pinfos = ArrayUtils.addAll(superpinfos, pinfos);
        }

        if (pinfos != null)
        {
            for (Field info : pinfos)
            {
                Type type= info.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType)type;
                    properties.put(info.getName(), (Class)pType.getActualTypeArguments()[0]);
                } else {
                    properties.put(info.getName(), info.getType());
                }
            }
        }
        return properties;
    }

    /**
     * 根据setter方法构建查询属�?列表
     */
    private void BuildOwnPropertiesMapping(){
        Method[] methods = localClass.getMethods();
        if(methods != null){
            for (Method method : methods){
                String methodName = method.getName();
                if (methodName.startsWith("set")){
                    String setterFieldName = methodName.substring(3);
                    // get the property name by lower case first char of setter
                    String fieldName = setterFieldName.substring(0, 1).toLowerCase() + setterFieldName.substring(1);
                    Class<?>[] typeParameters = method.getParameterTypes();
                    Type[] parameterTypes = method.getGenericParameterTypes();
                    if (typeParameters != null && typeParameters.length == 1){
//                        Type type = typeParameters[0];
                        Class parameterClass = typeParameters[0];
                        Type parameterType = parameterTypes[0];
                        // 如果是ParameterizedType将泛型类作为属�?类型
                        if (parameterType instanceof ParameterizedType) {
                            ParameterizedType pType = (ParameterizedType)parameterType;
                            properties.put(fieldName, (Class)pType.getActualTypeArguments()[0]);
                        } else {
                            properties.put(fieldName, parameterClass);
                        }
                    }
                }
            }
        }
    }

    private void BuildOwenPropertiesMapping()
    {
        Field[] pinfos = localClass.getDeclaredFields();
        if (pinfos != null)
        {
            for (Field info : pinfos)
            {
                Type type= info.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType)type;
                    properties.put(info.getName(), (Class)pType.getActualTypeArguments()[0]);
                } else {
                    properties.put(info.getName(), info.getType());
                }
            }
        }
    }

    private void SetConditions(String Name, Criterion condition, QueryConstants.QueryType QueryType)
    {
        switch (QueryType)
        {
            case Conjunction:
                ConjunctionConditions.put(Name, condition);
                if (DisjunctionConditions.containsKey(Name))
                {
                    DisjunctionConditions.remove(Name);
                }
                break;
            case Disjunction:
                DisjunctionConditions.put(Name, condition);
                if (ConjunctionConditions.containsKey(Name))
                {
                    ConjunctionConditions.remove(Name);
                }
                break;
        }

    }

    private String GetEscapedTextForLike(String text)
    {
        if (text==null||text.length()==0)
        {
            return text;
        }
        text = text.replace(EscapeChar, EscapeChar + EscapeChar);
        text = text.replace("%", EscapeChar + "%");
        text = text.replace("_", EscapeChar + "_");
        text = text.replace("[", EscapeChar + "[");
        return text;
    }

    private Criterion EscapedLikeRestriction(String propertyName, String value, MatchMode matchMode)
    {
        return Restrictions.like(propertyName, GetEscapedTextForLike(value), matchMode);
    }
}

