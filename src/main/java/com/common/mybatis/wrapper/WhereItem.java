package com.common.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * SQL 中 WHERE 语句的条件项
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhereItem {
    /**
     * 列名
     */
    private String column;

    /**
     * 比较运算符
     * "eq"、"le"、"like"等
     */
    private String operator;

    /**
     * 比较值
     */
    private String value;

    /**
     * 比较值范围
     * 专用于比较运算符为 "in" 和 "notIn" 的情况
     */
    private String[] values;

    /**
     * 与前一项的连接方式
     * "and" 或 "or"
     * <p>非条件组 或者 条件组中的第一项 时，此值为空<p/>
     */
    private String joinWay;

    /**
     * 条件组
     * <p>如果传了此值，则说明本项为条件组，则本项中的其他值都不用传<p/>
     */
    private WhereItem[] group;

    /**
     * 判断当前项是否为条件组
     */
    public boolean isGroup() {
        return group != null && group.length > 0;
    }

    /**
     * 构建查询条件
     */
    public static QueryWrapper<?> buildQueryWrapper(QueryWrapper<?> queryWrapper, WhereItem[] items) {
        for (int i = 0; i < items.length; i++) {
            WhereItem item = items[i];
            if (i == 0) {
                applyItem(queryWrapper, item);
            } else {
                String joinWay = item.getJoinWay();
                if (joinWay == null) {
                    throw new IllegalArgumentException("条件组中的非首个条件一定要包含与前一个条件的连接方式");
                }
                if (joinWay.equals("and")) {
                    queryWrapper.and(w -> applyItem(w, item));
                } else if (joinWay.equals("or")) {
                    queryWrapper.or(w -> applyItem(w, item));
                }
            }
        }
        return queryWrapper;
    }

    private static void applyItem(QueryWrapper<?> queryWrapper, WhereItem item) {
        if (item.isGroup()) {
            // 这里使用 and 是为了让条件组能正确包裹在括号中（虽然可能会造成最后的括号结构有点复杂和冗余，但是起码能保证是对的）
            queryWrapper.and(w -> buildQueryWrapper(w, item.getGroup()));
        } else {
            queryWrapper.eq(item.column, item.value);
        }
    }
}
