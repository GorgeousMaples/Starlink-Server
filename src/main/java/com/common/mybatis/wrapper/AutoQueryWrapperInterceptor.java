package com.common.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.common.core.utils.StringUtils;
import com.common.core.utils.WrapperUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class AutoQueryWrapperInterceptor extends JsqlParserSupport implements InnerInterceptor {
    // 缓存需要处理的Mapper方法
    private final Set<String> validCache = new ConcurrentSkipListSet<>();
    // 缓存无需处理的Mapper方法
    private final Set<String> skipCache = new ConcurrentSkipListSet<>();

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
                            RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 如果不需要处理，直接返回
        if (!shouldProcess(ms.getId(), parameter))
            return;
        // 获取参数中的 Wrapper
        Wrapper<?> wrapper = getWrapper(parameter);
        // 解析原始 SQL 并传递 wrapper（每次解析到 SELECT 语句时都会调用 processSelect 方法）
        String newSql = parserSingle(boundSql.getSql(), wrapper);
        // 通过反射修改BoundSql的sql内容
        try {
            Field field = BoundSql.class.getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
        } catch (Exception e) {
            throw new RuntimeException("BoundSql 解析失败: " + ms.getId(), e);
        }
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        // 判断传递的参数是否是 Wrapper
        if (obj instanceof AbstractWrapper<?,?,?> wrapper) {
            // 分别获取 select 和 where 对应的语句
            String selectSentence = wrapper.getSqlSelect();
            String whereSentence = WrapperUtils.getWhereSentence(wrapper);

            // 处理普通的 SELECT 语句（不包含 UNION 或 JOIN 的 SELECT 语句）
            if (select.getPlainSelect() != null) {
                PlainSelect plainSelect = select.getPlainSelect();
                try {
                    // 首先处理 select 语句
                    if (StringUtils.isNotBlank(selectSentence)) {
                        // 将原来的 select 语句分割开
                        String[] columns = selectSentence.split(",");
                        List<SelectItem<?>> newSelectItems = new ArrayList<>();
                        for (String col : columns) {
                            // 创建列表达式（支持带表名的复杂列）
                            Column column = new Column(col.trim());
                            // 封装为 SelectItem
                            newSelectItems.add(new SelectItem<>(column));
                        }
                        // 用新语句完全替代旧语句
                        plainSelect.setSelectItems(newSelectItems);
                    }
                    // 其次处理 where 语句
                    if (StringUtils.isNotBlank(whereSentence)) {
                        // 去除外层括号
                        if (whereSentence.startsWith("(") && whereSentence.endsWith(")")) {
                            whereSentence = whereSentence.substring(1, whereSentence.length() - 1).trim();
                        }
                        Expression newWhere = CCJSqlParserUtil.parseCondExpression(whereSentence);
                        Expression oldWhere = plainSelect.getWhere();


                        // 如果原 SQL 中没有 WHERE 语句，则直接设置 WHERE 条件，否则使用 AND 连接
                        if (oldWhere == null) {
                            plainSelect.setWhere(newWhere);
                        } else {
                            plainSelect.setWhere(new AndExpression(
                                    new Parenthesis(oldWhere),
                                    new Parenthesis(newWhere)
                            ));
                        }
                    }
                    System.out.println("执行的SQL语句: " + plainSelect);
                } catch (Exception e) {
                    throw new RuntimeException("条件 SQL 解析失败: " + whereSentence, e);
                }
            }
        }
    }

    /**
     * 判断是否需要处理
     */
    private boolean shouldProcess(String methodId, Object parameter) {
        if (validCache.contains(methodId))
            return true;
        if (skipCache.contains(methodId))
            return false;

        try {
            // 根据ID获取Mapper方法信息
            Class<?> mapperClass = Class.forName(methodId.substring(0, methodId.lastIndexOf('.')));
            // 获取方法名
            String methodName = methodId.substring(methodId.lastIndexOf('.') + 1);
            // 根据方法名找到 Mapper 类中对应的方法（由于MyBatis不支持方法重载，所以这个方案可行）
            Method method = Arrays.stream(mapperClass.getMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .findFirst()
                    .orElse(null);
            if (method == null) {
                throw new RuntimeException("无法找到对应的 Mapper 方法: " + methodId);
            }

            // 从参数中获取 Wrapper
            Wrapper<?> wrapper = getWrapper(parameter);

            // 当 wrapper 不为空且Mapper类或方法上有@AutoWrapper注解时才处理
            boolean shouldProcess = wrapper != null &&
                    (mapperClass.isAnnotationPresent(AutoQueryWrapper.class)
                            || method.isAnnotationPresent(AutoQueryWrapper.class));

            // 根据结果添加到相应的缓存中
            if (shouldProcess) {
                validCache.add(methodId);
            } else {
                skipCache.add(methodId);
            }
            return shouldProcess;
        } catch (Exception e) {
            skipCache.add(methodId);
            return false;
        }
    }

    /**
     * 根据参数获取 Wrapper
     */
    private Wrapper<?> getWrapper(Object parameter) {
        if (parameter instanceof Map<?,?> map
                && map.get(Constants.WRAPPER) instanceof AbstractWrapper<?,?,?> wrapper) {
            return wrapper;
        }
        return null;
    }

//    /**
//     * 根据 QueryWrapper 获取查询条件
//     */
//    public static String getSelectCondition(AbstractWrapper<?,?,?> wrapper) {
//        // 注意不能使用 getCustomSqlSegment()，因为这个方法会默认带上 WHERE 关键字，我们只需要查询的条件
//        String condition = wrapper.getSqlSegment();
//        Map<String, Object> paramMap = wrapper.getParamNameValuePairs();
//        if (StringUtils.isBlank(condition) || paramMap == null) {
//            return condition;
//        }
//
//        for (var entry : paramMap.entrySet()) {
//            String placeholder = "#{ew.paramNameValuePairs." + entry.getKey() + "}";
//            String value = formatValue(entry.getValue());
//            condition = condition.replace(placeholder, value);
//        }
//
//        return condition;
//    }
//
//    /**
//     * 根据数据类型格式化值（字符串添加单引号）
//     */
//    private static String formatValue(Object value) {
//        if (value == null) {
//            return "null";
//        }
//        if (value instanceof Number) {
//            return value.toString();
//        }
//        return "'" + value.toString().replace("'", "''") + "'";
//    }
}
