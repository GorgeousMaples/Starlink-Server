package com.app.domain.vo;

import java.time.LocalDateTime;

/** 管理端账户列表，仅包含允许展示的字段。 */
public record PlayerSummaryVo(
        String uid,
        Integer type,
        String name,
        LocalDateTime createTime,
        LocalDateTime lastLoginTime
) {
}
