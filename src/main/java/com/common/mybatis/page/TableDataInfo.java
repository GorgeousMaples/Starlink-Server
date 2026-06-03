package com.common.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 表格分页数据对象
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDataInfo<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public static <T> TableDataInfo<T> of() {
        return build();
    }

    public static <T> TableDataInfo<T> build() {
        return TableDataInfo.<T>builder()
                .rows(Collections.emptyList())
                .total(0)
                .build();
    }

    public static <T> TableDataInfo<T> build(IPage<T> page) {
        return TableDataInfo.<T>builder()
                .rows(page.getRecords())
                .total(page.getTotal())
                .build();
    }

    public static <T> TableDataInfo<T> build(List<T> list) {
        return TableDataInfo.<T>builder()
                .rows(list)
                .total(list.size())
                .build();
    }

    public static <T> TableDataInfo<T> build(List<T> list, Long total) {
        return TableDataInfo.<T>builder()
                .rows(list)
                .total(total)
                .build();
    }

}
