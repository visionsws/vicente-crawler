package cn.com.bluemoon.demo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGenUtils {

    private final static String SQL_UPSERT_INTO_TABLE =  "UPSERT into \"crawl\".\"%s\" ";

    public static String insertObject(String tableName, Object obj){
        Map<String,String> map = getProperty(obj);
        return insertData(tableName,map);
    }

    //生成保存数据的Sql语句
    public static String insertData(String tableName, Map<String,String> map ){
        String resultSql = "";
        try {
            StringBuffer upsertSql = new StringBuffer();
            upsertSql.append(String.format(SQL_UPSERT_INTO_TABLE,tableName)).append(" (");
            Set<String> set = map.keySet();
            for(String key : set){
                upsertSql.append ("\"" +key + "\",");
            }
            //去掉最后一个逗号
            upsertSql.deleteCharAt(upsertSql.length()-1);
            upsertSql.append(") values ( ") ;
            for(String key : set){
                upsertSql.append("'" + map.get(key) + "',");
            }
            upsertSql.deleteCharAt(upsertSql.length()-1).append(" )");
            resultSql = upsertSql.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSql;
    }

    /**
     * 返回一个对象的属性和属性值
     */
    public static Map<String,String> getProperty(Object entityName) {
        Map<String,String> map = new HashMap<>();
        try {
            Class c = entityName.getClass();
            // 获得对象属性
            Field field[] = c.getDeclaredFields();
            for (Field f : field) {
                Object v = invokeMethod(entityName, f.getName(), null);
                //驼峰转下划线
                String columnName = humpToLine(f.getName());
                map.put(columnName, v.toString());
            }
        } catch (Exception e) {
            map = null;
        }
        return map;
    }

    /**
     * 获得对象属性的值
     */
    private static Object invokeMethod(Object owner, String methodName,
                                       Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        methodName = methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
        Method method = null;
        try {
            method = ownerClass.getMethod("get" + methodName);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            return " can't find 'get" + methodName + "' method";
        }
        return method.invoke(owner);
    }

    /** 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)}) */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
