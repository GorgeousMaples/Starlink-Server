package com.app.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @description 用于标识用户信息
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User extends BaseEntity {
    @TableId("C_ID")
    private Long id;

    @TableField("C_NAME")
    @NotNull(message = "用户名不能为空")
    private String name;

    @TableField("C_PASSWORD")
    private String password;

    @TableField("C_PHONE")
    @NotNull(message = "手机号不能为空")
    private String phone;

    @TableField("C_EMAIL")
    private String email;
}
