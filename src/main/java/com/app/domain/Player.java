package com.app.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 玩家类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_player")
public class Player {
    /**
     * 玩家 ID
     */
    @NotNull
    @TableId(value = "C_UID")
    private String uid;

    /**
     * 账号类型【0：普通玩家，1：管理员，2：超级管理员】
     */
    @NotNull
    @TableField(value = "C_TYPE")
    private Integer type = 0;

    /**
     * 玩家名
     */
    @TableField("C_NAME")
    private String name;

    /**
     * 登录密码
     */
    @TableField("C_PASSWORD")
    private String password;

    /**
     * 注册时间
     */
    @TableField(value="C_CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    @TableField("C_LOGIN_TIME")
    private LocalDateTime lastLoginTime;
}
