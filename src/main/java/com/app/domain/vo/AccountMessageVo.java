package com.app.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountMessageVo {
    private Long id;

    private Long accountId;

    private Long messageId;

    private Integer messageType;

    /* Message类的关联字段 */
    private String title;

    private String content;
}
