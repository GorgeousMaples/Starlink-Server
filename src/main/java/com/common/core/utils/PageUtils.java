package com.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 */
@Data
public class PageUtils implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前记录起始索引 默认1
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认10条
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 查询的页数
     */
    private Integer pageNum;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 用于排序的列
     */
    private String orderColumn;

    /**
     * 是否升序排列（asc: 升序，desc: 降序）
     */
    private String isAsc;

    /**
     * 使用默认规则
     */
    public static <T> Page<T> build() {
        return new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    /**
     * 返回默认实体
     */
    public static PageUtils of() {
        return new PageUtils();
    }

    /**
     * 自定义页数与分页大小
     */
    public static PageUtils of(Integer pageNum, Integer pageSize) {
        PageUtils pageQuery = new PageUtils();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        return pageQuery;
    }

    /**
     * 构建分页查询实体
     */
//    public <T> Page<T> build() {
//        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
//        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
//        if (pageNum <= 0) {
//            pageNum = DEFAULT_PAGE_NUM;
//        }
//        Page<T> page = new Page<>(pageNum, pageSize);
//        List<OrderItem> orderItems = buildOrderItem();
//        if (CollUtil.isNotEmpty(orderItems)) {
//            page.addOrder(orderItems);
//        }
//        return page;
//    }

//    /**
//     * 通用排序
//     * 针对关联字段进行排序
//     *
//     * @param entityClass 实体类
//     * @param voClass     vo类
//     * @param result      结果
//     */
//    public <T> void generalSort(Class<?> entityClass, Class<?> voClass, List<T> result) {
//        String relatedColumns = this.listOrderByRelatedColumns(entityClass, voClass);
////        String relatedColumns = this.orderByColumn;
//        if (this.getIsAsc() == null || this.getIsAsc().trim().isEmpty()) {
//            this.setIsAsc("desc");
//        }
//        if (CollUtil.isNotEmpty(result) && StringUtils.isNotBlank(relatedColumns) && !this.getIsAsc().trim().isEmpty()) {
//            SortListUtils.sortDynamic(result, relatedColumns, this.getIsAsc());
//        }
//    }

//    /**
//     * 获取关联信息排序字段
//     * 关联字段只支持基本数据类型排序
//     *
//     * @param clazz clazz 实体类
//     * @return {@link List }<{@link String }>
//     */
//    public String listOrderByRelatedColumns(Class<?> clazz, Class<?> relatedClazz) {
//        if (StringUtils.isNotBlank(this.orderByColumn)) {
//            String[] orderFields = this.orderByColumn.split(StringUtils.SEPARATOR);
//            List<String> relatedColumnLs = Arrays.stream(orderFields).filter(orderField -> !ReflectUtils.myHasField(clazz, orderField)).collect(Collectors.toList());
//            String relatedColumns = String.join(StringUtils.SEPARATOR, relatedColumnLs);
//            return ReflectUtils.getMyHasField(relatedClazz, relatedColumns);
//        }
//        return null;
//    }

//    /**
//     * 获取实体排序字段
//     *
//     * @param clazz clazz 实体类
//     * @return {@link List }<{@link String }>
//     */
//    public String listOrderByEntityColumns(Class<?> clazz) {
//        if (StringUtils.isNotBlank(this.orderByColumn)) {
//            String[] orderFields = this.orderByColumn.split(StringUtils.SEPARATOR);
//            List<String> entityColumns = Arrays.stream(orderFields).filter(orderField -> ReflectUtils.myHasField(clazz, orderField)).collect(Collectors.toList());
//            return String.join(StringUtils.SEPARATOR, entityColumns);
//        }
//        return null;
//    }
//

    public void setDefaultValue() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        this.setPageNum(pageNum);
        this.setPageSize(pageSize);
        if (StringUtils.isBlank(orderColumn)) {
            orderColumn = "createTime";
        }
        if (StringUtils.isBlank(isAsc)) {
            isAsc = "desc";
        }
    }

//    /**
//     * 构建分页对象
//     *
//     * @param clazz clazz
//     * @return {@link Page }<{@link T }>
//     */
//    public <T> Page<T> build(Class<T> clazz) {
//        String entityColumns = this.listOrderByEntityColumns(clazz);
//        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
//        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
//        if (pageNum <= 0) {
//            pageNum = DEFAULT_PAGE_NUM;
//        }
//        Page<T> page = new Page<>(pageNum, pageSize);
//        List<OrderItem> orderItems = buildOrderItem(entityColumns, this.isAsc);
//        if (CollUtil.isNotEmpty(orderItems)) {
//            page.addOrder(orderItems);
//        }
//        return page;
//    }

//    /**
//     * 构建page对象
//     * 支持关联字段排序
//     *
//     * @param clazz clazz
//     * @return {@link Page }<{@link T }>
//     */
//    public <T> Page<T> buildForJoin(Class<T> clazz) {
//        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
//        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
//        if (pageNum <= 0) {
//            pageNum = DEFAULT_PAGE_NUM;
//        }
//        Page<T> page = new Page<>(pageNum, pageSize);
//        List<OrderItem> orderItems = buildOrderItem();
//        if (CollUtil.isNotEmpty(orderItems)) {
//            page.addOrder(orderItems);
//        }
//        return page;
//    }


    /**
     * 构建排序
     * <p>
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    private List<OrderItem> buildOrderItem() {
        if (StringUtils.isBlank(orderColumn)) {
            orderColumn = "createTime";
        }
        if (StringUtils.isBlank(isAsc)) {
            isAsc = "desc";
        }
        String orderBy = SqlUtils.escapeOrderBySql(orderColumn);
        orderBy = StringUtils.toUnderScoreCase(orderBy);
        orderBy = StringUtils.convertToDbColumnName(orderBy);
        // 兼容前端排序类型
        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(StringUtils.SEPARATOR);
        String[] isAscArr = isAsc.split(StringUtils.SEPARATOR);
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new RuntimeException("排序参数有误");
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equals(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new RuntimeException("排序参数有误");
            }
        }
        return list;
    }
//
//    private List<OrderItem> buildOrderItem(String orderByColumn, String isAsc) {
//        if (StringUtils.isBlank(orderByColumn)) {
////            orderByColumn = "create_time";
//            orderByColumn = "createTime";
//        }
//        if (StringUtils.isBlank(isAsc)) {
//            isAsc = "desc";
//        }
//        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
//        orderBy = StringUtils.toUnderScoreCase(orderBy);
//        orderBy = StringUtils.convertToDbColumnName(orderBy);
//        // 兼容前端排序类型
//        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});
//
//        String[] orderByArr = orderBy.split(StringUtils.SEPARATOR);
//        String[] isAscArr = isAsc.split(StringUtils.SEPARATOR);
//        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
//            throw new ServiceException("排序参数有误");
//        }
//
//        List<OrderItem> list = new ArrayList<>();
//        // 每个字段各自排序
//        for (int i = 0; i < orderByArr.length; i++) {
//            String orderByStr = orderByArr[i];
//            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
//            if ("asc".equals(isAscStr)) {
//                list.add(OrderItem.asc(orderByStr));
//            } else if ("desc".equals(isAscStr)) {
//                list.add(OrderItem.desc(orderByStr));
//            } else {
//                throw new ServiceException("排序参数有误");
//            }
//        }
//        return list;
//    }
}
