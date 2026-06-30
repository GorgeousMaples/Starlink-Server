package com.app.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@NotNull
@Builder
public class PlayerVo {
    /**
     * 玩家 ID
     */
    private String uid;

    /**
     * 玩家名
     */
    private String name;

    /**
     * 身份令牌
     */
    private String token;
}
