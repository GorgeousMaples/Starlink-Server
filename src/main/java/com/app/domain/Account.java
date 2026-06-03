package com.app.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.common.core.domain.BaseEntity;
import com.common.core.valid.IntegerConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 账号实体类
 *
 * <p>与 User 类是一对多的关系，同一个 User 可以有多个不同类型的 Account</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_account")
public class Account extends BaseEntity {
    /**
     * 账号ID（主键）
     */
    @TableId("C_ID")
    private Long id;

    /**
     * 用户ID（外键）
     */
    @TableField("C_USER_ID")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 账户类型【1：学生；2：教师；3：管理员】
     */
    @TableField("C_TYPE")
    @IntegerConstraint(values = {1, 2, 3}, message = "非法的账户类型")
    @NotNull(message = "账户类型不能为空")
    @Builder.Default
    private Integer type = 1;
}
