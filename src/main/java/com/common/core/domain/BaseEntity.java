package com.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.common.core.valid.IntegerConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * 基础实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

//    @TableField("C_CREATE_TIME")
//    private LocalTime createTime;
//
//    @TableField("C_UPDATE_TIME")
//    private LocalTime updateTime;

    @TableField("C_IS_DELETED")
    private Boolean isDeleted = false;
}
