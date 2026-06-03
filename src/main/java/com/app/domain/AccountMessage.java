package com.app.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.common.core.valid.IntegerConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_account_message")
public class AccountMessage {
    @TableId("C_ID")
    private Long id;

    @TableField("C_ACCOUNT_ID")
    private Long accountId;

    @TableField("C_MESSAGE_ID")
    private Long messageId;

    @TableField("C_TYPE")
    @Builder.Default
    @IntegerConstraint(values = {1, 2}, message = "非法的账户消息类型")
    private Integer type = 1;
}
