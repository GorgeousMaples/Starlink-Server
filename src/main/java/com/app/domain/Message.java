package com.app.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_message")
public class Message {
    @TableId("C_ID")
    private Long id;

    @TableField("C_TITLE")
    private String title;

    @TableField("C_CONTENT")
    private String content;
}
