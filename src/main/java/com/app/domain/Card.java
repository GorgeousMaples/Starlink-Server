package com.app.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 卡片类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_card")
public class Card {
//    /**
//     * 卡片ID（主键）
//     */
//    @TableId("C_ID")
//    private Long id;

    /**
     * 卡片存储路径（主键）
     */
    @NotNull
    @TableId("C_PATH")
    private String path;

    /**
     * 卡片名
     */
    @NotNull
    @TableField("C_NAME")
    private String name;
}
