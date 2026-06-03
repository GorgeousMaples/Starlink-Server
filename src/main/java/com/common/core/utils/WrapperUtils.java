package com.common.core.utils;

import com.app.domain.User;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda 表达式解析工具类，用于解析 Lambda 表达式获取字段名
 *
 * <p> WrapperUtils.getColum(User::getId) 会返回 t_user.C_ID ,再搭配 QueryWrapper 可以灵活生成跨表的查询语句 </p>
 * <p> getSelectCondition() 会根据查询包装器返回对应的 SQL 查询语句 </p>
 */
public class WrapperUtils {
    // 缓存实体类与表名的映射
    private static final Map<Class<?>, String> tableCache = new ConcurrentHashMap<>();
    // 缓存实体类的成员变量和字段的映射
    private static final Map<Field, String> fieldCache = new ConcurrentHashMap<>();

    /**
     * 从 lambda 表达式中解析出表名 + 字段名
     */
    public static <T> String parse(SFunction<T, ?> column) {
        // 通过 MyBatis-Plus 工具类解析 Lambda
        LambdaMeta lambda = LambdaUtils.extract(column);
        Class<?> entityClass = lambda.getInstantiatedClass();
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        String columnName = getColumnName(entityClass, fieldName);
        String tableName = getTableName(entityClass);
        return tableName + "." + columnName;
    }

    /**
     * 获取实体类的表名（优先读取 @TableName 注解）
     */
    public static String getTableName(Class<?> entityClass) {
        return tableCache.computeIfAbsent(entityClass, clazz -> {
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            return (tableNameAnnotation != null)
                    ? tableNameAnnotation.value()
                    : clazz.getSimpleName().toLowerCase();
        });
    }

    /**
     * 获取字段的列名（优先读取 @TableId 或 @TableField 注解）
     */
    public static String getColumnName(Class<?> entityClass, String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            if (fieldCache.containsKey(field)) {
                return fieldCache.get(field);
            }
            // 依次获取 @TableId 或 @TableField 注解的值，如果为空则使用字段名
            if (field.isAnnotationPresent(TableId.class)) {
                String value = field.getAnnotation(TableId.class).value();
                fieldName =  value.isBlank() ? fieldName : value;
            } else if (field.isAnnotationPresent(TableField.class)) {
                String value = field.getAnnotation(TableField.class).value();
                fieldName =  value.isBlank() ? fieldName : value;
            }
            fieldCache.put(field, fieldName);
            return fieldName;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("字段不存在: " + fieldName, e);
        }
    }

    /**
     * 根据 QueryWrapper 或 LambdaQueryWrapper 获取对应的 where 语句
     */
    public static String getWhereSentence(AbstractWrapper<?,?,?> wrapper) {
        // 注意不能使用 getCustomSqlSegment()，因为这个方法会默认带上 WHERE 关键字，我们只需要查询的条件
        String condition = wrapper.getSqlSegment();
        Map<String, Object> paramMap = wrapper.getParamNameValuePairs();
        if (StringUtils.isBlank(condition) || paramMap == null) {
            return condition;
        }

        for (var entry : paramMap.entrySet()) {
            String placeholder = "#{ew.paramNameValuePairs." + entry.getKey() + "}";
            String value = formatValue(entry.getValue());
            condition = condition.replace(placeholder, value);
        }

        return condition;
    }

    /**
     * 获取 QueryWrapper 中的 GROUP BY 表达式
     */
    public static String getGroupBy(AbstractWrapper<?,?,?> wrapper) {
        return null;
    }

    /**
     * 根据数据类型格式化值（字符串添加单引号）
     */
    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number) {
            return value.toString();
        }
        return "'" + value.toString().replace("'", "''") + "'";
    }

    public static void main(String[] args) throws Exception {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        String[] column = {"C_ID", "C_NAME"};
        wrapper.groupBy("C_STATUS", "DATE_FORMAT(t_project.C_START_TIME, '%Y-%m')").eq("C_STATUS", 1);
        String where = getWhereSentence(wrapper);
        System.out.println(where);
        if (where.startsWith("(") && where.endsWith(")")) {
            where = where.substring(1, where.length() - 1).trim();
        }
        String w = "select * from table where " + where;
        PlainSelect select = ((Select) CCJSqlParserUtil.parse(w)).getPlainSelect();
        System.out.println(select.getGroupBy());
        System.out.println(select.getWhere());
    }
}
