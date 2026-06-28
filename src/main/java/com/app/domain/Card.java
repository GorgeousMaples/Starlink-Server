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
    /**
     * 大卡号 (主键)
     */
    @NotNull
    @TableId(value = "C_ID")
    private String id;

    /**
     * 小编号
     */
    @TableField("C_SUB_ID")
    private String subId;

    /**
     * 卡片存储路径
     */
    @NotNull
    @TableField(value = "C_PATH")
    private String path;

    /**
     * 卡片名
     */
    @NotNull
    @TableField("C_NAME")
    private String name;

    /**
     * 星级
     */
    @TableField("C_LEVEL")
    private Integer level;

    /**
     * 设计师
     */
    @TableField("C_DESIGNER")
    private String designer;

    /**
     * 系列
     */
    @TableField("C_SERIES")
    private String series;

    /**
     * 类别
     */
    @TableField("C_CATEGORY")
    private String category;

    /**
     * 定位
     */
    @TableField("C_ROLE")
    private String role;

    /**
     * 是否特种 (0: 否, 1: 是)
     */
    @TableField("C_IS_SPECIAL")
    private Boolean isSpecial = false;

    /**
     * 稀有度
     */
    @TableField("C_RARITY")
    private String rarity;

    /**
     * 属性
     */
    @TableField("C_ATTRIBUTE")
    private String attribute;

    /**
     * 卡色
     */
    @TableField("C_COLOR")
    private String color;

    /**
     * 种族
     */
    @TableField("C_RACE")
    private String race;

    /**
     * 攻击距离
     */
    @TableField("C_RANGE")
    private Integer attackRange;

    /**
     * 攻击力
     */
    @TableField("C_ATTACK")
    private Integer attack;

    /**
     * 生命值
     */
    @TableField("C_HEALTH")
    private Integer health;

    /**
     * 强度
     */
    @TableField("C_STRENGTH")
    private Float strength;
}
